package LevelUP.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Service
public class RiotApiService {

    @Value("${riot.api.key}")
    private String riotApiKey;

    public JSONObject buscarPorRiotId(String gameName, String tagLine) throws IOException {
        String baseUrl = "https://americas.api.riotgames.com";
        String path = "/riot/account/v1/accounts/by-riot-id/"
                + encode(gameName) + "/" + encode(tagLine);

        String urlStr = baseUrl + path;

        HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Riot-Token", riotApiKey); // ✅ API KEY NO HEADER

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

    private String encode(String value) {
        return value.replace(" ", "%20");
        // Opcional: Pode usar URLEncoder.encode(value, "UTF-8") se quiser mais robusto.
    }
}
