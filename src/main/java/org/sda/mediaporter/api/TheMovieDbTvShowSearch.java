package org.sda.mediaporter.api;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sda.mediaporter.models.TvShow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TheMovieDbTvShowSearch {
    private final String apiKey = "7c97b163195d9428522398e8f1c32f63";
    private final JSONObject results;

    @Getter
    List<TvShow> tvShows;

    public TheMovieDbTvShowSearch(String tvShowName) {
        this.results = tvShowResults(tvShowName);
        this.tvShows = getValidatedTVShows();

    }

    private JSONObject tvShowResults (String tvShowName){
        String url = String.format("https://api.themoviedb.org/3/search/tv?api_key=%s&query=%s", this.apiKey, tvShowName.replace(" ", "+"));
        ApiConnect apiConnect = new ApiConnect(url);
        String jsonFile = apiConnect.getJsonString();
        return new JSONObject(jsonFile);
    }

    private List<TvShow> getValidatedTVShows(){
        String objectKey = "results";
        List<TvShow> tvShowsList = new ArrayList<>();
        if(checkObjects(this.results, objectKey)){
            JSONArray tvShows = this.results.getJSONArray(objectKey);
            for (int i = 0; i < tvShows.length(); i++) {
                JSONObject tvShow = tvShows.getJSONObject(i);
                tvShowsList.add(TvShow.builder()
                                .id(getValidatedId(tvShow, "id"))
                                .title(getValidatedStringObject(tvShow, "name"))
                                .originalTitle(getValidatedStringObject(tvShow, "original_name"))
                                .year(getYearFromLocalDateObject(tvShow, "first_air_date"))
                        .build());
            }
        }
        return tvShowsList;
    }

    private Long getValidatedId (JSONObject object, String key){
        return checkObjects(object, key)
                ? object.getLong(key)
                : null;
    }

    private String getValidatedStringObject(JSONObject object, String key){
        return checkObjects(object, key)
                ? object.getString(key)
                : null;
    }

    private Integer getYearFromLocalDateObject(JSONObject object, String key){
        return checkObjects(object, key)
                ? LocalDate.parse(object.getString(key)).getYear()
                : null;
    }

    private boolean checkObjects(JSONObject object, String key){
        return object != null && (!object.isNull(key) || !object.isEmpty() || object.has(key));
    }
}
