package org.bobpark.domain.game.service;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.bobpark.domain.game.model.GameTimeAction;
import org.bobpark.domain.game.model.GameTimeResponse;
import org.bobpark.domain.game.model.PauseGameTimeRequest;
import org.bobpark.domain.game.model.StartGameTimeRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameTimeService {

    private final List<RSocketRequester> requesters = new ArrayList<>();

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

    public Mono<GameTimeResponse> startGameTime(StartGameTimeRequest startRequest) {
        return Mono.just(
                GameTimeResponse.builder()
                    .action(GameTimeAction.START)
                    .seconds(startRequest.seconds())
                    .build())
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

    public Mono<GameTimeResponse> pauseGameTime(PauseGameTimeRequest endRequest) {
        return Mono.just(
                GameTimeResponse.builder()
                    .action(GameTimeAction.PAUSE)
                    .seconds(endRequest.seconds())
                    .build())
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
}
