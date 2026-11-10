package com.clevertechware.factories.plan;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class PlanClient {

    private final RestClient restClient;

    public PlanClient(RestClient.Builder builder, @Value("${plan.service.url}") String planServiceUrl) {
        this.restClient = builder.baseUrl(planServiceUrl).build();
    }

    public String fetchSvg(String warehouseCode) {
        try {
            return restClient.get()
                    .uri("/plans/{code}.svg", warehouseCode)
                    .retrieve()
                    .onStatus(status -> status == HttpStatus.NOT_FOUND, (request, response) -> {
                        throw new PlanNotFoundException(warehouseCode);
                    })
                    .body(String.class);
        } catch (PlanNotFoundException e) {
            throw e;
        } catch (RestClientException e) {
            throw new PlanUnavailableException(warehouseCode, e);
        }
    }
}
