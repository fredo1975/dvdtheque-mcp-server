package fr.bluechipit.dvdtheque_mcp_server.service;

import fr.bluechipit.dvdtheque_mcp_server.model.Film;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DVDthequeService {

    private final WebClient.Builder webClientBuilder;

    @Value("${dvdtheque.api.base-url}")
    private String baseUrl;

    private WebClient getWebClient() {
        return webClientBuilder.baseUrl(baseUrl).build();
    }

    public Film getFilmByTitre(String titre) {
        log.info("Récupération du FILM avec le titre: {}", titre);
        try {
            return getWebClient()
                    .get()
                    .uri("/film/{titre}", titre)
                    .retrieve()
                    .bodyToMono(Film.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Erreur lors de la récupération du FILM {}: {}", titre, e.getMessage());
            return null;
        }
    }
}
