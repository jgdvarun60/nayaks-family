package com.nayakfamily.familytree.web.rest;

import com.nayakfamily.familytree.domain.Family;
import com.nayakfamily.familytree.repository.FamilyRepository;
import com.nayakfamily.familytree.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nayakfamily.familytree.domain.Family}.
 */
@RestController
@RequestMapping("/api/families")
@Transactional
public class FamilyResource {

    private final Logger log = LoggerFactory.getLogger(FamilyResource.class);

    private static final String ENTITY_NAME = "family";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FamilyRepository familyRepository;

    public FamilyResource(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    /**
     * {@code POST  /families} : Create a new family.
     *
     * @param family the family to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new family, or with status {@code 400 (Bad Request)} if the family has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Family> createFamily(@RequestBody Family family) throws URISyntaxException {
        log.debug("REST request to save Family : {}", family);
        if (family.getId() != null) {
            throw new BadRequestAlertException("A new family cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (family.getFather() != null) {
            family.setName(family.getFather().getName());
        }
        family = familyRepository.save(family);
        return ResponseEntity.created(new URI("/api/families/" + family.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, family.getId().toString()))
            .body(family);
    }

    /**
     * {@code PUT  /families/:id} : Updates an existing family.
     *
     * @param id the id of the family to save.
     * @param family the family to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated family,
     * or with status {@code 400 (Bad Request)} if the family is not valid,
     * or with status {@code 500 (Internal Server Error)} if the family couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Family> updateFamily(@PathVariable(value = "id", required = false) final Long id, @RequestBody Family family)
        throws URISyntaxException {
        log.debug("REST request to update Family : {}, {}", id, family);
        if (family.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, family.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        if (family.getFather() != null) {
            family.setName(family.getFather().getName());
        }
        family = familyRepository.save(family);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, family.getId().toString()))
            .body(family);
    }

    /**
     * {@code PATCH  /families/:id} : Partial updates given fields of an existing family, field will ignore if it is null
     *
     * @param id the id of the family to save.
     * @param family the family to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated family,
     * or with status {@code 400 (Bad Request)} if the family is not valid,
     * or with status {@code 404 (Not Found)} if the family is not found,
     * or with status {@code 500 (Internal Server Error)} if the family couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Family> partialUpdateFamily(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Family family
    ) throws URISyntaxException {
        log.debug("REST request to partial update Family partially : {}, {}", id, family);
        if (family.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, family.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Family> result = familyRepository
            .findById(family.getId())
            .map(existingFamily -> {
                if (family.getName() != null) {
                    existingFamily.setName(family.getName());
                }
                if (family.getMotherMaidenName() != null) {
                    existingFamily.setMotherMaidenName(family.getMotherMaidenName());
                }
                if (family.getMarriageDate() != null) {
                    existingFamily.setMarriageDate(family.getMarriageDate());
                }
                if (family.getFamilyPhoto() != null) {
                    existingFamily.setFamilyPhoto(family.getFamilyPhoto());
                }
                if (family.getFamilyPhotoContentType() != null) {
                    existingFamily.setFamilyPhotoContentType(family.getFamilyPhotoContentType());
                }

                if (existingFamily.getFather() != null) {
                    existingFamily.setName(existingFamily.getFather().getName());
                }
                return existingFamily;
            })
            .map(familyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, family.getId().toString())
        );
    }

    /**
     * {@code GET  /families} : get all the families.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of families in body.
     */
    @GetMapping("")
    public List<Family> getAllFamilies(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Families");
        if (eagerload) {
            return familyRepository.findAllWithEagerRelationships();
        } else {
            return familyRepository.findAll();
        }
    }

    /**
     * {@code GET  /families/:id} : get the "id" family.
     *
     * @param id the id of the family to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the family, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Family> getFamily(@PathVariable("id") Long id) {
        log.debug("REST request to get Family : {}", id);
        Optional<Family> family = familyRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(family);
    }

    /**
     * {@code DELETE  /families/:id} : delete the "id" family.
     *
     * @param id the id of the family to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamily(@PathVariable("id") Long id) {
        log.debug("REST request to delete Family : {}", id);
        familyRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
