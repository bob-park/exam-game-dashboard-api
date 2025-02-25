package org.bobpark.domain.game.repository.query.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.r2dbc.core.DatabaseClient;

import reactor.core.publisher.Mono;

import org.bobpark.domain.game.entity.Game;
import org.bobpark.domain.game.entity.GameDashboard;
import org.bobpark.domain.game.model.UpdateGameDashboardRequest;
import org.bobpark.domain.game.repository.query.GameDashboardQueryRepository;

@RequiredArgsConstructor
public class GameDashboardQueryRepositoryImpl implements GameDashboardQueryRepository {

    private final DatabaseClient client;

    @Override
    public Mono<GameDashboard> getWithGame(long id) {

        String query =
            "select gd.id, gd.home_score, gd.away_score, gd.created_date, gd.last_modified_date, game.id, game.name, game.destcription from game_dashboards gd join games game on gd.game_id = game.id "
                + "where gd.id = " + id;

        return client.sql(query)
            .map(
                (row, metadata) -> {
                    GameDashboard result =
                        GameDashboard.builder()
                            .id(row.get("gd.id", Long.class))
                            .homeScore(row.get("gd.home_score", Long.class))
                            .awayScore(row.get("gd.away_score", Long.class))
                            .build();

                    result.updateGame(
                        Game.builder()
                            .id(row.get("game.id", Long.class))
                            .name(row.get("game.name", String.class))
                            .description(row.get("game.description", String.class))
                            .build());

                    return result;
                }
            )
            .one();
    }

    @Override
    public Mono<Long> updateScore(long id, UpdateGameDashboardRequest updateRequest) {

        String query =
            "update game_dashboards "
                // set
                + "set home_score = home_score + :home_score, "
                + "away_score = away_score + :away_score  "
                // where
                + "where id = :id";

        return client.sql(query)
            .bind("id", id)
            .bind("home_score", updateRequest.homeScore())
            .bind("away_score", updateRequest.awayScore())
            .fetch().rowsUpdated();
    }
}
