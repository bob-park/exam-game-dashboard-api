package org.bobpark.domain.stopwatch.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import org.bobpark.domain.stopwatch.model.PauseStopWatchRequest;
import org.bobpark.domain.stopwatch.model.StartStopWatchRequest;
import org.bobpark.domain.stopwatch.model.StopWatchResponse;
import org.bobpark.domain.stopwatch.service.StopWatchService;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/stopwatch")
public class StopWatchHandler {

    private final StopWatchService stopWatchService;

    @ConnectMapping("stopWatch.connect")
    public void onConnect(RSocketRequester requester) {
        stopWatchService.onConnect(requester);
    }

    @PostMapping(path = "start")
    public Mono<StopWatchResponse> start(@RequestBody StartStopWatchRequest startRequest) {
        return stopWatchService.startStopWatch(startRequest);
    }

    @PostMapping(path = "pause")
    public Mono<StopWatchResponse> pause(@RequestBody PauseStopWatchRequest pauseRequest) {
        return stopWatchService.pauseStopWatch(pauseRequest);
    }

}
