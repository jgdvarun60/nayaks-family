package com.nayakfamily.familytree.web.rest;

import static com.nayakfamily.familytree.domain.FamilyAsserts.*;
import static com.nayakfamily.familytree.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nayakfamily.familytree.IntegrationTest;
import com.nayakfamily.familytree.domain.Family;
import com.nayakfamily.familytree.repository.FamilyRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FamilyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FamilyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MOTHER_MAIDEN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MOTHER_MAIDEN_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_MARRIAGE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MARRIAGE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_FAMILY_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FAMILY_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FAMILY_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FAMILY_PHOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/families";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FamilyRepository familyRepository;

    @Mock
    private FamilyRepository familyRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFamilyMockMvc;

    private Family family;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Family createEntity(EntityManager em) {
        Family family = new Family()
            .name(DEFAULT_NAME)
            .motherMaidenName(DEFAULT_MOTHER_MAIDEN_NAME)
            .marriageDate(DEFAULT_MARRIAGE_DATE)
            .familyPhoto(DEFAULT_FAMILY_PHOTO)
            .familyPhotoContentType(DEFAULT_FAMILY_PHOTO_CONTENT_TYPE);
        return family;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Family createUpdatedEntity(EntityManager em) {
        Family family = new Family()
            .name(UPDATED_NAME)
            .motherMaidenName(UPDATED_MOTHER_MAIDEN_NAME)
            .marriageDate(UPDATED_MARRIAGE_DATE)
            .familyPhoto(UPDATED_FAMILY_PHOTO)
            .familyPhotoContentType(UPDATED_FAMILY_PHOTO_CONTENT_TYPE);
        return family;
    }

    @BeforeEach
    public void initTest() {
        family = createEntity(em);
    }

    @Test
    @Transactional
    void createFamily() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Family
        var returnedFamily = om.readValue(
            restFamilyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(family)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Family.class
        );

        // Validate the Family in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFamilyUpdatableFieldsEquals(returnedFamily, getPersistedFamily(returnedFamily));
    }

    @Test
    @Transactional
    void createFamilyWithExistingId() throws Exception {
        // Create the Family with an existing ID
        family.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(family)))
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFamilies() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        // Get all the familyList
        restFamilyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(family.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].motherMaidenName").value(hasItem(DEFAULT_MOTHER_MAIDEN_NAME)))
            .andExpect(jsonPath("$.[*].marriageDate").value(hasItem(DEFAULT_MARRIAGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].familyPhotoContentType").value(hasItem(DEFAULT_FAMILY_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].familyPhoto").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FAMILY_PHOTO))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFamiliesWithEagerRelationshipsIsEnabled() throws Exception {
        when(familyRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFamilyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(familyRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFamiliesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(familyRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFamilyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(familyRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        // Get the family
        restFamilyMockMvc
            .perform(get(ENTITY_API_URL_ID, family.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(family.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.motherMaidenName").value(DEFAULT_MOTHER_MAIDEN_NAME))
            .andExpect(jsonPath("$.marriageDate").value(DEFAULT_MARRIAGE_DATE.toString()))
            .andExpect(jsonPath("$.familyPhotoContentType").value(DEFAULT_FAMILY_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.familyPhoto").value(Base64.getEncoder().encodeToString(DEFAULT_FAMILY_PHOTO)));
    }

    @Test
    @Transactional
    void getNonExistingFamily() throws Exception {
        // Get the family
        restFamilyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the family
        Family updatedFamily = familyRepository.findById(family.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFamily are not directly saved in db
        em.detach(updatedFamily);
        updatedFamily
            .name(UPDATED_NAME)
            .motherMaidenName(UPDATED_MOTHER_MAIDEN_NAME)
            .marriageDate(UPDATED_MARRIAGE_DATE)
            .familyPhoto(UPDATED_FAMILY_PHOTO)
            .familyPhotoContentType(UPDATED_FAMILY_PHOTO_CONTENT_TYPE);

        restFamilyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFamily.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFamily))
            )
            .andExpect(status().isOk());

        // Validate the Family in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFamilyToMatchAllProperties(updatedFamily);
    }

    @Test
    @Transactional
    void putNonExistingFamily() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        family.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(put(ENTITY_API_URL_ID, family.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(family)))
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFamily() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        family.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(family))
            )
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFamily() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        family.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(family)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Family in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFamilyWithPatch() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the family using partial update
        Family partialUpdatedFamily = new Family();
        partialUpdatedFamily.setId(family.getId());

        partialUpdatedFamily
            .name(UPDATED_NAME)
            .motherMaidenName(UPDATED_MOTHER_MAIDEN_NAME)
            .marriageDate(UPDATED_MARRIAGE_DATE)
            .familyPhoto(UPDATED_FAMILY_PHOTO)
            .familyPhotoContentType(UPDATED_FAMILY_PHOTO_CONTENT_TYPE);

        restFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamily.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFamily))
            )
            .andExpect(status().isOk());

        // Validate the Family in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFamilyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFamily, family), getPersistedFamily(family));
    }

    @Test
    @Transactional
    void fullUpdateFamilyWithPatch() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the family using partial update
        Family partialUpdatedFamily = new Family();
        partialUpdatedFamily.setId(family.getId());

        partialUpdatedFamily
            .name(UPDATED_NAME)
            .motherMaidenName(UPDATED_MOTHER_MAIDEN_NAME)
            .marriageDate(UPDATED_MARRIAGE_DATE)
            .familyPhoto(UPDATED_FAMILY_PHOTO)
            .familyPhotoContentType(UPDATED_FAMILY_PHOTO_CONTENT_TYPE);

        restFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamily.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFamily))
            )
            .andExpect(status().isOk());

        // Validate the Family in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFamilyUpdatableFieldsEquals(partialUpdatedFamily, getPersistedFamily(partialUpdatedFamily));
    }

    @Test
    @Transactional
    void patchNonExistingFamily() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        family.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, family.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(family))
            )
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFamily() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        family.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(family))
            )
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFamily() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        family.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(family)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Family in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the family
        restFamilyMockMvc
            .perform(delete(ENTITY_API_URL_ID, family.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return familyRepository.count();
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

    protected Family getPersistedFamily(Family family) {
        return familyRepository.findById(family.getId()).orElseThrow();
    }

    protected void assertPersistedFamilyToMatchAllProperties(Family expectedFamily) {
        assertFamilyAllPropertiesEquals(expectedFamily, getPersistedFamily(expectedFamily));
    }

    protected void assertPersistedFamilyToMatchUpdatableProperties(Family expectedFamily) {
        assertFamilyAllUpdatablePropertiesEquals(expectedFamily, getPersistedFamily(expectedFamily));
    }
}
