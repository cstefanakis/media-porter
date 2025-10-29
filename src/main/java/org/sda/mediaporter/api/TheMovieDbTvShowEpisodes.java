package org.sda.mediaporter.api;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sda.mediaporter.models.Contributor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TheMovieDbTvShowEpisodes {

    private final String apiKey = "7c97b163195d9428522398e8f1c32f63";
    private final JSONObject result;

    @Getter
    private LocalDate airDate;
    @Getter
    private List<Contributor> writers;
    @Getter
    private List<Contributor> directors;
    @Getter
    private List<Contributor> actors;
    @Getter
    private Integer episodeNumber;
    @Getter
    private String episodeType;
    @Getter
    private Integer seasonNumber;
    @Getter
    private String episodeName;
    @Getter
    private String overview;
    @Getter
    private Double rate;
    @Getter
    private String image;

    public TheMovieDbTvShowEpisodes(int tvShowId, int season, int episode) {
        this.result = result(tvShowId, season, episode);
        this.airDate = airDate();
        this.writers = contributorsDirectorsAndWriters(this.result, "Writer");
        this.directors = contributorsDirectorsAndWriters(this.result,"Director");
        this.actors = contributorsActors(this.result);
        this.episodeNumber = getValidatedIntegerObject(this.result, "episode_number");
        this.episodeType = getValidatedStringObject(this.result, "episode_type");
        this.seasonNumber = getValidatedIntegerObject(this.result, "season_number");
        this.episodeName = getValidatedStringObject(this.result, "name");
        this.overview = getValidatedStringObject(this.result, "overview");
        this.rate = getValidatedRate();
        this.image = String.format("https://image.tmdb.org/t/p/w500%s",getValidatedStringObject(this.result, "still_path"));
    }

    private JSONObject result(int tvShowId, int season, int episode){
        String url = String.format("https://api.themoviedb.org/3/tv/%s/season/%s/episode/%s?api_key=%s&language=en-US",tvShowId, season, episode, apiKey);
        ApiConnect apiConnect = new ApiConnect(url);
        String jsonFile = apiConnect.getJsonString();
        return new JSONObject(jsonFile);
    }

    private LocalDate airDate(){
        String key = "air_date";
        if (this.result.has(key)) {
            String airDate =  this.result.getString(key);
            return LocalDate.parse(airDate);
        }return null;
    }

    private List<Contributor> contributorsDirectorsAndWriters(JSONObject object, String job){
        List<Contributor> constributorsList = new ArrayList<>();

        JSONArray contributors = new JSONArray();

        String objectKey = "crew";
        if(object.has(objectKey)) {
            contributors = this.result.getJSONArray("crew");
        }

        for (int i = 0; i < contributors.length(); i++) {

            JSONObject contributor = contributors.getJSONObject(i);

            String jobKey = "job";

            if(contributor.has(jobKey) && contributor.getString(jobKey).equals(job)){
                constributorsList.add(Contributor.builder()
                        .fullName(getValidatedStringObject(contributor, "original_name"))
                        .poster(String.format("https://image.tmdb.org/t/p/w500%s",
                                getValidatedStringObject(contributor, "profile_path")))
                        .build());
            }

        } return constributorsList;
    }

    private List<Contributor> contributorsActors(JSONObject object){
        List<Contributor> contributorsList = new ArrayList<>();

        String key = "guest_stars";

        if(object.has(key)){

            JSONArray contributors = object.getJSONArray(key);

            for (int i = 0; i < contributors.length(); i++) {

                JSONObject actor = contributors.getJSONObject(i);

                contributorsList.add(Contributor.builder()
                                .fullName(getValidatedStringObject(actor, "original_name"))
                                .poster(String.format("https://image.tmdb.org/t/p/w500%s",
                                        getValidatedStringObject(actor, "profile_path")))
                        .build());
            }
        }return contributorsList;
    }

    private Integer getValidatedIntegerObject(JSONObject object, String key) {
        return object.has(key)
                ? object.getInt(key)
                : null;
    }

    private Double getValidatedRate() {
        return this.result.has("vote_average")
                ? this.result.getDouble("vote_average")
                : null;
    }

    private String getValidatedStringObject(JSONObject object, String key){
        return object.has(key) && !object.isNull(key)
                ? object.getString(key)
                : null;
    }
}
