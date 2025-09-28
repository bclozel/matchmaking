package io.spring.sample.matchmaking.lobby;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("matchmaking.lobby")
class LobbyProperties {

	/**
	 * Number of teams per lobby.
	 */
	private int teamsPerLobby = 2;

	/**
	 * Number of players per team.
	 */
	private int playersPerTeam = 4;

	private final Player player = new Player();

	public int getTeamsPerLobby() {
		return this.teamsPerLobby;
	}

	public void setTeamsPerLobby(int teamsPerLobby) {
		this.teamsPerLobby = teamsPerLobby;
	}

	public int getPlayersPerTeam() {
		return this.playersPerTeam;
	}

	public void setPlayersPerTeam(int playersPerTeam) {
		this.playersPerTeam = playersPerTeam;
	}

	public Player getPlayer() {
		return this.player;
	}

	public static class Player {

		/**
		 * URL of the service providing profile information on players.
		 */
		private String profileUrl = "http://localhost:8081/players/";

		/**
		 * URL of the service providing statistics on players.
		 */
		private String statsUrl = "http://localhost:8081/players/";

		public String getProfileUrl() {
			return this.profileUrl;
		}

		public void setProfileUrl(String profileUrl) {
			this.profileUrl = profileUrl;
		}

		public String getStatsUrl() {
			return this.statsUrl;
		}

		public void setStatsUrl(String statsUrl) {
			this.statsUrl = statsUrl;
		}

	}

}
