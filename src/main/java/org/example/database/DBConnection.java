package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL =
            System.getenv().getOrDefault(
                    "DB_URL",
                    "jdbc:mariadb://localhost:3306/temperature_converter"
            );

    private static final String USER =
            System.getenv().getOrDefault("DB_USER", "maraim");

    private static final String PASSWORD =
            System.getenv().getOrDefault("DB_PASSWORD", "1234");

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {

        String unitsTable = """
                CREATE TABLE IF NOT EXISTS temperature_units (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(50) NOT NULL,
                    symbol VARCHAR(10) NOT NULL
                )
                """;

        String recordsTable = """
                CREATE TABLE IF NOT EXISTS temperature_records (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    value DOUBLE NOT NULL,
                    unit_id INT NOT NULL,
                    converted_value DOUBLE NOT NULL,
                    converted_unit_id INT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (unit_id)
                        REFERENCES temperature_units(id),
                    FOREIGN KEY (converted_unit_id)
                        REFERENCES temperature_units(id)
                )
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(unitsTable);
            statement.execute(recordsTable);

            statement.executeUpdate("""
                    INSERT IGNORE INTO temperature_units
                    (id, name, symbol)
                    VALUES
                    (1, 'Celsius', 'C'),
                    (2, 'Fahrenheit', 'F'),
                    (3, 'Kelvin', 'K')
                    """);

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Database initialization failed", e);
        }
    }
}