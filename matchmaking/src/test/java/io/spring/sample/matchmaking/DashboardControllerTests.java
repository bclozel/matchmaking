package io.spring.sample.matchmaking;

import java.util.List;
import java.util.UUID;

import io.spring.sample.matchmaking.lobby.Lobbies;
import io.spring.sample.matchmaking.lobby.Lobby;
import io.spring.sample.matchmaking.lobby.PlayerRegistrar;
import io.spring.sample.matchmaking.team.PlayerRegion;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(controllers = DashboardController.class)
class DashboardControllerTests {

	@Autowired
	private MockMvcTester mvc;

	@MockitoBean
	private PlayerRegistrar playerRegistrar;

	@MockitoBean
	private Lobbies lobbies;

	@Test
	void getPopulatesModel() {
		Mockito.when(playerRegistrar.getQueueSize()).thenReturn(42L);
		Lobby lobby = new Lobby(UUID.randomUUID(), PlayerRegion.EU, List.of());
		Mockito.when(lobbies.getLobbies()).thenReturn(List.of(lobby));
		assertThat(mvc.get().uri("/")).hasViewName("index")
			.model()
			.containsEntry("queueSize", 42L)
			.containsEntry("lobbies", List.of(lobby));
	}

}
