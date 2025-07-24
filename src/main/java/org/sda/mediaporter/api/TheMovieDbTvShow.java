package org.sda.mediaporter.api;

public class TheMovieDbTvShow {

    private ApiConnect TheMovieDB;
    private final String apiKey = "7c97b163195d9428522398e8f1c32f63";
    private int resultsObjectIndex;


    public TheMovieDbTvShow(String tvShowTitle, Integer realiseDate) {
        String url = String.format("GET https://api.themoviedb.org/3/search/tv?api_key=%s&query=%s&first_air_date_year=%s",apiKey, search(tvShowTitle), realiseDate);
        this.TheMovieDB = new ApiConnect(url);
    }

    private String search(String search){
        return search.replace(" ", "+")
                .replace(".", "+");
    }


}
