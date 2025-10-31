package org.sda.mediaporter.api;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDate;
import java.util.List;

public class TheMovieDbTvShow {
    private final String apiKey = "7c97b163195d9428522398e8f1c32f63";
    private final JSONObject results;

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
    private List<String> genres;


    public TheMovieDbTvShow(String tvShowName) {
        this.results = tvShowResults(tvShowName);
        this.id = id();
        this.originalName = getValidatedStringObject(this.results, "original_name");
        this.poster = String.format("https://image.tmdb.org/t/p/w500%s", getValidatedStringObject(this.results, "poster_path"));
        this.overview = getValidatedStringObject(this.results, "overview");
        this.originalLanguage = getValidatedStringObject(this.results, "original_language");
        this.firstAirDate = firstAirDate();
        this.rate = rate();
        this.countries = countries();
        this.genres = genres();
    }

    private JSONObject tvShowResults (String tvShowName){
        String url = String.format("https://api.themoviedb.org/3/search/tv?api_key=%s&query=%s", this.apiKey, tvShowName.replace(" ", "+"));
        ApiConnect apiConnect = new ApiConnect(url);
        String jsonFile = apiConnect.getJsonString();
        JSONObject tvShow = new JSONObject(jsonFile);
        JSONArray results = tvShow.getJSONArray("results");
        return results.isEmpty()
                ? null
                : results.getJSONObject(0);
    }

    private Long id (){
        String key = "id";

        if(!checkObjects(this.results, key)){
            return null;
        }

        return this.results.optLong(key, -1) == -1
                ? null
                : this.results.getLong(key);
    }

    private String getValidatedStringObject(JSONObject object, String key){
        if(!checkObjects(object, key)){
            return null;
        }

        String result = object.getString(key);

        return result.isEmpty()
                ? null
                : result;
    }

    private LocalDate firstAirDate(){
        String key = "first_air_date";

        if(!checkObjects(this.results, key)){
            return null;
        }

        String date = results.getString(key);

        return date.isEmpty()
            ? null
            : LocalDate.parse(date);
    }

    private Double rate(){
        String key  = "vote_average";

        if(!checkObjects(this.results, key)){
            return null;
        }

        double rate =  results.getDouble(key);

        return rate == 0.0
                ? null
                : rate;
    }

    private List<String> countries(){
        String key = "origin_country";

        if(!checkObjects(this.results, key)){
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

    private List<String> genres(){
        String key = "genre_ids";

        if(!checkObjects(this.results, key)){
            return null;
        }

        List<Object> genreIds = results.getJSONArray(key).toList();


        return genreIds.isEmpty()
                ? null
                : genreIds.stream()

                    .map(c-> new TheMovieDbGenres(((Number) c).longValue()).getName())
                    .toList();
    }

    private boolean checkObjects(JSONObject object, String key){
        return object != null && (!object.isNull(key) || !object.isEmpty() || object.has(key));
    }
}
