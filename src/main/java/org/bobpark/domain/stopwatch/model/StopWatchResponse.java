package org.bobpark.domain.stopwatch.model;

import lombok.Builder;

@Builder
public record StopWatchResponse(StopWatchAction action,
                                Long seconds) {
}
