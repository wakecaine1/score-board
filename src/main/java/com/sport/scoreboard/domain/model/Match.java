package com.sport.scoreboard.domain.model;

import com.sport.scoreboard.util.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    private final Integer id = Utils.getRandomInt();
    private Team homeTeam;
    private Team awayTeam;
    private int homeScore;
    private int awayScore;
    private OffsetDateTime startAt = OffsetDateTime.now();

    public int totalScore() {
        return homeScore + awayScore;
    }
}
