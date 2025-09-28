package io.spring.sample.matchmaking.lobby;

import org.springframework.context.ApplicationEvent;

class LobbyCreatedEvent extends ApplicationEvent {

	private final Lobby lobby;

	LobbyCreatedEvent(Object source, Lobby lobby) {
		super(source);
		this.lobby = lobby;
	}

	public Lobby getLobby() {
		return this.lobby;
	}

}
