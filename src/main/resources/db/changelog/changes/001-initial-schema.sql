-- liquibase formatted sql

-- changeset kotlin-awesome-starter:001-create-users-table
-- comment: Create users table with UUID primary key
CREATE TABLE users (
    id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
);
-- rollback DROP TABLE users;

-- changeset kotlin-awesome-starter:001-create-products-table
-- comment: Create products table with UUID primary key
CREATE TABLE products (
    id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    price DECIMAL(19,2) NOT NULL,
    stock INTEGER DEFAULT 0 NOT NULL,
    available BOOLEAN DEFAULT TRUE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
);
-- rollback DROP TABLE products;

-- changeset kotlin-awesome-starter:001-create-users-email-index
-- comment: Create index on users email for faster lookups
CREATE INDEX idx_users_email ON users(email);
-- rollback DROP INDEX idx_users_email;

-- changeset kotlin-awesome-starter:001-create-products-name-index
-- comment: Create index on products name for search performance
CREATE INDEX idx_products_name ON products(name);
-- rollback DROP INDEX idx_products_name;
