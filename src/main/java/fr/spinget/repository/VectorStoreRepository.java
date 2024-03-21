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
public interface VectorStoreRepository extends JpaRepository<VectorStore, UUID> {}
