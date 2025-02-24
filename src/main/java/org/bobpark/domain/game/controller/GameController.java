package org.bobpark.domain.game.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import org.bobpark.common.model.SimplePageable;
import org.bobpark.domain.game.model.CreateGameRequest;
import org.bobpark.domain.game.model.GameResponse;
import org.bobpark.domain.game.model.SearchGameRequest;
import org.bobpark.domain.game.service.GameService;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/games")
public class GameController {

    private final GameService gameService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public Mono<GameResponse> createGame(@RequestBody CreateGameRequest createRequest) {
        return gameService.createGame(createRequest);
    }

    @GetMapping(path = "")
    public Mono<Page<GameResponse>> getGames(SearchGameRequest searchRequest,
        SimplePageable pageable) {
        return gameService.getGames(searchRequest, pageable.toPageable());
    }

}
