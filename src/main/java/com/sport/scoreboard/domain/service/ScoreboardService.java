package com.sport.scoreboard.domain.service;

import com.sport.scoreboard.domain.model.Match;

import java.util.List;

public interface ScoreboardService {
    Match startGame(String homeTeam, String awayTeam);
    void finishGame(Match match);
    void updateScore(Match match, int homeScore, int awayScore);
    List<Match> getSummary();
}
