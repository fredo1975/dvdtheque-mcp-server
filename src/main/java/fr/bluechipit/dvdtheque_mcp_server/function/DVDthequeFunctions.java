package fr.bluechipit.dvdtheque_mcp_server.function;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.bluechipit.dvdtheque_mcp_server.model.Film;
import fr.bluechipit.dvdtheque_mcp_server.service.DVDthequeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DVDthequeFunctions {

    private final DVDthequeService dvdthequeService;

    @Bean
    @Description("récupère les détails d'un FILM spécifique par son titre. Utilise cette fonction quand l'utilisateur demande des détails sur un film précis.")
    public Function<GetFilmByTitreRequest, Film> getFilmByTitre() {
        return request -> {
            log.info("request: {}", request.toString());
            Film film = dvdthequeService.getFilmByTitre(request.titre());
            if (film == null) {
                return null;
            }
            log.info("Film récupéré: {}", film.toString());
            return film;
        };
    }

    // Records pour les paramètres des fonctions

    public record GetFilmByTitreRequest(@JsonProperty(required = true) String titre) {}
}
