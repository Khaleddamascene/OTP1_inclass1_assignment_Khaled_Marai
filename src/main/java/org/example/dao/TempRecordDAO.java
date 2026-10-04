
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

