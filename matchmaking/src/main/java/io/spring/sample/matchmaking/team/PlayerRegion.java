package io.spring.sample.matchmaking.team;

public enum PlayerRegion {

	EU("Europe"), AMER("America"), ME("Middle East"), ASIA("Asia"), OCE("Oceania");

	private final String label;

	PlayerRegion(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

}
