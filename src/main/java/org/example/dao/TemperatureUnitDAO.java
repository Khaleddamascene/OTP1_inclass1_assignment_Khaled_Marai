package org.example.dao;

import org.example.database.DBConnection;
import org.example.model.TemperatureUnit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TemperatureUnitDAO {

    public List<TemperatureUnit> findAll() throws SQLException {
        List<TemperatureUnit> units = new ArrayList<>();

        String sql = "SELECT id, name, symbol FROM temperature_units ORDER BY id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                units.add(new TemperatureUnit(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("symbol")
                ));
            }
        }

        return units;
    }

    public TemperatureUnit findById(int id) throws SQLException {
        String sql = "SELECT id, name, symbol FROM temperature_units WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new TemperatureUnit(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("symbol")
                    );
                }
            }
        }

        return null;
    }
}
