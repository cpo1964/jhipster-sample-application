package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.TextblockAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Textblock;
import com.mycompany.myapp.repository.TextblockRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TextblockResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TextblockResourceIT {

    private static final String DEFAULT_DESCRIBER = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIBER = "BBBBBBBBBB";

    private static final String DEFAULT_NR = "AAAAAAAAAA";
    private static final String UPDATED_NR = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/textblocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TextblockRepository textblockRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTextblockMockMvc;

    private Textblock textblock;

    private Textblock insertedTextblock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Textblock createEntity() {
        return new Textblock().describer(DEFAULT_DESCRIBER).nr(DEFAULT_NR).text(DEFAULT_TEXT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Textblock createUpdatedEntity() {
        return new Textblock().describer(UPDATED_DESCRIBER).nr(UPDATED_NR).text(UPDATED_TEXT);
    }

    @BeforeEach
    void initTest() {
        textblock = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTextblock != null) {
            textblockRepository.delete(insertedTextblock);
            insertedTextblock = null;
        }
    }

    @Test
    @Transactional
    void createTextblock() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Textblock
        var returnedTextblock = om.readValue(
            restTextblockMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(textblock)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Textblock.class
        );

        // Validate the Textblock in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTextblockUpdatableFieldsEquals(returnedTextblock, getPersistedTextblock(returnedTextblock));

        insertedTextblock = returnedTextblock;
    }

    @Test
    @Transactional
    void createTextblockWithExistingId() throws Exception {
        // Create the Textblock with an existing ID
        textblock.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTextblockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(textblock)))
            .andExpect(status().isBadRequest());

        // Validate the Textblock in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTextblocks() throws Exception {
        // Initialize the database
        insertedTextblock = textblockRepository.saveAndFlush(textblock);

        // Get all the textblockList
        restTextblockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(textblock.getId().intValue())))
            .andExpect(jsonPath("$.[*].describer").value(hasItem(DEFAULT_DESCRIBER)))
            .andExpect(jsonPath("$.[*].nr").value(hasItem(DEFAULT_NR)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)));
    }

    @Test
    @Transactional
    void getTextblock() throws Exception {
        // Initialize the database
        insertedTextblock = textblockRepository.saveAndFlush(textblock);

        // Get the textblock
        restTextblockMockMvc
            .perform(get(ENTITY_API_URL_ID, textblock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(textblock.getId().intValue()))
            .andExpect(jsonPath("$.describer").value(DEFAULT_DESCRIBER))
            .andExpect(jsonPath("$.nr").value(DEFAULT_NR))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT));
    }

    @Test
    @Transactional
    void getNonExistingTextblock() throws Exception {
        // Get the textblock
        restTextblockMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTextblock() throws Exception {
        // Initialize the database
        insertedTextblock = textblockRepository.saveAndFlush(textblock);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the textblock
        Textblock updatedTextblock = textblockRepository.findById(textblock.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTextblock are not directly saved in db
        em.detach(updatedTextblock);
        updatedTextblock.describer(UPDATED_DESCRIBER).nr(UPDATED_NR).text(UPDATED_TEXT);

        restTextblockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTextblock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTextblock))
            )
            .andExpect(status().isOk());

        // Validate the Textblock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTextblockToMatchAllProperties(updatedTextblock);
    }

    @Test
    @Transactional
    void putNonExistingTextblock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        textblock.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTextblockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, textblock.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(textblock))
            )
            .andExpect(status().isBadRequest());

        // Validate the Textblock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTextblock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        textblock.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextblockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(textblock))
            )
            .andExpect(status().isBadRequest());

        // Validate the Textblock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTextblock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        textblock.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextblockMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(textblock)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Textblock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTextblockWithPatch() throws Exception {
        // Initialize the database
        insertedTextblock = textblockRepository.saveAndFlush(textblock);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the textblock using partial update
        Textblock partialUpdatedTextblock = new Textblock();
        partialUpdatedTextblock.setId(textblock.getId());

        restTextblockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTextblock.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTextblock))
            )
            .andExpect(status().isOk());

        // Validate the Textblock in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTextblockUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTextblock, textblock),
            getPersistedTextblock(textblock)
        );
    }

    @Test
    @Transactional
    void fullUpdateTextblockWithPatch() throws Exception {
        // Initialize the database
        insertedTextblock = textblockRepository.saveAndFlush(textblock);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the textblock using partial update
        Textblock partialUpdatedTextblock = new Textblock();
        partialUpdatedTextblock.setId(textblock.getId());

        partialUpdatedTextblock.describer(UPDATED_DESCRIBER).nr(UPDATED_NR).text(UPDATED_TEXT);

        restTextblockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTextblock.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTextblock))
            )
            .andExpect(status().isOk());

        // Validate the Textblock in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTextblockUpdatableFieldsEquals(partialUpdatedTextblock, getPersistedTextblock(partialUpdatedTextblock));
    }

    @Test
    @Transactional
    void patchNonExistingTextblock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        textblock.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTextblockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, textblock.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(textblock))
            )
            .andExpect(status().isBadRequest());

        // Validate the Textblock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTextblock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        textblock.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextblockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(textblock))
            )
            .andExpect(status().isBadRequest());

        // Validate the Textblock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTextblock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        textblock.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextblockMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(textblock)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Textblock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTextblock() throws Exception {
        // Initialize the database
        insertedTextblock = textblockRepository.saveAndFlush(textblock);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the textblock
        restTextblockMockMvc
            .perform(delete(ENTITY_API_URL_ID, textblock.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return textblockRepository.count();
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

    protected Textblock getPersistedTextblock(Textblock textblock) {
        return textblockRepository.findById(textblock.getId()).orElseThrow();
    }

    protected void assertPersistedTextblockToMatchAllProperties(Textblock expectedTextblock) {
        assertTextblockAllPropertiesEquals(expectedTextblock, getPersistedTextblock(expectedTextblock));
    }

    protected void assertPersistedTextblockToMatchUpdatableProperties(Textblock expectedTextblock) {
        assertTextblockAllUpdatablePropertiesEquals(expectedTextblock, getPersistedTextblock(expectedTextblock));
    }
}
