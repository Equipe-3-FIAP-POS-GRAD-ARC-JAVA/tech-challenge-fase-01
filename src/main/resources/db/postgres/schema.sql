CREATE TABLE IF NOT EXISTS "users" (
    id TEXT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    role TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS address (
    id TEXT PRIMARY KEY,
    user_id TEXT REFERENCES "users"(id),
    street VARCHAR(100) NOT NULL,
    number VARCHAR(20) NOT NULL,
    city VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);