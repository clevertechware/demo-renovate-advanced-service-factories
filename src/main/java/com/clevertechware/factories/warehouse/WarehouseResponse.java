package com.clevertechware.factories.warehouse;

public record WarehouseResponse(String code, String name, String city, int surfaceM2) {

    static WarehouseResponse from(Warehouse warehouse) {
        return new WarehouseResponse(
                warehouse.getCode(), warehouse.getName(), warehouse.getCity(), warehouse.getSurfaceM2());
    }
}
