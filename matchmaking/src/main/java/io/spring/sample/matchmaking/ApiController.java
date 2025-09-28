package io.spring.sample.matchmaking;

import java.util.List;

import io.spring.sample.matchmaking.lobby.Lobbies;
import io.spring.sample.matchmaking.lobby.Lobby;
import io.spring.sample.matchmaking.lobby.PlayerRegistrar;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
class ApiController {

	private final PlayerRegistrar playerRegistrar;

	private final Lobbies lobbies;

	ApiController(PlayerRegistrar playerRegistrar, Lobbies lobbies) {
		this.playerRegistrar = playerRegistrar;
		this.lobbies = lobbies;
	}

	@PostMapping(value = "/queue", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> queue(@RequestBody QueueRequest queueRequest) throws Exception {
		this.playerRegistrar.queuePlayer(queueRequest.playerId());
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/lobbies")
	public List<Lobby> lobbies() {
		return this.lobbies.getLobbies();
	}

	record QueueRequest(String playerId) {
	}

}
