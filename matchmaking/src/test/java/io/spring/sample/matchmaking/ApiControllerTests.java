package io.spring.sample.matchmaking;

import java.util.List;
import java.util.UUID;

import io.spring.sample.matchmaking.lobby.Lobbies;
import io.spring.sample.matchmaking.lobby.Lobby;
import io.spring.sample.matchmaking.lobby.PlayerRegistrar;
import io.spring.sample.matchmaking.team.PlayerRank;
import io.spring.sample.matchmaking.team.PlayerRegion;
import io.spring.sample.matchmaking.team.Team;
import io.spring.sample.matchmaking.team.TeamMember;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiController.class)
public class ApiControllerTests {

	@MockitoBean
	private PlayerRegistrar playerRegistrar;

	@MockitoBean
	private Lobbies lobbies;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldListLobbies() throws Exception {
		UUID uuid = UUID.randomUUID();
		List<TeamMember> teamA = List.of(
				new TeamMember("p1", "Player1", PlayerRegion.EU, 100, "Console", new PlayerRank(500, "Silver")),
				new TeamMember("p2", "Player2", PlayerRegion.EU, 100, "Console", new PlayerRank(600, "Gold")));
		List<TeamMember> teamB = List.of(
				new TeamMember("p3", "Player3", PlayerRegion.EU, 100, "Console", new PlayerRank(500, "Silver")),
				new TeamMember("p4", "Player4", PlayerRegion.EU, 100, "Console", new PlayerRank(600, "Gold")));
		Lobby lobby = new Lobby(uuid, PlayerRegion.EU, List.of(new Team(teamA), new Team(teamB)));
		Mockito.when(lobbies.getLobbies()).thenReturn(List.of(lobby));

		mockMvc.perform(get("/api/lobbies"))
			.andExpectAll(status().isOk(), content().contentType("application/json"),
					jsonPath("$[:1].uuid").value(uuid.toString()),
					jsonPath("$[:1].region").value(PlayerRegion.EU.name()), jsonPath("$[:1].teams.length()").value(2));

	}

}
