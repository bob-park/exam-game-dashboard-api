package org.bobpark.domain.game.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;

import org.bobpark.domain.game.entity.Game;

@Builder
public record GameResponse(Long id,
                           String name,
                           String description,
                           LocalDate gameDate,
                           LocalDateTime createdDate,
                           LocalDateTime lastModifiedDate) {

    public static GameResponse from(Game game) {
        return GameResponse.builder()
            .id(game.getId())
            .name(game.getName())
            .description(game.getDescription())
            .gameDate(game.getGameDate())
            .createdDate(game.getCreatedDate())
            .lastModifiedDate(game.getLastModifiedDate())
            .build();
    }
}
