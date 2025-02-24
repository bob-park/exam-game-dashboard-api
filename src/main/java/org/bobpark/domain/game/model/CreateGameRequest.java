package org.bobpark.domain.game.model;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record CreateGameRequest(String name,
                                String description,
                                LocalDate gameDate) {
}
