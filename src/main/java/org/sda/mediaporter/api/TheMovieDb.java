package org.sda.mediaporter.api;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCastDto;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCrewDto;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class TheMovieDb {
    @Getter
    private final String apiKey = "7c97b163195d9428522398e8f1c32f63";
    @Getter
    private final String posterRootPath = "https://image.tmdb.org/t/p/w500";

    public boolean isValidatedObject(JSONObject jsonObject, String key){
        return jsonObject.has(key) && !jsonObject.isNull(key) && !jsonObject.isEmpty();
    }

    public String getValidatedStringJsonObject(JSONObject jsonObject, String key){
        return isValidatedObject(jsonObject, key)
                ? jsonObject.getString(key)
                : null;
    }

    public Long getValidatedLongJsonObject(JSONObject jsonObject, String key){
        return isValidatedObject(jsonObject, key)
                ? jsonObject.getLong(key)
                : null;
    }

    public Double getValidatedDoubleJsonObject(JSONObject jsonObject, String key){
        return isValidatedObject(jsonObject, key)
                ? jsonObject.getDouble(key)
                : null;
    }

    public Integer getValidatedIntegerJsonObject(JSONObject jsonObject, String key){
        return isValidatedObject(jsonObject, key)
                ? jsonObject.getInt(key)
                : null;
    }

    public Integer getYearValidatedLocalDateJsonObject(JSONObject jsonObject, String key){
        return isValidatedObject(jsonObject, key)
                ? LocalDate.parse(jsonObject.getString(key)).getYear()
                : null;
    }

    public LocalDate getValidatedLocalDateJsonObject(JSONObject jsonObject, String key) {
        return isValidatedObject(jsonObject, key)
                ? LocalDate.parse(jsonObject.getString(key))
                : null;
    }

    public Integer getYearFromLocalDateObject(JSONObject object, String key){
        if (!isValidatedObject(object, key)){
            return null;
        }
        try {
            return LocalDate.parse(object.getString(key)).getYear();
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public List<String> getListOfStringsOfJsonArrayFromObject(JSONObject object, String arrayKey, String objectKey) {
        List<String> stringsList = new ArrayList<>();
        if(isValidatedObject(object, arrayKey)){
            JSONArray objects = object.getJSONArray(arrayKey);
            for (int i = 0; i < objects.length(); i++) {
                String string = objects.getJSONObject(i).getString(objectKey);
                stringsList.add(string);
            }
        }
        return stringsList;
    }

    public List<String> getListOfStringsOfArray(JSONObject object, String arrayKey){
        List<String> stringList = new ArrayList<>();
        if(isValidatedObject(object, arrayKey)){
            JSONArray strings = object.getJSONArray(arrayKey);
            for (int i = 0; i < strings.length(); i++) {
                String string = strings.getString(i);
                stringList.add(string);
            }
        }return stringList;
    }

    public JSONArray getObjectArray(JSONObject jsonObject, String jsonArrayKey){
        if(jsonObject != null){
            return jsonObject.getJSONArray(jsonArrayKey);
        } throw new EntityNotFoundException(String.format("results for %s not found",jsonArrayKey));
    }

    public List<TheMovieDbCrewDto> getContributorsFromCrewByDepartment(JSONObject jsonObject, String departmentName){
        List<TheMovieDbCrewDto> contributors = new ArrayList<>();
        JSONArray jsonArray = getObjectArray(jsonObject,"crew");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject castObjectIndex = jsonArray.getJSONObject(i);
            String jobOfObject = getValidatedStringJsonObject(castObjectIndex, "department");
            if(jobOfObject != null && jobOfObject.equals(departmentName)){
                contributors.add(buildTheMovieDbCrewDto(castObjectIndex));
            }
        }return contributors;
    }

    public List<TheMovieDbCastDto> getContributorsFromCast(JSONObject jsonObject, String arrayKey){
        List<TheMovieDbCastDto> contributors = new ArrayList<>();
        JSONArray jsonArray = getObjectArray(jsonObject,arrayKey);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject castObjectIndex = jsonArray.getJSONObject(i);
                contributors.add(buildTheMovieDbCastDto(castObjectIndex));
        }return contributors;
    }

    private TheMovieDbCastDto buildTheMovieDbCastDto(JSONObject jsonObject){
        return TheMovieDbCastDto.builder()
                .gender(getGender(jsonObject))
                .theMovieDbId(getValidatedLongJsonObject(jsonObject,"id"))
                .fullName(getValidatedStringJsonObject(jsonObject, "name"))
                .poster(getValidatedStringJsonObject(jsonObject, "profile_path") == null
                        ? null
                        : getPosterRootPath() + getValidatedStringJsonObject(jsonObject, "profile_path"))
                .character(getValidatedStringJsonObject(jsonObject, "character"))
                .order(getValidatedIntegerJsonObject(jsonObject, "order"))
                .build();
    }

    private TheMovieDbCrewDto buildTheMovieDbCrewDto(JSONObject jsonObject){
        return TheMovieDbCrewDto.builder()
                .fullName(getValidatedStringJsonObject(jsonObject, "name"))
                .theMovieDbId(getValidatedLongJsonObject(jsonObject, "id"))
                .poster(getValidatedStringJsonObject(jsonObject, "profile_path") == null
                        ? null
                        : getPosterRootPath() + getValidatedStringJsonObject(jsonObject, "profile_path"))
                .gender(getGender(jsonObject))
                .department(getValidatedStringJsonObject(jsonObject, "department"))
                .job(getValidatedStringJsonObject(jsonObject, "job"))
                .build();
    }

    private String getGender(JSONObject jsonObject){
        Integer genderId = getValidatedIntegerJsonObject(jsonObject, "gender");
        return switch (genderId) {
            case 1 -> "female";
            case 2 -> "male";
            default -> null;
        };
    }
}
