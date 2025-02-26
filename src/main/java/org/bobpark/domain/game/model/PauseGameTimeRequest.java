package org.bobpark.domain.game.model;

import static org.apache.commons.lang3.ObjectUtils.*;

import lombok.Builder;

@Builder
public record PauseGameTimeRequest(Long seconds) {

    public PauseGameTimeRequest {
        seconds = defaultIfNull(seconds, 600L);
    }

}
