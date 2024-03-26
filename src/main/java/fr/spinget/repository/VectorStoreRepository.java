package fr.spinget.repository;

import fr.spinget.domain.VectorStore;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VectorStore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VectorStoreRepository extends JpaRepository<VectorStore, UUID> {
    Long countByCodePostal(Integer codePostal);

    @Query("SELECT AVG(e.valeur/e.superficieCarrez) FROM VectorStore e WHERE e.commune = ?1")
    Float prixMoyenAuM2PourCommune(String commune);

    @Query("SELECT AVG(e.valeur/e.superficieCarrez) FROM VectorStore e WHERE e.codePostal = ?1")
    Float prixMoyenAuM2PourCodePostal(Integer codePostal);

    @Query("SELECT AVG(e.valeur/e.superficieCarrez) FROM VectorStore e WHERE e.codeDepartement = ?1")
    Float prixMoyenAuM2PourDepartement(Integer departement);

    @Query("SELECT AVG(e.valeur/e.superficieCarrez) FROM VectorStore e GROUP BY e.codeDepartement")
    Float prixMoyenAuM2ParDepartement();
}
