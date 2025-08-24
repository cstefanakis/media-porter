package org.sda.mediaporter.api;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OmdbApi {
    private ApiConnect omdbApi;
    private final String apiKey = "c30304ec";

    public OmdbApi(String imdbId) {
        String url = String.format("http://www.omdbapi.com/?i=%s&apikey=%s", imdbId, apiKey);
        this.omdbApi = new ApiConnect(url);
    }

    private String search(String search){
        System.out.println(search);
        try {
            String text = URLEncoder.encode(search, StandardCharsets.UTF_8);
            System.out.println("text: " + text);
            return text;
        }catch (Exception e){
            return "";
        }
    }

    private JSONObject rootObject(){
        return new JSONObject(omdbApi.getJsonString());
    }

    public String getTitle(){
        return rootObject().getString("Title");
    }

    public Integer getYear(){
        if(!rootObject().has("Year")){
            return null;
        }
        return rootObject().getInt("Year");
    }

    public LocalDate getReleasedDate(){
        if(!rootObject().has("Released")){
            return null;
        }

        String date = rootObject().getString("Released");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return date != null && !date.isEmpty() && !date.equalsIgnoreCase("N/A")
                ? LocalDate.parse(date, formatter)
                : null;
    }

    public List<String> getGenre(){
        return getApiProperties("Genre");
    }

    public List<String> getDirector(){
        return getApiProperties("Director");
    }

    public List<String> getWriter(){
        return getApiProperties("Writer");
    }

    public List<String> getActors(){
        return getApiProperties("Actors");
    }

    public String getPlot(){
        return getApiProperty("Plot");
    }

    public List<String> getLanguages(){
        return getApiProperties("Language");
    }

    public List<String> getCountries(){
        return getApiProperties("Country");
    }

    public String getPoster(){
        return getApiProperty("Poster");
    }
    public Double getImdbRating(){
        if(rootObject().has("imdbRating")){
            return null;
        }

        return rootObject().getDouble("imdbRating");
    }

    public String getType(){
        return getApiProperty("Type");
    }

    public String getBoxOffice(){
        return getApiProperty("BoxOffice");
    }

    private String getApiProperty(String apiProperty){
        if(rootObject().has(apiProperty)) {
            return null;
        }

        String property =  rootObject().getString(apiProperty);
        return property.equalsIgnoreCase("N/A")
                ? null
                : property;
    }

    private List<String> getApiProperties(String property){
        if(!rootObject().has(property)){
            return null;
        }

        String[] properties = rootObject().getString(property)
                .split(",");

        List<String> propertiesList =  Arrays.stream(properties)
                .filter(c -> !c.equalsIgnoreCase("N/A"))
                .map(c -> c.trim())
                .toList();

        return propertiesList.isEmpty()
                ? null
                :propertiesList;
    }

}
