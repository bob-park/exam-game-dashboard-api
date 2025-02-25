package org.bobpark.domain.game.repository.query.impl;

import java.time.LocalDate;

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
            "select gd.id, gd.home_score, gd.away_score, gd.created_date, gd.last_modified_date, game.id as game_id, game.name as game_name, game.description as game_description, game.game_date as game_date from game_dashboards gd join games game on gd.game_id = game.id "
                + "where gd.id = " + id;

        return client.sql(query)
            .map(
                (row, metadata) -> {
                    GameDashboard result =
                        GameDashboard.builder()
                            .id(row.get("id", Long.class))
                            .homeScore(row.get("home_score", Long.class))
                            .awayScore(row.get("away_score", Long.class))
                            .gameId(row.get("game_id", Long.class))
                            .build();

                    result.updateGame(
                        Game.builder()
                            .id(row.get("game_id", Long.class))
                            .name(row.get("game_name", String.class))
                            .description(row.get("game_description", String.class))
                            .gameDate(row.get("game_date", LocalDate.class))
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
