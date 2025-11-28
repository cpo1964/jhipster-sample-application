package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Textblock;
import com.mycompany.myapp.repository.TextblockRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Textblock}.
 */
@RestController
@RequestMapping("/api/textblocks")
@Transactional
public class TextblockResource {

    private static final Logger LOG = LoggerFactory.getLogger(TextblockResource.class);

    private static final String ENTITY_NAME = "textblock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TextblockRepository textblockRepository;

    public TextblockResource(TextblockRepository textblockRepository) {
        this.textblockRepository = textblockRepository;
    }

    /**
     * {@code POST  /textblocks} : Create a new textblock.
     *
     * @param textblock the textblock to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new textblock, or with status {@code 400 (Bad Request)} if the textblock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Textblock> createTextblock(@Valid @RequestBody Textblock textblock) throws URISyntaxException {
        LOG.debug("REST request to save Textblock : {}", textblock);
        if (textblock.getId() != null) {
            throw new BadRequestAlertException("A new textblock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        textblock = textblockRepository.save(textblock);
        return ResponseEntity.created(new URI("/api/textblocks/" + textblock.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, textblock.getId().toString()))
            .body(textblock);
    }

    /**
     * {@code PUT  /textblocks/:id} : Updates an existing textblock.
     *
     * @param id the id of the textblock to save.
     * @param textblock the textblock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated textblock,
     * or with status {@code 400 (Bad Request)} if the textblock is not valid,
     * or with status {@code 500 (Internal Server Error)} if the textblock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Textblock> updateTextblock(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Textblock textblock
    ) throws URISyntaxException {
        LOG.debug("REST request to update Textblock : {}, {}", id, textblock);
        if (textblock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, textblock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!textblockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        textblock = textblockRepository.save(textblock);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, textblock.getId().toString()))
            .body(textblock);
    }

    /**
     * {@code PATCH  /textblocks/:id} : Partial updates given fields of an existing textblock, field will ignore if it is null
     *
     * @param id the id of the textblock to save.
     * @param textblock the textblock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated textblock,
     * or with status {@code 400 (Bad Request)} if the textblock is not valid,
     * or with status {@code 404 (Not Found)} if the textblock is not found,
     * or with status {@code 500 (Internal Server Error)} if the textblock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Textblock> partialUpdateTextblock(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Textblock textblock
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Textblock partially : {}, {}", id, textblock);
        if (textblock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, textblock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!textblockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Textblock> result = textblockRepository
            .findById(textblock.getId())
            .map(existingTextblock -> {
                if (textblock.getDescriber() != null) {
                    existingTextblock.setDescriber(textblock.getDescriber());
                }
                if (textblock.getNr() != null) {
                    existingTextblock.setNr(textblock.getNr());
                }
                if (textblock.getText() != null) {
                    existingTextblock.setText(textblock.getText());
                }

                return existingTextblock;
            })
            .map(textblockRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, textblock.getId().toString())
        );
    }

    /**
     * {@code GET  /textblocks} : get all the textblocks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of textblocks in body.
     */
    @GetMapping("")
    public List<Textblock> getAllTextblocks() {
        LOG.debug("REST request to get all Textblocks");
        return textblockRepository.findAll();
    }

    /**
     * {@code GET  /textblocks/:id} : get the "id" textblock.
     *
     * @param id the id of the textblock to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the textblock, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Textblock> getTextblock(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Textblock : {}", id);
        Optional<Textblock> textblock = textblockRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(textblock);
    }

    /**
     * {@code DELETE  /textblocks/:id} : delete the "id" textblock.
     *
     * @param id the id of the textblock to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTextblock(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Textblock : {}", id);
        textblockRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
