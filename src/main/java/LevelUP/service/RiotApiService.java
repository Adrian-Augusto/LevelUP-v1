package LevelUP.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Service
public class RiotApiService {

    private final String API_KEY = "RGAPI-2a5e9c86-4fe2-4def-9468-9a406d920f0f";

    public JSONObject buscarPorRiotId(String gameName, String tagLine) throws IOException {
        String baseUrl = "https://americas.api.riotgames.com";
        String path = "/riot/account/v1/accounts/by-riot-id/" + gameName + "/" + tagLine;
        String query = "?api_key=" + API_KEY;

        String urlStr = baseUrl + path + query;

        HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
        connection.setRequestMethod("GET");

        int status = connection.getResponseCode();

        if (status != 200) {
            try (Scanner errorScanner = new Scanner(connection.getErrorStream())) {
                String errorResponse = errorScanner.useDelimiter("\\A").hasNext() ? errorScanner.next() : "";
                throw new RuntimeException("Erro na API da Riot: " + status + " - " + errorResponse);
            }
        }

        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            String response = scanner.useDelimiter("\\A").next();
            return new JSONObject(response);
        }
    }
}
