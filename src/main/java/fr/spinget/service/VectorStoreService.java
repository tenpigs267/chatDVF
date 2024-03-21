package fr.spinget.service;

import fr.spinget.domain.VectorStore;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link fr.spinget.domain.VectorStore}.
 */
public interface VectorStoreService {
    /**
     * Save a vectorStore.
     *
     * @param vectorStore the entity to save.
     * @return the persisted entity.
     */
    VectorStore save(VectorStore vectorStore);

    /**
     * Updates a vectorStore.
     *
     * @param vectorStore the entity to update.
     * @return the persisted entity.
     */
    VectorStore update(VectorStore vectorStore);

    /**
     * Partially updates a vectorStore.
     *
     * @param vectorStore the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VectorStore> partialUpdate(VectorStore vectorStore);

    /**
     * Get all the vectorStores.
     *
     * @return the list of entities.
     */
    List<VectorStore> findAll();

    /**
     * Get the "id" vectorStore.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VectorStore> findOne(UUID id);

    /**
     * Delete the "id" vectorStore.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
