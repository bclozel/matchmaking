package io.spring.sample.matchmaking.lobby;

import java.util.Collections;

import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
class PlayerApiClient {

	final RestTemplate playerProfileClient;

	final RestTemplate playerStatsClient;

	PlayerApiClient(RestTemplateBuilder clientBuilder, LobbyProperties properties) {
		this.playerProfileClient = clientBuilder.rootUri(properties.getPlayer().getProfileUrl()).build();
		this.playerStatsClient = clientBuilder.rootUri(properties.getPlayer().getStatsUrl()).build();
	}

	PlayerProfile getPlayerProfile(String playerId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		ResponseEntity<PlayerProfile> exchange = playerProfileClient.exchange("/profile/{playerId}", HttpMethod.GET,
				new HttpEntity<>(headers), PlayerProfile.class, playerId);
		return exchange.getBody();

	}

	PlayerStats getchPlayerStats(String playerId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		ResponseEntity<PlayerStats> exchange = playerStatsClient.exchange("/stats/{playerId}", HttpMethod.GET,
				new HttpEntity<>(headers), PlayerStats.class, playerId);
		return exchange.getBody();
	}

}
