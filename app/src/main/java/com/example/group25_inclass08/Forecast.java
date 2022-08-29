/**
 * Assignment #: InClass08
 * File Name: Group25_InClass08 Forecast.java
 * Full Name: Kristin Pflug
 */

package com.example.group25_inclass08;

public class Forecast {
    String dateTime;
    double temperature;
    double maxTemp;
    double minTemp;
    int humidity;
    String description;
    String weatherIcon;

    public Forecast() {
        this.dateTime = "";
        this.temperature = 0.0;
        this.maxTemp = 0.0;
        this.minTemp = 0.0;
        this.humidity = 0;
        this.description = "";
        this.weatherIcon = "";
    }

    public Forecast(String dateTime, double temperature, double maxTemp, double minTemp, int humidity, String description, String weatherIcon) {
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.humidity = humidity;
        this.description = description;
        this.weatherIcon = weatherIcon;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }
}
