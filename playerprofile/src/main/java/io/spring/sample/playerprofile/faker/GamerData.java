package io.spring.sample.playerprofile.faker;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import io.spring.sample.playerprofile.PlayerProfile;
import io.spring.sample.playerprofile.PlayerRank;
import io.spring.sample.playerprofile.PlayerStats;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class GamerData {

	private final DataFaker dataFaker = new DataFaker();

	public PlayerProfile getPlayerProfile(String playerId) {
		String gamerTag = StringUtils.capitalize(dataFaker.word().adjective())
				+ StringUtils.capitalize(dataFaker.word().noun()) + dataFaker.number().digits(2);
		long ping = dataFaker.number().numberBetween(30L, 200L);
		return new PlayerProfile(playerId, gamerTag, dataFaker.videoGame().platform(),
				dataFaker.onlineGaming().region(), ping);
	}

	public PlayerStats getPlayerStats(String playerId) {
		int score = this.dataFaker.number().numberBetween(0, 1000);
		String level = resolveLevel(score);
		Instant lastSeen = this.dataFaker.timeAndDate().past(30, TimeUnit.DAYS);
		return new PlayerStats(playerId, lastSeen, new PlayerRank(score, level));
	}

	private String resolveLevel(int score) {
		if (score > 200 && score < 400) {
			return "Silver";
		}
		else if (score > 400 && score < 500) {
			return "Gold";
		}
		else if (score > 500 && score < 600) {
			return "Platinum";
		}
		else if (score > 600 && score < 700) {
			return "Diamond";
		}
		else if (score > 700 && score < 800) {
			return "Elite";
		}
		else if (score > 800 && score < 900) {
			return "Champion";
		}
		else if (score > 900) {
			return "Unreal";
		}
		return "Bronze";
	}

}
