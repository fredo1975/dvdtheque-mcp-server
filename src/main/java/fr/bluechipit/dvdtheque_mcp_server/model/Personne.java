package fr.bluechipit.dvdtheque_mcp_server.model;

import lombok.Data;

import java.util.Date;

@Data
public class Personne {
    private Long id;
    private String nom;
    private String prenom;
    private Date dateN;
    private Pays pays;
}
