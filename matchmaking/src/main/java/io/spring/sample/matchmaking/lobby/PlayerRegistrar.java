package io.spring.sample.matchmaking.lobby;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicReference;

import io.spring.sample.matchmaking.team.PlayerRegion;
import io.spring.sample.matchmaking.team.Team;
import io.spring.sample.matchmaking.team.TeamMember;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.groupingBy;

@Component
public class PlayerRegistrar implements ApplicationEventPublisherAware {

	private final AtomicReference<State> state = new AtomicReference<>(State.QUEUING);

	private final ConcurrentLinkedDeque<QueuingPlayer> matchMakingQueue = new ConcurrentLinkedDeque<>();

	private final PlayerApiClient playerApiClient;

	private final int lobbySize;

	private final int teamSize;

	private ApplicationEventPublisher eventPublisher;

	PlayerRegistrar(PlayerApiClient playerApiClient, LobbyProperties properties) {
		this.playerApiClient = playerApiClient;
		this.lobbySize = properties.getTeamsPerLobby();
		this.teamSize = properties.getPlayersPerTeam();
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}

	public void queuePlayer(String playerId) {
		PlayerProfile profile = this.playerApiClient.getPlayerProfile(playerId);
		PlayerStats stats = this.playerApiClient.fetchPlayerStats(playerId);
		this.matchMakingQueue.addLast(new QueuingPlayer(profile, stats, Instant.now()));
		attemptLobbyCreation();
	}

	public long getQueueSize() {
		return this.matchMakingQueue.size();
	}

	public Iterator<QueuingPlayer> getQueuingPlayers() {
		return this.matchMakingQueue.iterator();
	}

	private void attemptLobbyCreation() {
		if (queueHasEnoughPlayers() && this.state.compareAndSet(State.QUEUING, State.MATCHING)) {
			Map<PlayerRegion, List<QueuingPlayer>> byRegion = this.matchMakingQueue.stream()
				.collect(groupingBy(player -> player.playerProfile().region()));
			byRegion.forEach(this::createLobbies);
			this.state.set(State.QUEUING);
		}
	}

	private boolean queueHasEnoughPlayers() {
		return this.matchMakingQueue.size() > (this.lobbySize * this.teamSize * 2);
	}

	private void createLobbies(PlayerRegion region, List<QueuingPlayer> players) {
		if (players.size() > this.teamSize * this.lobbySize) {
			int teamCount = Math.floorDiv(players.size(), this.teamSize);
			List<Team> teams = new ArrayList<>();
			for (int i = 0; i < teamCount; i++) {
				List<QueuingPlayer> queuingPlayers = players.subList(i * this.teamSize, (i + 1) * this.teamSize);
				List<TeamMember> teamMembers = new ArrayList<>(queuingPlayers.size());
				queuingPlayers.forEach(player -> {
					PlayerProfile profile = player.playerProfile();
					teamMembers.add(new TeamMember(profile.playerId(), profile.gamerTag(), profile.region(),
							profile.ping(), profile.platform(), player.playerStats().rank()));
					this.matchMakingQueue.remove(player);
				});
				teams.add(new Team(teamMembers));
			}
			Lobby lobby = new Lobby(UUID.randomUUID(), region, teams);
			this.eventPublisher.publishEvent(new LobbyCreatedEvent(this, lobby));
		}
	}

	enum State {

		QUEUING, MATCHING

	}

}
