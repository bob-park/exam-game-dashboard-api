package org.bobpark.domain.game.model;

import static org.apache.commons.lang3.ObjectUtils.*;

import lombok.Builder;

@Builder
public record StartGameTimeRequest(Long seconds) {

    public StartGameTimeRequest {
        seconds = defaultIfNull(seconds, 600L);
    }

}
