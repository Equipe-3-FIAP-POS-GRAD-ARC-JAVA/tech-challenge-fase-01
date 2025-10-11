-- Fixed UUIDs for users
-- vpadovam: a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11
-- mariasilva: a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12
-- joaop: a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13
-- anasouza: a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14
-- carlosol: a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15
-- joao.silva2: a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16

INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, roles, is_active)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Vinicius Padovam', 'vinicius@example.com', 'vpadovam', '$2a$10$eNG0/QQuDXqfEIlvDBfDb.hkiy22teXIBEMLlug.AZnZTqDE4UAUi', NOW(), NOW(), '{OWNER}', true) ON CONFLICT (email) DO NOTHING;

INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, roles, is_active)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Maria Silva', 'maria.silva@example.com', 'mariasilva', '$2a$10$eNG0/QQuDXqfEIlvDBfDb.hkiy22teXIBEMLlug.AZnZTqDE4UAUi', NOW(), NOW(), '{CLIENT}', true) ON CONFLICT (email) DO NOTHING;

INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, roles, is_active)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'João Pereira', 'joao.pereira@example.com', 'joaop', '$2a$10$eNG0/QQuDXqfEIlvDBfDb.hkiy22teXIBEMLlug.AZnZTqDE4UAUi', NOW(), NOW(), '{CLIENT}', true) ON CONFLICT (email) DO NOTHING;

INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, roles, is_active)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'Ana Souza', 'ana.souza@example.com', 'anasouza', '$2a$10$eNG0/QQuDXqfEIlvDBfDb.hkiy22teXIBEMLlug.AZnZTqDE4UAUi', NOW(), NOW(), '{CLIENT}', true) ON CONFLICT (email) DO NOTHING;

INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, roles, is_active)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'Carlos Oliveira', 'carlos.oliveira@example.com', 'carlosol', '$2a$10$eNG0/QQuDXqfEIlvDBfDb.hkiy22teXIBEMLlug.AZnZTqDE4UAUi', NOW(), NOW(), '{OWNER}', true) ON CONFLICT (email) DO NOTHING;

-- Adicionar o usuário joao.silva com senha123
INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, roles, is_active)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'João Silva', 'joao.silva@example.com', 'joao.silva', '$2a$10$eNG0/QQuDXqfEIlvDBfDb.hkiy22teXIBEMLlug.AZnZTqDE4UAUi', NOW(), NOW(), '{CLIENT}', true) ON CONFLICT (email) DO NOTHING;


-- ADDRESSES (referenciam os usuários já criados)
INSERT INTO address (id, user_id, street, number, city, created_at, updated_at)
VALUES (gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Rua das Palmeiras', '123', 'São Paulo', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;

INSERT INTO address (id, user_id, street, number, city, created_at, updated_at)
VALUES (gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Av. Paulista', '1000', 'São Paulo', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;

INSERT INTO address (id, user_id, street, number, city, created_at, updated_at)
VALUES (gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'Rua XV de Novembro', '200', 'Curitiba', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;

INSERT INTO address (id, user_id, street, number, city, created_at, updated_at)
VALUES (gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'Av. Atlântica', '500', 'Rio de Janeiro', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;

INSERT INTO address (id, user_id, street, number, city, created_at, updated_at)
VALUES (gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'Rua das Flores', '45B', 'Belo Horizonte', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;