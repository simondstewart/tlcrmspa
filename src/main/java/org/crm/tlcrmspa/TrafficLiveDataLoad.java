package org.crm.tlcrmspa;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.crm.tlcrmspa.domain.Client;
import org.crm.tlcrmspa.repository.search.ClientSearchRepository;
import org.crm.tlcrmspa.service.ClientService;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.deltek.integration.trafficlive.domain.ClientCRMEntryTO;
import com.deltek.integration.trafficlive.domain.ClientPagedResultsTO;
import com.deltek.integration.trafficlive.service.TrafficLiveRestClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class TrafficLiveDataLoad {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	public ClientService clientService;
	
	@Autowired
	public ClientSearchRepository clientRepository;
	
	@Autowired
	public TrafficLiveRestClient trafficLiveRestClient;

	@RequestMapping(path="/api/action/load", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClientPagedResultsTO> loadClientFromTrafficLIVE(
		@RequestParam(value = "currentPage", required = false, defaultValue="1") Integer currentPage,
		@RequestParam(value = "windowSize", required = false, defaultValue="100") Integer windowSize) {
		ClientPagedResultsTO result = trafficLiveRestClient.client().getPage(currentPage, windowSize);
		ingestPageOfClients(result);
//		result.getResultList().forEach(i -> clientService.save(createClientFromTrafficClient(i)));
		return ResponseEntity.ok().body(result);
	}

	@RequestMapping(path="/api/action/legacyload", method=RequestMethod.GET)
	public void loadClientFromTrafficLIVE() {
		ClientPagedResultsTO result = trafficLiveRestClient.client().getPage(0, 100);
		result.getResultList().forEach(i -> clientService.save(createClientFromTrafficClient(i)));
	}
	
	private void ingestPageOfClients(ClientPagedResultsTO clientResults) {
		Map<Long, ClientCRMEntryTO> tlClientIdMap = clientResults.getResultList().stream().collect(Collectors.toMap(ClientCRMEntryTO::getId, v -> v));
		Iterable<Client> existingClients = 
				clientService.searchQuery(QueryBuilders.termsQuery("externalCode", tlClientIdMap.keySet()), new PageRequest(0, tlClientIdMap.keySet().size()));
		Map<String, Client> localClientIdMap = new HashMap<>();
		existingClients.forEach(item -> localClientIdMap.put(item.getExternalCode(), item));
		tlClientIdMap.values().forEach(tlClient -> {
			Client existingClient = localClientIdMap.get(tlClient.getId()+"");				
			if(existingClient == null) {
				existingClient = createClientFromTrafficClient(tlClient);
			} else {
				mapProperties(tlClient, existingClient);
			}	
			clientService.save(existingClient);
		});
		
	}
	
	private void mapProperties(ClientCRMEntryTO tlClient, Client existingClient) {
		existingClient.setName(tlClient.name);
		existingClient.setDescription(tlClient.description);
		existingClient.setAccountManagerId(tlClient.accountManagerId);
		existingClient.setWebsite(tlClient.website);
	}

	private String buildSearchQueryFromClientIds(Set<Long> clientIds) {
		return null;
	}

	private Client createClientFromTrafficClient(ClientCRMEntryTO client) {
		Client localClient = objectMapper.convertValue(client, Client.class);
		localClient.setExternalCode(""+client.getId());
		return localClient;
	}

}
