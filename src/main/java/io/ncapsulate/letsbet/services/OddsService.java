package io.ncapsulate.letsbet.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ncapsulate.letsbet.models.GameScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

@Service
public class OddsService {

    @Value("${odds.api.key}")
    private String apiKey;

    private static final Logger logger = LoggerFactory.getLogger(OddsService.class);

    private final RestTemplate restTemplate;

    public OddsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String getLiveOdds() {
        String apiUrl = "https://api.the-odds-api.com/v4/sports/basketball_nba/odds/?apiKey=" + apiKey + "&regions=us&markets=spreads,totals&oddsFormat=decimal&bookmakers=draftkings";
        return restTemplate.getForObject(apiUrl, String.class);
    }

    public List<GameScore> fetchGameScores() throws Exception{
        logger.info("Fetching game scores...");
        String apiUrl = "https://api.the-odds-api.com/v4/sports/basketball_nba/scores/?daysFrom=1&apiKey=" + apiKey;
        HttpClient client = HttpClient.newHttpClient();

        // Create an HttpRequest to the API endpoint
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        // Send the request and receive the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Check if the request was successful (status code 200)
        if (response.statusCode() == 200) {
            // Parse the JSON response into an array of GameScore objects
            logger.info("Successfully fetched game scores.");
            ObjectMapper objectMapper = new ObjectMapper();
            GameScore[] gameScoresArray = objectMapper.readValue(response.body(), GameScore[].class);

//            logger.info("Fetched game scores: {}", Arrays.toString(gameScoresArray));
//            logger.info("Fetched game scores:");
//            try {
//                for (GameScore gameScore : gameScoresArray) {
//                    logger.info("ID: {}, Total Score: {}, Spread: {}", gameScore.getId(), gameScore.getTotalScore(), gameScore.getSpread());
//                }
//            }
//            catch (Exception e) {
//                // Handle exceptions
//                logger.error("Error occurred during fetch Game Scores: {}", e.getMessage());
//                e.printStackTrace();
//            }
            // Convert the array to a List and return it
            return Arrays.asList(gameScoresArray);
        } else {
            // If the request was not successful, throw an exception with the error message
            throw new Exception("Failed to fetch game scores: HTTP " + response.statusCode());
        }
    }
}
