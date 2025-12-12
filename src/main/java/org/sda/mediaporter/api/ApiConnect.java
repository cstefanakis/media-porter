package org.sda.mediaporter.api;

import lombok.Getter;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

public class ApiConnect {
    private final String url;
    @Getter
    private String jsonString;
    @Getter
    private JSONObject rootJsonObject;

    public ApiConnect(String url) {
        this.url = url;
        this.jsonString = jsonString();
        this.rootJsonObject = new JSONObject(this.jsonString);
    }

    private String jsonString() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(this.url);
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
