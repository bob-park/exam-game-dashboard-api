package org.bobpark.domain.stopwatch.service;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.bobpark.domain.stopwatch.model.PauseStopWatchRequest;
import org.bobpark.domain.stopwatch.model.StartStopWatchRequest;
import org.bobpark.domain.stopwatch.model.StopWatchAction;
import org.bobpark.domain.stopwatch.model.StopWatchResponse;

@Slf4j
@RequiredArgsConstructor
@Service
public class StopWatchService {
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

    public Mono<StopWatchResponse> startStopWatch(StartStopWatchRequest startRequest) {
        return Mono.just(
                StopWatchResponse.builder()
                    .action(StopWatchAction.START)
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

    public Mono<StopWatchResponse> pauseStopWatch(PauseStopWatchRequest endRequest) {
        return Mono.just(
                StopWatchResponse.builder()
                    .action(StopWatchAction.PAUSE)
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
