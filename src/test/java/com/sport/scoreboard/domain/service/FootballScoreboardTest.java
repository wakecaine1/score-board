package com.sport.scoreboard.domain.service;

import com.sport.scoreboard.domain.service.impl.FootballScoreboardService;
import com.sport.scoreboard.repository.MatchRepository;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.sport.scoreboard.repository.InMemoryMatchRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class FootballScoreboardTest {

    private ScoreboardService service;
    private MatchRepository repository;
    private SimpleMeterRegistry registry;

    @BeforeEach
    void setUp(){
        repository = mock(InMemoryMatchRepository.class);
        registry = new SimpleMeterRegistry();
        service = new FootballScoreboardService(repository, registry);
    }
}
