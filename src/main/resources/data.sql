INSERT INTO warehouse (code, name, city, surface_m2) VALUES
    ('LYS-01', 'Entrepôt de Lyon', 'Lyon', 12000),
    ('NTE-02', 'Entrepôt de Nantes', 'Nantes', 8500),
    ('LIL-03', 'Entrepôt de Lille', 'Lille', 6200)
ON CONFLICT (code) DO NOTHING;
