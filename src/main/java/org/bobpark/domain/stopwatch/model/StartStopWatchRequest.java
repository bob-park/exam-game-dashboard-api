package org.bobpark.domain.stopwatch.model;

import static org.apache.commons.lang3.ObjectUtils.*;

import lombok.Builder;

@Builder
public record StartStopWatchRequest(Long seconds) {

    public StartStopWatchRequest {
        seconds = defaultIfNull(seconds, 24L);
    }

}
