package org.bobpark.domain.game.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import org.bobpark.domain.game.model.CreateGameRequest;
import org.bobpark.domain.game.model.GameDashboardResponse;
import org.bobpark.domain.game.model.UpdateGameDashboardRequest;
import org.bobpark.domain.game.service.GameDashboardService;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/games/{gameId:\\d+}/dashboards")
public class GameDashboardController {

    private final GameDashboardService gameDashboardService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public Mono<GameDashboardResponse> createDashboard(@PathVariable long gameId,
        @RequestBody CreateGameRequest createRequest) {
        return gameDashboardService.createdDashboard(gameId, createRequest);
    }

    @GetMapping(path = "")
    public Mono<GameDashboardResponse> getAllByGameId(@PathVariable long gameId) {
        return gameDashboardService.getAllByGameId(gameId);
    }

    @PutMapping(path = "")
    public Mono<GameDashboardResponse> updateDashboard(@PathVariable long gameId,
        @RequestBody UpdateGameDashboardRequest updateRequest) {
        return gameDashboardService.updateDashboard(gameId, updateRequest);
    }

    @PostMapping(path = "current")
    public Mono<GameDashboardResponse> setCurrent(@PathVariable long gameId) {
        return gameDashboardService.setCurrent(gameId);
    }

}
