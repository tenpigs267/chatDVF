package fr.spinget.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A VectorStore.
 */
@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "embedding")
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 1536) // dimensions
    private float[] embedding;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata")
    private Map<Object, Object> metadata;

    @Column(name = "commune")
    private String commune;

    @Column(name = "code_postal")
    private Integer codePostal;

    @Column(name = "code_departement")
    private Integer codeDepartement;

    @Column(name = "type_local")
    private String typeLocal;

    @Column(name = "superficie_carrez")
    private Float superficieCarrez;

    @Column(name = "superficie_terrain")
    private Float superficieTerrain;

    @Column(name = "nb_pieces")
    private Integer nbPieces;

    @Column(name = "valeur")
    private Float valeur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public VectorStore id(UUID id) {
        this.setId(id);
        return this;
    }

    public VectorStore content(String content) {
        this.setContent(content);
        return this;
    }

    public VectorStore embedding(float[] embedding) {
        this.setEmbedding(embedding);
        return this;
    }

    public VectorStore metadata(Map<Object, Object> metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public VectorStore commune(String commune) {
        this.setCommune(commune);
        return this;
    }

    public VectorStore codePostal(Integer codePostal) {
        this.setCodePostal(codePostal);
        return this;
    }

    public VectorStore codeDepartement(Integer codeDepartement) {
        this.setCodeDepartement(codeDepartement);
        return this;
    }

    public VectorStore typeLocal(String typeLocal) {
        this.setTypeLocal(typeLocal);
        return this;
    }

    public VectorStore superficieCarrez(Float superficieCarrez) {
        this.setSuperficieCarrez(superficieCarrez);
        return this;
    }

    public VectorStore superficieTerrain(Float superficieTerrain) {
        this.setSuperficieTerrain(superficieTerrain);
        return this;
    }

    public VectorStore nbPieces(Integer nbPieces) {
        this.setNbPieces(nbPieces);
        return this;
    }

    public VectorStore valeur(Float valeur) {
        this.setValeur(valeur);
        return this;
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
            ", codeDepartement=" + getCodeDepartement() +
            ", typeLocal='" + getTypeLocal() + "'" +
            ", superficieCarrez=" + getSuperficieCarrez() +
            ", superficieTerrain=" + getSuperficieTerrain() +
            ", nbPieces=" + getNbPieces() +
            ", valeur=" + getValeur() +
            "}";
    }
}
