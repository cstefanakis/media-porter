package org.sda.mediaporter.services.tvShowServices;

import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbTvShowSearchDTO;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.TvShowEpisode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sda.mediaporter.models.TvShow;


import java.time.LocalDateTime;
import java.util.List;

public interface TvShowService {

    TvShow updateTvShowModificationDateTime(TvShow tvShow, LocalDateTime modificationDateTime);
    TvShow getOrCreateTvShowByTitle(String tvShowTitle);

    void updateModificationDateTime(TvShow tvShow, LocalDateTime modificationDateTime);

    TvShow getTvShowById(Long id);

    Page<TvShow> getTvShows(Pageable pageable);

    List<TvShowEpisode> getTvShowEpisodesOlderThanXDays(int days);

    List<Long> getTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath(Integer days, SourcePath sourcePath);

    Long getTvShowIdByVideoFilePathId(Long videoFilePathId);

    void deleteTvShowWithoutTvShowEpisodes(Long tvShowId);

    void deleteVideoFilePathsFromTvShowsWithUnveiledPath();

    void deleteTvShowsWithoutTvShowEpisodes();

    TheMovieDbTvShowSearchDTO getTvShowAPISearchDTO(String searchTitle);
}
