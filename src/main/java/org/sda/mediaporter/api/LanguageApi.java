package org.sda.mediaporter.api;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sda.mediaporter.models.Language;

import java.util.ArrayList;
import java.util.List;

public class LanguageApi {
    private final ApiConnect theMovieDb;

    public LanguageApi() {
        String apiKey = "7c97b163195d9428522398e8f1c32f63";
        String url = String.format("https://api.themoviedb.org/3/configuration/languages?api_key=%s", apiKey);
        this.theMovieDb = new ApiConnect(url);
    }

    private JSONArray rootObject(){
        return new JSONArray(this.theMovieDb.getJsonString());
    }

    public List<Language> getLanguages(){
        List<Language> languages = new ArrayList<>();
        for (int i = 0; i < rootObject().length(); i++) {
            Language newlanguage = new Language();
            JSONObject language = rootObject().getJSONObject(i);
            newlanguage.setEnglishTitle(language.getString("english_name"));
            newlanguage.setTitle(language.getString("name"));
            newlanguage.setCode(language.getString("iso_639_1"));
            languages.add(newlanguage);
        }
        return languages;
    }
}
