package org.sda.mediaporter.services.fileServices.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.repositories.VideoFilePathRepository;
import org.sda.mediaporter.services.audioServices.AudioService;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.fileServices.SourcePathService;
import org.sda.mediaporter.services.fileServices.VideoFilePathService;
import org.sda.mediaporter.services.subtitleServices.SubtitleService;
import org.sda.mediaporter.services.videoServices.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VideoFilePathServiceImpl implements VideoFilePathService {

    private final VideoFilePathRepository videoFilePathRepository;
    private final VideoService videoService;
    private final AudioService audioService;
    private final SubtitleService subtitleService;
    private final SourcePathService sourcePathService;
    private final FileService fileService;

    @Autowired
    public VideoFilePathServiceImpl(VideoFilePathRepository videoFilePathRepository, VideoService videoService, AudioService audioService, SubtitleService subtitleService, SourcePathService sourcePathService, FileService fileService) {
        this.videoFilePathRepository = videoFilePathRepository;
        this.videoService = videoService;
        this.audioService = audioService;
        this.subtitleService = subtitleService;
        this.sourcePathService = sourcePathService;
        this.fileService = fileService;
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
    public VideoFilePath getVideoFilePath(Path filePath) {
        SourcePath sourcePath = sourcePathService.getSourcePathFromPath(filePath);
        VideoFilePath videoFilePath = toEntity(filePath, sourcePath);
        videoService.getVideoFromPath(filePath, videoFilePath);
        audioService.getAudiosFromPathFile(filePath, videoFilePath);
        subtitleService.getSubtitleListFromFile(filePath, videoFilePath);

        return videoFilePath;
    }

    @Override
    public VideoFilePath createVideoFilePath(Path filePath) {
        SourcePath sourcePath = sourcePathService.getSourcePathFromPath(filePath);
        String filePathWithoutSourcePath = getPathWithoutSourcePath(filePath, sourcePath);
        Optional<VideoFilePath> videoFilePathOptional = videoFilePathRepository.findVideoFilePathByPath(filePathWithoutSourcePath);
        if(videoFilePathOptional.isPresent()){
            return videoFilePathOptional.get();
        }
        else {
            VideoFilePath videoFilePath = videoFilePathRepository.save(toEntity(filePath, sourcePath));
            videoService.createVideoFromPath(filePath, videoFilePath);
            audioService.getCreatedAudiosFromPathFile(filePath, videoFilePath);
            subtitleService.createSubtitleListFromFile(filePath, videoFilePath);
            return videoFilePath;
        }
    }

    @Override
    public VideoFilePath updateSourcePathFileAndPath(VideoFilePath videoFilePath, Path newFilePath){
        Path originalFilePath = getFullPathOfVideoFilePath(videoFilePath);
        String newFilePathString = newFilePath.toString();
        fileService.moveFile(originalFilePath, newFilePath);
        if (!isVideoFilePathWithPathExist(newFilePathString)){
            SourcePath sourcePath = sourcePathService.getSourcePathFromPath(newFilePath);
            String filePathWithoutSourcePath = getPathWithoutSourcePath(newFilePath, sourcePath);
            videoFilePath.setSourcePath(sourcePath);
            videoFilePath.setFilePath(filePathWithoutSourcePath);
            return videoFilePathRepository.save(videoFilePath);
        } else {
            throw new EntityExistsException(String.format("File path %s already exist", newFilePath));
        }
    }

    @Override
    public void deleteVideoFilePath(VideoFilePath videoFilePath) {
        videoFilePathRepository.delete(videoFilePath);
    }

    @Override
    public List<VideoFilePath> getAllVideoFilePaths() {
        return videoFilePathRepository.findAll();
    }

    @Override
    public boolean isVideoFilePathWithPathExist(String videoFilePathPath) {
        return videoFilePathRepository.findVideoFilePathByPath(videoFilePathPath).isPresent();
    }

    @Override
    @Transactional
    public void addMovie(Movie movie, VideoFilePath videoFilePath) {
        videoFilePath.setMovie(movie);
        videoFilePathRepository.save(videoFilePath);
    }

    private VideoFilePath toEntity(Path filePath, SourcePath sourcePath){
        return VideoFilePath.builder()
                .filePath(getPathWithoutSourcePath(filePath, sourcePath))
                .sourcePath(sourcePath)
                .build();
    }

    private String getPathWithoutSourcePath(Path filePath, SourcePath sourcePath){
        String path = filePath.toString();
        return path.replace(sourcePath.getPath(), File.separator);
    }

    private Path getFullPathOfVideoFilePath(VideoFilePath videoFilePath){
        String pathOfSourcePath = videoFilePath.getSourcePath().getPath();
        String path = videoFilePath.getFilePath();
        return Path.of(pathOfSourcePath + path);
    }
}
