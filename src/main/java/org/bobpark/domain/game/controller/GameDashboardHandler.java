package org.bobpark.domain.game.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import org.bobpark.domain.game.model.GameDashboardResponse;
import org.bobpark.domain.game.service.GameDashboardService;

@RequiredArgsConstructor
@RestController
public class GameDashboardHandler {

    private final GameDashboardService gameDashboardService;

    @ConnectMapping("connect")
    public void onConnect(RSocketRequester requester) {
        gameDashboardService.onConnect(requester);
    }

    @MessageMapping("gameDashboard.current")
    public Mono<GameDashboardResponse> getCurrent() {
        return gameDashboardService.getCurrent();
    }



}
