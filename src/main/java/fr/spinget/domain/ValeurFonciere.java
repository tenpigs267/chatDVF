package fr.spinget.domain;

import lombok.Data;

@Data
public class ValeurFonciere {

    private Float valeurFonciere;
    private Integer noVoie;
    private String typeDeVoie;
    private String codeVoie;
    private String voie;
    private Integer codePostal;
    private String commune;
    private Integer codeDepartement;
    private String codeCommune;
    private String section;
    private Float surfaceCarrezDuPremierLot;
    private String deuxiemeLot;
    private Float surfaceCarrezDuDeuxiemeLot;
    private String troisiemeLot;
    private Float surfaceCarrezDuTroisiemeLot;
    private String quatriemeLot;
    private Float surfaceCarrezDuQuatriemeLot;
    private String cinquiemeLot;
    private Float surfaceCarrezDuCinquiemeLot;
    private String typeLocal;
    private Float surfaceReelleBati;
    private Integer nombrePiecesPrincipales; // Exemple de champ non-String
    private Float surfaceTerrain;
}
