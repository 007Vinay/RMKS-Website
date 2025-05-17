package com.rmks.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import java.util.Locale;

@SpringBootApplication
@EnableConfigurationProperties
public class RMKSWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(RMKSWebsiteApplication.class, args);
	}

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver resolver = new CookieLocaleResolver();
		resolver.setDefaultLocale(new Locale("en"));
		resolver.setCookieName("preferredLanguage");
		resolver.setCookieMaxAge(60 * 60 * 24 * 365); // 1 year
		return resolver;
	}
}