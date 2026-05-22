package com.musicdiary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class LastFmConfig {

    @Bean
    public WebClient lastFmWebClient() {
        return WebClient.builder()
                .baseUrl("http://ws.audioscrobbler.com/2.0")
                .build();
    }

}
