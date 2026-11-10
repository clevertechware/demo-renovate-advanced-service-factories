package com.clevertechware.factories.warehouse;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clevertechware.factories.plan.PlanClient;
import com.clevertechware.factories.plan.PlanNotFoundException;
import com.clevertechware.factories.plan.PlanUnavailableException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WarehouseController.class)
class WarehouseControllerTest {

    private static final Warehouse LYON = new Warehouse("LYS-01", "Entrepôt de Lyon", "Lyon", 12000);

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    WarehouseRepository repository;

    @MockitoBean
    PlanClient planClient;

    @Test
    void listsWarehousesAsJson() throws Exception {
        given(repository.findAll()).willReturn(List.of(LYON));

        mockMvc.perform(get("/warehouses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("LYS-01"))
                .andExpect(jsonPath("$[0].surfaceM2").value(12000));
    }

    @Test
    void returnsAWarehouseByCode() throws Exception {
        given(repository.findByCode("LYS-01")).willReturn(Optional.of(LYON));

        mockMvc.perform(get("/warehouses/LYS-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Lyon"));
    }

    @Test
    void answers404ForAnUnknownWarehouse() throws Exception {
        given(repository.findByCode("XXX-99")).willReturn(Optional.empty());

        mockMvc.perform(get("/warehouses/XXX-99")).andExpect(status().isNotFound());
    }

    @Test
    void relaysThePlanSvgWithItsMediaType() throws Exception {
        given(repository.findByCode("LYS-01")).willReturn(Optional.of(LYON));
        given(planClient.fetchSvg("LYS-01")).willReturn("<svg/>");

        mockMvc.perform(get("/warehouses/LYS-01/plan"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/svg+xml"))
                .andExpect(content().string("<svg/>"));
    }

    @Test
    void answers404ForThePlanOfAnUnknownWarehouse() throws Exception {
        given(repository.findByCode("XXX-99")).willReturn(Optional.empty());

        mockMvc.perform(get("/warehouses/XXX-99/plan")).andExpect(status().isNotFound());
    }

    @Test
    void answers404WhenThePlanServiceHasNoPlan() throws Exception {
        given(repository.findByCode("LYS-01")).willReturn(Optional.of(LYON));
        given(planClient.fetchSvg("LYS-01")).willThrow(new PlanNotFoundException("LYS-01"));

        mockMvc.perform(get("/warehouses/LYS-01/plan")).andExpect(status().isNotFound());
    }

    @Test
    void answers502WhenThePlanServiceIsUnavailable() throws Exception {
        given(repository.findByCode("LYS-01")).willReturn(Optional.of(LYON));
        given(planClient.fetchSvg("LYS-01")).willThrow(new PlanUnavailableException("LYS-01", new RuntimeException()));

        mockMvc.perform(get("/warehouses/LYS-01/plan")).andExpect(status().isBadGateway());
    }
}
