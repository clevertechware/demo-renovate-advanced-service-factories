package com.clevertechware.factories.warehouse;

import com.clevertechware.factories.plan.PlanClient;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WarehouseController {

    private static final MediaType IMAGE_SVG = MediaType.valueOf("image/svg+xml");

    private final WarehouseRepository repository;
    private final PlanClient planClient;

    public WarehouseController(WarehouseRepository repository, PlanClient planClient) {
        this.repository = repository;
        this.planClient = planClient;
    }

    @GetMapping("/warehouses")
    public List<WarehouseResponse> list() {
        return repository.findAll().stream().map(WarehouseResponse::from).toList();
    }

    @GetMapping("/warehouses/{code}")
    public ResponseEntity<WarehouseResponse> get(@PathVariable String code) {
        return repository.findByCode(code)
                .map(WarehouseResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/warehouses/{code}/plan", produces = "image/svg+xml")
    public ResponseEntity<String> plan(@PathVariable String code) {
        return repository.findByCode(code)
                .map(warehouse -> ResponseEntity.ok().contentType(IMAGE_SVG).body(planClient.fetchSvg(code)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
