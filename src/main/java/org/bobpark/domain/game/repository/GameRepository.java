package org.bobpark.domain.game.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import org.bobpark.domain.game.entity.Game;
import org.bobpark.domain.game.repository.query.GameQueryRepository;

public interface GameRepository extends ReactiveCrudRepository<Game, Long>, GameQueryRepository {



}
