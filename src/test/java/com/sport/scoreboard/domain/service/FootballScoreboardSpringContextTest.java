package com.sport.scoreboard.domain.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.sport.scoreboard.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class FootballScoreboardSpringContextTest {

    @Autowired
    ScoreboardService service;

    @Test
    void contextLoads(){
        assertThat(service).isNotNull();
    }
}
