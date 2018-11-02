package com.vilkazz.grpc.web.rest;

import com.vilkazz.grpc.GrpcSpringApp;

import com.vilkazz.grpc.domain.PythonResult;
import com.vilkazz.grpc.repository.PythonResultRepository;
import com.vilkazz.grpc.service.PythonResultService;
import com.vilkazz.grpc.service.dto.PythonResultDTO;
import com.vilkazz.grpc.service.mapper.PythonResultMapper;
import com.vilkazz.grpc.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.vilkazz.grpc.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PythonResultResource REST controller.
 *
 * @see PythonResultResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrpcSpringApp.class)
public class PythonResultResourceIntTest {

    private static final String DEFAULT_REQUEST_HASH = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_INPUT = "AAAAAAAAAA";
    private static final String UPDATED_INPUT = "BBBBBBBBBB";

    private static final Integer DEFAULT_OUTPUT = 1;
    private static final Integer UPDATED_OUTPUT = 2;

    @Autowired
    private PythonResultRepository pythonResultRepository;

    @Autowired
    private PythonResultMapper pythonResultMapper;
    
    @Autowired
    private PythonResultService pythonResultService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPythonResultMockMvc;

    private PythonResult pythonResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PythonResultResource pythonResultResource = new PythonResultResource(pythonResultService);
        this.restPythonResultMockMvc = MockMvcBuilders.standaloneSetup(pythonResultResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PythonResult createEntity(EntityManager em) {
        PythonResult pythonResult = new PythonResult()
            .requestHash(DEFAULT_REQUEST_HASH)
            .input(DEFAULT_INPUT)
            .output(DEFAULT_OUTPUT);
        return pythonResult;
    }

    @Before
    public void initTest() {
        pythonResult = createEntity(em);
    }

    @Test
    @Transactional
    public void createPythonResult() throws Exception {
        int databaseSizeBeforeCreate = pythonResultRepository.findAll().size();

        // Create the PythonResult
        PythonResultDTO pythonResultDTO = pythonResultMapper.toDto(pythonResult);
        restPythonResultMockMvc.perform(post("/api/python-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pythonResultDTO)))
            .andExpect(status().isCreated());

        // Validate the PythonResult in the database
        List<PythonResult> pythonResultList = pythonResultRepository.findAll();
        assertThat(pythonResultList).hasSize(databaseSizeBeforeCreate + 1);
        PythonResult testPythonResult = pythonResultList.get(pythonResultList.size() - 1);
        assertThat(testPythonResult.getRequestHash()).isEqualTo(DEFAULT_REQUEST_HASH);
        assertThat(testPythonResult.getInput()).isEqualTo(DEFAULT_INPUT);
        assertThat(testPythonResult.getOutput()).isEqualTo(DEFAULT_OUTPUT);
    }

    @Test
    @Transactional
    public void createPythonResultWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pythonResultRepository.findAll().size();

        // Create the PythonResult with an existing ID
        pythonResult.setId(1L);
        PythonResultDTO pythonResultDTO = pythonResultMapper.toDto(pythonResult);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPythonResultMockMvc.perform(post("/api/python-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pythonResultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PythonResult in the database
        List<PythonResult> pythonResultList = pythonResultRepository.findAll();
        assertThat(pythonResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRequestHashIsRequired() throws Exception {
        int databaseSizeBeforeTest = pythonResultRepository.findAll().size();
        // set the field null
        pythonResult.setRequestHash(null);

        // Create the PythonResult, which fails.
        PythonResultDTO pythonResultDTO = pythonResultMapper.toDto(pythonResult);

        restPythonResultMockMvc.perform(post("/api/python-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pythonResultDTO)))
            .andExpect(status().isBadRequest());

        List<PythonResult> pythonResultList = pythonResultRepository.findAll();
        assertThat(pythonResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPythonResults() throws Exception {
        // Initialize the database
        pythonResultRepository.saveAndFlush(pythonResult);

        // Get all the pythonResultList
        restPythonResultMockMvc.perform(get("/api/python-results?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pythonResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestHash").value(hasItem(DEFAULT_REQUEST_HASH.toString())))
            .andExpect(jsonPath("$.[*].input").value(hasItem(DEFAULT_INPUT.toString())))
            .andExpect(jsonPath("$.[*].output").value(hasItem(DEFAULT_OUTPUT)));
    }
    
    @Test
    @Transactional
    public void getPythonResult() throws Exception {
        // Initialize the database
        pythonResultRepository.saveAndFlush(pythonResult);

        // Get the pythonResult
        restPythonResultMockMvc.perform(get("/api/python-results/{id}", pythonResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pythonResult.getId().intValue()))
            .andExpect(jsonPath("$.requestHash").value(DEFAULT_REQUEST_HASH.toString()))
            .andExpect(jsonPath("$.input").value(DEFAULT_INPUT.toString()))
            .andExpect(jsonPath("$.output").value(DEFAULT_OUTPUT));
    }

    @Test
    @Transactional
    public void getNonExistingPythonResult() throws Exception {
        // Get the pythonResult
        restPythonResultMockMvc.perform(get("/api/python-results/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePythonResult() throws Exception {
        // Initialize the database
        pythonResultRepository.saveAndFlush(pythonResult);

        int databaseSizeBeforeUpdate = pythonResultRepository.findAll().size();

        // Update the pythonResult
        PythonResult updatedPythonResult = pythonResultRepository.findById(pythonResult.getId()).get();
        // Disconnect from session so that the updates on updatedPythonResult are not directly saved in db
        em.detach(updatedPythonResult);
        updatedPythonResult
            .requestHash(UPDATED_REQUEST_HASH)
            .input(UPDATED_INPUT)
            .output(UPDATED_OUTPUT);
        PythonResultDTO pythonResultDTO = pythonResultMapper.toDto(updatedPythonResult);

        restPythonResultMockMvc.perform(put("/api/python-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pythonResultDTO)))
            .andExpect(status().isOk());

        // Validate the PythonResult in the database
        List<PythonResult> pythonResultList = pythonResultRepository.findAll();
        assertThat(pythonResultList).hasSize(databaseSizeBeforeUpdate);
        PythonResult testPythonResult = pythonResultList.get(pythonResultList.size() - 1);
        assertThat(testPythonResult.getRequestHash()).isEqualTo(UPDATED_REQUEST_HASH);
        assertThat(testPythonResult.getInput()).isEqualTo(UPDATED_INPUT);
        assertThat(testPythonResult.getOutput()).isEqualTo(UPDATED_OUTPUT);
    }

    @Test
    @Transactional
    public void updateNonExistingPythonResult() throws Exception {
        int databaseSizeBeforeUpdate = pythonResultRepository.findAll().size();

        // Create the PythonResult
        PythonResultDTO pythonResultDTO = pythonResultMapper.toDto(pythonResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPythonResultMockMvc.perform(put("/api/python-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pythonResultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PythonResult in the database
        List<PythonResult> pythonResultList = pythonResultRepository.findAll();
        assertThat(pythonResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePythonResult() throws Exception {
        // Initialize the database
        pythonResultRepository.saveAndFlush(pythonResult);

        int databaseSizeBeforeDelete = pythonResultRepository.findAll().size();

        // Get the pythonResult
        restPythonResultMockMvc.perform(delete("/api/python-results/{id}", pythonResult.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PythonResult> pythonResultList = pythonResultRepository.findAll();
        assertThat(pythonResultList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PythonResult.class);
        PythonResult pythonResult1 = new PythonResult();
        pythonResult1.setId(1L);
        PythonResult pythonResult2 = new PythonResult();
        pythonResult2.setId(pythonResult1.getId());
        assertThat(pythonResult1).isEqualTo(pythonResult2);
        pythonResult2.setId(2L);
        assertThat(pythonResult1).isNotEqualTo(pythonResult2);
        pythonResult1.setId(null);
        assertThat(pythonResult1).isNotEqualTo(pythonResult2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PythonResultDTO.class);
        PythonResultDTO pythonResultDTO1 = new PythonResultDTO();
        pythonResultDTO1.setId(1L);
        PythonResultDTO pythonResultDTO2 = new PythonResultDTO();
        assertThat(pythonResultDTO1).isNotEqualTo(pythonResultDTO2);
        pythonResultDTO2.setId(pythonResultDTO1.getId());
        assertThat(pythonResultDTO1).isEqualTo(pythonResultDTO2);
        pythonResultDTO2.setId(2L);
        assertThat(pythonResultDTO1).isNotEqualTo(pythonResultDTO2);
        pythonResultDTO1.setId(null);
        assertThat(pythonResultDTO1).isNotEqualTo(pythonResultDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pythonResultMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pythonResultMapper.fromId(null)).isNull();
    }
}
