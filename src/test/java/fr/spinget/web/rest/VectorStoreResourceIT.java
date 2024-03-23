package fr.spinget.web.rest;

import static fr.spinget.domain.VectorStoreAsserts.*;
import static fr.spinget.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.spinget.IntegrationTest;
import fr.spinget.domain.VectorStore;
import fr.spinget.repository.VectorStoreRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VectorStoreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VectorStoreResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Map<Object, Object> DEFAULT_METADATA = Map.of("AAAAAAAAAA", "BBBBBBBBBB");
    private static final Map<Object, Object> UPDATED_METADATA = Map.of("CCCCCCCCCC", "DDDDDDDDDD");

    private static final String DEFAULT_COMMUNE = "AAAAAAAAAA";
    private static final String UPDATED_COMMUNE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CODE_POSTAL = 1;
    private static final Integer UPDATED_CODE_POSTAL = 2;

    private static final String DEFAULT_DEPARTEMENT = "AAAAAAAAAA";

    private static final Integer DEFAULT_CODE_DEPARTEMENT = 1;
    private static final Integer UPDATED_CODE_DEPARTEMENT = 2;

    private static final String DEFAULT_TYPE_LOCAL = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_LOCAL = "BBBBBBBBBB";

    private static final float DEFAULT_SUPERFICIE_CARREZ = 1.1f;
    private static final float UPDATED_SUPERFICIE_CARREZ = 2.2f;

    private static final float DEFAULT_SUPERFICIE_TERRAIN = 1.1f;
    private static final float UPDATED_SUPERFICIE_TERRAIN = 2.2f;

    private static final Integer DEFAULT_NB_PIECES = 1;
    private static final Integer UPDATED_NB_PIECES = 2;

    private static final LocalDate DEFAULT_DATE_VENTE = LocalDate.ofEpochDay(0L);

    private static final Float DEFAULT_VALEUR = 1F;
    private static final Float UPDATED_VALEUR = 2F;

    private static final String ENTITY_API_URL = "/api/vector-stores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VectorStoreRepository vectorStoreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVectorStoreMockMvc;

    private VectorStore vectorStore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VectorStore createEntity(EntityManager em) {
        VectorStore vectorStore = new VectorStore()
            .content(DEFAULT_CONTENT)
            .metadata(DEFAULT_METADATA)
            .commune(DEFAULT_COMMUNE)
            .codePostal(DEFAULT_CODE_POSTAL)
            .codeDepartement(DEFAULT_CODE_DEPARTEMENT)
            .typeLocal(DEFAULT_TYPE_LOCAL)
            .superficieCarrez(DEFAULT_SUPERFICIE_CARREZ)
            .superficieTerrain(DEFAULT_SUPERFICIE_TERRAIN)
            .nbPieces(DEFAULT_NB_PIECES)
            .valeur(DEFAULT_VALEUR);
        return vectorStore;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VectorStore createUpdatedEntity(EntityManager em) {
        VectorStore vectorStore = new VectorStore()
            .content(UPDATED_CONTENT)
            .metadata(UPDATED_METADATA)
            .commune(UPDATED_COMMUNE)
            .codePostal(UPDATED_CODE_POSTAL)
            .codeDepartement(UPDATED_CODE_DEPARTEMENT)
            .typeLocal(UPDATED_TYPE_LOCAL)
            .superficieCarrez(UPDATED_SUPERFICIE_CARREZ)
            .superficieTerrain(UPDATED_SUPERFICIE_TERRAIN)
            .nbPieces(UPDATED_NB_PIECES)
            .valeur(UPDATED_VALEUR);
        return vectorStore;
    }

    @BeforeEach
    public void initTest() {
        vectorStore = createEntity(em);
    }

    @Test
    @Transactional
    void createVectorStore() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VectorStore
        var returnedVectorStore = om.readValue(
            restVectorStoreMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vectorStore))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VectorStore.class
        );

        // Validate the VectorStore in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVectorStoreUpdatableFieldsEquals(returnedVectorStore, getPersistedVectorStore(returnedVectorStore));
    }

    @Test
    @Transactional
    void createVectorStoreWithExistingId() throws Exception {
        // Create the VectorStore with an existing ID
        vectorStoreRepository.saveAndFlush(vectorStore);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVectorStoreMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vectorStore)))
            .andExpect(status().isBadRequest());

        // Validate the VectorStore in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVectorStores() throws Exception {
        // Initialize the database
        vectorStoreRepository.saveAndFlush(vectorStore);

        // Get all the vectorStoreList
        restVectorStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vectorStore.getId().toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA.toString())))
            .andExpect(jsonPath("$.[*].commune").value(hasItem(DEFAULT_COMMUNE)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].departement").value(hasItem(DEFAULT_DEPARTEMENT)))
            .andExpect(jsonPath("$.[*].codeDepartement").value(hasItem(DEFAULT_CODE_DEPARTEMENT)))
            .andExpect(jsonPath("$.[*].typeLocal").value(hasItem(DEFAULT_TYPE_LOCAL)))
            .andExpect(jsonPath("$.[*].superficieCarrez").value(hasItem(DEFAULT_SUPERFICIE_CARREZ)))
            .andExpect(jsonPath("$.[*].superficieTerrain").value(hasItem(DEFAULT_SUPERFICIE_TERRAIN)))
            .andExpect(jsonPath("$.[*].nbPieces").value(hasItem(DEFAULT_NB_PIECES)))
            .andExpect(jsonPath("$.[*].dateVente").value(hasItem(DEFAULT_DATE_VENTE.toString())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR.doubleValue())));
    }

    @Test
    @Transactional
    void getVectorStore() throws Exception {
        // Initialize the database
        vectorStoreRepository.saveAndFlush(vectorStore);

        // Get the vectorStore
        restVectorStoreMockMvc
            .perform(get(ENTITY_API_URL_ID, vectorStore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vectorStore.getId().toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA.toString()))
            .andExpect(jsonPath("$.commune").value(DEFAULT_COMMUNE))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL))
            .andExpect(jsonPath("$.departement").value(DEFAULT_DEPARTEMENT))
            .andExpect(jsonPath("$.codeDepartement").value(DEFAULT_CODE_DEPARTEMENT))
            .andExpect(jsonPath("$.typeLocal").value(DEFAULT_TYPE_LOCAL))
            .andExpect(jsonPath("$.superficieCarrez").value(DEFAULT_SUPERFICIE_CARREZ))
            .andExpect(jsonPath("$.superficieTerrain").value(DEFAULT_SUPERFICIE_TERRAIN))
            .andExpect(jsonPath("$.nbPieces").value(DEFAULT_NB_PIECES))
            .andExpect(jsonPath("$.dateVente").value(DEFAULT_DATE_VENTE.toString()))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingVectorStore() throws Exception {
        // Get the vectorStore
        restVectorStoreMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVectorStore() throws Exception {
        // Initialize the database
        vectorStoreRepository.saveAndFlush(vectorStore);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vectorStore
        VectorStore updatedVectorStore = vectorStoreRepository.findById(vectorStore.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVectorStore are not directly saved in db
        em.detach(updatedVectorStore);
        updatedVectorStore
            .content(UPDATED_CONTENT)
            .metadata(UPDATED_METADATA)
            .commune(UPDATED_COMMUNE)
            .codePostal(UPDATED_CODE_POSTAL)
            .codeDepartement(UPDATED_CODE_DEPARTEMENT)
            .typeLocal(UPDATED_TYPE_LOCAL)
            .superficieCarrez(UPDATED_SUPERFICIE_CARREZ)
            .superficieTerrain(UPDATED_SUPERFICIE_TERRAIN)
            .nbPieces(UPDATED_NB_PIECES)
            .valeur(UPDATED_VALEUR);

        restVectorStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVectorStore.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVectorStore))
            )
            .andExpect(status().isOk());

        // Validate the VectorStore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVectorStoreToMatchAllProperties(updatedVectorStore);
    }

    @Test
    @Transactional
    void putNonExistingVectorStore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vectorStore.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVectorStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vectorStore.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vectorStore))
            )
            .andExpect(status().isBadRequest());

        // Validate the VectorStore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVectorStore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vectorStore.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVectorStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vectorStore))
            )
            .andExpect(status().isBadRequest());

        // Validate the VectorStore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVectorStore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vectorStore.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVectorStoreMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vectorStore)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VectorStore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVectorStoreWithPatch() throws Exception {
        // Initialize the database
        vectorStoreRepository.saveAndFlush(vectorStore);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vectorStore using partial update
        VectorStore partialUpdatedVectorStore = new VectorStore();
        partialUpdatedVectorStore.setId(vectorStore.getId());

        partialUpdatedVectorStore
            .metadata(UPDATED_METADATA)
            .codePostal(UPDATED_CODE_POSTAL)
            .codeDepartement(UPDATED_CODE_DEPARTEMENT)
            .typeLocal(UPDATED_TYPE_LOCAL)
            .superficieTerrain(UPDATED_SUPERFICIE_TERRAIN);

        restVectorStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVectorStore.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVectorStore))
            )
            .andExpect(status().isOk());

        // Validate the VectorStore in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVectorStoreUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVectorStore, vectorStore),
            getPersistedVectorStore(vectorStore)
        );
    }

    @Test
    @Transactional
    void fullUpdateVectorStoreWithPatch() throws Exception {
        // Initialize the database
        vectorStoreRepository.saveAndFlush(vectorStore);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vectorStore using partial update
        VectorStore partialUpdatedVectorStore = new VectorStore();
        partialUpdatedVectorStore.setId(vectorStore.getId());

        partialUpdatedVectorStore
            .content(UPDATED_CONTENT)
            .metadata(UPDATED_METADATA)
            .commune(UPDATED_COMMUNE)
            .codePostal(UPDATED_CODE_POSTAL)
            .codeDepartement(UPDATED_CODE_DEPARTEMENT)
            .typeLocal(UPDATED_TYPE_LOCAL)
            .superficieCarrez(UPDATED_SUPERFICIE_CARREZ)
            .superficieTerrain(UPDATED_SUPERFICIE_TERRAIN)
            .nbPieces(UPDATED_NB_PIECES)
            .valeur(UPDATED_VALEUR);

        restVectorStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVectorStore.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVectorStore))
            )
            .andExpect(status().isOk());

        // Validate the VectorStore in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVectorStoreUpdatableFieldsEquals(partialUpdatedVectorStore, getPersistedVectorStore(partialUpdatedVectorStore));
    }

    @Test
    @Transactional
    void patchNonExistingVectorStore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vectorStore.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVectorStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vectorStore.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vectorStore))
            )
            .andExpect(status().isBadRequest());

        // Validate the VectorStore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVectorStore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vectorStore.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVectorStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vectorStore))
            )
            .andExpect(status().isBadRequest());

        // Validate the VectorStore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVectorStore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vectorStore.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVectorStoreMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vectorStore))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VectorStore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVectorStore() throws Exception {
        // Initialize the database
        vectorStoreRepository.saveAndFlush(vectorStore);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vectorStore
        restVectorStoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, vectorStore.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vectorStoreRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected VectorStore getPersistedVectorStore(VectorStore vectorStore) {
        return vectorStoreRepository.findById(vectorStore.getId()).orElseThrow();
    }

    protected void assertPersistedVectorStoreToMatchAllProperties(VectorStore expectedVectorStore) {
        assertVectorStoreAllPropertiesEquals(expectedVectorStore, getPersistedVectorStore(expectedVectorStore));
    }

    protected void assertPersistedVectorStoreToMatchUpdatableProperties(VectorStore expectedVectorStore) {
        assertVectorStoreAllUpdatablePropertiesEquals(expectedVectorStore, getPersistedVectorStore(expectedVectorStore));
    }
}
