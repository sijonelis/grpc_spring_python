package com.vilkazz.grpc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vilkazz.grpc.service.PythonResultService;
import com.vilkazz.grpc.web.rest.errors.BadRequestAlertException;
import com.vilkazz.grpc.web.rest.util.HeaderUtil;
import com.vilkazz.grpc.service.dto.PythonResultDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PythonResult.
 */
@RestController
@RequestMapping("/api")
public class PythonResultResource {

    private final Logger log = LoggerFactory.getLogger(PythonResultResource.class);

    private static final String ENTITY_NAME = "pythonResult";

    private PythonResultService pythonResultService;

    public PythonResultResource(PythonResultService pythonResultService) {
        this.pythonResultService = pythonResultService;
    }

    /**
     * POST  /python-results : Create a new pythonResult.
     *
     * @param pythonResultDTO the pythonResultDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pythonResultDTO, or with status 400 (Bad Request) if the pythonResult has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/python-results")
    @Timed
    public ResponseEntity<PythonResultDTO> createPythonResult(@Valid @RequestBody PythonResultDTO pythonResultDTO) throws URISyntaxException {
        log.debug("REST request to save PythonResult : {}", pythonResultDTO);
        if (pythonResultDTO.getId() != null) {
            throw new BadRequestAlertException("A new pythonResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PythonResultDTO result = pythonResultService.save(pythonResultDTO);
        return ResponseEntity.created(new URI("/api/python-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /python-results : Updates an existing pythonResult.
     *
     * @param pythonResultDTO the pythonResultDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pythonResultDTO,
     * or with status 400 (Bad Request) if the pythonResultDTO is not valid,
     * or with status 500 (Internal Server Error) if the pythonResultDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/python-results")
    @Timed
    public ResponseEntity<PythonResultDTO> updatePythonResult(@Valid @RequestBody PythonResultDTO pythonResultDTO) throws URISyntaxException {
        log.debug("REST request to update PythonResult : {}", pythonResultDTO);
        if (pythonResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PythonResultDTO result = pythonResultService.save(pythonResultDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pythonResultDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /python-results : get all the pythonResults.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pythonResults in body
     */
    @GetMapping("/python-results")
    @Timed
    public List<PythonResultDTO> getAllPythonResults() {
        log.debug("REST request to get all PythonResults");
        return pythonResultService.findAll();
    }

    /**
     * GET  /python-results/:id : get the "id" pythonResult.
     *
     * @param id the id of the pythonResultDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pythonResultDTO, or with status 404 (Not Found)
     */
    @GetMapping("/python-results/{id}")
    @Timed
    public ResponseEntity<PythonResultDTO> getPythonResult(@PathVariable Long id) {
        log.debug("REST request to get PythonResult : {}", id);
        Optional<PythonResultDTO> pythonResultDTO = pythonResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pythonResultDTO);
    }

    /**
     * DELETE  /python-results/:id : delete the "id" pythonResult.
     *
     * @param id the id of the pythonResultDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/python-results/{id}")
    @Timed
    public ResponseEntity<Void> deletePythonResult(@PathVariable Long id) {
        log.debug("REST request to delete PythonResult : {}", id);
        pythonResultService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
