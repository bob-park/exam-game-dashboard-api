package org.bobpark.domain.game.repository.query.impl;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;

import reactor.core.publisher.Mono;

import org.bobpark.domain.game.entity.Game;
import org.bobpark.domain.game.model.SearchGameRequest;
import org.bobpark.domain.game.repository.query.GameQueryRepository;

@RequiredArgsConstructor
public class GameQueryRepositoryImpl implements GameQueryRepository {

    private final DatabaseClient client;

    @Override
    public Mono<Page<Game>> search(SearchGameRequest searchRequest, Pageable pageable) {

        // select
        String countQuery = "select count(id) as count "
            + "from games ";

        String query = "select id, name, description, game_date, created_date, last_modified_date "

            // from
            + "from games "

            // where
            // TODO: 구현 해야되나....

            // order by
            + "order by created_date desc "

            // pagination
            + String.format("limit %s offset %d", pageable.getPageSize(), pageable.getOffset());

        // count
        Mono<Long> count =
            client.sql(countQuery)
                .map((row, rowMetadata) -> row.get("count", Long.class))
                .one();

        return client.sql(query)
            .map((row, rowMetadata) ->
                Game.builder()
                    .id(row.get("id", Long.class))
                    .name(row.get("name", String.class))
                    .description(row.get("description", String.class))
                    .gameDate(row.get("game_date", LocalDate.class))
                    .build())
            .all()
            .collectList()
            .zipWith(count)
            .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }
}
