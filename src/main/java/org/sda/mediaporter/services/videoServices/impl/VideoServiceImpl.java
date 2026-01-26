package org.sda.mediaporter.services.videoServices.impl;

import jakarta.transaction.Transactional;
import org.sda.mediaporter.dtos.VideoDto;
import org.sda.mediaporter.models.VideoFilePath;
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
    @Transactional
    public Video createVideoFromPath(Path filePath, VideoFilePath videoFilePath) {
        Video video = getVideoFromPath(filePath, videoFilePath);
        if(video !=null){
            video.setVideoFilePath(videoFilePath);
            videoFilePath.setVideo(video);
            return videoRepository.save(video);
        }
        return null;
    }

    @Override
    public Video getVideoById(Long videoId) {
        return null;
    }

    private String getCodecFromFilePathViFFMpeg(String [] videoProperties) {
        return validatedProperties(videoProperties, 0);
    }

    private String getResolutionFromFilePathViFFMpeg(String [] videoProperties) {
         Integer height = parseValidatedPropertiesToInteger(videoProperties, 1);
         return generatedResolutionByHeight(height);
    }

    private Integer getBitrateFromFilePathViFFMpeg(String [] videoProperties) {
        Integer bitrate =  parseValidatedPropertiesToInteger(videoProperties, 2);
        return bitrate != null?
                bitrate / 1000
                :null;
    }

    private String[] videoProperties(Path filePath){
        String videoInfo = videoInfo(filePath);
        System.out.println(videoInfo);
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

    private String validatedProperties(String [] videoProperties, int length){
        if(videoProperties == null || videoProperties[length] == null){
            return null;
        }

        String resolution = videoProperties[length].trim().toLowerCase();

        return switch (resolution) {
            case "", "n/a" -> null;
            default -> resolution.replaceAll("[^a-zA-Z0-9]", "");
        };

    }

    private Integer parseValidatedPropertiesToInteger(String [] videoProperties, int length){
        String property = validatedProperties(videoProperties, length);
        return property == null
                ? null
                : Integer.parseInt(property);
    }

    @Override
    public Video getVideoFromPath(Path filePath, VideoFilePath videoFilePath) {

        VideoDto videoDto = getVideoDetails(filePath);

        String videoCodec = videoDto.getVideoCodec();
        String videoResolution = videoDto.getVideoResolution();
        Integer videoBitrate = videoDto.getVideoBitrate();

        Codec codec = codecService.getOrCreateCodecByCodecNameAndMediaType(videoCodec, MediaTypes.VIDEO);
        Resolution resolution = resolutionService.getResolutionByName(videoResolution);

        return videoCodec == null
                && videoResolution == null
                && videoBitrate == null
                ? null
                : Video.builder()
                    .codec(codec)
                    .resolution(resolution)
                    .bitrate(videoBitrate)
                    .videoFilePath(videoFilePath)
                .build();
    }

    @Override
    @Transactional
    public void deleteVideoById(Long videoId) {
        videoRepository.deleteById(videoId);
    }

    @Override
    public VideoDto getVideoDetails(Path file) {
        String [] videoProperties = videoProperties(file);
        String videoCodec = getCodecFromFilePathViFFMpeg(videoProperties);
        String videoResolution = getResolutionFromFilePathViFFMpeg(videoProperties);
        Integer videoBitrate = getBitrateFromFilePathViFFMpeg(videoProperties);
        return VideoDto.builder()
                .videoBitrate(videoBitrate)
                .videoCodec(videoCodec)
                .videoResolution(videoResolution)
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
