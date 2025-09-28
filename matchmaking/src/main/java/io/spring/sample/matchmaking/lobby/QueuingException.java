package io.spring.sample.matchmaking.lobby;

public class QueuingException extends RuntimeException {

	private final String playerId;

	public QueuingException(String playerId, Throwable cause) {
		super("Failed to queue player [%s]".formatted(playerId), cause);
		this.playerId = playerId;
	}

	public String getPlayerId() {
		return this.playerId;
	}

}
