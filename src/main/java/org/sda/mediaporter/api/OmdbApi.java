package org.sda.mediaporter.api;

import org.json.JSONObject;
import org.sda.mediaporter.models.multimedia.videoFiles.Movie;
import org.sda.mediaporter.models.multimedia.videoFiles.TvShow;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OmdbApi {
    private ApiConnect omdbApi;
    private final String apiKey = "c30304ec";
    private List<Movie> movies = new ArrayList<>();
    private List<TvShow> serials = new ArrayList<>();

    public OmdbApi(String search) {
        String url = String.format("https://www.omdbapi.com/?t=%s+&y=%s&apikey=%s", title(search), year(search), this.apiKey);
        this.omdbApi = new ApiConnect(url);
    }

    private String title(String search){
        return search.replace(String.valueOf(year(search)), "");
    }

    private Integer year(String search){
        for (int i = 1990; i < Year.now().getValue(); i++) {
            if(search.contains(String.valueOf(i))){
                return i;
            }
        }return null;
    }

    private JSONObject rootObject(){
        return new JSONObject().getJSONObject(omdbApi.getJsonString());
    }

    public String getTitle(){
        return rootObject().getString("Title");
    }

    public Integer getYear(){
        return rootObject().getInt("Year");
    }

    public LocalDate getReleasedDate(){
        String date = rootObject().getString("01 Jul 2009");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return LocalDate.parse(date, formatter);
    }

    public String[] getGenre(){
        return rootObject().getString("Genre").split(",");
    }

    public String[] getDirector(){
        return rootObject().getString("Director").split(",");
    }
    public String[] getWriter(){
        return rootObject().getString("Writer").split(",");
    }

    public String[] getActors(){
        return rootObject().getString("Actors").split(",");
    }

    public String getPlot(){
        return rootObject().getString("Plot");
    }

    public String getLanguage(){
        return  rootObject().getString("Language");
    }

    public String getCountry(){
        return  rootObject().getString("Country");
    }

    public String getPoster(){
        return  rootObject().getString("Poster");
    }
    public Double getImdbRating(){
        return  rootObject().getDouble("imdbRating");
    }

    public String getType(){
        return  rootObject().getString("Type");
    }

    public String getBoxOffice(){
        return  rootObject().getString("BoxOffice");
    }

}
