package org.bobpark.domain.game.model;

import static org.apache.commons.lang3.ObjectUtils.*;

import lombok.Builder;

@Builder
public record UpdateGameDashboardRequest(Long homeScore,
                                         Long awayScore) {

    public UpdateGameDashboardRequest {
        homeScore = defaultIfNull(homeScore , 0L);
        awayScore = defaultIfNull(awayScore , 0L);
    }

}
