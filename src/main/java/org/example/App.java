package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.dao.TempRecordDAO;
import org.example.dao.TemperatureUnitDAO;
import org.example.database.DBConnection;
import org.example.model.TemperatureUnit;

import java.sql.SQLException;
import java.util.List;

public class App extends Application {

    private final TemperatureConverter converter = new TemperatureConverter();
    private final TempRecordDAO recordDAO = new TempRecordDAO();
    private final TemperatureUnitDAO unitDAO = new TemperatureUnitDAO();

    private TextField valueField;
    private ComboBox<String> fromUnit;
    private ComboBox<String> toUnit;
    private Label resultLabel;
    private Label statusLabel;
    private List<TemperatureUnit> units;

    @Override
    public void start(Stage stage) {
        initializeDatabase();

        Label title = new Label("Temperature Converter");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        valueField = new TextField();
        valueField.setPromptText("Enter temperature");

        fromUnit = new ComboBox<>();
        toUnit = new ComboBox<>();

        loadUnits();

        resultLabel = new Label("Result will appear here");
        resultLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button convertButton = new Button("Convert");
        convertButton.setOnAction(e -> convert());

        Button saveButton = new Button("Save record");
        saveButton.setOnAction(e -> saveRecord());

        Button extremeButton = new Button("Check extreme temperature");
        extremeButton.setOnAction(e -> checkExtreme());

        Button databaseButton = new Button("Test database");
        databaseButton.setOnAction(e -> testDatabase());

        statusLabel = new Label();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);
        form.add(new Label("Temperature:"), 0, 0);
        form.add(valueField, 1, 0);
        form.add(new Label("From:"), 0, 1);
        form.add(fromUnit, 1, 1);
        form.add(new Label("To:"), 0, 2);
        form.add(toUnit, 1, 2);

        HBox buttons = new HBox(10, convertButton, saveButton, extremeButton);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(18, title, form, buttons, resultLabel, databaseButton, statusLabel);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 650, 500);
        stage.setTitle("Temperature Converter");
        stage.setScene(scene);
        stage.show();
    }

    private void initializeDatabase() {
        DBConnection.initializeDatabase();
        statusLabelSafe("Database initialized.");
    }

    private void loadUnits() {
        try {
            units = unitDAO.findAll();

            fromUnit.getItems().clear();
            toUnit.getItems().clear();

            for (TemperatureUnit unit : units) {
                fromUnit.getItems().add(unit.getName());
                toUnit.getItems().add(unit.getName());
            }

            if (!fromUnit.getItems().isEmpty()) {
                fromUnit.getSelectionModel().select("Celsius");
                toUnit.getSelectionModel().select("Fahrenheit");
            }
        } catch (SQLException e) {
            statusLabelSafe("Could not load temperature units.");
        }
    }

    private void convert() {
        try {
            double value = Double.parseDouble(valueField.getText());
            double result = convertTemperature(value, fromUnit.getValue(), toUnit.getValue());

            resultLabel.setText(String.format("%.2f %s = %.2f %s",
                    value, fromUnit.getValue(), result, toUnit.getValue()));

            statusLabel.setText("");
        } catch (NumberFormatException e) {
            resultLabel.setText("Please enter a valid number.");
        }
    }

    private double convertTemperature(double value, String from, String to) {
        if (from.equals(to)) {
            return value;
        }

        double celsius;

        switch (from) {
            case "Fahrenheit" -> celsius = converter.fahrenheitToCelsius(value);
            case "Kelvin" -> celsius = converter.kelvinToCelsius(value);
            default -> celsius = value;
        }

        return switch (to) {
            case "Fahrenheit" -> converter.celsiusToFahrenheit(celsius);
            case "Kelvin" -> celsius + 273.15;
            default -> celsius;
        };
    }

    private void saveRecord() {
        try {
            double value = Double.parseDouble(valueField.getText());
            double converted = convertTemperature(value, fromUnit.getValue(), toUnit.getValue());

            TemperatureUnit from = findUnit(fromUnit.getValue());
            TemperatureUnit to = findUnit(toUnit.getValue());

            recordDAO.save(value, from.getId(), converted, to.getId());
            statusLabel.setText("Temperature record saved.");
        } catch (NumberFormatException e) {
            statusLabel.setText("Enter a valid temperature first.");
        } catch (SQLException e) {
            statusLabel.setText("Could not save record: " + e.getMessage());
        }
    }

    private void checkExtreme() {
        try {
            double value = Double.parseDouble(valueField.getText());
            double celsius = convertTemperature(value, fromUnit.getValue(), "Celsius");

            if (converter.isExtremeTemperature(celsius)) {
                resultLabel.setText(String.format("%.2f °C is an extreme temperature.", celsius));
            } else {
                resultLabel.setText(String.format("%.2f °C is not an extreme temperature.", celsius));
            }
        } catch (NumberFormatException e) {
            resultLabel.setText("Please enter a valid number.");
        }
    }

    private void testDatabase() {
        try {
            List<TemperatureUnit> allUnits = unitDAO.findAll();
            statusLabel.setText("Database OK. " + allUnits.size() + " units found.");
        } catch (SQLException e) {
            statusLabel.setText("Database connection failed: " + e.getMessage());
        }
    }

    private TemperatureUnit findUnit(String name) {
        return units.stream()
                .filter(unit -> unit.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }

    private void statusLabelSafe(String text) {
        if (statusLabel != null) {
            statusLabel.setText(text);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
