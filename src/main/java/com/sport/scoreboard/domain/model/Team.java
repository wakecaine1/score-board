package com.sport.scoreboard.domain.model;

import com.sport.scoreboard.util.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    private final Integer id = Utils.getRandomInt();
    private String name;

}
