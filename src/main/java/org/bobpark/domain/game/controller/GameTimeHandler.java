package org.bobpark.domain.game.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import org.bobpark.domain.game.model.GameTimeResponse;
import org.bobpark.domain.game.model.PauseGameTimeRequest;
import org.bobpark.domain.game.model.StartGameTimeRequest;
import org.bobpark.domain.game.service.GameTimeService;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/game/time")
public class GameTimeHandler {

    private final GameTimeService gameTimeService;

    @ConnectMapping("gameTime.connect")
    public void onConnect(RSocketRequester requester) {
        gameTimeService.onConnect(requester);
    }

    @PostMapping(path = "start")
    public Mono<GameTimeResponse> start(@RequestBody StartGameTimeRequest startRequest) {
        return gameTimeService.startGameTime(startRequest);
    }

    @PostMapping(path = "pause")
    public Mono<GameTimeResponse> pause(@RequestBody PauseGameTimeRequest pauseRequest) {
        return gameTimeService.pauseGameTime(pauseRequest);
    }

}
