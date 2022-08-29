/**
 * Assignment #: InClass08
 * File Name: Group25_InClass08 Weather.java
 * Full Name: Kristin Pflug
 */

package com.example.group25_inclass08;

public class Weather {
    String city;
    double currentTemp;
    double maxTemp;
    double minTemp;
    String description;
    int humidity;
    double windSpeed;
    double windDegree;
    int cloudiness;

    public Weather() {
        this.city = "";
        this.currentTemp = 0.0;
        this.maxTemp = 0.0;
        this.minTemp = 0.0;
        this.description = "";
        this.humidity = 0;
        this.windSpeed = 0.0;
        this.windDegree = 0.0;
        this.cloudiness = 0;
    }

    public Weather(String city, double currentTemp, double maxTemp, double minTemp, String description,
                   int humidity, double windSpeed, double windDegree, int cloudiness) {
        this.city = city;
        this.currentTemp = currentTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.description = description;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDegree = windDegree;
        this.cloudiness = cloudiness;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(double currentTemp) {
        this.currentTemp = currentTemp;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(double windDegree) {
        this.windDegree = windDegree;
    }

    public int getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(int cloudiness) {
        this.cloudiness = cloudiness;
    }
}
