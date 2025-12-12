package org.sda.mediaporter.api;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbTvShowSearchDTO;
import java.util.ArrayList;
import java.util.List;

public class TheMovieDbTvShowSearch {
    private final TheMovieDb theMovieDb = new TheMovieDb();
    private final JSONObject root;

    @Getter
    List<TheMovieDbTvShowSearchDTO> tvShows;

    public TheMovieDbTvShowSearch(String tvShowName) {
        this.root = tvShowResults(tvShowName);
        this.tvShows = getValidatedTVShows();

    }

    private JSONObject tvShowResults (String tvShowName){
        String url = String.format("https://api.themoviedb.org/3/search/tv?api_key=%s&query=%s", theMovieDb.getApiKey(), tvShowName.replace(" ", "+"));
        ApiConnect apiConnect = new ApiConnect(url);
        String jsonFile = apiConnect.getJsonString();
        return new JSONObject(jsonFile);
    }

    private List<TheMovieDbTvShowSearchDTO> getValidatedTVShows(){
        String objectKey = "results";
        List<TheMovieDbTvShowSearchDTO> tvShowsList = new ArrayList<>();
        if(theMovieDb.isValidatedObject(this.root, objectKey)){
            JSONArray tvShows = this.root.getJSONArray(objectKey);
            for (int i = 0; i < tvShows.length(); i++) {
                JSONObject tvShow = tvShows.getJSONObject(i);
                tvShowsList.add(buildTheMovieDbTvShowSearchDTO(tvShow));
            }
        }
        return tvShowsList;
    }

    private TheMovieDbTvShowSearchDTO buildTheMovieDbTvShowSearchDTO(JSONObject jsonObject){
        return TheMovieDbTvShowSearchDTO.builder()
                .theMovieDbId(theMovieDb.getValidatedLongJsonObject(jsonObject, "id"))
                .title(theMovieDb.getValidatedStringJsonObject(jsonObject, "name"))
                .originalTitle(theMovieDb.getValidatedStringJsonObject(jsonObject, "original_name"))
                .year(theMovieDb.getYearFromLocalDateObject(jsonObject, "first_air_date"))
                .originalLanguage(theMovieDb.getValidatedStringJsonObject(jsonObject, "original_language"))
                .overview(theMovieDb.getValidatedStringJsonObject(jsonObject, "overview"))
                .rate(theMovieDb.getValidatedDoubleJsonObject(jsonObject, "vote_average"))
                .poster(theMovieDb.getPosterRootPath() + theMovieDb.getValidatedStringJsonObject(jsonObject, "poster_path"))
                .build();
    }
}
