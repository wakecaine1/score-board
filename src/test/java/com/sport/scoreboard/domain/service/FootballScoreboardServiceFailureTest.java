package com.sport.scoreboard.domain.service;

import com.sport.scoreboard.domain.model.Match;
import com.sport.scoreboard.domain.model.Team;
import com.sport.scoreboard.domain.service.impl.FootballScoreboardService;
import com.sport.scoreboard.repository.MatchRepository;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Football Scoreboard Service Failure Tests")
public class FootballScoreboardServiceFailureTest {

    private MatchRepository repository;
    private SimpleMeterRegistry registry;
    private ScoreboardService service;

    @BeforeEach
    void setup() {
        repository = mock(MatchRepository.class);
        registry = new SimpleMeterRegistry();
        service = new FootballScoreboardService(repository, registry);
    }

    @Test
    void when_TeamNamesProvidedBlank_expect_IllegalArgumentException() {
        //when && then
        assertThatThrownBy(() -> service.startGame("", "Germany"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("valid names");
        assertThatThrownBy(() -> service.startGame("Poland", " "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("valid names");
    }

    @Test
    void when_TeamNamesAreTheSame_expect_IllegalArgumentException() {
        //when && then
        assertThatThrownBy(() -> service.startGame("German", "German"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot play against itself");
    }

    @Test
    void when_StartGameAlreadyExists_expect_IllegalStateException() {
        //given
        var startedGame = service.startGame("Germany", "Poland");
        when(repository.findByTeams("Germany", "Poland"))
                .thenReturn(Optional.of((startedGame)));

        //when && then
        assertThatThrownBy(() -> service.startGame("Germany", "Poland"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already started");

        assertThat(registry.counter("matches.started").count()).isEqualTo(1);
    }

    @Test
    void when_UpdateNotFoundGame_expect_IllegalArgumentException() {
        //given
        var homeTeam = new Team("Germany");
        var awayTeam = new Team("Poland");
        Match match = new Match(homeTeam, awayTeam, 0, 0, OffsetDateTime.now());
        when(repository.findByTeams(homeTeam.getName(), awayTeam.getName())).thenReturn(Optional.of(match));

        // when && then
        assertThatThrownBy(() -> service.updateScore(match, -1, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Score cannot be negative");

        assertThat(registry.counter("matches.updated").count()).isEqualTo(0);
    }

    @Test
    void when_FinishNotFoundGame_expect_IllegalStateException() {
        //given
        var homeTeam = new Team("USA");
        var awayTeam = new Team("Poland");
        when(repository.findByTeams(homeTeam.getName(), awayTeam.getName())).thenReturn(Optional.empty());

        //when && then
        assertThatThrownBy(() -> service.finishGame(new Match(homeTeam, awayTeam, 0, 0, OffsetDateTime.now())))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot finish match that does not exist");

        assertThat(registry.counter("matches.finished").count()).isEqualTo(0);
    }
}