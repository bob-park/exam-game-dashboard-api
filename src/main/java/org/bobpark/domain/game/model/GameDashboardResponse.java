package org.bobpark.domain.game.model;

import java.time.LocalDateTime;

import lombok.Builder;

import org.bobpark.domain.game.entity.GameDashboard;

@Builder(toBuilder = true)
public record GameDashboardResponse(Long id,
                                    GameResponse game,
                                    Long homeScore,
                                    Long awayScore,
                                    LocalDateTime createdDate,
                                    LocalDateTime lastModifiedDate) {

    public static GameDashboardResponse from(GameDashboard gameDashboard) {
        return GameDashboardResponse.builder()
            .id(gameDashboard.getId())
            .game(gameDashboard.getGame() != null ? GameResponse.from(gameDashboard.getGame()) : null)
            .homeScore(gameDashboard.getHomeScore())
            .awayScore(gameDashboard.getAwayScore())
            .createdDate(gameDashboard.getCreatedDate())
            .lastModifiedDate(gameDashboard.getLastModifiedDate())
            .build();
    }

}
