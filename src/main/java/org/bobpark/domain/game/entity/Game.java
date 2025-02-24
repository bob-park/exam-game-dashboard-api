package org.bobpark.domain.game.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import org.apache.commons.lang3.StringUtils;

import org.bobpark.common.entity.BaseEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "games")
public class Game extends BaseEntity {

    @Id
    private Long id;

    private String name;
    private String description;

    private LocalDate gameDate;

    @Exclude
    @Transient
    private List<GameDashboard> gameDashboards = new ArrayList<>();

    @Builder
    private Game(Long id, String name, String description, LocalDate gameDate) {

        checkArgument(StringUtils.isNotBlank(name), "name must be provided.");
        checkArgument(isNotEmpty(gameDate), "gameDate must be provided.");

        this.id = id;
        this.name = name;
        this.description = description;
        this.gameDate = defaultIfNull(gameDate, LocalDate.now());
    }

    public void addGameDashboard(GameDashboard gameDashboard) {

        gameDashboard.updateGame(this);

        getGameDashboards().add(gameDashboard);
    }

}
