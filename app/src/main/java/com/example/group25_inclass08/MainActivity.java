/**
 * Assignment #: InClass08
 * File Name: Group25_InClass08 MainActivity.java
 * Full Name: Kristin Pflug
 */

package com.example.group25_inclass08;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements CitiesFragment.CitiesFragmentListener, CurrentWeatherFragment.CurrentWeatherFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new CitiesFragment())
                .commit();
    }

    @Override
    public void gotoCurrentWeather(DataService.City city) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, CurrentWeatherFragment.newInstance(city))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToWeatherForecast(DataService.City city) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, WeatherForecastFragment.newInstance(city))
                .addToBackStack(null)
                .commit();
    }
}