-- Pet Clinic Database Schema

CREATE TABLE IF NOT EXISTS owners (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    city VARCHAR(100),
    telephone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS pets (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    type VARCHAR(50) NOT NULL,
    owner_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES owners(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vets (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    specialty VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS visits (
    id SERIAL PRIMARY KEY,
    visit_date DATE NOT NULL,
    description TEXT,
    pet_id INTEGER NOT NULL,
    vet_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE,
    FOREIGN KEY (vet_id) REFERENCES vets(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS patient_cards (
    id SERIAL PRIMARY KEY,
    pet_id INTEGER NOT NULL UNIQUE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE
);

-- Insert sample data
INSERT INTO owners (first_name, last_name, address, city, telephone) VALUES
    ('Иван', 'Петров', 'ул. Ленина, 10', 'Москва', '+7-495-123-4567'),
    ('Мария', 'Смирнова', 'пр. Победы, 25', 'Санкт-Петербург', '+7-812-234-5678'),
    ('Алексей', 'Сидоров', 'ул. Садовая, 8', 'Новосибирск', '+7-383-345-6789');

INSERT INTO vets (first_name, last_name, specialty) VALUES
    ('Иван', 'Климов', 'Хирургия'),
    ('Михаил', 'Столяров', 'Стоматология'),
    ('Елена', 'Иванова', 'Терапия');

INSERT INTO pets (name, birth_date, type, owner_id) VALUES
    ('Макс', '2020-05-15', 'Собака', 1),
    ('Луна', '2021-08-22', 'Кошка', 1),
    ('Чарли', '2019-03-10', 'Собака', 2),
    ('Белла', '2022-01-05', 'Кошка', 3);

INSERT INTO visits (visit_date, description, pet_id, vet_id) VALUES
    ('2024-01-15', 'Ежегодный осмотр', 1, 3),
    ('2024-02-20', 'Чистка зубов', 2, 2),
    ('2024-03-10', 'Вакцинация', 3, 1);

INSERT INTO patient_cards (pet_id, notes) VALUES
    (1, 'Здоровая собака, очень активная. Нужны регулярные физические нагрузки. Аллергия на курицу.'),
    (2, 'Домашняя кошка, немного избыточный вес. Рекомендуется диета.'),
    (3, 'Пожилая собака, артрит. Следить за подвижностью и болевыми ощущениями.');
