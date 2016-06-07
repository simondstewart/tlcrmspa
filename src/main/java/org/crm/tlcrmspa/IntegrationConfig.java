package org.crm.tlcrmspa;

import javax.annotation.PostConstruct;

import org.crm.tlcrmspa.service.ClientService;
import org.crm.tlcrmspa.trafficlive.TrafficLiveRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class IntegrationConfig {

	@Bean
	public TrafficLiveRestClient trafficLiveRestClient() {
		return new TrafficLiveRestClient(restTemplate());
	}
	
	@Bean
	public RestTemplate restTemplate() {
		TestRestTemplate basicAuthRestTemplate = new TestRestTemplate("simonstewart@deltek.com", "turK0QoPU5gkO6usNGfZcYcBpMEIaI0nUNLZQQSA");
		return basicAuthRestTemplate;
	}

}
