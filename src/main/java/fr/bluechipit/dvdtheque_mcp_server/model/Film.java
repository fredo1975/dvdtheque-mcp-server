package fr.bluechipit.dvdtheque_mcp_server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Long id;
    private Integer annee;
    private String titre;
    private String titreO;

    @Override
    public String toString() {
        return String.format("Film[id=%d, titre='%s', titreO='%s', annee=%d]",
                id, titre, titreO,annee);
    }
}
