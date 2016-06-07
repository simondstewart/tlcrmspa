package org.crm.tlcrmspa.web.rest;

import org.crm.tlcrmspa.TlcrmspaApp;
import org.crm.tlcrmspa.domain.Client;
import org.crm.tlcrmspa.repository.ClientRepository;
import org.crm.tlcrmspa.service.ClientService;
import org.crm.tlcrmspa.repository.search.ClientSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ClientResource REST controller.
 *
 * @see ClientResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TlcrmspaApp.class)
@WebAppConfiguration
@IntegrationTest
public class ClientResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_WEBSITE = "AAAAA";
    private static final String UPDATED_WEBSITE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_CRM_ENTRY_TYPE = "AAAAA";
    private static final String UPDATED_CRM_ENTRY_TYPE = "BBBBB";
    private static final String DEFAULT_INDUSTRY_TYPE = "AAAAA";
    private static final String UPDATED_INDUSTRY_TYPE = "BBBBB";

    private static final Long DEFAULT_ACCOUNT_MANAGER_ID = 1L;
    private static final Long UPDATED_ACCOUNT_MANAGER_ID = 2L;
    private static final String DEFAULT_EXTERNAL_CODE = "AAAAA";
    private static final String UPDATED_EXTERNAL_CODE = "BBBBB";

    @Inject
    private ClientRepository clientRepository;

    @Inject
    private ClientService clientService;

    @Inject
    private ClientSearchRepository clientSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClientMockMvc;

    private Client client;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientResource clientResource = new ClientResource();
        ReflectionTestUtils.setField(clientResource, "clientService", clientService);
        this.restClientMockMvc = MockMvcBuilders.standaloneSetup(clientResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        clientSearchRepository.deleteAll();
        client = new Client();
        client.setName(DEFAULT_NAME);
        client.setWebsite(DEFAULT_WEBSITE);
        client.setDescription(DEFAULT_DESCRIPTION);
        client.setCrmEntryType(DEFAULT_CRM_ENTRY_TYPE);
        client.setIndustryType(DEFAULT_INDUSTRY_TYPE);
        client.setAccountManagerId(DEFAULT_ACCOUNT_MANAGER_ID);
        client.setExternalCode(DEFAULT_EXTERNAL_CODE);
    }

    @Test
    @Transactional
    public void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client

        restClientMockMvc.perform(post("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(client)))
                .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clients.get(clients.size() - 1);
        assertThat(testClient.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClient.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testClient.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testClient.getCrmEntryType()).isEqualTo(DEFAULT_CRM_ENTRY_TYPE);
        assertThat(testClient.getIndustryType()).isEqualTo(DEFAULT_INDUSTRY_TYPE);
        assertThat(testClient.getAccountManagerId()).isEqualTo(DEFAULT_ACCOUNT_MANAGER_ID);
        assertThat(testClient.getExternalCode()).isEqualTo(DEFAULT_EXTERNAL_CODE);

        // Validate the Client in ElasticSearch
        Client clientEs = clientSearchRepository.findOne(testClient.getId());
        assertThat(clientEs).isEqualToComparingFieldByField(testClient);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setName(null);

        // Create the Client, which fails.

        restClientMockMvc.perform(post("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(client)))
                .andExpect(status().isBadRequest());

        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clients
        restClientMockMvc.perform(get("/api/clients?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].crmEntryType").value(hasItem(DEFAULT_CRM_ENTRY_TYPE.toString())))
                .andExpect(jsonPath("$.[*].industryType").value(hasItem(DEFAULT_INDUSTRY_TYPE.toString())))
                .andExpect(jsonPath("$.[*].accountManagerId").value(hasItem(DEFAULT_ACCOUNT_MANAGER_ID.intValue())))
                .andExpect(jsonPath("$.[*].externalCode").value(hasItem(DEFAULT_EXTERNAL_CODE.toString())));
    }

    @Test
    @Transactional
    public void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.crmEntryType").value(DEFAULT_CRM_ENTRY_TYPE.toString()))
            .andExpect(jsonPath("$.industryType").value(DEFAULT_INDUSTRY_TYPE.toString()))
            .andExpect(jsonPath("$.accountManagerId").value(DEFAULT_ACCOUNT_MANAGER_ID.intValue()))
            .andExpect(jsonPath("$.externalCode").value(DEFAULT_EXTERNAL_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClient() throws Exception {
        // Initialize the database
        clientService.save(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        Client updatedClient = new Client();
        updatedClient.setId(client.getId());
        updatedClient.setName(UPDATED_NAME);
        updatedClient.setWebsite(UPDATED_WEBSITE);
        updatedClient.setDescription(UPDATED_DESCRIPTION);
        updatedClient.setCrmEntryType(UPDATED_CRM_ENTRY_TYPE);
        updatedClient.setIndustryType(UPDATED_INDUSTRY_TYPE);
        updatedClient.setAccountManagerId(UPDATED_ACCOUNT_MANAGER_ID);
        updatedClient.setExternalCode(UPDATED_EXTERNAL_CODE);

        restClientMockMvc.perform(put("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClient)))
                .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clients.get(clients.size() - 1);
        assertThat(testClient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClient.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testClient.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testClient.getCrmEntryType()).isEqualTo(UPDATED_CRM_ENTRY_TYPE);
        assertThat(testClient.getIndustryType()).isEqualTo(UPDATED_INDUSTRY_TYPE);
        assertThat(testClient.getAccountManagerId()).isEqualTo(UPDATED_ACCOUNT_MANAGER_ID);
        assertThat(testClient.getExternalCode()).isEqualTo(UPDATED_EXTERNAL_CODE);

        // Validate the Client in ElasticSearch
        Client clientEs = clientSearchRepository.findOne(testClient.getId());
        assertThat(clientEs).isEqualToComparingFieldByField(testClient);
    }

    @Test
    @Transactional
    public void deleteClient() throws Exception {
        // Initialize the database
        clientService.save(client);

        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Get the client
        restClientMockMvc.perform(delete("/api/clients/{id}", client.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean clientExistsInEs = clientSearchRepository.exists(client.getId());
        assertThat(clientExistsInEs).isFalse();

        // Validate the database is empty
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClient() throws Exception {
        // Initialize the database
        clientService.save(client);

        // Search the client
        restClientMockMvc.perform(get("/api/_search/clients?query=id:" + client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].crmEntryType").value(hasItem(DEFAULT_CRM_ENTRY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].industryType").value(hasItem(DEFAULT_INDUSTRY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accountManagerId").value(hasItem(DEFAULT_ACCOUNT_MANAGER_ID.intValue())))
            .andExpect(jsonPath("$.[*].externalCode").value(hasItem(DEFAULT_EXTERNAL_CODE.toString())));
    }
}
