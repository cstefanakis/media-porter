package org.sda.mediaporter.services.videoServices.impl;

import org.sda.mediaporter.services.CodecService;
import org.sda.mediaporter.services.fileServices.impl.FileServiceImpl;
import org.sda.mediaporter.services.videoServices.ResolutionService;
import org.sda.mediaporter.services.videoServices.VideoService;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;
import org.sda.mediaporter.models.metadata.Video;
import org.sda.mediaporter.repositories.metadata.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;
    private final CodecService codecService;
    private final ResolutionService resolutionService;;

    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository, CodecService codecService, ResolutionService resolutionService) {
        this.videoRepository = videoRepository;
        this.codecService = codecService;
        this.resolutionService = resolutionService;
    }

    @Override
    public Video createVideoFromPath(Path videoFilePath) {
        Video video = getVideoFromPath(videoFilePath);
        if(video !=null){
            return videoRepository.save(video);
        }
        return null;
    }

    @Override
    public Video getVideoById(Long videoId) {
        return null;
    }

    @Override
    public String getCodecFromFilePathViFFMpeg(Path videoFilePath) {
        return validatedProperties(videoFilePath, 0);
    }

    @Override
    public String getResolutionFromFilePathViFFMpeg(Path videoFilePath) {
         Integer height = parseValidatedPropertiesToInteger(videoFilePath, 1);
         return generatedResolutionByHeight(height);
    }

    @Override
    public Integer getBitrateFromFilePathViFFMpeg(Path videoFilePath) {
        Integer bitrate =  parseValidatedPropertiesToInteger(videoFilePath, 2);
        return bitrate != null?
                bitrate / 1000
                :null;
    }

    private String[] videoProperties(Path videoFilePath){
        String videoInfo = videoInfo(videoFilePath);

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

    private String validatedProperties(Path videoFilePath, int length){
        String [] videoProperties = videoProperties(videoFilePath);
        if(videoProperties == null || videoProperties[length] == null){
            return null;
        }

        String resolution = videoProperties[length].trim().toLowerCase();

        return switch (resolution) {
            case "", "n/a" -> null;
            default -> resolution.replaceAll("[^a-zA-Z0-9]", "");
        };

    }

    private Integer parseValidatedPropertiesToInteger(Path videoFilePath, int length){
        String property = validatedProperties(videoFilePath, length);
        return property == null
                ? null
                : Integer.parseInt(property);
    }

    @Override
    public Video getVideoFromPath(Path videoFilePath) {
        String propertyCodec = getCodecFromFilePathViFFMpeg(videoFilePath);
        String propertyResolution = getResolutionFromFilePathViFFMpeg(videoFilePath);
        Integer propertyBitrateKbps = getBitrateFromFilePathViFFMpeg(videoFilePath);

        Codec codec = codecService.getOrCreateCodecByCodecNameAndMediaType(propertyCodec, MediaTypes.VIDEO);
        Resolution resolution = resolutionService.getResolutionByName(propertyResolution);

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
    private String videoInfo(Path videoFilePath){
        return  FileServiceImpl.runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=codec_name,height,bit_rate",
                "-of", "csv=p=0",
                videoFilePath.toString()
        });
    }
}
