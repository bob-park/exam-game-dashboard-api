package org.bobpark.domain.game.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.bobpark.domain.game.entity.Game;
import org.bobpark.domain.game.entity.GameDashboard;
import org.bobpark.domain.game.model.CreateGameRequest;
import org.bobpark.domain.game.model.GameDashboardResponse;
import org.bobpark.domain.game.repository.GameDashboardRepository;
import org.bobpark.domain.game.repository.GameRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GameDashboardService {

    private final GameRepository gameRepository;
    private final GameDashboardRepository gameDashboardRepository;

    @Transactional
    public Mono<GameDashboardResponse> createdDashboard(Long gameId, CreateGameRequest createRequest) {

        Mono<Game> gameMono =
            gameRepository.findById(gameId)
                .doOnError(e -> {
                    log.error("game not found.. (gameId={})", gameId);
                    throw new IllegalArgumentException("game not found");
                });

        GameDashboard createdGameDashboard =
            GameDashboard.builder()
                .gameId(gameId)
                .build();

        return gameDashboardRepository.save(createdGameDashboard)
            .zipWith(gameMono)
            .map(tuple -> {
                tuple.getT1().updateGame(tuple.getT2());

                return GameDashboardResponse.from(tuple.getT1());
            });
    }

    public Flux<GameDashboardResponse> getAllByGameId(Long gameId) {
        return gameDashboardRepository.findByGameId(gameId)
            .map(GameDashboardResponse::from);
    }
}
