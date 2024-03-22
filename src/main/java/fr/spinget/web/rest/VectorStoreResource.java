package fr.spinget.web.rest;

import fr.spinget.domain.VectorStore;
import fr.spinget.repository.VectorStoreRepository;
import fr.spinget.service.VectorStoreService;
import fr.spinget.service.dto.DeepChatRequestBodyDTO;
import fr.spinget.service.dto.DeepChatTextResponseDTO;
import fr.spinget.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.spinget.domain.VectorStore}.
 */
@RestController
@RequestMapping("/api/vector-stores")
public class VectorStoreResource {

    private final Logger log = LoggerFactory.getLogger(VectorStoreResource.class);

    private static final String ENTITY_NAME = "vectorStore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VectorStoreService vectorStoreService;

    private final VectorStoreRepository vectorStoreRepository;

    public VectorStoreResource(VectorStoreService vectorStoreService, VectorStoreRepository vectorStoreRepository) {
        this.vectorStoreService = vectorStoreService;
        this.vectorStoreRepository = vectorStoreRepository;
    }

    /**
     * {@code POST  /vector-stores} : Create a new vectorStore.
     *
     * @param vectorStore the vectorStore to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vectorStore, or with status {@code 400 (Bad Request)} if the vectorStore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VectorStore> createVectorStore(@RequestBody VectorStore vectorStore) throws URISyntaxException {
        log.debug("REST request to save VectorStore : {}", vectorStore);
        if (vectorStore.getId() != null) {
            throw new BadRequestAlertException("A new vectorStore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vectorStore = vectorStoreService.save(vectorStore);
        return ResponseEntity.created(new URI("/api/vector-stores/" + vectorStore.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, vectorStore.getId().toString()))
            .body(vectorStore);
    }

    /**
     * {@code PUT  /vector-stores/:id} : Updates an existing vectorStore.
     *
     * @param id          the id of the vectorStore to save.
     * @param vectorStore the vectorStore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vectorStore,
     * or with status {@code 400 (Bad Request)} if the vectorStore is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vectorStore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VectorStore> updateVectorStore(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody VectorStore vectorStore
    ) throws URISyntaxException {
        log.debug("REST request to update VectorStore : {}, {}", id, vectorStore);
        if (vectorStore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vectorStore.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vectorStoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vectorStore = vectorStoreService.update(vectorStore);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vectorStore.getId().toString()))
            .body(vectorStore);
    }

    /**
     * {@code PATCH  /vector-stores/:id} : Partial updates given fields of an existing vectorStore, field will ignore if it is null
     *
     * @param id          the id of the vectorStore to save.
     * @param vectorStore the vectorStore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vectorStore,
     * or with status {@code 400 (Bad Request)} if the vectorStore is not valid,
     * or with status {@code 404 (Not Found)} if the vectorStore is not found,
     * or with status {@code 500 (Internal Server Error)} if the vectorStore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VectorStore> partialUpdateVectorStore(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody VectorStore vectorStore
    ) throws URISyntaxException {
        log.debug("REST request to partial update VectorStore partially : {}, {}", id, vectorStore);
        if (vectorStore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vectorStore.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vectorStoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VectorStore> result = vectorStoreService.partialUpdate(vectorStore);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vectorStore.getId().toString())
        );
    }

    /**
     * {@code GET  /vector-stores} : get all the vectorStores.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vectorStores in body.
     */
    @GetMapping("")
    public List<VectorStore> getAllVectorStores() {
        log.debug("REST request to get all VectorStores");
        return vectorStoreService.findAll();
    }

    /**
     * {@code GET  /vector-stores/:id} : get the "id" vectorStore.
     *
     * @param id the id of the vectorStore to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vectorStore, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VectorStore> getVectorStore(@PathVariable("id") UUID id) {
        log.debug("REST request to get VectorStore : {}", id);
        Optional<VectorStore> vectorStore = vectorStoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vectorStore);
    }

    /**
     * {@code DELETE  /vector-stores/:id} : delete the "id" vectorStore.
     *
     * @param id the id of the vectorStore to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVectorStore(@PathVariable("id") UUID id) {
        log.debug("REST request to delete VectorStore : {}", id);
        vectorStoreService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/chatdvf")
    public DeepChatTextResponseDTO chatDVF(@RequestBody DeepChatRequestBodyDTO requestBody) {
        return this.vectorStoreService.chat(requestBody);
    }
}
