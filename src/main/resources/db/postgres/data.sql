INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, role)
VALUES (gen_random_uuid(), 'Vinicius Padovam', 'vinicius@example.com', 'vpadovam', '123456', NOW(), NOW(), 'OWNER') ON CONFLICT (email) DO NOTHING;

INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, role)
VALUES (gen_random_uuid(), 'Maria Silva', 'maria.silva@example.com', 'mariasilva', 'senha123', NOW(), NOW(), 'CLIENT') ON CONFLICT (email) DO NOTHING;

INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, role)
VALUES (gen_random_uuid(), 'João Pereira', 'joao.pereira@example.com', 'joaop', 'abc123', NOW(), NOW(), 'CLIENT') ON CONFLICT (email) DO NOTHING;

INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, role)
VALUES (gen_random_uuid(), 'Ana Souza', 'ana.souza@example.com', 'anasouza', 'qwerty', NOW(), NOW(), 'CLIENT') ON CONFLICT (email) DO NOTHING;

INSERT INTO "users" (id, name, email, login, password, created_at, updated_at, role)
VALUES (gen_random_uuid(), 'Carlos Oliveira', 'carlos.oliveira@example.com', 'carlosol', 'pass123', NOW(), NOW(), 'OWNER') ON CONFLICT (email) DO NOTHING;


-- ADDRESSES (referenciam os usuários já criados)
INSERT INTO address (id, user_id, street, number, city, created_at, updated_at)
VALUES (gen_random_uuid(), (SELECT id FROM "users" WHERE login = 'vpadovam'), 'Rua das Palmeiras', '123', 'São Paulo', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;

INSERT INTO address (id, user_id, street, number, city, created_at, updated_at)
VALUES (gen_random_uuid(), (SELECT id FROM "users" WHERE login = 'mariasilva'), 'Av. Paulista', '1000', 'São Paulo', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;

INSERT INTO address (id, user_id, street, number, city, created_at, updated_at)
VALUES (gen_random_uuid(), (SELECT id FROM "users" WHERE login = 'joaop'), 'Rua XV de Novembro', '200', 'Curitiba', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;

INSERT INTO address (id, user_id, street, number, city, created_at, updated_at)
VALUES (gen_random_uuid(), (SELECT id FROM "users" WHERE login = 'anasouza'), 'Av. Atlântica', '500', 'Rio de Janeiro', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;

INSERT INTO address (id, user_id, street, number, city, created_at, updated_at)
VALUES (gen_random_uuid(), (SELECT id FROM "users" WHERE login = 'carlosol'), 'Rua das Flores', '45B', 'Belo Horizonte', NOW(), NOW()) ON CONFLICT (id) DO NOTHING;