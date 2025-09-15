package io.spring.sample.playerprofile;

import java.time.Instant;

public record PlayerStats(String playerId, Instant lastSeen, PlayerRank rank) {
}
