package com.ryabov.currency.bot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@org.springframework.context.annotation.Configuration
@EnableConfigurationProperties(Configuration.TelegramConfiguration.class)
@EnableScheduling
public class Configuration {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @ConfigurationProperties(prefix = "telegram")
    public static class TelegramConfiguration {
        private String token;
        private String name;
        private Long userLimit;
        private Long delay;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getUserLimit() {
            return userLimit;
        }

        public void setUserLimit(Long userLimit) {
            this.userLimit = userLimit;
        }

        public Long getDelay() {
            return delay;
        }

        public void setDelay(Long delay) {
            this.delay = delay;
        }
    }
}
