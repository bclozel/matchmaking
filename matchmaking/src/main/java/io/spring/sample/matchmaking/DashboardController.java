package io.spring.sample.matchmaking;

import io.spring.sample.matchmaking.lobby.Lobbies;
import io.spring.sample.matchmaking.lobby.PlayerRegistrar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class DashboardController {

	private final PlayerRegistrar playerRegistrar;

	private final Lobbies lobbies;

	DashboardController(PlayerRegistrar playerRegistrar, Lobbies lobbies) {
		this.playerRegistrar = playerRegistrar;
		this.lobbies = lobbies;
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("queueSize", this.playerRegistrar.getQueueSize());
		model.addAttribute("lobbies", this.lobbies.getLobbies());
		return "index";
	}

}
