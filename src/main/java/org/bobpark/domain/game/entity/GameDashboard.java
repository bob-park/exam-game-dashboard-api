package org.bobpark.domain.game.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import org.bobpark.common.entity.BaseEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "game_dashboards")
public class GameDashboard extends BaseEntity {

    @Id
    private Long id;

    private Long gameId;

    @Exclude
    @Transient
    private Game game;

    private Long homeScore;
    private Long awayScore;

    @Builder
    private GameDashboard(Long id, Long gameId, Long homeScore, Long awayScore) {

        checkArgument(isNotEmpty(gameId), "gameId must be provided.");

        this.id = id;
        this.gameId = gameId;
        this.homeScore = defaultIfNull(homeScore, 0L);
        this.awayScore = defaultIfNull(awayScore, 0L);
    }

    public void updateGame(Game game) {
        checkArgument(isNotEmpty(game), "game must be provided.");

        this.game = game;

        this.gameId = game.getId();
    }

    public void updateHomeScore(long homeScore) {
        this.homeScore = homeScore;
    }

    public void updateAwayScore(long awayScore) {
        this.awayScore = awayScore;
    }

}
