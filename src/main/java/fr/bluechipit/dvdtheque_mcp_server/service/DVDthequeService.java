package fr.bluechipit.dvdtheque_mcp_server.service;

import fr.bluechipit.dvdtheque_mcp_server.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
//@RequiredArgsConstructor
public class DVDthequeService {

    @Value("${dvdtheque.api.base-url}")
    private String baseUrl;

    private WebClient webClient;
    public DVDthequeService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Tool(description = "récupération des détails d'un film par titre de la dvdtheque")
    public Film getFilmByTitre(@ToolParam(description = "titre du film")String titre) {
        log.info("Récupération du FILM avec le titre: {}", titre);
        try {
            var film = webClient
                    .get()
                    .uri(baseUrl+"/film/{titre}", titre)
                    .retrieve()
                    .bodyToMono(Film.class)
                    .block();
            log.info("FILM récupéré: {}", film);
            return film;
        } catch (WebClientResponseException e) {
            log.error("Erreur lors de la récupération du FILM {}: {}", titre, e.getMessage());
            return null;
        }
    }
}
