package com.sport.scoreboard.repository;

import com.sport.scoreboard.domain.model.Match;
import org.springframework.stereotype.Repository;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryMatchRepository implements MatchRepository{

    private final Map<String, Match> ongoingMatches = new ConcurrentHashMap<>();

    private String key(String homeTeam, String awayTeam){
        return homeTeam.toLowerCase() + " : " + awayTeam.toLowerCase();
    }

    @Override
    public Match save(Match match){
        ongoingMatches.put(key(match.getHomeTeam(), match.getAwayTeam()), match);
        return match;
    }

    @Override
    public void delete(Match match){
        ongoingMatches.remove(key(match.getHomeTeam(), match.getAwayTeam()));
    }

    @Override
    public Optional<Match> findByTeams(String homeTeam, String awayTeam) {
        return Optional.ofNullable(ongoingMatches.get(key(homeTeam, awayTeam)));
    }

    @Override
    public List<Match> findAll(){
        return new ArrayList<>(ongoingMatches.values());
    }
}
