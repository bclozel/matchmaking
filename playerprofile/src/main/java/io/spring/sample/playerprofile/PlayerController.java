package io.spring.sample.playerprofile;

import java.util.Random;

import io.spring.sample.playerprofile.faker.GamerData;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

	private final FailureSimulator failureSimulator;

	private final GamerData gamerData;

	public PlayerController(GamerData gamerData) {
		this.gamerData = gamerData;
		this.failureSimulator = new FailureSimulator();
	}

	@GetMapping(path = "/players/profile/{playerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public PlayerProfile fetchPlayerInfo(@PathVariable String playerId,
			@RequestParam(defaultValue = "200") String delay) throws InterruptedException {
		Thread.sleep(Long.parseLong(delay));
		return gamerData.getPlayerProfile(playerId);
	}

	@GetMapping(path = "/players/stats/{playerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public PlayerStats fetchPlayerStats(@PathVariable String playerId, @RequestParam(defaultValue = "200") String delay)
			throws InterruptedException {
		Thread.sleep(Long.parseLong(delay));
		if (this.failureSimulator.isFailure()) {
			throw new HttpMessageNotWritableException("cannot write player stats data");
		}
		return gamerData.getPlayerStats(playerId);
	}

	@PostMapping(path = "/admin/failureRate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> setFailureRate(@RequestBody FailureUpdate update) {
		this.failureSimulator.updateFailureRate(update.rate);
		return ResponseEntity.noContent().build();
	}

	public record FailureUpdate(float rate) {

	}

	static class FailureSimulator {

		private final Random random = new Random();

		private float failureRate = 0;

		boolean isFailure() {
			return random.nextFloat(0, 1) < this.failureRate;
		}

		void updateFailureRate(float failureRate) {
			this.failureRate = failureRate;
		}

	}

}
