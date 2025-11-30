package org.sda.mediaporter.services.fileServices.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.models.metadata.Video;
import org.sda.mediaporter.repositories.VideoFilePathRepository;
import org.sda.mediaporter.services.audioServices.AudioService;
import org.sda.mediaporter.services.fileServices.SourcePathService;
import org.sda.mediaporter.services.fileServices.VideoFilePathService;
import org.sda.mediaporter.services.subtitleServices.SubtitleService;
import org.sda.mediaporter.services.videoServices.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class VideoFilePathServiceImpl implements VideoFilePathService {

    private final VideoFilePathRepository videoFilePathRepository;
    private final VideoService videoService;
    private final AudioService audioService;
    private final SubtitleService subtitleService;
    private final SourcePathService sourcePathService;

    @Autowired
    public VideoFilePathServiceImpl(VideoFilePathRepository videoFilePathRepository, VideoService videoService, AudioService audioService, SubtitleService subtitleService, SourcePathService sourcePathService) {
        this.videoFilePathRepository = videoFilePathRepository;
        this.videoService = videoService;
        this.audioService = audioService;
        this.subtitleService = subtitleService;
        this.sourcePathService = sourcePathService;
    }

    @Override
    public VideoFilePath getVideoFilePathByPath(String videoFilePathFile) {

        return videoFilePathRepository.findVideoFilePathByPath(videoFilePathFile)
                .orElseThrow(() -> new EntityNotFoundException(String.format("video path file with path: %s not found", videoFilePathFile)));
    }

    @Override
    public VideoFilePath getVideoFilePathById(Long videoPathFileId) {
        return videoFilePathRepository.findById(videoPathFileId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("video path file with id: %s not found", videoPathFileId)));
    }

    @Override
    public VideoFilePath getVideoFilePath(Path videoFilePathFile) {


        Video video = videoService.getVideoFromPath(videoFilePathFile);
        List<Audio> audios = audioService.getCreatedAudiosFromPathFile(videoFilePathFile);
        List<Subtitle> subtitles = subtitleService.getSubtitleListFromFile(videoFilePathFile);


        return toEntity(videoFilePathFile, video, audios, subtitles);
    }

    @Override
    public VideoFilePath createVideoFilePath(SourcePath sourcePath, Path videoFilePathFile) {
        Optional<VideoFilePath> videoFilePathOptional = videoFilePathRepository.findVideoFilePathByPath(videoFilePathFile.toString());
        if(videoFilePathOptional.isEmpty()){
            Video video = videoService.createVideoFromPath(videoFilePathFile);
            List<Audio> audios = audioService.getCreatedAudiosFromPathFile(videoFilePathFile);
            List<Subtitle> subtitles = subtitleService.createSubtitleListFromFile(videoFilePathFile);
            videoFilePathRepository.save(toEntity(videoFilePathFile, video, audios, subtitles));
        }
        throw new EntityExistsException(String.format("File with path: %s already exist", videoFilePathFile));
    }

    @Override
    public void deleteVideoFilePath(VideoFilePath videoFilePath) {
        videoFilePathRepository.delete(videoFilePath);
    }

    @Override
    public List<VideoFilePath> getAllVideoFilePaths() {
        return videoFilePathRepository.findAll();
    }

    private VideoFilePath toEntity(Path videoFilePath, Video video, List<Audio> audios, List<Subtitle> subtitles){
        SourcePath sourcePath = sourcePathService.getSourcePathByPath(videoFilePath);
        String pathWithoutSourcePath = videoFilePath.toString()
                .replace(sourcePath.getPath(), File.separator);
        return VideoFilePath.builder()
                .filePath(pathWithoutSourcePath)
                .video(video)
                .audios(audios)
                .subtitles(subtitles)
                .sourcePath(sourcePath)
                .sourcePath(sourcePath)
                .build();
    }
}
