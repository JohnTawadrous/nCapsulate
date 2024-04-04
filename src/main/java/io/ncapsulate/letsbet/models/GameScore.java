package io.ncapsulate.letsbet.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameScore {
    private String id;

    private String commenceTime;
    private boolean completed;
    @JsonProperty("home_team")
    private String homeTeam;
    @JsonProperty("away_team")
    private String awayTeam;
    private List<Score> scores;
    private String lastUpdate;

    private String sportKey;


    private static final Logger logger = LoggerFactory.getLogger(GameScore.class);

    public GameScore(){

    };
    public GameScore(String id/*, String commenceTime*/, boolean completed, String homeTeam, String awayTeam, List<Score> scores, String lastUpdate,String sportKey) {
        this.id = id;
//        this.commenceTime = commenceTime;
        this.completed = completed;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.scores = scores != null ? scores : new ArrayList<>();
        this.lastUpdate = lastUpdate;
        this.sportKey = sportKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommenceTime() {
        return commenceTime;
    }

    public void setCommenceTime(String commenceTime) {
        this.commenceTime = commenceTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getSportKey() {
        return sportKey;
    }

    public void setSportKey(String sportKey) {
        this.sportKey = sportKey;
    }

    public String getScoreName(int index) {
        return scores.get(index).getName();
    }

    public int getTotalScore() {
        int total = 0;

        if (scores == null || scores.isEmpty()) {
            return 0; // or throw an exception, depending on your requirements
        }

        for (Score score : scores) {
            total += score.getScore();
        }
        return total;
    }

    public int getSpread() {
        if (scores == null || scores.size() < 2) {
            return 0; // or handle the case appropriately based on your requirements
        }

        // Assuming first score is home team and second score is away team
        return scores.get(0).getScore() - scores.get(1).getScore();
    }
}

class Score {
    private String name;
    private int score;

    // Constructor, getters, and setters

    public Score(){

    }

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
