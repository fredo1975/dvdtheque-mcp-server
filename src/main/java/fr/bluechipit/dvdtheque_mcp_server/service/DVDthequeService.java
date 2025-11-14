package fr.bluechipit.dvdtheque_mcp_server.service;

import fr.bluechipit.dvdtheque_mcp_server.model.Film;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DVDthequeService {

    @Value("${dvdtheque.api.base-url}")
    private String baseUrl;
    @Tool(description = "récupération des détails d'un film par titre de la dvdtheque")
    public Film getFilmByTitre(@ToolParam(description = "titre du film")String titre) {
        log.info("Récupération du FILM avec le titre: {}", titre);
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(baseUrl)
                    .filter((request, next) ->
                            ReactiveSecurityContextHolder.getContext()
                                    .map(securityContext -> securityContext.getAuthentication())
                                    .filter(authentication -> authentication instanceof JwtAuthenticationToken)
                                    .cast(JwtAuthenticationToken.class)
                                    .map(jwtAuth -> jwtAuth.getToken().getTokenValue())
                                    .map(token -> {
                                        return org.springframework.web.reactive.function.client.ClientRequest
                                                .from(request)
                                                .header("Authorization", "Bearer " + token)
                                                .build();
                                    })
                                    .defaultIfEmpty(request)
                                    .flatMap(next::exchange)
                    )
                    .build();
            var film = webClient
                    .get()
                    .uri("/film/{titre}", titre)
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
