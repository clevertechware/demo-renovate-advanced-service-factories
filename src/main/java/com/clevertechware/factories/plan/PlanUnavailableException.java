package com.clevertechware.factories.plan;

public class PlanUnavailableException extends RuntimeException {

    public PlanUnavailableException(String warehouseCode, Throwable cause) {
        super("Plan service unavailable for warehouse " + warehouseCode, cause);
    }
}
