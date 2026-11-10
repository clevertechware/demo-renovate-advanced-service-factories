package com.clevertechware.factories.plan;

public class PlanNotFoundException extends RuntimeException {

    public PlanNotFoundException(String warehouseCode) {
        super("No plan for warehouse " + warehouseCode);
    }
}
