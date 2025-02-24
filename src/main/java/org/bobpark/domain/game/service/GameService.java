package org.bobpark.domain.game.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Mono;

import org.bobpark.domain.game.entity.Game;
import org.bobpark.domain.game.model.CreateGameRequest;
import org.bobpark.domain.game.model.GameResponse;
import org.bobpark.domain.game.model.SearchGameRequest;
import org.bobpark.domain.game.repository.GameRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GameService {

    private final GameRepository gameRepository;

    @Transactional
    public Mono<GameResponse> createGame(CreateGameRequest createRequest) {

        Game createdGame =
            Game.builder()
                .name(createRequest.name())
                .description(createRequest.description())
                .gameDate(createRequest.gameDate())
                .build();

        return gameRepository.save(createdGame)
            .doOnSuccess(item -> {
                log.debug("created game. {}", item);
            })
            .map(GameResponse::from);
    }

    public Mono<Page<GameResponse>> getGames(SearchGameRequest searchRequest, Pageable pageable) {
        return gameRepository.search(searchRequest, pageable)
            .map(page -> page.map(GameResponse::from));

    }
}
