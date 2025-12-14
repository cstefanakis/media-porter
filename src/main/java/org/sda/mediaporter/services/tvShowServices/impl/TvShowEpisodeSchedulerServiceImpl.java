package org.sda.mediaporter.services.tvShowServices.impl;

import jakarta.transaction.Transactional;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.TvShow;
import org.sda.mediaporter.models.TvShowEpisode;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.fileServices.SourcePathService;
import org.sda.mediaporter.services.fileServices.VideoFilePathService;
import org.sda.mediaporter.services.tvShowServices.TvShowEpisodeSchedulerService;
import org.sda.mediaporter.services.tvShowServices.TvShowEpisodeService;
import org.sda.mediaporter.services.tvShowServices.TvShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Service
public class TvShowEpisodeSchedulerServiceImpl implements TvShowEpisodeSchedulerService {

    private final SourcePathService sourcePathService;
    private final FileService fileService;
    private final TvShowEpisodeService tvShowEpisodeService;
    private final VideoFilePathService videoFilePathService;
    private final TvShowService tvShowService;

    @Autowired
    public TvShowEpisodeSchedulerServiceImpl(SourcePathService sourcePathService, FileService fileService, TvShowEpisodeService tvShowEpisodeService, VideoFilePathService videoFilePathService, TvShowService tvShowService) {
        this.sourcePathService = sourcePathService;
        this.fileService = fileService;
        this.tvShowEpisodeService = tvShowEpisodeService;
        this.videoFilePathService = videoFilePathService;
        this.tvShowService = tvShowService;
    }

    @Override
    @Scheduled(fixedRate = 20000)
    public void moveTvShowEpisodeFromDownloadsRootPathToMovieRootPath() {
        SourcePath downloadsSourcePath = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.DOWNLOAD, LibraryItems.TV_SHOW).getFirst();
        SourcePath tvShowEpisodeSourcePath = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.TV_SHOW).getFirst();

        Path downloadsRootPath = Path.of(downloadsSourcePath.getPath());
        Path tvShowEpisodeRootPath = Path.of(tvShowEpisodeSourcePath.getPath());

        List<Path> videoPaths = fileService.getVideoFiles(downloadsRootPath);

        for(Path filePath : videoPaths){
            //Create a tvShowEpisode with TvShow
            TvShowEpisode tvShowEpisode = tvShowEpisodeService.createTvShowEpisodeFromPath(filePath);

            if(tvShowEpisode != null){
                TvShow tvShow = tvShowService.getTvShowById(tvShowEpisode.getTvShow().getId());
                //create VideoFilePath
                VideoFilePath videoFilePath = videoFilePathService.createVideoFilePath(filePath);
                //Add tvShowEpisode to VideoFilePath
                videoFilePathService.addTvShowEpisode(tvShowEpisode, videoFilePath);
                //get extension of a file;
                String fileNameOfPath = filePath.getFileName().toString();
                String fileExtension = fileService.getFileExtensionWithDot(fileNameOfPath);
                //Create a path to move a file
                Path newTvShowEpisodePath = newTvShowPath(tvShowEpisode, tvShow, tvShowEpisodeRootPath, fileExtension, videoFilePath);
                //update videoFilePath with new filePath
                videoFilePathService.updateSourcePathFileAndPath(videoFilePath,null, tvShowEpisode, newTvShowEpisodePath);
                tvShowEpisodeService.updateModificationDateTime(tvShowEpisode, newTvShowEpisodePath);
                tvShowService.updateModificationDateTime(tvShow, tvShowEpisode.getModificationDateTime());
            }
        }
    }

    private Path newTvShowPath(TvShowEpisode tvShowEpisode, TvShow tvShow,  Path tvShowEpisodeRootPath, String fileExtension, VideoFilePath videoFilePath) {
        String tvShowEpisodeTitle = fileService.getSafeFileName(tvShowEpisode.getEpisodeName());
        String tvShowEpisodeSeason = String.format("%02d", tvShowEpisode.getSeasonNumber());
        String tvShowEpisodeEpisode = String.format("%02d", tvShowEpisode.getEpisodeNumber());
        String tvShowTitle = fileService.getSafeFileName(tvShow.getTitle());
        Integer tvShowYear = tvShow.getYear();
        String tvShowEpisodeVideo = videoFilePathService.getVideoFileNamePart(videoFilePath.getVideo());
        String tvShowEpisodeAudios = videoFilePathService.getAudioFileNamePart(videoFilePath.getAudios());
        String tvShowTitleAndYear = String.format("%s (%s)", tvShowTitle, tvShowYear);
        String movieFileName = String.format("%s - S%sE%s - %s %s%s%s", tvShowTitleAndYear , tvShowEpisodeSeason, tvShowEpisodeEpisode, tvShowEpisodeTitle,tvShowEpisodeVideo, tvShowEpisodeAudios, fileExtension);
        return Path.of(tvShowEpisodeRootPath
                + File.separator
                + tvShowTitleAndYear
                + File.separator
                + String.format("Season %s", tvShowEpisodeSeason)
                + File.separator
                + movieFileName);
    }
}
