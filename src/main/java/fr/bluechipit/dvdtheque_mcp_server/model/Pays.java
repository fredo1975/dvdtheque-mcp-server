package fr.bluechipit.dvdtheque_mcp_server.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Pays {
    private java.lang.Integer id;
    @NotNull
    private String lib;
    private String i18n;
}
