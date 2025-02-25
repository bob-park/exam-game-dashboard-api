package org.bobpark.domain.game.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

import org.bobpark.domain.game.entity.GameDashboard;
import org.bobpark.domain.game.repository.query.GameDashboardQueryRepository;

public interface GameDashboardRepository extends ReactiveCrudRepository<GameDashboard, Long>,
    GameDashboardQueryRepository {

    Flux<GameDashboard> findAllByGameId(Long gameId);
}
