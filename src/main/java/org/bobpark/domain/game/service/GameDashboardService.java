package org.bobpark.domain.game.service;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import org.bobpark.domain.game.entity.Game;
import org.bobpark.domain.game.entity.GameDashboard;
import org.bobpark.domain.game.model.CreateGameRequest;
import org.bobpark.domain.game.model.GameDashboardResponse;
import org.bobpark.domain.game.model.UpdateGameDashboardRequest;
import org.bobpark.domain.game.repository.GameDashboardRepository;
import org.bobpark.domain.game.repository.GameRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GameDashboardService {

    private GameDashboardResponse current;

    private final List<RSocketRequester> requesters = new ArrayList<>();

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
        return gameDashboardRepository.findAllByGameId(gameId)
            .map(GameDashboardResponse::from);
    }

    public void onConnect(RSocketRequester requester) {
        requester.rsocket()
            .onClose()
            .doFirst(() -> {
                requesters.add(requester);

                log.debug("connected...");
            })
            .doOnError(error -> {
                log.error("rsocket error - {}", error.getMessage(), error);
            })
            .doFinally(consumer -> {
                if (requesters.contains(requester)) {
                    requesters.remove(requester);
                    log.debug("disconnected...");
                }
            })
            .subscribe();
    }

    public Mono<GameDashboardResponse> getCurrent() {
        return Mono.justOrEmpty(current)
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(
                item ->
                    Flux.fromIterable(requesters)
                        .publishOn(Schedulers.boundedElastic())
                        .doOnNext(
                            ea ->
                                ea.route("")
                                    .data(item)
                                    .send()
                                    .subscribe())
                        .subscribe());
    }

    public Mono<GameDashboardResponse> setCurrent(long id) {

        return gameDashboardRepository.findById(id)
            .map(GameDashboardResponse::from)
            .doOnSuccess(item -> current = item);
    }

    @Transactional
    public Mono<GameDashboardResponse> updateDashboard(long id, UpdateGameDashboardRequest updateRequest) {

        return gameDashboardRepository.updateScore(id, updateRequest)
            .doOnSuccess((count) -> {
                log.debug("updated game dashboard. (id={})", id);
            })
            .zipWith(gameDashboardRepository.findById(id))
            .map(Tuple2::getT2)
            .map(GameDashboardResponse::from)
            .doOnNext(item -> current = item)
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(item ->
                Flux.fromIterable(requesters)
                    .publishOn(Schedulers.boundedElastic())
                    .doOnNext(ea -> ea.route("")
                        .data(item)
                        .send()
                        .subscribe())
                    .subscribe()
            );

    }

}
