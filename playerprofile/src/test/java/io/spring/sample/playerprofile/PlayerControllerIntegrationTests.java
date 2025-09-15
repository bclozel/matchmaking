package io.spring.sample.playerprofile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerIntegrationTests {

	private static final List<String> REGIONS = List.of("EU", "AMER", "ME", "ASIA", "OCE");

	@Autowired
	private MockMvcTester mockMvc;

	@Test
	void fetchPlayerInfo() {
		assertThat(mockMvc.get().uri("/players/profile/{id}", "id-123").queryParam("delay", "0")).hasStatusOk()
			.bodyJson()
			.convertTo(PlayerProfile.class)
			.satisfies((profile) -> {
				assertThat(profile.playerId()).isEqualTo("id-123");
				assertThat(REGIONS).contains(profile.region());
			});
	}

}
