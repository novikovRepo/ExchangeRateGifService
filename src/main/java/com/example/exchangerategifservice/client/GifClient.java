package com.example.exchangerategifservice.client;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface GifClient {

    String getRandomGif(String apiKey, String tag);

}
