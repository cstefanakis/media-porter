package org.sda.mediaporter.api;


import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TheMovieDbTvShowsById {
    private final String apiKey = "7c97b163195d9428522398e8f1c32f63";
    private JSONObject result;

    @Getter
    private String name;
    @Getter
    private String originalName;
    @Getter
    private LocalDate firstAirDate;
    @Getter
    private LocalDate lastAirDate;
    @Getter
    private List<String> genres;
    @Getter
    private String homePage;
    @Getter
    private String languageCode;
    @Getter
    private List<String> countries;
    @Getter
    private String overview;
    @Getter
    private String poster;
    @Getter
    private String status;
    @Getter
    private Double rate;

    public TheMovieDbTvShowsById(int tvShowId){
        this.result = result(tvShowId);
        this.name = getValidatedString(this.result, "name");
        this.originalName = getValidatedString(this.result, "original_name");
        this.firstAirDate = getValidatedLocalDate(this.result, "first_air_date");
        this.lastAirDate = getValidatedLocalDate(this.result, "last_air_date");
        this.homePage = getValidatedString(this.result, "homepage");
        this.languageCode = getValidatedString(this.result, "original_language");
        this.countries = getValidatedListOfStrings(this.result, "origin_country");
        this.genres = getValidatedListOfObjects(this.result, "genres", "name");
        this.overview = getValidatedString(this.result, "overview");
        this.poster = String.format("https://image.tmdb.org/t/p/w500%s", getValidatedString(this.result, "poster_path"));
        this.status = getValidatedString(this.result, "status");
        this.rate = getValidatedBoolean(this.result, "vote_average");
    }

    private JSONObject result(int tvShowId){
        String url = String.format("https://api.themoviedb.org/3/tv/%s?api_key=%s&language=en-US", tvShowId, apiKey);
        ApiConnect apiConnect = new ApiConnect(url);
        String jsonFile = apiConnect.getJsonString();
        return new JSONObject(jsonFile);
    }

    private String getValidatedString(JSONObject object, String key){
        return validateObject(object, key)
                ? object.getString(key)
                : null;
    }

    private Double getValidatedBoolean(JSONObject object, String key){
        return validateObject(object, key)
                ? object.getDouble(key)
                : null;
    }

    private LocalDate getValidatedLocalDate(JSONObject object, String key){
        return validateObject(object, key)
                ? LocalDate.parse(object.getString(key))
                : null;
    }

    private boolean validateObject(JSONObject object, String key){
        return object.has(key)
                && !object.isEmpty()
                && !object.isNull(key);
    }

    private List<String> getValidatedListOfObjects(JSONObject object, String arrayKey, String objectKey) {
        List<String> stringsList = new ArrayList<>();
        if(validateObject(object, arrayKey)){
            JSONArray objects = object.getJSONArray(arrayKey);
            for (int i = 0; i < objects.length(); i++) {
                String string = objects.getJSONObject(i).getString(objectKey);
                stringsList.add(string);
            }
        }
        return stringsList;
    }

    private List<String> getValidatedListOfStrings(JSONObject object, String arrayKey){
        List<String> stringList = new ArrayList<>();
        if(validateObject(object, arrayKey)){
            JSONArray strings = object.getJSONArray(arrayKey);
            for (int i = 0; i < strings.length(); i++) {
                String string = strings.getString(i);
                stringList.add(string);
            }
        }return stringList;
    }

}
