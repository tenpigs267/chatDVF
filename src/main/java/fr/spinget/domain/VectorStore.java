package fr.spinget.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * A VectorStore.
 */
@Entity
@Table(name = "vector_store")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VectorStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "content")
    private String content;

    @Lob
    @Column(name = "metadata")
    private String metadata;

    @Column(name = "commune")
    private String commune;

    @Column(name = "code_postal")
    private Integer codePostal;

    @Column(name = "departement")
    private String departement;

    @Column(name = "code_departement")
    private Integer codeDepartement;

    @Column(name = "type_local")
    private String typeLocal;

    @Column(name = "superficie_carrez")
    private Integer superficieCarrez;

    @Column(name = "superficie_terrain")
    private Integer superficieTerrain;

    @Column(name = "nb_pieces")
    private Integer nbPieces;

    @Column(name = "date_vente")
    private LocalDate dateVente;

    @Column(name = "valeur")
    private Float valeur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public VectorStore id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public VectorStore content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public VectorStore metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getCommune() {
        return this.commune;
    }

    public VectorStore commune(String commune) {
        this.setCommune(commune);
        return this;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public Integer getCodePostal() {
        return this.codePostal;
    }

    public VectorStore codePostal(Integer codePostal) {
        this.setCodePostal(codePostal);
        return this;
    }

    public void setCodePostal(Integer codePostal) {
        this.codePostal = codePostal;
    }

    public String getDepartement() {
        return this.departement;
    }

    public VectorStore departement(String departement) {
        this.setDepartement(departement);
        return this;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public Integer getCodeDepartement() {
        return this.codeDepartement;
    }

    public VectorStore codeDepartement(Integer codeDepartement) {
        this.setCodeDepartement(codeDepartement);
        return this;
    }

    public void setCodeDepartement(Integer codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public String getTypeLocal() {
        return this.typeLocal;
    }

    public VectorStore typeLocal(String typeLocal) {
        this.setTypeLocal(typeLocal);
        return this;
    }

    public void setTypeLocal(String typeLocal) {
        this.typeLocal = typeLocal;
    }

    public Integer getSuperficieCarrez() {
        return this.superficieCarrez;
    }

    public VectorStore superficieCarrez(Integer superficieCarrez) {
        this.setSuperficieCarrez(superficieCarrez);
        return this;
    }

    public void setSuperficieCarrez(Integer superficieCarrez) {
        this.superficieCarrez = superficieCarrez;
    }

    public Integer getSuperficieTerrain() {
        return this.superficieTerrain;
    }

    public VectorStore superficieTerrain(Integer superficieTerrain) {
        this.setSuperficieTerrain(superficieTerrain);
        return this;
    }

    public void setSuperficieTerrain(Integer superficieTerrain) {
        this.superficieTerrain = superficieTerrain;
    }

    public Integer getNbPieces() {
        return this.nbPieces;
    }

    public VectorStore nbPieces(Integer nbPieces) {
        this.setNbPieces(nbPieces);
        return this;
    }

    public void setNbPieces(Integer nbPieces) {
        this.nbPieces = nbPieces;
    }

    public LocalDate getDateVente() {
        return this.dateVente;
    }

    public VectorStore dateVente(LocalDate dateVente) {
        this.setDateVente(dateVente);
        return this;
    }

    public void setDateVente(LocalDate dateVente) {
        this.dateVente = dateVente;
    }

    public Float getValeur() {
        return this.valeur;
    }

    public VectorStore valeur(Float valeur) {
        this.setValeur(valeur);
        return this;
    }

    public void setValeur(Float valeur) {
        this.valeur = valeur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VectorStore)) {
            return false;
        }
        return getId() != null && getId().equals(((VectorStore) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VectorStore{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", commune='" + getCommune() + "'" +
            ", codePostal=" + getCodePostal() +
            ", departement='" + getDepartement() + "'" +
            ", codeDepartement=" + getCodeDepartement() +
            ", typeLocal='" + getTypeLocal() + "'" +
            ", superficieCarrez=" + getSuperficieCarrez() +
            ", superficieTerrain=" + getSuperficieTerrain() +
            ", nbPieces=" + getNbPieces() +
            ", dateVente='" + getDateVente() + "'" +
            ", valeur=" + getValeur() +
            "}";
    }
}
