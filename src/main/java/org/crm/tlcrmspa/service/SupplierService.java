package org.crm.tlcrmspa.service;

import org.crm.tlcrmspa.domain.Supplier;
import org.crm.tlcrmspa.repository.SupplierRepository;
import org.crm.tlcrmspa.repository.search.SupplierSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Supplier.
 */
@Service
@Transactional
public class SupplierService {

    private final Logger log = LoggerFactory.getLogger(SupplierService.class);
    
    @Inject
    private SupplierRepository supplierRepository;
    
    @Inject
    private SupplierSearchRepository supplierSearchRepository;
    
    /**
     * Save a supplier.
     * 
     * @param supplier the entity to save
     * @return the persisted entity
     */
    public Supplier save(Supplier supplier) {
        log.debug("Request to save Supplier : {}", supplier);
        Supplier result = supplierRepository.save(supplier);
        supplierSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the suppliers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Supplier> findAll(Pageable pageable) {
        log.debug("Request to get all Suppliers");
        Page<Supplier> result = supplierRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one supplier by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Supplier findOne(Long id) {
        log.debug("Request to get Supplier : {}", id);
        Supplier supplier = supplierRepository.findOne(id);
        return supplier;
    }

    /**
     *  Delete the  supplier by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Supplier : {}", id);
        supplierRepository.delete(id);
        supplierSearchRepository.delete(id);
    }

    /**
     * Search for the supplier corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Supplier> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Suppliers for query {}", query);
        return supplierSearchRepository.search(queryStringQuery(query), pageable);
    }
}
