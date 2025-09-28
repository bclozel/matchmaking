package io.spring.sample.matchmaking.lobby;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Lobbies {

	public static final Logger logger = LoggerFactory.getLogger(Lobbies.class);

	private final Deque<Lobby> lobbies = new ConcurrentLinkedDeque<>();

	@EventListener(LobbyCreatedEvent.class)
	void lobbyCreated(LobbyCreatedEvent event) {
		logger.info("New Lobby created {}", event.getLobby());
		this.lobbies.addLast(event.getLobby());
	}

	public List<Lobby> getLobbies() {
		return this.lobbies.stream().toList();
	}

}
