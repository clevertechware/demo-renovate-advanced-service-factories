package com.clevertechware.factories.warehouse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class WarehouseRepositoryTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16.4");

    @Autowired
    WarehouseRepository repository;

    @Test
    void loadsTheThreeDemoWarehousesFromDataSql() {
        assertThat(repository.findAll())
                .extracting(Warehouse::getCode)
                .containsExactlyInAnyOrder("LYS-01", "NTE-02", "LIL-03");
    }

    @Test
    void findsAWarehouseByItsCode() {
        assertThat(repository.findByCode("NTE-02"))
                .hasValueSatisfying(warehouse -> {
                    assertThat(warehouse.getCity()).isEqualTo("Nantes");
                    assertThat(warehouse.getSurfaceM2()).isEqualTo(8500);
                });
    }

    @Test
    void returnsEmptyForAnUnknownCode() {
        assertThat(repository.findByCode("XXX-99")).isEmpty();
    }

    @Test
    void rejectsASecondWarehouseWithTheSameCode() {
        assertThatThrownBy(() -> repository.saveAndFlush(new Warehouse("LYS-01", "Doublon", "Lyon", 1)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
