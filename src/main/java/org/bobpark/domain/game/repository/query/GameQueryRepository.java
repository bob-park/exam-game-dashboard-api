package org.bobpark.domain.game.repository.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import reactor.core.publisher.Mono;

import org.bobpark.domain.game.entity.Game;
import org.bobpark.domain.game.model.SearchGameRequest;

public interface GameQueryRepository {
    Mono<Page<Game>> search(SearchGameRequest searchRequest, Pageable pageable);
}
