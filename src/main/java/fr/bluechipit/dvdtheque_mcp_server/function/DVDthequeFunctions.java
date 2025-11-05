package fr.bluechipit.dvdtheque_mcp_server.function;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.bluechipit.dvdtheque_mcp_server.model.Film;
import fr.bluechipit.dvdtheque_mcp_server.service.DVDthequeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class DVDthequeFunctions {

    private final DVDthequeService dvdthequeService;


    @Bean
    @Description("Récupère un FILM spécifique par son titre. Utilise cette fonction quand l'utilisateur demande des détails sur un film précis.")
    public Function<GetFilmByTitreRequest, String> getDVDById() {
        return request -> {
            Film film = dvdthequeService.getFilmByTitre(request.titre());
            if (film == null) {
                return "FILM non trouvé avec le titre: " + request.titre();
            }
            return String.format("Détails du FILM:\nTitre: %s\nTitre Original: %s\nAnnée: %d\nid: %d",
                    film.getTitre(), film.getTitreO(), film.getAnnee(),
                    film.getId());
        };
    }

    // Records pour les paramètres des fonctions

    public record GetFilmByTitreRequest(@JsonProperty(required = true) String titre) {}
}
