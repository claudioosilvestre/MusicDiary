package com.musicdiary.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicdiary.dtos.ArtistDTO;
import com.musicdiary.dtos.TrackDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class LastFmServiceImpl implements LastFmService {

    @Value("${lastfm.api.key}")
    private String apiKey;

    private WebClient webClient;

    public LastFmServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<ArtistDTO> searchArtists(String artistName) {

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("method", "artist.search")
                        .queryParam("artist", artistName)
                        .queryParam("api_key", apiKey)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode artists = root.path("results").path("artistmatches").path("artist");

            List<ArtistDTO> result = new ArrayList<>();

            for (JsonNode artistNode : artists) {
                ArtistDTO dto = new ArtistDTO();
                dto.setName(artistNode.path("name").asText());
                dto.setTotalListeners(artistNode.path("listeners").asInt());
                dto.setProfileURL(artistNode.path("url").asText());

                JsonNode images = artistNode.path("image");
                String imageUrl = images.get(3).path("#text").asText();
                dto.setImageURL(imageUrl);

                result.add(dto);
            }
            return result;

        }catch (Exception e) {
            return List.of();
        }

    }

    @Override
    public List<TrackDTO> searchTracks(String trackName) {
        return List.of();
    }
}
