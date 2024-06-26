package com.nayakfamily.familytree.web.rest;

import com.nayakfamily.familytree.domain.Person;
import com.nayakfamily.familytree.repository.PersonRepository;
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
 * REST controller for managing {@link com.nayakfamily.familytree.domain.Person}.
 */
@RestController
@RequestMapping("/api/people")
@Transactional
public class PersonResource {

    private final Logger log = LoggerFactory.getLogger(PersonResource.class);

    private static final String ENTITY_NAME = "person";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonRepository personRepository;

    public PersonResource(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * {@code POST  /people} : Create a new person.
     *
     * @param person the person to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new person, or with status {@code 400 (Bad Request)} if the person has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) throws URISyntaxException {
        log.debug("REST request to save Person : {}", person);
        if (person.getId() != null) {
            throw new BadRequestAlertException("A new person cannot already have an ID", ENTITY_NAME, "idexists");
        }
        person = personRepository.save(person);
        return ResponseEntity.created(new URI("/api/people/" + person.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, person.getId().toString()))
            .body(person);
    }

    /**
     * {@code PUT  /people/:id} : Updates an existing person.
     *
     * @param id the id of the person to save.
     * @param person the person to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated person,
     * or with status {@code 400 (Bad Request)} if the person is not valid,
     * or with status {@code 500 (Internal Server Error)} if the person couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable(value = "id", required = false) final Long id, @RequestBody Person person)
        throws URISyntaxException {
        log.debug("REST request to update Person : {}, {}", id, person);
        if (person.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, person.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        person = personRepository.save(person);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, person.getId().toString()))
            .body(person);
    }

    /**
     * {@code PATCH  /people/:id} : Partial updates given fields of an existing person, field will ignore if it is null
     *
     * @param id the id of the person to save.
     * @param person the person to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated person,
     * or with status {@code 400 (Bad Request)} if the person is not valid,
     * or with status {@code 404 (Not Found)} if the person is not found,
     * or with status {@code 500 (Internal Server Error)} if the person couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Person> partialUpdatePerson(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Person person
    ) throws URISyntaxException {
        log.debug("REST request to partial update Person partially : {}, {}", id, person);
        if (person.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, person.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Person> result = personRepository
            .findById(person.getId())
            .map(existingPerson -> {
                if (person.getName() != null) {
                    existingPerson.setName(person.getName());
                }
                if (person.getGender() != null) {
                    existingPerson.setGender(person.getGender());
                }
                if (person.getMarried() != null) {
                    existingPerson.setMarried(person.getMarried());
                }
                if (person.getAbout() != null) {
                    existingPerson.setAbout(person.getAbout());
                }
                if (person.getFathersName() != null) {
                    existingPerson.setFathersName(person.getFathersName());
                }
                if (person.getDateOfBirth() != null) {
                    existingPerson.setDateOfBirth(person.getDateOfBirth());
                }
                if (person.getPhoneNumber1() != null) {
                    existingPerson.setPhoneNumber1(person.getPhoneNumber1());
                }
                if (person.getPhoneNumber2() != null) {
                    existingPerson.setPhoneNumber2(person.getPhoneNumber2());
                }
                if (person.getWhatsAppNo() != null) {
                    existingPerson.setWhatsAppNo(person.getWhatsAppNo());
                }
                if (person.getEmail() != null) {
                    existingPerson.setEmail(person.getEmail());
                }
                if (person.getCurrentLocation() != null) {
                    existingPerson.setCurrentLocation(person.getCurrentLocation());
                }
                if (person.getPhoto() != null) {
                    existingPerson.setPhoto(person.getPhoto());
                }
                if (person.getPhotoContentType() != null) {
                    existingPerson.setPhotoContentType(person.getPhotoContentType());
                }

                return existingPerson;
            })
            .map(personRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, person.getId().toString())
        );
    }

    /**
     * {@code GET  /people} : get all the people.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of people in body.
     */
    @GetMapping("")
    public List<Person> getAllPeople(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all People");
        if (eagerload) {
            return personRepository.findAllWithEagerRelationships();
        } else {
            return personRepository.findAll();
        }
    }

    /**
     * {@code GET  /people/:id} : get the "id" person.
     *
     * @param id the id of the person to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the person, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable("id") Long id) {
        log.debug("REST request to get Person : {}", id);
        Optional<Person> person = personRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(person);
    }

    /**
     * {@code DELETE  /people/:id} : delete the "id" person.
     *
     * @param id the id of the person to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable("id") Long id) {
        log.debug("REST request to delete Person : {}", id);
        personRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
