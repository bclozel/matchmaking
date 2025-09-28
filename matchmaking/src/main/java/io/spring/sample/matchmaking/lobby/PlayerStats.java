package io.spring.sample.matchmaking.lobby;

import java.time.Instant;

import io.spring.sample.matchmaking.team.PlayerRank;

record PlayerStats(String playerId, Instant lastSeen, PlayerRank rank) {
}
