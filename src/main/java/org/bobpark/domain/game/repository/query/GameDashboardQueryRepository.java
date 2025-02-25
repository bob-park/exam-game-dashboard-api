package org.bobpark.domain.game.repository.query;

import reactor.core.publisher.Mono;

import org.bobpark.domain.game.entity.GameDashboard;

public interface GameDashboardQueryRepository {

    Mono<GameDashboard> getWithGame(long id);

}
