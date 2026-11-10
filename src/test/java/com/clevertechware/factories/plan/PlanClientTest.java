package com.clevertechware.factories.plan;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class PlanClientTest {

    private static final String PLAN_URL = "http://plan.test";

    private MockRestServiceServer planService;
    private PlanClient planClient;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        planService = MockRestServiceServer.bindTo(builder).build();
        planClient = new PlanClient(builder, PLAN_URL);
    }

    @Test
    void returnsTheSvgServedByThePlanService() {
        planService.expect(requestTo(PLAN_URL + "/plans/LYS-01.svg"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("<svg/>", MediaType.valueOf("image/svg+xml")));

        assertThat(planClient.fetchSvg("LYS-01")).isEqualTo("<svg/>");
        planService.verify();
    }

    @Test
    void throwsPlanNotFoundWhenThePlanServiceAnswers404() {
        planService.expect(requestTo(PLAN_URL + "/plans/LIL-03.svg")).andRespond(withResourceNotFound());

        assertThatThrownBy(() -> planClient.fetchSvg("LIL-03")).isInstanceOf(PlanNotFoundException.class);
    }

    @Test
    void throwsPlanUnavailableWhenThePlanServiceAnswers500() {
        planService.expect(requestTo(PLAN_URL + "/plans/NTE-02.svg")).andRespond(withServerError());

        assertThatThrownBy(() -> planClient.fetchSvg("NTE-02")).isInstanceOf(PlanUnavailableException.class);
    }
}
