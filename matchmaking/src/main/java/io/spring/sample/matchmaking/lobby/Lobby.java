package io.spring.sample.matchmaking.lobby;

import java.util.List;
import java.util.UUID;

import io.spring.sample.matchmaking.team.PlayerRegion;
import io.spring.sample.matchmaking.team.Team;
import io.spring.sample.matchmaking.team.TeamMember;

public record Lobby(UUID uuid, PlayerRegion region, List<Team> teams) {

	public List<TeamMember> allPlayers() {
		return this.teams.stream().flatMap(team -> team.players().stream()).toList();
	}

}
