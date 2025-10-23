package com.sport.scoreboard.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    private String homeTeam;
    private String awayTeam;
    private int homeScore;
    private int awayScore;
    private long startAt = System.nanoTime();

    public int totalScore() {
        return homeScore + awayScore;
    }
}
