package org.sda.mediaporter.api;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class TheMovieDbGenres {

    private final String apiKey = "7c97b163195d9428522398e8f1c32f63";
    private JSONArray result;

    @Getter
    private Long id;
    @Getter
    private String name;

    public TheMovieDbGenres(Long genreId) {
        this.result = genres();
        findById(genreId);
    }

    private JSONArray genres(){
        String url = String.format("https://api.themoviedb.org/3/genre/tv/list?api_key=7c97b163195d9428522398e8f1c32f63&language=en-US");
        ApiConnect apiConnect = new ApiConnect(url);
        String jsonFile = apiConnect.getJsonString();
        JSONObject jsonObject = new JSONObject(jsonFile);
        JSONArray genres = jsonObject.getJSONArray("genres");
        return genres.isEmpty()
                ? null
                : genres;
    }

    private void findById(Long id){
        String idKey = "id";
        String nameKey = "name";
        for (int i = 0; i < this.result.length(); i++) {
            JSONObject genre = this.result.getJSONObject(i);
            if(genre.has(idKey) && genre.has(nameKey)){
                Long genreId = genre.getLong(idKey);
                String genreName = genre.getString(nameKey);
                if(genreId.equals(id)){
                    this.id = genreId;
                    this.name = genreName;
                    break;
                }
            }

        }
    }
}
