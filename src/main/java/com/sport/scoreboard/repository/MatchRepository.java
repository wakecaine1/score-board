package com.sport.scoreboard.repository;

import com.sport.scoreboard.domain.model.Match;

import java.util.List;
import java.util.Optional;

public interface MatchRepository {
    Match save(Match match);
    void delete(Match match);
    Optional<Match> findByTeams(String homeTeam, String awayTeam);
    List<Match> findAll();
}
