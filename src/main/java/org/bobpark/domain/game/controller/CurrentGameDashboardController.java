package org.bobpark.domain.game.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import org.bobpark.domain.game.service.CurrentGameDashboardService;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/games/{gameId:\\d+}/dashboards/current")
public class CurrentGameDashboardController {

    private final CurrentGameDashboardService currentService;

}
