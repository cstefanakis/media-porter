package org.sda.mediaporter.api;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbMovieSearchDTO;

import java.util.ArrayList;
import java.util.List;

public class TheMovieDbMovieSearch {
    private final JSONObject root;
    private final TheMovieDb theMovieDb = new TheMovieDb();

    @Getter
    List<TheMovieDbMovieSearchDTO> moviesSearchFromApi;

    public TheMovieDbMovieSearch(String movieTitle, Integer year) {
        this.root = results(movieTitle, year);
        this.moviesSearchFromApi = moviesSearchFromApi();
    }

    private JSONObject results(String movieTitle, Integer movieYear){
        String url = String.format("https://api.themoviedb.org/3/search/movie?api_key=%s&query=%s&year=%s", theMovieDb.getApiKey(), movieTitle, movieYear);
        ApiConnect apiConnect = new ApiConnect(url);
        return apiConnect.getRootJsonObject();
    }

    private List<TheMovieDbMovieSearchDTO> moviesSearchFromApi(){
        String objectKey = "results";
        List<TheMovieDbMovieSearchDTO> movieList = new ArrayList<>();
        if(theMovieDb.isValidatedObject(this.root, objectKey)){
            JSONArray movies = this.root.getJSONArray(objectKey);
            for (int i = 0; i < movies.length(); i++) {
                JSONObject movie = movies.getJSONObject(i);
                movieList.add(buildTheMovieDbMovieSearchDTO(movie));
            }
        }
        return movieList;
    }

    private TheMovieDbMovieSearchDTO buildTheMovieDbMovieSearchDTO(JSONObject jsonObject){
        return TheMovieDbMovieSearchDTO.builder()
                .theMovieDbId(theMovieDb.getValidatedLongJsonObject(jsonObject, "id"))
                .title(theMovieDb.getValidatedStringJsonObject(jsonObject, "title"))
                .originalTitle(theMovieDb.getValidatedStringJsonObject(jsonObject, "original_title"))
                .year(theMovieDb.getYearFromLocalDateObject(jsonObject, "release_date"))
                .originalLanguage(theMovieDb.getValidatedStringJsonObject(jsonObject, "original_language"))
                .overview(theMovieDb.getValidatedStringJsonObject(jsonObject, "overview"))
                .posterPath(theMovieDb.getPosterRootPath() + theMovieDb.getValidatedStringJsonObject(jsonObject, "poster_path"))
                .build();
    }
}
