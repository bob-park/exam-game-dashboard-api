package org.bobpark.domain.game.repository.query;

import reactor.core.publisher.Mono;

import org.bobpark.domain.game.entity.GameDashboard;
import org.bobpark.domain.game.model.UpdateGameDashboardRequest;

public interface GameDashboardQueryRepository {

    Mono<GameDashboard> getWithGame(long id);

    Mono<Long> updateScore(long id, UpdateGameDashboardRequest updateRequest);

}
