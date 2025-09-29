package io.spring.sample.matchmaking.lobby;

import org.springframework.http.MediaType;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
class PlayerApiClient {

	private final RestClient playerProfileClient;

	private final RestClient playerStatsClient;

	PlayerApiClient(RestClient.Builder clientBuilder, LobbyProperties properties) {
		this.playerProfileClient = clientBuilder.baseUrl(properties.getPlayer().getProfileUrl()).build();
		this.playerStatsClient = clientBuilder.baseUrl(properties.getPlayer().getStatsUrl()).build();
	}

	PlayerProfile getPlayerProfile(String playerId) {
		PlayerProfile profile = playerProfileClient.get()
			.uri("/profile/{playerId}", playerId)
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.body(PlayerProfile.class);
		Assert.state(profile != null, "Player profile should not be null");
		return profile;
	}

	@Retryable(includes = RestClientException.class, delayString = "300ms", multiplier = 1.5)
	PlayerStats fetchPlayerStats(String playerId) {
		PlayerStats stats = playerStatsClient.get()
			.uri("/stats/{playerId}", playerId)
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.body(PlayerStats.class);
		Assert.notNull(stats, "Player stats should not be null");
		return stats;
	}

}
