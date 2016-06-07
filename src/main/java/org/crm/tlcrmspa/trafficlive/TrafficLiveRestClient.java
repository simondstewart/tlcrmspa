package org.crm.tlcrmspa.trafficlive;

import java.net.URI;
import java.util.Arrays;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class TrafficLiveRestClient {

	private static final String API_BASEPATH = "https://api.sohnar.com/TrafficLiteServer/openapi";
	
	private final RestTemplate restTemplate;
	
	public TrafficLiveRestClient(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
	}

	public ClientPagedResultsTO getClients(Integer currentPage, Integer windowSize) {
		return getPagedResults(new ParameterizedTypeReference<ClientPagedResultsTO>() {}, "/crm/client", currentPage, windowSize);
	}

	public <TO extends BaseTO, PAGE extends PagedResultsTO<TO>> PAGE getPagedResults(ParameterizedTypeReference<PAGE> parameterizedTypeReference, String relativePath, Integer currentPage, Integer windowSize) {
		ResponseEntity<PAGE> page = getPage(parameterizedTypeReference, 
											buildUrl(API_BASEPATH, relativePath, currentPage, windowSize), 
											createHttpEntity());
		return page.getBody();
	}
	
	public <TO extends BaseTO> TO getById(String basePath, Long id) {
		return null;
	}
	
	public <TO extends BaseTO> TO update(String basePath, TO toUpdate) {
		return null;
	}
	
	public <TO extends BaseTO> TO create(String basePath, TO toCreate) {
		return null;
	}
	
	private HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		return entity;
	}

	private URI buildUrl(String baseUrl, String relativePath, Integer currentPage, Integer windowSize) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + relativePath).queryParam("windowSize", windowSize);
        builder.queryParam("currentPage", currentPage);
        return builder.build().encode().toUri();
	}
	
	private <TO extends BaseTO, PAGE extends PagedResultsTO<TO>> ResponseEntity<PAGE> 
												getPage(ParameterizedTypeReference<PAGE> pageType, URI uri, HttpEntity<String> entity) {
		ResponseEntity<PAGE> response = null;

		try {
			response = restTemplate.exchange(uri, HttpMethod.GET, entity, pageType);
		} catch (HttpStatusCodeException ex) {
			throw new RuntimeException(ex);
		}
		return response;
	}
	
}
