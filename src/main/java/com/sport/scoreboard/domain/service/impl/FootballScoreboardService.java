package com.sport.scoreboard.domain.service.impl;

import com.sport.scoreboard.domain.model.Match;
import com.sport.scoreboard.domain.service.ScoreboardService;
import com.sport.scoreboard.repository.MatchRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FootballScoreboardService implements ScoreboardService {

    private final MatchRepository repository;
    private final MeterRegistry registry;

    public FootballScoreboardService(MatchRepository repository, MeterRegistry registry) {
        this.repository = repository;
        this.registry = registry;
    }

    @Override
    public Match startGame(String homeTeam, String awayTeam) {
        if (homeTeam == null || homeTeam.isBlank() || awayTeam == null || awayTeam.isBlank()) {
            throw new IllegalArgumentException("Teams must have valid names to start a match!");
        }

        if (homeTeam.equalsIgnoreCase(awayTeam)) {
            throw new IllegalArgumentException("Same team cannot play against itself!");
        }

        repository.findByTeams(homeTeam, awayTeam).ifPresent(message -> {
            throw new IllegalStateException("Match already started for these teams");
        });

        Match match = new Match(homeTeam, awayTeam, 0, 0, System.nanoTime());
        repository.save(match);
        registry.counter("matches.started").increment();

        return match;
    }

    @Override
    public void updateScore(String homeTeam, int homeScore, String awayTeam, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Score cannot be negative.");
        }

        repository.findByTeams(homeTeam, awayTeam)
                .map(match -> {
                    match.setHomeScore(homeScore);
                    match.setAwayScore(awayScore);
                    repository.save(match);
                    registry.counter("matches.updated").increment();
                    return match;
                })
                .orElseThrow(() -> new IllegalStateException("There is no ongoing match with provided teams!"));
    }

    @Override
    public List<Match> getSummary() {
        return repository.findAll().stream()
                .sorted(Comparator
                        .comparingInt(Match::totalScore).reversed()
                        .thenComparing(Match::getStartAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void finishGame(String homeTeam, String awayTeam) {
        boolean found = repository.findByTeams(homeTeam, awayTeam).map(match -> {
            repository.delete(match);
            registry.counter("matches.finished").increment();
            return true;
        }).orElse(false);

        if (!found) {
            throw new IllegalStateException("Cannot finish match that does not exist!");
        }
    }
}
