package org.sda.mediaporter.api;

import lombok.Getter;
import org.json.JSONObject;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbTvShowEpisodeDto;

public class TheMovieDbTvShowEpisodes {

    private final TheMovieDb theMovieDb = new TheMovieDb();

    @Getter
    private TheMovieDbTvShowEpisodeDto theMovieDbTvShowEpisodeDto;

    public TheMovieDbTvShowEpisodes(Long tvShowId, int season, int episode) {
        JSONObject root = result(tvShowId, season, episode);
        theMovieDbTvShowEpisodeDto = buildTheMovieDbTvShowEpisodeDto(root);
    }

    private JSONObject result(Long tvShowId, int season, int episode){
        String url = String.format("https://api.themoviedb.org/3/tv/%s/season/%s/episode/%s?api_key=%s&language=en-US",tvShowId, season, episode, theMovieDb.getApiKey());
        ApiConnect apiConnect = new ApiConnect(url);
        return apiConnect.getRootJsonObject();
    }

    private TheMovieDbTvShowEpisodeDto buildTheMovieDbTvShowEpisodeDto(JSONObject jsonObject){
        return TheMovieDbTvShowEpisodeDto.builder()
                .airDate(theMovieDb.getValidatedLocalDateJsonObject(jsonObject, "air_date"))
                .writers(theMovieDb.getContributorsFromCrewByDepartment(jsonObject, "Writing"))
                .directors(theMovieDb.getContributorsFromCrewByDepartment(jsonObject, "Directing"))
                .actors(theMovieDb.getContributorsFromCast(jsonObject,"guest_stars"))
                .episodeNumber(theMovieDb.getValidatedIntegerJsonObject(jsonObject, "episode_number"))
                .episodeType(theMovieDb.getValidatedStringJsonObject(jsonObject, "episode_type"))
                .seasonNumber(theMovieDb.getValidatedIntegerJsonObject(jsonObject, "season_number"))
                .episodeName(theMovieDb.getValidatedStringJsonObject(jsonObject, "name"))
                .overview(theMovieDb.getValidatedStringJsonObject(jsonObject, "overview"))
                .rate(theMovieDb.getValidatedDoubleJsonObject(jsonObject, "vote_average"))
                .poster(theMovieDb.getPosterRootPath() + theMovieDb.getValidatedStringJsonObject(jsonObject, "still_path"))
                .theMovieDbId(theMovieDb.getValidatedLongJsonObject(jsonObject, "id"))
                .build();
    }
}
