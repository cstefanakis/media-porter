package org.sda.mediaporter.api;

import lombok.Getter;
import org.json.JSONObject;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCastDto;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCrewDto;

import java.util.List;

public class TheMovieDbCreditsForMovieById {
    private final TheMovieDb theMovieDb = new TheMovieDb();

    @Getter
    private List<TheMovieDbCastDto> actors;
    @Getter
    private List<TheMovieDbCrewDto> writers;
    @Getter
    private List<TheMovieDbCrewDto> directors;

    public TheMovieDbCreditsForMovieById(Long theMovieDbId) {
        JSONObject root = result(theMovieDbId);
        this.actors = theMovieDb.getContributorsFromCast(root, "cast");
        this.writers = theMovieDb.getContributorsFromCrewByDepartment(root, "Editing");
        this.directors = theMovieDb.getContributorsFromCrewByDepartment(root, "Directing");
    }

    private JSONObject result(Long theMovieDbId){
        String url = String.format("https://api.themoviedb.org/3/movie/%s/credits?api_key=%s",theMovieDbId, theMovieDb.getApiKey());
        ApiConnect apiConnect = new ApiConnect(url);
        return apiConnect.getRootJsonObject();
    }
}
