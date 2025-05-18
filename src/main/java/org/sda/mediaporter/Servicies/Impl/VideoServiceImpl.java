package org.sda.mediaporter.Servicies.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Servicies.CodecService;
import org.sda.mediaporter.Servicies.VideoService;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.enums.Resolutions;
import org.sda.mediaporter.models.metadata.Video;
import org.sda.mediaporter.repositories.metadata.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Optional;

@Service
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;
    private final CodecService codecService;

    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository, CodecService codecService) {
        this.videoRepository = videoRepository;
        this.codecService = codecService;
    }

    @Override
    public Video createVideo(Video video) {
        return videoRepository.save(video);
    }

    @Override
    public Video createVideoFromPath(Path file) {
        String[] properties = videoInfo(file).split(",");
        Video video = new Video();
        if(properties.length > 1){
            video.setCodec(codecService.autoCreateCodec(properties[1]));
        }
        if(properties.length > 3){
            video.setResolution(generatedResolution(properties[2], properties[3]));
        }
        if(properties.length > 4){
            video.setBitrate(FileServiceImpl.convertStringToInt(properties[4]));
        }
        return videoRepository.save(video);
    }

    @Override
    public Video updateMovieVideo(Long id, Video video, Movie movie) {
        Optional<Video> videoOptional = videoRepository.findById(id);
        if(videoOptional.isPresent()){
            Video videoToUpdate = videoOptional.get();
            videoToUpdate.setMovie(movie);
            return videoRepository.save(toEntity(videoToUpdate, video));
        }throw new EntityNotFoundException(String.format("Video with id %s not found", id));
    }

    private Video toEntity(Video updatedVideo, Video video) {
        updatedVideo.setResolution(video.getResolution() == null? updatedVideo.getResolution() : video.getResolution());
        updatedVideo.setCodec(video.getCodec() == null? updatedVideo.getCodec() : video.getCodec());
        updatedVideo.setBitrate(video.getBitrate() == null? updatedVideo.getBitrate() : video.getBitrate());
        return updatedVideo;
    }

    private String generatedResolution(String widthStr, String heightStr){
        Integer width = FileServiceImpl.convertStringToInt(widthStr);
        Integer height = FileServiceImpl.convertStringToInt(heightStr);
        for(Resolutions resolution : Resolutions.values()){
            if(width !=null &&
                    resolution.getWidth() == width && height != null &&
                    resolution.getHeight() == height ){
                return resolution.getLabel();
            }

            if(width != null && resolution.getWidth() == width){
                return resolution.getLabel();
            }
        }return null;
    }

    private String videoInfo(Path filePath){
        return  FileServiceImpl.runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=index,codec_name,width,height,bit_rate",
                "-of", "csv=p=0",
                filePath.toString()
        });
    }
}
