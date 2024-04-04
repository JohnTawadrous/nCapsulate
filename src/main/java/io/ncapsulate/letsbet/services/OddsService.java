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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
        // Get tomorrow's date and time at 3:00 AM
        LocalDateTime tomorrow3AM = LocalDateTime.now(ZoneId.of("America/New_York")).plusDays(1).withHour(3).withMinute(0).withSecond(0);

        // Format the tomorrow's date to ISO 8601 format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String tomorrow3AMString = tomorrow3AM.format(formatter);

        // Construct the commenceTimeTo parameter
        String commenceTimeTo = tomorrow3AMString;

        // Get today's date in ISO 8601 format
        LocalDateTime today = LocalDateTime.now(ZoneId.of("America/New_York"));
        String todayString = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Construct the API URL with commenceTimeFrom and commenceTimeTo parameters
        String apiUrl = "https://api.the-odds-api.com/v4/sports/basketball_nba/odds/?apiKey=" + apiKey + "&regions=us&markets=spreads,totals&oddsFormat=decimal&bookmakers=draftkings&commenceTimeFrom=" + todayString + "T00:00:00Z&commenceTimeTo=" + commenceTimeTo;

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

            // Convert the array to a List and return it
            return Arrays.asList(gameScoresArray);
        } else {
            // If the request was not successful, throw an exception with the error message
            throw new Exception("Failed to fetch game scores: HTTP " + response.statusCode());
        }
    }
}
