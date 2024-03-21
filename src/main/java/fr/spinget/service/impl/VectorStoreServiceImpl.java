package fr.spinget.service.impl;

import fr.spinget.domain.VectorStore;
import fr.spinget.repository.VectorStoreRepository;
import fr.spinget.service.VectorStoreService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.spinget.domain.VectorStore}.
 */
@Service
@Transactional
public class VectorStoreServiceImpl implements VectorStoreService {

    private final Logger log = LoggerFactory.getLogger(VectorStoreServiceImpl.class);

    private final VectorStoreRepository vectorStoreRepository;

    public VectorStoreServiceImpl(VectorStoreRepository vectorStoreRepository) {
        this.vectorStoreRepository = vectorStoreRepository;
    }

    @Override
    public VectorStore save(VectorStore vectorStore) {
        log.debug("Request to save VectorStore : {}", vectorStore);
        return vectorStoreRepository.save(vectorStore);
    }

    @Override
    public VectorStore update(VectorStore vectorStore) {
        log.debug("Request to update VectorStore : {}", vectorStore);
        return vectorStoreRepository.save(vectorStore);
    }

    @Override
    public Optional<VectorStore> partialUpdate(VectorStore vectorStore) {
        log.debug("Request to partially update VectorStore : {}", vectorStore);

        return vectorStoreRepository
            .findById(vectorStore.getId())
            .map(existingVectorStore -> {
                if (vectorStore.getContent() != null) {
                    existingVectorStore.setContent(vectorStore.getContent());
                }
                if (vectorStore.getMetadata() != null) {
                    existingVectorStore.setMetadata(vectorStore.getMetadata());
                }
                if (vectorStore.getCommune() != null) {
                    existingVectorStore.setCommune(vectorStore.getCommune());
                }
                if (vectorStore.getCodePostal() != null) {
                    existingVectorStore.setCodePostal(vectorStore.getCodePostal());
                }
                if (vectorStore.getDepartement() != null) {
                    existingVectorStore.setDepartement(vectorStore.getDepartement());
                }
                if (vectorStore.getCodeDepartement() != null) {
                    existingVectorStore.setCodeDepartement(vectorStore.getCodeDepartement());
                }
                if (vectorStore.getTypeLocal() != null) {
                    existingVectorStore.setTypeLocal(vectorStore.getTypeLocal());
                }
                if (vectorStore.getSuperficieCarrez() != null) {
                    existingVectorStore.setSuperficieCarrez(vectorStore.getSuperficieCarrez());
                }
                if (vectorStore.getSuperficieTerrain() != null) {
                    existingVectorStore.setSuperficieTerrain(vectorStore.getSuperficieTerrain());
                }
                if (vectorStore.getNbPieces() != null) {
                    existingVectorStore.setNbPieces(vectorStore.getNbPieces());
                }
                if (vectorStore.getDateVente() != null) {
                    existingVectorStore.setDateVente(vectorStore.getDateVente());
                }
                if (vectorStore.getValeur() != null) {
                    existingVectorStore.setValeur(vectorStore.getValeur());
                }

                return existingVectorStore;
            })
            .map(vectorStoreRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VectorStore> findAll() {
        log.debug("Request to get all VectorStores");
        return vectorStoreRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VectorStore> findOne(UUID id) {
        log.debug("Request to get VectorStore : {}", id);
        return vectorStoreRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete VectorStore : {}", id);
        vectorStoreRepository.deleteById(id);
    }
}
