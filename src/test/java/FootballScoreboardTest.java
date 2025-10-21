import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sport.scoreboard.repository.InMemoryMatchRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class FootballScoreboardTest {

    private ScoreboardService service;

    @BeforeEach
    void setUp(){
        repository = mock(InMemoryMatchRepository.class);
        registry = new SimpleMeterRegistry();
        service = new FootballScoreboardService(repository, registry);
    }

    @Test
    @DisplayName
    void when_bothTeamsAreTheSame_expect_IllegalArgumentException(){
        //given
        String team = "Germany";

        assertThatThrownBy(() -> service.startGame(team, team))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining
    }
}
