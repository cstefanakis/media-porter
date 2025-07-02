package org.sda.mediaporter.Services.Impl;

import org.sda.mediaporter.Services.CodecService;
import org.sda.mediaporter.Services.ResolutionService;
import org.sda.mediaporter.Services.VideoService;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;
import org.sda.mediaporter.models.metadata.Video;
import org.sda.mediaporter.repositories.metadata.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;
    private final CodecService codecService;
    private final ResolutionService resolutionService;

    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository, CodecService codecService, ResolutionService resolutionService) {
        this.videoRepository = videoRepository;
        this.codecService = codecService;
        this.resolutionService = resolutionService;
    }

    @Override
    public Video createVideoFromPath(Path file, Movie movie) {
        if(!videoInfo(file).isEmpty()) {
            String[] properties = videoInfo(file).split(",", -1);
            try {
                String codecName = properties[0].isEmpty()  || properties[0].trim().equals("N/A")? null : properties[0].replaceAll("[^a-zA-Z0-9]", "");
                Integer resolutionHeight = properties[1].isEmpty() || properties[1].trim().equals("N/A") ? null : Integer.parseInt(properties[1].replaceAll("[^a-zA-Z0-9]", ""));
                Integer videoBitrate = properties[2].isEmpty() || properties[2].trim().equals("N/A") ? null : Integer.parseInt(properties[2].replaceAll("[^a-zA-Z0-9]", ""));

                Codec videoCodec = codecName == null? null : codecService.getCodecByNameAndMediaType(codecName, MediaTypes.VIDEO);
                Resolution resolution = generatedResolution(resolutionHeight);
                Integer videoBitrateKbps = videoBitrate == null? null: videoBitrate / 1000;

                videoRepository.save(Video.builder()
                                .codec(videoCodec)
                                .resolution(resolution)
                                .bitrate(videoBitrateKbps)
                                .movie(movie)
                        .build());
            }catch (ResolutionException e){
                return null;
            }

        }return null;
    }

    private Video toEntity(Video updatedVideo, Video video) {
        updatedVideo.setResolution(video.getResolution() == null? updatedVideo.getResolution() : video.getResolution());
        updatedVideo.setCodec(video.getCodec() == null? updatedVideo.getCodec() : video.getCodec());
        updatedVideo.setBitrate(video.getBitrate() == null? updatedVideo.getBitrate() : video.getBitrate());
        return updatedVideo;
    }

    private Resolution generatedResolution(Integer height){
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

        String resolutionName = null;

        for(Map.Entry<Integer, String> entity: resolutions.entrySet()){
            if(height > entity.getKey()){
                resolutionName = entity.getValue();
            }
        }

        return resolutionService.getResolutionByName(resolutionName);
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
