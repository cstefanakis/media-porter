package org.sda.mediaporter.services.fileServices;

import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.TvShowEpisode;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.Video;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public interface VideoFilePathService {
    VideoFilePath getVideoFilePathByPath(String videoFilePathFile);
    VideoFilePath getVideoFilePathById(Long videoPathFileId);
    VideoFilePath getVideoFilePath(Path videoFilePathFile);
    VideoFilePath createVideoFilePath(Path videoFilePathFile);
    void updateSourcePathFileAndPath(VideoFilePath videoFilePath, Movie movie, TvShowEpisode tvShowEpisode, Path newFilePath);
    void deleteVideoFilePath(VideoFilePath videoFilePath, Movie movie, TvShowEpisode tvShowEpisode);
    List<VideoFilePath> getAllVideoFilePaths();
    boolean isVideoFilePathWithPathExist(String filePath);
    void addMovie(Movie movie, VideoFilePath videoFilePath);

    void addTvShowEpisode(TvShowEpisode tvShowEpisode, VideoFilePath videoFilePath);

    String getVideoFileNamePart(Video video);

    String getAudioFileNamePart(List<Audio> audios);
}
