package org.bobpark.domain.game.model;

import static org.apache.commons.lang3.ObjectUtils.*;

import lombok.Builder;

@Builder
public record ScoreRequest(Long minus,
                           Long plus) {

    public ScoreRequest {
        minus = defaultIfNull(minus, 0L);
        plus = defaultIfNull(plus, 0L);
    }
}
