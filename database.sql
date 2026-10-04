CREATE DATABASE IF NOT EXISTS temperature_converter;

CREATE USER IF NOT EXISTS 'temperature_user'@'localhost'
IDENTIFIED BY 'temperature_password';

GRANT ALL PRIVILEGES ON temperature_converter.*
TO 'temperature_user'@'localhost';

FLUSH PRIVILEGES;

USE temperature_converter;

CREATE TABLE IF NOT EXISTS temperature_units (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL UNIQUE,
    symbol VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS temperature_records (
    id INT PRIMARY KEY AUTO_INCREMENT,
    value DOUBLE NOT NULL,
    unit_id INT NOT NULL,
    converted_value DOUBLE NOT NULL,
    converted_unit_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (unit_id) REFERENCES temperature_units(id),
    FOREIGN KEY (converted_unit_id) REFERENCES temperature_units(id)
);

INSERT IGNORE INTO temperature_units (id, name, symbol)
VALUES
    (1, 'Celsius', '°C'),
    (2, 'Fahrenheit', '°F'),
    (3, 'Kelvin', 'K');
