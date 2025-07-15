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
        return rootObject().getInt("Year");
    }

    public LocalDate getReleasedDate(){

            String date = rootObject().getString("Released");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
            if (date != null && !date.isEmpty() && !date.equalsIgnoreCase("N/A")){
                return LocalDate.parse(date, formatter);
            }
            return null;

    }

    public List<String> getGenre(){

            List<String> genres = new ArrayList<>();
            String[] apiGenres = rootObject().getString("Genre")
                    .replace(" ", "")
                    .split(",");
            Collections.addAll(genres, apiGenres);
            return genres.stream().filter(g-> !g.equalsIgnoreCase("N/A")).toList();

    }

    public List<String> getDirector(){
        if(rootObject().has("Director")) {
            String[] directors = rootObject().getString("Director")
                    .split(",");
            return Arrays.stream(directors).filter(d-> !d.equalsIgnoreCase("N/A")).toList();
        }
        return Collections.emptyList();
    }
    public List<String> getWriter(){
        if(rootObject().has("Writer")) {
            String[] writers = rootObject().getString("Writer")
                    .replace(", ", ",")
                    .split(",");
            return Arrays.stream(writers).filter(w-> !w.equalsIgnoreCase("N/A")).toList();
        }
        return Collections.emptyList();
    }

    public List<String> getActors(){
        if(rootObject().has("Actors")){
            String[] actors =  rootObject().getString("Actors")
                    .replace(", ", ",")
                    .split(",");
            return Arrays.stream(actors).filter(a-> !a.equalsIgnoreCase("N/A")).toList();
        }
        return Collections.emptyList();
    }

    public String getPlot(){
        if(rootObject().has("Plot")){
            String plot = rootObject().getString("Plot");
            if(!plot.equalsIgnoreCase("N/A")){
                return plot;
            }
        }
        return null;
    }

    public List<String> getLanguages(){
            String[] languages = rootObject().getString("Language")
                    .replace(" ", "")
                    .split(",");

            return Arrays.stream(languages).filter(l-> !l.equals("N/A")).toList();
    }

    public String getCountry(){
        if(rootObject().has("Country")) {
            String country = rootObject().getString("Country");
            if(!country.equalsIgnoreCase("N/A")){
                return country;
            }
        }
        return null;
    }

    public String getPoster(){
        if(rootObject().has("Poster")) {
            String poster =  rootObject().getString("Poster");
            if(!poster.equalsIgnoreCase("N/A")){
                return poster;
            }
        }
        return null;
    }
    public Double getImdbRating(){
        if(rootObject().has("imdbRating")){
            String rate = rootObject().getString("imdbRating");
            if (rate != null && !rate.isEmpty() && !rate.equalsIgnoreCase("N/A")){
                return rootObject().getDouble("imdbRating");
            }
            return  null;
        }
        return null;
    }

    public String getType(){
        if(rootObject().has("Type")){
            String type = rootObject().getString("Type");
            if(!type.equalsIgnoreCase("N/A")){
                return type;
            }
        }
        return null;
    }

    public String getBoxOffice(){
        if(rootObject().has("BoxOffice")){
            String boxOffice = rootObject().getString("BoxOffice");
            if(!boxOffice.equalsIgnoreCase("N/A")){
                return boxOffice;
            }
        }
        return null;
    }

}
