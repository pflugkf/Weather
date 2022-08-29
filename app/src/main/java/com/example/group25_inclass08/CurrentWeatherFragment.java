/**
 * Assignment #: InClass08
 * File Name: Group25_InClass08 CurrentWeatherFragment.java
 * Full Name: Kristin Pflug
 */

package com.example.group25_inclass08;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.group25_inclass08.databinding.FragmentCurrentWeatherBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class CurrentWeatherFragment extends Fragment {
    private static final String ARG_PARAM_CITY = "ARG_PARAM_CITY";

    private final OkHttpClient client = new OkHttpClient();
    private DataService.City mCity;
    FragmentCurrentWeatherBinding binding;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    public static CurrentWeatherFragment newInstance(DataService.City city) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = (DataService.City) getArguments().getSerializable(ARG_PARAM_CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    TextView cityNameText;
    TextView currentTempText;
    TextView maxTempText;
    TextView minTempText;
    TextView descriptionText;
    TextView humidityText;
    TextView windSpeedText;
    TextView windDegreeText;
    TextView cloudinessText;
    ImageView weatherIcon;
    Button checkForecast;

    String iconCode;
    double latitude;
    double longitude;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Current Weather");

        cityNameText = view.findViewById(R.id.current_cityName);
        currentTempText = view.findViewById(R.id.current_temperature);
        maxTempText = view.findViewById(R.id.current_temperatureMax);
        minTempText = view.findViewById(R.id.current_temperatureMin);
        descriptionText = view.findViewById(R.id.current_description);
        humidityText = view.findViewById(R.id.current_humidity);
        windSpeedText = view.findViewById(R.id.current_windSpeed);
        windDegreeText = view.findViewById(R.id.current_windDegree);
        cloudinessText = view.findViewById((R.id.current_cloudiness));

        weatherIcon = view.findViewById(R.id.current_weatherIcon);

        checkForecast = view.findViewById(R.id.current_checkForecastButton);

        getCurrentWeather();

        checkForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToWeatherForecast(mCity);
            }
        });
    }

    void getCurrentWeather() {
        String cityName;
        if(!mCity.getCountry().equals("UK")) {
            cityName = mCity.toString();
        } else {
            cityName = mCity.getCity() + ",GB";
        }

        HttpUrl geoUrl = HttpUrl.parse("https://api.openweathermap.org/geo/1.0/direct").newBuilder()
                .addQueryParameter("q", cityName)
                .addQueryParameter("appid", "30b80bfe6e05fbee291d62c4bc25e668")
                .build();

        Request geoRequest = new Request.Builder()
                .url(geoUrl)
                .build();

        client.newCall(geoRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JSONArray citiesList = new JSONArray(response.body().string());
                        JSONObject cityJSONObject = citiesList.getJSONObject(0);
                        latitude = cityJSONObject.getDouble("lat");
                        longitude = cityJSONObject.getDouble("lon");

                        HttpUrl url = HttpUrl.parse("https://api.openweathermap.org/data/2.5/weather").newBuilder()
                                .addQueryParameter("lat", String.valueOf(latitude))
                                .addQueryParameter("lon", String.valueOf(longitude))
                                .addQueryParameter("appid", "30b80bfe6e05fbee291d62c4bc25e668")
                                .addQueryParameter("units", "Imperial")
                                .build();

                        Request request = new Request.Builder()
                                .url(url)
                                .build();

                        Weather weather = new Weather();
                        weather.setCity(mCity.toString());

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if(response.isSuccessful()) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().string());
                                        JSONArray weatherInfoArray = jsonObject.getJSONArray("weather");
                                        JSONObject primaryWeatherInfoObject = weatherInfoArray.getJSONObject(0);
                                        JSONObject tempInfoObject = jsonObject.getJSONObject("main");
                                        JSONObject windInfoObject = jsonObject.getJSONObject("wind");
                                        JSONObject cloudInfoObject = jsonObject.getJSONObject("clouds");

                                        weather.setCurrentTemp(tempInfoObject.getDouble("temp"));
                                        weather.setMinTemp(tempInfoObject.getDouble("temp_min"));
                                        weather.setMaxTemp(tempInfoObject.getDouble("temp_max"));

                                        String description = primaryWeatherInfoObject.getString("description");
                                        String[] descriptionUnformatted = description.split(" ");
                                        for(int i = 0; i < descriptionUnformatted.length; i++){
                                            descriptionUnformatted[i] = descriptionUnformatted[i].substring(0,1).toUpperCase() + descriptionUnformatted[i].substring(1).toLowerCase();
                                        }
                                        description = TextUtils.join(" ", descriptionUnformatted);
                                        weather.setDescription(description);

                                        weather.setHumidity(tempInfoObject.getInt("humidity"));
                                        weather.setWindSpeed(windInfoObject.getDouble("speed"));
                                        weather.setWindDegree(windInfoObject.getInt("deg"));
                                        weather.setCloudiness(cloudInfoObject.getInt("all"));

                                        iconCode = primaryWeatherInfoObject.getString("icon");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    String imgURL = "https://openweathermap.org/img/wn/" + iconCode + ".png";

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            cityNameText.setText(weather.getCity());
                                            currentTempText.setText(String.format("%.1f F", weather.getCurrentTemp()));
                                            maxTempText.setText(String.format("%.1f F", weather.getMaxTemp()));
                                            minTempText.setText(String.format("%.1f F", weather.getMinTemp()));
                                            descriptionText.setText(weather.getDescription());
                                            humidityText.setText(String.valueOf(weather.getHumidity()) + "%");
                                            windSpeedText.setText(String.valueOf(weather.getWindSpeed()) + " miles/hr");
                                            windDegreeText.setText(String.format("%.1f degrees", weather.getWindDegree()));
                                            cloudinessText.setText(String.valueOf(weather.getCloudiness()) + "%");
                                            Picasso.get().load(imgURL).resize(50,50).into(weatherIcon);
                                        }
                                    });
                                } else {
                                    ResponseBody responseBody = response.body();
                                    String body = responseBody.string();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    CurrentWeatherFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CurrentWeatherFragmentListener) context;
    }

    interface CurrentWeatherFragmentListener {
        void goToWeatherForecast(DataService.City city);
    }
}