package com.sport.scoreboard.domain.service;

import com.sport.scoreboard.domain.model.Match;

import java.util.List;

public interface ScoreboardService {
    Match startGame(String homeTeam, String awayTeam);
    void finishGame(String homeTeam, String awayTeam);
    void updateScore(String home, int homeScore, String awayTeam, int awayScore);
    List<Match> getSummary();
}
