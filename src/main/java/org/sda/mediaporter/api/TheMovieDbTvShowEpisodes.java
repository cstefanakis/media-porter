package org.sda.mediaporter.api;

public class TheMovieDbTvShowEpisodes {

    private ApiConnect TheMovieDB;
    private final String apiKey = "7c97b163195d9428522398e8f1c32f63";
    private int resultsObjectIndex;


    public TheMovieDbTvShowEpisodes(String tvShowId, Integer season, Integer episode) {
        String url = String.format("GET https://api.themoviedb.org/3/tv/%s/season/%s/episode/%s?api_key=%s&language=en-US",tvShowId, season, episode, apiKey);
        this.TheMovieDB = new ApiConnect(url);
    }
}
