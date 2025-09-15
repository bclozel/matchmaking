package io.spring.sample.playerprofile.faker;

import net.datafaker.Faker;

class DataFaker extends Faker {

	public OnlineGaming onlineGaming() {
		return this.getProvider(OnlineGaming.class, OnlineGaming::new);
	}

}
