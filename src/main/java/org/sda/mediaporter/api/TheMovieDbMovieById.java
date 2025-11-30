package org.sda.mediaporter.api;

import lombok.Getter;
import org.json.JSONObject;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbMovieDto;

public class TheMovieDbMovieById {
    private final TheMovieDb theMovieDb = new TheMovieDb();

    @Getter
    private TheMovieDbMovieDto theMovieDbMovieDto;

    public TheMovieDbMovieById(Long theMoveDbId) {
        JSONObject root = result(theMoveDbId);
        this.theMovieDbMovieDto = buildTheMovieDbMovieDto(root);
    }

    private JSONObject result(Long theMovieDbId){
        String url = String.format("https://api.themoviedb.org/3/movie/%s?api_key=%s&language=en-US", theMovieDbId, theMovieDb.getApiKey());
        ApiConnect apiConnect = new ApiConnect(url);
        return apiConnect.getRootJsonObject();
    }

    private TheMovieDbMovieDto buildTheMovieDbMovieDto(JSONObject jsonObject){
        return TheMovieDbMovieDto.builder()
                .title(theMovieDb.getValidatedStringJsonObject(jsonObject, "title"))
                .originalTitle(theMovieDb.getValidatedStringJsonObject(jsonObject, "original_title"))
                .year(theMovieDb.getYearValidatedLocalDateJsonObject(jsonObject, "release_date"))
                .homePage(theMovieDb.getValidatedStringJsonObject(jsonObject, "homepage"))
                .languageCode(theMovieDb.getValidatedStringJsonObject(jsonObject, "original_language"))
                .countries(theMovieDb.getListOfStringsOfJsonArrayFromObject(jsonObject, "production_companies","origin_country"))
                .genres(theMovieDb.getListOfStringsOfJsonArrayFromObject(jsonObject, "genres", "name"))
                .overview(theMovieDb.getValidatedStringJsonObject(jsonObject, "overview"))
                .poster(theMovieDb.getPosterRootPath() + theMovieDb.getValidatedStringJsonObject(jsonObject, "poster_path"))
                .status(theMovieDb.getValidatedStringJsonObject(jsonObject, "status"))
                .rate(theMovieDb.getValidatedDoubleJsonObject(jsonObject, "vote_average"))
                .theMoveDbId(theMovieDb.getValidatedLongJsonObject(jsonObject, "id"))
                .releaseDate(theMovieDb.getValidatedLocalDateJsonObject(jsonObject, "release_date"))
                .build();
    }
}