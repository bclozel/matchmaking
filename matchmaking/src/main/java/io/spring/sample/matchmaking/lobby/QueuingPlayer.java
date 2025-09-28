package io.spring.sample.matchmaking.lobby;

import java.time.Instant;

public record QueuingPlayer(PlayerProfile playerProfile, PlayerStats playerStats, Instant queuingSince) {
}
