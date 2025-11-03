INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, roles, is_active)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Vinicius Padovam', 'vinicius@example.com', 'vpadovam', '$2a$10$eNG0/QQuDXqfEIlvDBfDb.hkiy22teXIBEMLlug.AZnZTqDE4UAUi', NOW(), NOW(), '{OWNER}', true) ON CONFLICT (id) DO NOTHING;

INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, roles, is_active)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Maria Silva', 'maria.silva@example.com', 'mariasilva', '$2a$10$eNG0/QQuDXqfEIlvDBfDb.hkiy22teXIBEMLlug.AZnZTqDE4UAUi', NOW(), NOW(), '{CLIENT}', true) ON CONFLICT (id) DO NOTHING;

INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, roles, is_active)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'João Pereira', 'joao.pereira@example.com', 'joaop', '$2a$10$eNG0/QQuDXqfEIlvDBfDb.hkiy22teXIBEMLlug.AZnZTqDE4UAUi', NOW(), NOW(), '{CLIENT}', true) ON CONFLICT (id) DO NOTHING;

INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, roles, is_active)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'Ana Souza', 'ana.souza@example.com', 'anasouza', '$2a$10$eNG0/QQuDXqfEIlvDBfDb.hkiy22teXIBEMLlug.AZnZTqDE4UAUi', NOW(), NOW(), '{CLIENT}', true) ON CONFLICT (id) DO NOTHING;

INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, roles, is_active)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'Carlos Oliveira', 'carlos.oliveira@example.com', 'carlosol', '$2a$10$eNG0/QQuDXqfEIlvDBfDb.hkiy22teXIBEMLlug.AZnZTqDE4UAUi', NOW(), NOW(), '{OWNER}', true) ON CONFLICT (id) DO NOTHING;

INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, roles, is_active)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'João Silva', 'joao.silva@example.com', 'joao.silva', '$2a$10$eNG0/QQuDXqfEIlvDBfDb.hkiy22teXIBEMLlug.AZnZTqDE4UAUi', NOW(), NOW(), '{CLIENT}', true) ON CONFLICT (id) DO NOTHING;

INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, roles, is_active)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'Emerson Silva', 'emerson.silva@example.com', 'emerson.silva', '$2a$10$eNG0/QQuDXqfEIlvDBfDb.hkiy22teXIBEMLlug.AZnZTqDE4UAUi', NOW(), NOW(), '{ADMIN}', true) ON CONFLICT (id) DO NOTHING;

-- ADDRESSES (referenciam os usuários já criados)
INSERT INTO address (id, user_id, street, number, complement, neighborhood, city, zip_code, created_at, updated_at)
VALUES (gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Rua das Palmeiras', '123', 'Apto 45', 'Jardins', 'São Paulo', '01414-000', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;

INSERT INTO address (id, user_id, street, number, complement, neighborhood, city, zip_code, created_at, updated_at)
VALUES (gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Av. Paulista', '1000', NULL, 'Bela Vista', 'São Paulo', '01310-100', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;

INSERT INTO address (id, user_id, street, number, complement, neighborhood, city, zip_code, created_at, updated_at)
VALUES (gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'Rua XV de Novembro', '200', 'Sala 302', 'Centro', 'Curitiba', '80020-310', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;

INSERT INTO address (id, user_id, street, number, complement, neighborhood, city, zip_code, created_at, updated_at)
VALUES (gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'Av. Atlântica', '500', 'Cobertura', 'Copacabana', 'Rio de Janeiro', '22070-000', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;

INSERT INTO address (id, user_id, street, number, complement, neighborhood, city, zip_code, created_at, updated_at)
VALUES (gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'Rua das Flores', '45B', NULL, 'Centro', 'Belo Horizonte', '30112-000', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;

INSERT INTO address (id, user_id, street, number, complement, neighborhood, city, zip_code, created_at, updated_at)
VALUES (gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'Av. Paulista', '1000', 'Bloco B', 'Bela Vista', 'São Paulo', '01310-100', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;
