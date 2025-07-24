package org.sda.mediaporter.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TheMovieDbContributor {

    private final ApiConnect theMovieDb;
    private final String apiKey = "7c97b163195d9428522398e8f1c32f63";
    private int resultsObjectIndex;

    public TheMovieDbContributor(String contributorName) {
        String url = String.format("https://api.themoviedb.org/3/search/person?query=%s&api_key=%s", search(contributorName), apiKey);
        theMovieDb = new ApiConnect(url);
    }

    private String search(String search){
        return search.replace(" ", "+")
                .replace(".", "+");
    }

    public String getContributorName(){
        String key = "original_name";
        try {
            return jsonStringObjectResult(key);
        }catch (JSONException e){
            return null;
        }
    }

    public String getContributorPoster(){
        String key = "profile_path";
        try {
            String path = jsonStringObjectResult(key);
            return "https://image.tmdb.org/t/p/w500" + path;
        }catch (JSONException e) {
            return null;
        }
    }

    public String getContributorWebsite(){
        String key = "id";
        try {
            int id = jsonIntegerObjectResult(key);
            return "https://www.themoviedb.org/person/" + id;
        }catch (JSONException e){
            return null;
        }
    }

    private Integer jsonIntegerObjectResult(String key){
        return  resultsObjectIndexJsonObject()
                .getInt(key);
    }

    private String jsonStringObjectResult(String key){
        return  resultsObjectIndexJsonObject()
                .getString(key);
    }

    private JSONObject resultsObjectIndexJsonObject(){
        return results()
                .getJSONObject(this.resultsObjectIndex);
    }

    private JSONObject rootObject(){
        return new JSONObject(this.theMovieDb.getJsonString());
    }

    private JSONArray results(){
        String key = "results";
        return rootObject().getJSONArray(key);
    }
}
