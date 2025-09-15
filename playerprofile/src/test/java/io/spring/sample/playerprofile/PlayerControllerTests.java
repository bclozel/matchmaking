package io.spring.sample.playerprofile;

import io.spring.sample.playerprofile.faker.GamerData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@WebMvcTest(PlayerController.class)
class PlayerControllerTests {

	@MockitoBean
	private GamerData gamerData;

	@Autowired
	private MockMvcTester mockMvc;

	@Test
	void fetchPlayerInfo() {
		PlayerProfile playerProfile = new PlayerProfile("id-123", "Player123", "Console", "EU", 40);
		given(gamerData.getPlayerProfile("id-123")).willReturn(playerProfile);
		assertThat(mockMvc.get().uri("/players/profile/{id}", "id-123").queryParam("delay", "0")).bodyJson()
			.isLenientlyEqualTo("""
					{
					  "playerId":"id-123",
					  "gamerTag":"Player123",
					  "platform":"Console",
					  "region":"EU",
					  "ping":40
					 }""");
		then(gamerData).should().getPlayerProfile("id-123");
		then(gamerData).shouldHaveNoMoreInteractions();
	}

	@Test
	void fetchPlayerStats() {
		PlayerStats playerStats = new PlayerStats("id-123", Instant.parse("2025-06-01T10:11:00.000Z"),
				new PlayerRank(500, "Silver"));
		given(gamerData.getPlayerStats("id-123")).willReturn(playerStats);
		assertThat(mockMvc.get().uri("/players/stats/{id}", "id-123").queryParam("delay", "0")).bodyJson()
			.isLenientlyEqualTo("""
					{
					  "playerId":"id-123",
					  "lastSeen":"2025-06-01T10:11:00Z",
					  "rank": {
					    "score":500,
					    "level":"Silver"
					  }
					}""");
		then(gamerData).should().getPlayerStats("id-123");
		then(gamerData).shouldHaveNoMoreInteractions();
	}

	@Test
	void fetchPlayerStatsCanFail() {
		String failureRate = """
				{ "rate": 1.0 }""";
		assertThat(
				mockMvc.post().uri("/admin/failureRate").content(failureRate).contentType(MediaType.APPLICATION_JSON))
			.hasStatus(HttpStatus.NO_CONTENT);

		given(gamerData.getPlayerStats("id-123")).willReturn(mock(PlayerStats.class));
		assertThat(mockMvc.get().uri("/players/stats/{id}", "id-123").queryParam("delay", "0")).hasFailed()
			.hasStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		then(gamerData).shouldHaveNoMoreInteractions();
	}

}
