
package org.example.dao;

import org.example.database.DBConnection;
import org.example.model.TempRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TempRecordDAO {

    public void save(double value, int unitId,
                     double convertedValue, int convertedUnitId) throws SQLException {

        String sql = """
                INSERT INTO temperature_records
                (value, unit_id, converted_value, converted_unit_id)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, value);
            statement.setInt(2, unitId);
            statement.setDouble(3, convertedValue);
            statement.setInt(4, convertedUnitId);

            statement.executeUpdate();
        }
    }

    public List<TempRecord> findAll() throws SQLException {

        List<TempRecord> records = new ArrayList<>();

        String sql = """
                SELECT id, value, unit_id, converted_value,
                       converted_unit_id, created_at
                FROM temperature_records
                ORDER BY id DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                records.add(new TempRecord(
                        resultSet.getInt("id"),
                        resultSet.getDouble("value"),
                        resultSet.getInt("unit_id"),
                        resultSet.getDouble("converted_value"),
                        resultSet.getInt("converted_unit_id"),
                        resultSet.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        }

        return records;
    }
}

/*
package org.example.dao;

import org.example.database.DBConnection;
import org.example.model.TempRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TempRecordDAO {

    public void save(TempRecord record) {

        String sql = """
                INSERT INTO temp_records
                (unit_id, input_temperature, output_temperature,
                 speed, distance, time_seconds)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, record.getUnitId());
            statement.setDouble(2, record.getInputTemperature());
            statement.setDouble(3, record.getOutputTemperature());
            statement.setDouble(4, record.getSpeed());
            statement.setDouble(5, record.getDistance());
            statement.setDouble(6, record.getTimeSeconds());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not save record", e);
        }
    }

    public List<TempRecord> findAll() {

        List<TempRecord> records = new ArrayList<>();

        String sql = """
                SELECT id, unit_id, input_temperature,
                       output_temperature, speed,
                       distance, time_seconds
                FROM temp_records
                ORDER BY id DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {

                TempRecord record = new TempRecord(
                        rs.getInt("unit_id"),
                        rs.getDouble("input_temperature"),
                        rs.getDouble("output_temperature"),
                        rs.getDouble("speed"),
                        rs.getDouble("distance"),
                        rs.getDouble("time_seconds")
                );

                records.add(record);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not load records", e);
        }

        return records;
    }
}*/