package io.spring.sample.matchmaking.lobby;

import io.spring.sample.matchmaking.team.PlayerRegion;

record PlayerProfile(String playerId, String gamerTag, String platform, PlayerRegion region, long ping) {

}
