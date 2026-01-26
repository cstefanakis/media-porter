package org.sda.mediaporter.services.tvShowServices.impl;

import org.sda.mediaporter.api.TheMovieDbTvShowsById;
import org.sda.mediaporter.dtos.AudioDto;
import org.sda.mediaporter.dtos.VideoDto;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbTvShowEpisodeDto;
import org.sda.mediaporter.models.*;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.services.ConfigurationService;
import org.sda.mediaporter.services.GenreService;
import org.sda.mediaporter.services.audioServices.AudioService;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.fileServices.SourcePathService;
import org.sda.mediaporter.services.fileServices.VideoFilePathService;
import org.sda.mediaporter.services.tvShowServices.TvShowEpisodeSchedulerService;
import org.sda.mediaporter.services.tvShowServices.TvShowEpisodeService;
import org.sda.mediaporter.services.tvShowServices.TvShowService;
import org.sda.mediaporter.services.videoServices.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TvShowEpisodeSchedulerServiceImpl implements TvShowEpisodeSchedulerService {

    private final SourcePathService sourcePathService;
    private final FileService fileService;
    private final TvShowEpisodeService tvShowEpisodeService;
    private final VideoFilePathService videoFilePathService;
    private final TvShowService tvShowService;
    private final VideoService videoService;
    private final AudioService audioService;
    private final GenreService genreService;
    private final ConfigurationService configurationService;

    @Autowired
    public TvShowEpisodeSchedulerServiceImpl(SourcePathService sourcePathService, FileService fileService, TvShowEpisodeService tvShowEpisodeService, VideoFilePathService videoFilePathService, TvShowService tvShowService, VideoService videoService, AudioService audioService, GenreService genreService, ConfigurationService configurationService) {
        this.sourcePathService = sourcePathService;
        this.fileService = fileService;
        this.tvShowEpisodeService = tvShowEpisodeService;
        this.videoFilePathService = videoFilePathService;
        this.tvShowService = tvShowService;
        this.videoService = videoService;
        this.audioService = audioService;
        this.genreService = genreService;
        this.configurationService = configurationService;
    }

    @Override
    @Scheduled(fixedRate = 20000)
    public void moveTvShowEpisodeFromDownloadsRootPathToTvShowsRootPath() {
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

    @Override
    @Scheduled(fixedRate = 20000)
    public void scanTvShowSourcePath() {
        SourcePath tvShowEpisodeSourcePath = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.TV_SHOW).getFirst();
        Path tvShowRootPath = Path.of(tvShowEpisodeSourcePath.getPath());

        List<Path> videoPaths = fileService.getVideoFiles(tvShowRootPath);

        for(Path filePath : videoPaths) {
            String filePathWithoutTvShowSourcePath = videoFilePathService.getFilePathWithoutSourcePath(filePath, tvShowEpisodeSourcePath);
            if (tvShowEpisodeService.getTvShowEpisodeByPathOrNull(filePathWithoutTvShowSourcePath) == null) {
                //Create a tvShowEpisode with TvShow
                TvShowEpisode tvShowEpisode = tvShowEpisodeService.createTvShowEpisodeFromPath(filePath);

                if (tvShowEpisode != null) {
                    //create VideoFilePath
                    VideoFilePath videoFilePath = videoFilePathService.createVideoFilePath(filePath);
                    //Add tvShowEpisode to VideoFilePath
                    videoFilePathService.addTvShowEpisode(tvShowEpisode, videoFilePath);
                }
            }
        }
    }

    @Override
    @Scheduled(fixedRate = 20000)
    public void deleteTvShowsOlderThan() {
        List<SourcePath> sourcePathsSource = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.TV_SHOW);
        for(SourcePath sourcePath : sourcePathsSource){
            Integer days = sourcePath.getConfiguration().getMaxDatesSaveFile();
            if (days != null) {
                //get tv Shows older than x days
                List<Long> tvShowsVideoFilePathIdsThatTvShowIsOlderThanXDays = tvShowService.getTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath(days, sourcePath);
                for(Long videoFilePathId : tvShowsVideoFilePathIdsThatTvShowIsOlderThanXDays){
                    //Get tv show episodes from videoFilePath
                    Long tvShowEpisodeId = tvShowEpisodeService.getTvShowEpisodeIdByVideoFilePathId(videoFilePathId);
                    Long tvShowId = tvShowService.getTvShowIdByVideoFilePathId(videoFilePathId);
                    //delete videoFilePath and file
                    videoFilePathService.deleteVideoFilePathAndFileByVideoFilePathId(videoFilePathId);
                    //delete tvShowEpisode without videoFilePath
                    tvShowEpisodeService.deleteTvShowEpisodeWithoutVideoFilePaths(tvShowEpisodeId);
                    //delete tvShow without tvShowEpisodes
                    tvShowService.deleteTvShowWithoutTvShowEpisodes(tvShowId);
                }
            }
        }
    }

    @Override
    @Scheduled(cron = "0 */1 * * * *")
    public void copyTvShowsEpisodesFromExternalSources() {
        videoFilePathService.deleteVideoFilePathsWithNullFilePath();
        //delete tvShows videoFilePaths with no validated paths
        tvShowService.deleteVideoFilePathsFromTvShowsWithUnveiledPath();
        //delete tvShowsEpisodes videoFilePaths with null FilePaths
        tvShowEpisodeService.deleteTvShowEpisodesWithoutVideoFilePaths();
        //delete tvShows without tvShowEpisodes
        tvShowService.deleteTvShowsWithoutTvShowEpisodes();
        //Copy Files
        List<SourcePath> externalTVShowSourcePaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.EXTERNAL, LibraryItems.TV_SHOW);
        List<SourcePath> downloadTvShowSourcePaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.DOWNLOAD, LibraryItems.TV_SHOW);
        if(!downloadTvShowSourcePaths.isEmpty()){
            System.out.println("downloadTvShowSourcePaths");
            SourcePath downloadSourcePath = downloadTvShowSourcePaths.getFirst();
            System.out.println(downloadSourcePath.getPath());
            for(SourcePath externalSourcePath : externalTVShowSourcePaths){
                System.out.println("go loop");
                getTvShowEpisodesFromVideoFilesAndCopyTo(externalSourcePath, downloadSourcePath);
            }
        }
    }

    private void getTvShowEpisodesFromVideoFilesAndCopyTo(SourcePath externalSourcePath, SourcePath downloadSourcePath) {
        Path sourcePathDirectory = Path.of(externalSourcePath.getPath());
        List<Path> videoPaths = fileService.getVideoFiles(sourcePathDirectory);
        for(Path filePath : videoPaths){
            System.out.println(filePath);
            //get tvShowEpisode
            TheMovieDbTvShowEpisodeDto theMovieDbTvShowEpisodeDto = tvShowEpisodeService.getTvShowEpisodeDtoByPath(filePath);
            if(theMovieDbTvShowEpisodeDto != null){
                Long tvShowEpisodeTheMovieDbId = theMovieDbTvShowEpisodeDto.getTheMovieDbId();
                TheMovieDbTvShowsById theMovieDbTvShowsById = new TheMovieDbTvShowsById(theMovieDbTvShowEpisodeDto.getTvShowTheMovieDbId());
                boolean isTvShowExist = tvShowEpisodeService.isExistTvShowEpisodeWithTheMovieDbId(tvShowEpisodeTheMovieDbId);
                if(!isTvShowExist){
                    VideoDto videoDto = videoService.getVideoDetails(filePath);
                    System.out.println(videoDto.getVideoCodec());
                    List<AudioDto> audiosDto = audioService.getAudiosDetails(filePath);
                    List <String> tvShowEpisodeGenresString = theMovieDbTvShowsById.getTheMovieDbTvShowDto().getGenres();
                    List <Genre> genres = tvShowEpisodeGenresString.stream().map(genreService::getOrCreateGenre).toList();
                    double fileSize = fileService.getFileSizeInMB(filePath);
                    LocalDateTime fileModificationDateTime = fileService.getModificationLocalDateTimeOfPath(filePath);
                    boolean isFileForCopy = configurationService.isFileForCopy(videoDto, audiosDto, genres, externalSourcePath, fileSize, fileModificationDateTime);
                    if(isFileForCopy){
                        Path filePathRoot = Path.of(externalSourcePath.getPath());
                        Path newPathRoot = Path.of(downloadSourcePath.getPath());
                        Path destinationFilePath = sourcePathService.replaceRootOfFilePathWithOtherRoot(filePath, filePathRoot, newPathRoot);
                        fileService.copyFile(filePath, destinationFilePath);
                    }
                }
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
