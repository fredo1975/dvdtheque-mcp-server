package fr.bluechipit.dvdtheque_mcp_server.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Long id;
    private Integer annee;
    private String titre;
    private String titreO;
    private Date dateSortie;
    private Date dateInsertion;
    private Date dateSortieDvd;
    private boolean vu;
    private String posterPath;
    private LocalDate dateVue;
    private Integer runtime;
    private String overview;
    private FilmOrigine origine;
    private PersonnesFilm personnesFilm;
}
