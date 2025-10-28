package org.sda.mediaporter.api;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sda.mediaporter.models.Genre;

import java.time.LocalDate;
import java.util.List;

public class TheMovieDbTvShow {
    private ApiConnect apiConnect;
    private final String apiKey = "7c97b163195d9428522398e8f1c32f63";
    private JSONObject results;

    @Getter
    private Long id;
    @Getter
    private String originalName;
    @Getter
    private String poster;
    @Getter
    private String overview;
    @Getter
    private String originalLanguage;
    @Getter
    private LocalDate firstAirDate;
    @Getter
    private Double rate;
    @Getter
    private List<String> countries;
    @Getter
    private List<Integer> genres;


    public TheMovieDbTvShow(String tvShowName) {
        this.results = tvShowResults(tvShowName);
        this.id = id();
        this.originalName = originalName();
        this.poster = poster();
        this.overview = overview();
        this.originalLanguage = originalLanguage();
        this.firstAirDate = firstAirDate();
        this.rate = rate();
        this.countries = countries();
        this.genres = genres();
    }

    private JSONObject tvShowResults (String tvShowName){
        String url = String.format("https://api.themoviedb.org/3/search/tv?api_key=%s&query=%s", this.apiKey, tvShowName.replace(" ", "+"));
        this.apiConnect = new ApiConnect(url);
        String jsonFile = this.apiConnect.getJsonString();
        JSONObject tvShow = new JSONObject(jsonFile);
        JSONArray results = tvShow.getJSONArray("results");
        return results.isEmpty()
                ? null
                : results.getJSONObject(0);
    }

    private Long id (){
        String key = "id";

        if(!checkObjects(key)){
            return null;
        }

        return this.results.optLong(key, -1) == -1
                ? null
                : this.results.getLong(key);
    }

    private String originalName(){
        String key = "original_name";

        if(!checkObjects(key)){
            return null;
        }

        String originalName = results.getString(key);

        return originalName.isEmpty()
                ? null
                : originalName;
    }

    private String poster() {
        String key = "poster_path";

        if(!checkObjects(key)){
            return null;
        }
        String poster =  results.getString(key);

        return poster.isEmpty()
            ? null
            :String.format("https://image.tmdb.org/t/p/w500%s",poster);
    }

    private String overview() {
        String key = "overview";

        if(!checkObjects(key)){
            return null;
        }
        String overview =  results.getString(key);

        return overview.isEmpty()
                ? null
                : overview;
    }

    private String originalLanguage(){
        String key = "original_language";

        if(!checkObjects(key)){
            return null;
        }

        String originalLanguage =  results.getString(key);

        return originalLanguage.isEmpty()
                ? null
                : originalLanguage;
    }

    private LocalDate firstAirDate(){
        String key = "first_air_date";

        if(!checkObjects(key)){
            return null;
        }

        String date = results.getString(key);

        return date.isEmpty()
            ? null
            : LocalDate.parse(date);
    }

    private Double rate(){
        String key  = "vote_average";

        if(!checkObjects(key )){
            return null;
        }

        double rate =  results.getDouble(key);

        return rate == 0.0
                ? null
                : rate;
    }

    private List<String> countries(){
        String key = "origin_country";

        if(!checkObjects(key)){
            return null;
        }

        JSONArray countriesJsonArray = results.getJSONArray(key);

        List<Object> countries = countriesJsonArray.toList();

        return countries.isEmpty()
                ? null
                : countries.stream()
                    .map(c -> c.toString())
                    .toList();
    }

    private List<Integer> genres(){
        String key = "genre_ids";

        if(!checkObjects(key)){
            return null;
        }

        List<Object> genreIds = results.getJSONArray(key).toList();

        return genreIds.isEmpty()
                ? null
                : genreIds.stream()
                    .map(obj -> ((Number) obj).intValue())
                    .toList();
    }

    private boolean checkObjects(String objectName){
        if(this.results == null){
            return false;
        }
        if(!this.results.has(objectName)){
            return false;
        }
        return true;
    }
}
