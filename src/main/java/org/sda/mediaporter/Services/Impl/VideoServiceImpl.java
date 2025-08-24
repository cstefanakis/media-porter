package org.sda.mediaporter.Services.Impl;

import org.sda.mediaporter.Services.CodecService;
import org.sda.mediaporter.Services.ResolutionService;
import org.sda.mediaporter.Services.VideoService;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;
import org.sda.mediaporter.models.metadata.Video;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.sda.mediaporter.repositories.metadata.ResolutionRepository;
import org.sda.mediaporter.repositories.metadata.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;
    private final CodecService codecService;
    private final ResolutionService resolutionService;
    private final ResolutionRepository resolutionRepository;
    private final CodecRepository codecRepository;

    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository, CodecService codecService, ResolutionService resolutionService, ResolutionRepository resolutionRepository, CodecRepository codecRepository) {
        this.videoRepository = videoRepository;
        this.codecService = codecService;
        this.resolutionService = resolutionService;
        this.resolutionRepository = resolutionRepository;
        this.codecRepository = codecRepository;
    }

    @Override
    public Video createVideoFromPath(Path file, Movie movie) {
        Video video = getVideoFromPath(file);
        if(video !=null){
            video.setMovie(movie);
            return videoRepository.save(video);
        }
        return null;
    }

    @Override
    public String getCodecFromFilePathViFFMpeg(Path file) {
        return validatedProperties(file, 0);
    }

    @Override
    public String getResolutionFromFilePathViFFMpeg(Path file) {
         Integer height = parseValidatedPropertiesToInteger(file, 1);
         return generatedResolutionByHeight(height);
    }

    @Override
    public Integer getBitrateFromFilePathViFFMpeg(Path file) {
        Integer bitrate =  parseValidatedPropertiesToInteger(file, 2);
        return bitrate != null?
                bitrate / 1000
                :null;
    }

    private String[] videoProperties(Path file){
        String videoInfo = videoInfo(file);

        if(videoInfo.isEmpty()) {
            return null;
        }

        String[] videoPropertiesSplit = videoInfo.split(",", -1);

        if(videoPropertiesSplit.length > 3){
            return null;
        }

        String[] videoProperties = new String[3];

        System.arraycopy(videoPropertiesSplit, 0, videoProperties, 0, videoPropertiesSplit.length);

        return videoProperties;
    }

    private String validatedProperties(Path file, int length){
        String [] videoProperties = videoProperties(file);
        if(videoProperties == null || videoProperties[length] == null){
            return null;
        }

        String resolution = videoProperties[length].trim().toLowerCase();

        return switch (resolution) {
            case "", "n/a" -> null;
            default -> resolution.replaceAll("[^a-zA-Z0-9]", "");
        };

    }

    private Integer parseValidatedPropertiesToInteger(Path file, int length){
        String property = validatedProperties(file, length);
        return property == null
                ? null
                : Integer.parseInt(property);
    }

    @Override
    public Video getVideoFromPath(Path file) {
        String propertyCodec = getCodecFromFilePathViFFMpeg(file);
        String propertyResolution = getResolutionFromFilePathViFFMpeg(file);
        Integer propertyBitrateKbps = getBitrateFromFilePathViFFMpeg(file);

        Codec codec = codecService.autoCreateCodec(propertyCodec, MediaTypes.VIDEO);
        Resolution resolution = resolutionService.autoCreateResolution(propertyResolution);

        return propertyCodec == null
                && propertyResolution == null
                && propertyBitrateKbps == null
                ? null
                : Video.builder()
                    .codec(codec)
                    .resolution(resolution)
                    .bitrate(propertyBitrateKbps)
                .build();
    }

    private String generatedResolutionByHeight(Integer height){
        if(height == null){
            return null;
        }
        LinkedHashMap<Integer, String> resolutions = new LinkedHashMap<>();
        resolutions.put(8000, "16K");
        resolutions.put(4000, "8K");
        resolutions.put(2000, "4K");
        resolutions.put(1000, "1080P");
        resolutions.put(700, "720P");
        resolutions.put(400, "480P");
        resolutions.put(300, "360P");
        resolutions.put(200, "240P");
        resolutions.put(100, "144P");

        for(Map.Entry<Integer, String> entity: resolutions.entrySet()){
            System.out.println("resolution height: " + height);
            if(height > entity.getKey()){
                System.out.println(entity.getKey());
                return entity.getValue();
            }
        }
        return null;
    }

    //Print String h264,1080,4567890
    private String videoInfo(Path filePath){
        return  FileServiceImpl.runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=codec_name,height,bit_rate",
                "-of", "csv=p=0",
                filePath.toString()
        });
    }
}
