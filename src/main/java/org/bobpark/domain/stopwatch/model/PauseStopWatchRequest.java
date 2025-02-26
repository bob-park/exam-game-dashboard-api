package org.bobpark.domain.stopwatch.model;

import lombok.Builder;

@Builder
public record PauseStopWatchRequest(Long seconds) {
}
