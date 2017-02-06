package com.cs499.jhipster.web.rest;

import com.cs499.jhipster.JHipsterApp;

import com.cs499.jhipster.domain.Director;
import com.cs499.jhipster.repository.DirectorRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DirectorResource REST controller.
 *
 * @see DirectorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JHipsterApp.class)
public class DirectorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DATE_OF_BIRTH = "AAAAAAAAAA";
    private static final String UPDATED_DATE_OF_BIRTH = "BBBBBBBBBB";

    @Inject
    private DirectorRepository directorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDirectorMockMvc;

    private Director director;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DirectorResource directorResource = new DirectorResource();
        ReflectionTestUtils.setField(directorResource, "directorRepository", directorRepository);
        this.restDirectorMockMvc = MockMvcBuilders.standaloneSetup(directorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Director createEntity(EntityManager em) {
        Director director = new Director()
                .name(DEFAULT_NAME)
                .dateOfBirth(DEFAULT_DATE_OF_BIRTH);
        return director;
    }

    @Before
    public void initTest() {
        director = createEntity(em);
    }

    @Test
    @Transactional
    public void createDirector() throws Exception {
        int databaseSizeBeforeCreate = directorRepository.findAll().size();

        // Create the Director

        restDirectorMockMvc.perform(post("/api/directors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(director)))
            .andExpect(status().isCreated());

        // Validate the Director in the database
        List<Director> directorList = directorRepository.findAll();
        assertThat(directorList).hasSize(databaseSizeBeforeCreate + 1);
        Director testDirector = directorList.get(directorList.size() - 1);
        assertThat(testDirector.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDirector.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void createDirectorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = directorRepository.findAll().size();

        // Create the Director with an existing ID
        Director existingDirector = new Director();
        existingDirector.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirectorMockMvc.perform(post("/api/directors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingDirector)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Director> directorList = directorRepository.findAll();
        assertThat(directorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = directorRepository.findAll().size();
        // set the field null
        director.setName(null);

        // Create the Director, which fails.

        restDirectorMockMvc.perform(post("/api/directors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(director)))
            .andExpect(status().isBadRequest());

        List<Director> directorList = directorRepository.findAll();
        assertThat(directorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDirectors() throws Exception {
        // Initialize the database
        directorRepository.saveAndFlush(director);

        // Get all the directorList
        restDirectorMockMvc.perform(get("/api/directors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(director.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())));
    }

    @Test
    @Transactional
    public void getDirector() throws Exception {
        // Initialize the database
        directorRepository.saveAndFlush(director);

        // Get the director
        restDirectorMockMvc.perform(get("/api/directors/{id}", director.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(director.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDirector() throws Exception {
        // Get the director
        restDirectorMockMvc.perform(get("/api/directors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDirector() throws Exception {
        // Initialize the database
        directorRepository.saveAndFlush(director);
        int databaseSizeBeforeUpdate = directorRepository.findAll().size();

        // Update the director
        Director updatedDirector = directorRepository.findOne(director.getId());
        updatedDirector
                .name(UPDATED_NAME)
                .dateOfBirth(UPDATED_DATE_OF_BIRTH);

        restDirectorMockMvc.perform(put("/api/directors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDirector)))
            .andExpect(status().isOk());

        // Validate the Director in the database
        List<Director> directorList = directorRepository.findAll();
        assertThat(directorList).hasSize(databaseSizeBeforeUpdate);
        Director testDirector = directorList.get(directorList.size() - 1);
        assertThat(testDirector.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDirector.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void updateNonExistingDirector() throws Exception {
        int databaseSizeBeforeUpdate = directorRepository.findAll().size();

        // Create the Director

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDirectorMockMvc.perform(put("/api/directors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(director)))
            .andExpect(status().isCreated());

        // Validate the Director in the database
        List<Director> directorList = directorRepository.findAll();
        assertThat(directorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDirector() throws Exception {
        // Initialize the database
        directorRepository.saveAndFlush(director);
        int databaseSizeBeforeDelete = directorRepository.findAll().size();

        // Get the director
        restDirectorMockMvc.perform(delete("/api/directors/{id}", director.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Director> directorList = directorRepository.findAll();
        assertThat(directorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
