package io.spring.sample.matchmaking.team;

public record TeamMember(String playerId, String gamerTag, PlayerRegion region, long ping, String platform,
		PlayerRank rank) {
}
