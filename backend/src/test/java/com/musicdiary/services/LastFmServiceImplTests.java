package com.musicdiary.services;

import com.musicdiary.dtos.ArtistDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LastFmServiceImplTests {
    private String apiKey;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private LastFmServiceImpl lastFmService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(lastFmService, "apiKey", "test-api-key");
    }

    @Test
    void searchArtistWithValidData_shouldReturnArtistsList() {

        String fakeJson = "{\"results\":{\"artistmatches\":{\"artist\":[{\"name\":\"Radiohead\",\"listeners\":\"1234\",\"url\":\"http://test.com\",\"image\":[{},{},{},{\"#text\":\"http://img.com\"}]}]}}}";

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(fakeJson));

        List<ArtistDTO> artistDTOList = lastFmService.searchArtists("Radiohead");

        assertNotNull(artistDTOList);
        assertEquals("Radiohead", artistDTOList.get(0).getName());
    }

    @Test
    void searchArtistWithInvalidData_shouldReturnEmptyList() {

        String fakeJson = "{}";

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(fakeJson));

        List<ArtistDTO> artistDTOList = lastFmService.searchArtists("test");

        assertNotNull(artistDTOList);
        assertEquals(0, artistDTOList.size());
    }
}
