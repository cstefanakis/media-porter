package org.sda.mediaporter.services.fileServices.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.sda.mediaporter.models.*;
import org.sda.mediaporter.models.metadata.*;
import org.sda.mediaporter.repositories.VideoFilePathRepository;
import org.sda.mediaporter.services.audioServices.AudioService;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.fileServices.SourcePathService;
import org.sda.mediaporter.services.fileServices.VideoFilePathService;
import org.sda.mediaporter.services.movieServices.MovieService;
import org.sda.mediaporter.services.subtitleServices.SubtitleService;
import org.sda.mediaporter.services.tvShowServices.TvShowEpisodeService;
import org.sda.mediaporter.services.videoServices.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
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
    public void updateSourcePathFileAndPath(VideoFilePath videoFilePath, Movie movie, TvShowEpisode tvShowEpisode, Path newFilePath){

        SourcePath sourcePath = sourcePathService.getSourcePathFromPath(newFilePath);
        String filePathWithoutSourcePath = getPathWithoutSourcePath(newFilePath, sourcePath);
        String newFilePathString = newFilePath.toString();
        Optional<VideoFilePath> videoFilePathOptional = videoFilePathRepository.findVideoFilePathByPathAndSourcePath(filePathWithoutSourcePath, sourcePath);

        if(videoFilePathOptional.isPresent() && !isVideoFilePathWithPathExist(newFilePathString)) {
            System.out.println("strat delete video file path");
            deleteVideoFilePath(videoFilePathOptional.get(), movie, tvShowEpisode);

        }

        if (!isVideoFilePathWithPathExist(newFilePathString)) {
            Path originalFilePath = getFullPathOfVideoFilePath(videoFilePath);
            fileService.moveFile(originalFilePath, newFilePath);
            videoFilePath.setSourcePath(sourcePath);
            videoFilePath.setFilePath(filePathWithoutSourcePath);
            videoFilePathRepository.save(videoFilePath);
        }
    }

    @Override
    @Transactional
    public void deleteVideoFilePath(VideoFilePath videoFilePath, Movie movie, TvShowEpisode tvShowEpisode) {

        removeVideoFilePAthFromTvShowEpisode(videoFilePath, tvShowEpisode);
        System.out.println("removedFromTvShowEpisode");
        videoFilePathRepository.delete(videoFilePath);
        System.out.println("delele");
    }

    private void removeVideoFilePAthFromTvShowEpisode(VideoFilePath videoFilePath, TvShowEpisode tvShowEpisode){
        if(tvShowEpisode != null){
            tvShowEpisode.getVideoFilePaths().remove(videoFilePath);
            videoFilePath.setTvShowEpisode(null);
        }
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

    @Override
    @Transactional
    public void addTvShowEpisode(TvShowEpisode tvShowEpisode, VideoFilePath videoFilePath) {
        videoFilePath.setTvShowEpisode(tvShowEpisode);
        videoFilePathRepository.save(videoFilePath);
    }

    @Override
    public String getVideoFileNamePart(Video video) {
        if(video == null){
            return "";
        }
        return " (" + String.format("%s %s",verifiedVideoResolution(video), verifiedVideoCodec(video)).trim() + ")";
    }

    @Override
    public String getAudioFileNamePart(List<Audio> audios) {
        if(audios == null || audios.isEmpty()){
            return "";
        }
        String audioString = "";
        for(Audio audio : audios){
            audioString = audioString + verifiedAudioString(audio) + " ";
        }
        return " (" + audioString.trim() + ")";
    }

    private String verifiedAudioString(Audio audio){
        String language = verifiedLanguage(audio);
        String channels = verifiedAudioChannel(audio);
        if(language.isBlank() && channels.isBlank()){
            return "";
        }else{
            return " [" + (channels + " " + language).trim() + "]";
        }
    }

    private String verifiedLanguage(Audio audio){
        Language language = audio.getLanguage();
        return language !=null
                ? language.getIso6392B().toUpperCase(Locale.ROOT)
                : "";
    }

    private String verifiedAudioChannel(Audio audio){
        AudioChannel audioChannel = audio.getAudioChannel();
        return audioChannel !=null
                ? audioChannel.getChannels().toString()
                : "";
    }

    private String verifiedVideoResolution(Video video){
        Resolution resolution = video.getResolution();
        return resolution != null
                ? fileService.getSafeFileName(resolution.getName())
                : "";
    }

    private String verifiedVideoCodec(Video video){
        Codec codec = video.getCodec();
        return codec != null
                ? fileService.getSafeFileName(codec.getName())
                : "";
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
