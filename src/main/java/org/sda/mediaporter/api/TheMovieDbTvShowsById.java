package org.sda.mediaporter.api;

import lombok.Getter;
import org.json.JSONObject;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbTvShowDto;

import java.time.LocalDate;

public class TheMovieDbTvShowsById {
    private final TheMovieDb theMovieDb = new TheMovieDb();

    @Getter
    private TheMovieDbTvShowDto theMovieDbTvShowDto;

    public TheMovieDbTvShowsById(Long tvShowId){
        JSONObject root = result(tvShowId);
        LocalDate firstAirDate = theMovieDb.getValidatedLocalDateJsonObject(root, "first_air_date");
        this.theMovieDbTvShowDto = TheMovieDbTvShowDto.builder()
                    .title(theMovieDb.getValidatedStringJsonObject(root, "name"))
                    .originalTitle(theMovieDb.getValidatedStringJsonObject(root, "original_name"))
                    .firstAirDate(firstAirDate)
                    .lastAirDate(theMovieDb.getValidatedLocalDateJsonObject(root, "last_air_date"))
                    .homePage(theMovieDb.getValidatedStringJsonObject(root, "homepage"))
                    .languageCode(theMovieDb.getValidatedStringJsonObject(root, "original_language"))
                    .countriesCodes(theMovieDb.getListOfStringsOfArray(root, "origin_country"))
                    .genres(theMovieDb.getListOfStringsOfJsonArrayFromObject(root, "genres", "name"))
                    .overview(theMovieDb.getValidatedStringJsonObject(root, "overview"))
                    .poster(theMovieDb.getPosterRootPath() + theMovieDb.getValidatedStringJsonObject(root, "poster_path"))
                    .status(theMovieDb.getValidatedStringJsonObject(root, "status"))
                    .rate(theMovieDb.getValidatedDoubleJsonObject(root, "vote_average"))
                    .year(firstAirDate.getYear())
                .build();
    }

    private JSONObject result(Long tvShowId){
        String url = String.format("https://api.themoviedb.org/3/tv/%s?api_key=%s&language=en-US", tvShowId, theMovieDb.getApiKey());
        ApiConnect apiConnect = new ApiConnect(url);
        return apiConnect.getRootJsonObject();
    }
}
