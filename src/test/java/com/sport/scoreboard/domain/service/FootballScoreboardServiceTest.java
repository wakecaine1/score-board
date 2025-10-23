package com.sport.scoreboard.domain.service;

import com.sport.scoreboard.domain.model.Match;
import com.sport.scoreboard.domain.model.Team;
import com.sport.scoreboard.domain.service.impl.FootballScoreboardService;
import com.sport.scoreboard.repository.InMemoryMatchRepository;
import com.sport.scoreboard.repository.MatchRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Football Scoreboard Service Tests")
public class FootballScoreboardServiceTest {

    private FootballScoreboardService service;
    private MeterRegistry registry;
    private MatchRepository repository;
    private Team homeTeam;
    private Team awayTeam;

    @BeforeEach
    void setUp() {
        repository = new InMemoryMatchRepository();
        registry = new SimpleMeterRegistry();
        service = new FootballScoreboardService(repository, registry);
        homeTeam = new Team("German");
        awayTeam = new Team("Poland");
    }

    @Test
    void when_StartGameAndUpdateMetrics_expect_NewGameStartedAndMetricsCorrect() {
        //when
        service.startGame(homeTeam.getName(), awayTeam.getName());
        List<Match> matchList = service.getSummary();
        double countStarted = registry.counter("matches.started").count();

        //then
        assertThat(matchList)
                .hasSize(1)
                .first()
                .extracting(match -> match.getHomeTeam().getName(), match -> match.getAwayTeam().getName())
                .containsExactly(homeTeam.getName(), awayTeam.getName());

        assertThat(countStarted).isEqualTo(1);
    }

    @Test
    void when_UpdateScoreAndMetrics_expect_GameAndMetricsUpdatedCorrectly() {
        //given
        var newMatch = service.startGame(homeTeam.getName(), awayTeam.getName());

        //when
        service.updateScore(newMatch, 2, 1);
        Match updatedMatch = service.getSummary().getFirst();
        double countUpdated = registry.counter("matches.updated").count();

        //then
        assertThat(updatedMatch.getHomeScore()).isEqualTo(2);
        assertThat(updatedMatch.getAwayScore()).isEqualTo(1);

        assertThat(countUpdated).isEqualTo(1);
    }

    @Test
    void when_FinishGameAndUpdateMetrics_GameFinishedAndMetricsUpdatedCorrectly() {
        //given
        homeTeam.setName("Japan");
        awayTeam.setName("China");

        var match = service.startGame(homeTeam.getName(), awayTeam.getName());
        assertThat(service.getSummary()).hasSize(1);
        //when
        service.finishGame(match);
        double countFinished = registry.counter("matches.finished").count();

        //then
        assertThat(service.getSummary()).isEmpty();

        assertThat(countFinished).isEqualTo(1);
    }

    @Test
    void when_ReturnSummaryOrdered_expect_SummaryReturnedCorrectly() {
        //given
        Match mexicoCanada = newMatch("Mexico", "Canada", 0, 5, 3);
        Match spainBrazil = newMatch("Spain", "Brazil", 10, 2, 2);
        Match germanyFrance = newMatch("Germany", "France", 2, 2, 5);
        Match uruguayItaly = newMatch("Uruguay", "Italy", 6, 6, 1);
        Match argentinaAustralia = newMatch("Argentina", "Australia", 3, 1, 4);

        repository.save(uruguayItaly);
        repository.save(spainBrazil);
        repository.save(mexicoCanada);
        repository.save(argentinaAustralia);
        repository.save(germanyFrance);

        //when
        List<Match> summaryList = service.getSummary();

        //then
        assertThat(summaryList)
                .extracting(match -> match.getHomeTeam().getName() + " " + match.getHomeScore() + " - "
                        + match.getAwayTeam().getName() + " " + match.getAwayScore())
                .containsExactly(
                        "Uruguay 6 - Italy 6",
                        "Spain 10 - Brazil 2",
                        "Mexico 0 - Canada 5",
                        "Argentina 3 - Australia 1",
                        "Germany 2 - France 2");
    }

    private Match newMatch(String homeTeam, String awayTeam, int homeScore, int awayScore, int hours) {
        return new Match(new Team(homeTeam), new Team(awayTeam), homeScore, awayScore, OffsetDateTime.now().minusHours(hours));
    }
}
