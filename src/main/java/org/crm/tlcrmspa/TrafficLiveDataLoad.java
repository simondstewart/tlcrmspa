package org.crm.tlcrmspa;

import javax.annotation.PostConstruct;

import org.crm.tlcrmspa.domain.Client;
import org.crm.tlcrmspa.service.ClientService;
import org.crm.tlcrmspa.trafficlive.ClientCRMEntryTO;
import org.crm.tlcrmspa.trafficlive.ClientPagedResultsTO;
import org.crm.tlcrmspa.trafficlive.TrafficLiveRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.gatling.core.result.writer.RequestMessage;

@Controller
public class TrafficLiveDataLoad {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	public ClientService clientService;
	
	@Autowired
	public TrafficLiveRestClient trafficLiveRestClient;

	@RequestMapping(path="/api/action/load", method=RequestMethod.GET)
	public void loadClientFromTrafficLIVE() {
		ClientPagedResultsTO result = trafficLiveRestClient.getClients(0, 100);
		result.getResultList().forEach(i -> clientService.save(createClientFromTrafficClient(i)));
	}
	
	private Client createClientFromTrafficClient(ClientCRMEntryTO client) {
		Client localClient = objectMapper.convertValue(client, Client.class);
		return localClient;
	}

}
