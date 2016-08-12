package org.crm.tlcrmspa;

import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.deltek.integration.trafficlive.service.TrafficLiveRestClient;

@Configuration
public class IntegrationConfig {
	
	@Bean
	public RestTemplate restTemplate() {
		TestRestTemplate basicAuthRestTemplate = new TestRestTemplate("simonstewart@deltek.com", "turK0QoPU5gkO6usNGfZcYcBpMEIaI0nUNLZQQSA");
		return basicAuthRestTemplate;
	}
	
	@Bean
	public TrafficLiveRestClient restClient() {
		TrafficLiveRestClient tlClient = new TrafficLiveRestClient(restTemplate(), "https://stage-api.sohnar.com/TrafficLiteServer/openapi");
		return tlClient;
	}
	
}
