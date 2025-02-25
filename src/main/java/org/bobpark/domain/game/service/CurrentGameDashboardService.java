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

import org.bobpark.domain.game.model.GameDashboardResponse;
import org.bobpark.domain.game.repository.GameDashboardRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CurrentGameDashboardService {

    private GameDashboardResponse current;

    private final List<RSocketRequester> requesters = new ArrayList<>();

    private final GameDashboardRepository gameDashboardRepository;

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

        return null;
    }
}
