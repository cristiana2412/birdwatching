-- Initialize the birds table
CREATE TABLE IF NOT EXISTS birds
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR(100) NOT NULL,
    color  VARCHAR(50),
    weight DECIMAL(5, 2),
    height DECIMAL(5, 2)
);

-- Initialize the sightings table with a foreign key reference to the birds table
CREATE TABLE IF NOT EXISTS sightings
(
    id            SERIAL PRIMARY KEY,
    bird_id       INT NOT NULL,
    location      VARCHAR(255),
    sighting_date TIMESTAMP,
    FOREIGN KEY (bird_id) REFERENCES birds (id) ON DELETE CASCADE
);

-- Create indexes to optimize query performance
CREATE INDEX idx_bird_name ON birds (name);
CREATE INDEX idx_bird_color ON birds (color);
CREATE INDEX idx_sighting_bird_id ON sightings (bird_id);
CREATE INDEX idx_sighting_location ON sightings (location);
CREATE INDEX idx_sighting_date ON sightings (sighting_date);

-- Insert initial data for testing
INSERT INTO birds (name, color, weight, height)
VALUES ('Sparrow', 'Brown', 0.02, 16.0),
       ('Eagle', 'Gray', 4.5, 70.0),
       ('Parrot', 'Green', 0.1, 20.0);

INSERT INTO sightings (bird_id, location, sighting_date)
VALUES (1, 'Central Park', '2023-10-01 10:30:00'),
       (2, 'Grand Canyon', '2023-09-15 08:00:00'),
       (3, 'Amazon Rainforest', '2023-08-12 14:45:00');
