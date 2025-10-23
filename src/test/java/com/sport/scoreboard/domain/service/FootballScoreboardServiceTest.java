package com.sport.scoreboard.domain.service;

import com.sport.scoreboard.domain.model.Match;
import com.sport.scoreboard.domain.service.impl.FootballScoreboardService;
import com.sport.scoreboard.repository.InMemoryMatchRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Football Scoreboard Service Tests")
public class FootballScoreboardServiceTest {

    private FootballScoreboardService service;
    private MeterRegistry registry;
    private String homeTeam;
    private String awayTeam;

    @BeforeEach
    void setUp(){
        registry = new SimpleMeterRegistry();
        service = new FootballScoreboardService(new InMemoryMatchRepository(), registry);
        homeTeam = "German";
        awayTeam = "Poland";
    }

    @Test
    void when_StartGameAndUpdateMetrics_expect_NewGameStartedAndMetricsCorrect(){
        //when
        service.startGame(homeTeam, awayTeam);
        List<Match> matchList = service.getSummary();
        double countStarted = registry.counter("matches.started").count();

        //then
        assertThat(matchList)
                .hasSize(1)
                .first()
                .extracting(Match::getHomeTeam, Match::getAwayTeam)
                .containsExactly(homeTeam, awayTeam);

        assertThat(countStarted).isEqualTo(1);
    }

    @Test
    void when_UpdateScoreAndMetrics_expect_GameAndMetricsUpdatedCorrectly(){
        //given
        service.startGame(homeTeam, awayTeam);

        //when
        service.updateScore(homeTeam, 2, awayTeam, 1);
        Match match = service.getSummary().getFirst();
        double countUpdated = registry.counter("matches.updated").count();

        //then
        assertThat(match.getHomeScore()).isEqualTo(2);
        assertThat(match.getAwayScore()).isEqualTo(1);

        assertThat(countUpdated).isEqualTo(1);
    }

    @Test
    void when_FinishGameAndUpdateMetrics_GameFinishedAndMetricsUpdatedCorrectly(){
        //given
        homeTeam = "Japan";
        awayTeam = "China";

        service.startGame(homeTeam, awayTeam);
        assertThat(service.getSummary()).hasSize(1);
        //when
        service.finishGame(homeTeam, awayTeam);
        double countFinished = registry.counter("matches.finished").count();

        //then
        assertThat(service.getSummary()).isEmpty();

        assertThat(countFinished).isEqualTo(1);
    }
}
