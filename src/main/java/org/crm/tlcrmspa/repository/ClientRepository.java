package org.crm.tlcrmspa.repository;

import org.crm.tlcrmspa.domain.Client;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Client entity.
 */
public interface ClientRepository extends JpaRepository<Client,Long> {

    @Query("select distinct client from Client client left join fetch client.tags")
    List<Client> findAllWithEagerRelationships();

    @Query("select client from Client client left join fetch client.tags where client.id =:id")
    Client findOneWithEagerRelationships(@Param("id") Long id);

}
