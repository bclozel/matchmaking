package io.spring.sample.matchmaking.lobby;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
class PlayerApiClient {

	private final RestClient playerProfileClient;

	private final RestClient playerStatsClient;

	PlayerApiClient(RestClient.Builder clientBuilder, LobbyProperties properties) {
		this.playerProfileClient = clientBuilder.baseUrl(properties.getPlayer().getProfileUrl()).build();
		this.playerStatsClient = clientBuilder.baseUrl(properties.getPlayer().getStatsUrl()).build();
	}

	PlayerProfile getPlayerProfile(String playerId) {
		return playerProfileClient.get()
			.uri("/profile/{playerId}", playerId)
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.body(PlayerProfile.class);
	}

	PlayerStats fetchPlayerStats(String playerId) {
		return playerStatsClient.get()
			.uri("/stats/{playerId}", playerId)
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.body(PlayerStats.class);
	}

}
