package com.clevertechware.factories.warehouse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(name = "surface_m2", nullable = false)
    private int surfaceM2;

    protected Warehouse() {
    }

    public Warehouse(String code, String name, String city, int surfaceM2) {
        this.code = code;
        this.name = name;
        this.city = city;
        this.surfaceM2 = surfaceM2;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public int getSurfaceM2() {
        return surfaceM2;
    }
}
