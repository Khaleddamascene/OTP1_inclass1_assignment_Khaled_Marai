package org.example;

public class Main {

    public static void main(String[] args) {
        TemperatureConverter converter = new TemperatureConverter();

        System.out.println("Temperature Converter");

        System.out.println("100 Fahrenheit = "
                + converter.fahrenheitToCelsius(100) + " Celsius");

        System.out.println("0 Celsius = "
                + converter.celsiusToFahrenheit(0) + " Fahrenheit");

        System.out.println("300 Kelvin = "
                + converter.kelvinToCelsius(300) + " Celsius");

        System.out.println("60 Celsius is extreme: "
                + converter.isExtremeTemperature(60));
    }
}