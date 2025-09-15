package io.spring.sample.playerprofile.faker;

import java.io.IOException;
import java.util.Locale;

import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.providers.base.BaseProviders;

import org.springframework.core.io.ClassPathResource;

class OnlineGaming extends AbstractProvider<BaseProviders> {

	public OnlineGaming(BaseProviders faker) {
		super(faker);
		ClassPathResource resource = new ClassPathResource("faker/online-gaming.yml");
		try {
			faker.addUrl(Locale.ENGLISH, resource.getURL());
		}
		catch (IOException ex) {
			throw new IllegalStateException("Could not load custom fake data file", ex);
		}
	}

	public String region() {
		return resolve("online_gaming.region");
	}

}
