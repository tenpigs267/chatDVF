package fr.spinget.repository;

import fr.spinget.domain.VectorStore;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VectorStore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VectorStoreRepository extends JpaRepository<VectorStore, UUID> {
    @Query("SELECT AVG(e.valeur/e.superficieCarrez) FROM VectorStore e WHERE e.commune = ?1")
    public Float prixMoyenAuM2PourCommune(String commune);

    @Query("SELECT AVG(e.valeur/e.superficieCarrez) FROM VectorStore e WHERE e.codePostal = ?1")
    public Float prixMoyenAuM2PourCodePostal(Integer codePostal);

    @Query("SELECT AVG(e.valeur/e.superficieCarrez) FROM VectorStore e WHERE e.departement = ?1")
    public Float prixMoyenAuM2PourDepartement(String departement);

    @Query("SELECT AVG(e.valeur/e.superficieCarrez) FROM VectorStore e GROUP BY e.departement")
    public Float prixMoyenAuM2ParDepartement();
}
