package com.sport.scoreboard.util;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static Integer getRandomInt(){
        return ThreadLocalRandom.current().nextInt(0, 1000000);
    }
}
