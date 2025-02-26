package org.bobpark.domain.game.model;

import lombok.Builder;

@Builder
public record GameTimeResponse(GameTimeAction action,
                               Long seconds) {
}
