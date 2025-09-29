package io.spring.sample.matchmaking.lobby;

import java.time.Instant;

import io.spring.sample.matchmaking.team.PlayerRank;
import io.spring.sample.matchmaking.team.PlayerRegion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.http.HttpHeadersAssert;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerApiClientTests {

	private final PlayerApiClient client;

	private final MockRestServiceServer server;

	PlayerApiClientTests(@Autowired RestClient.Builder clientBuilder, @Autowired MockRestServiceServer server) {
		this.client = new PlayerApiClient(clientBuilder, new LobbyProperties());
		this.server = server;
	}

	@Test
	void getPlayerProfile() {
		String profile = """
				{
				  "playerId": "az-123",
				  "gamerTag": "Other78",
				  "platform": "PlayStation 2",
				  "region": "EU",
				  "ping": 20
				}
				""";
		server.expect(prepareRequest("/players/profile/az-123"))
			.andRespond(withSuccess(profile, MediaType.APPLICATION_JSON));
		assertThat(client.getPlayerProfile("az-123"))
			.isEqualTo(new PlayerProfile("az-123", "Other78", "PlayStation 2", PlayerRegion.EU, 20));
	}

	@Test
	void getPlayerStats() {
		String profile = """
				{
				  "lastSeen": "2025-08-31T20:22:06.788Z",
				  "playerId": "az-123",
				  "rank": {
				    "score": 941,
				    "level": "Unreal"
				  }
				}
				""";
		server.expect(prepareRequest("/players/stats/az-123"))
			.andRespond(withSuccess(profile, MediaType.APPLICATION_JSON));
		assertThat(client.fetchPlayerStats("az-123")).isEqualTo(
				new PlayerStats("az-123", Instant.parse("2025-08-31T20:22:06.788Z"), new PlayerRank(941, "Unreal")));
	}

	public static RequestMatcher prepareRequest(String path) {
		return (request) -> {
			assertThat(request.getURI()).hasPath(path);
			new HttpHeadersAssert(request.getHeaders()).hasSingleValue(HttpHeaders.ACCEPT,
					MediaType.APPLICATION_JSON_VALUE);
		};
	}

}
