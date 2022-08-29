/**
 * Assignment #: InClass08
 * File Name: Group25_InClass08 WeatherForecastFragment.java
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherForecastFragment extends Fragment {

    private static final String ARG_PARAM_CITY = "ARG_PARAM_CITY";

    private final OkHttpClient client = new OkHttpClient();
    private DataService.City mCity;

    public WeatherForecastFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param city Parameter 1.
     * @return A new instance of fragment WeatherForcastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherForecastFragment newInstance(DataService.City city) {
        WeatherForecastFragment fragment = new WeatherForecastFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_forecast, container, false);
    }

    ListView listView;
    ArrayList<Forecast> forecasts;
    ForecastListAdapter adapter;

    double latitude;
    double longitude;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Weather Forecast");

        TextView cityNameText = view.findViewById(R.id.forecast_cityName);
        cityNameText.setText(mCity.toString());

        forecasts = new ArrayList<>();

        getForecasts();

        listView = view.findViewById(R.id.listView);
        adapter = new ForecastListAdapter(getActivity(), R.layout.forecast_list_item, forecasts);
        listView.setAdapter(adapter);
    }

    class ForecastListAdapter extends ArrayAdapter<Forecast> {

        public ForecastListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Forecast> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.forecast_list_item, parent, false);
            }

            Forecast forecast = getItem(position);

            TextView timeText = convertView.findViewById(R.id.forecastItem_time);
            TextView tempText = convertView.findViewById(R.id.forecastItem_currentTemp);
            TextView maxTempText = convertView.findViewById(R.id.forecastItem_maxTemp);
            TextView minTempText = convertView.findViewById(R.id.forecastItem_minTemp);
            TextView humidityText = convertView.findViewById(R.id.forecastItem_humidity);
            TextView descriptionText = convertView.findViewById(R.id.forecastItem_description);
            ImageView weatherIcon = convertView.findViewById(R.id.forecastItem_weatherIcon);

            String imgURL = "https://openweathermap.org/img/wn/" + forecast.getWeatherIcon() + ".png";

            timeText.setText(forecast.getDateTime());
            tempText.setText(String.format("%.1fF", forecast.getTemperature()));
            maxTempText.setText(String.format("%.1fF", forecast.getMaxTemp()));
            minTempText.setText(String.format("%.1fF", forecast.getMinTemp()));
            humidityText.setText(String.valueOf(forecast.getHumidity()) + "%");

            String description = forecast.getDescription();
            String[] descriptionUnformatted = description.split(" ");
            for(int i = 0; i < descriptionUnformatted.length; i++){
                descriptionUnformatted[i] = descriptionUnformatted[i].substring(0,1).toUpperCase() + descriptionUnformatted[i].substring(1).toLowerCase();
            }
            description = TextUtils.join(" ", descriptionUnformatted);
            descriptionText.setText(description);

            Picasso.get().load(imgURL).resize(50,50).into(weatherIcon);

            return convertView;
        }
    }

    void getForecasts() {
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

                        HttpUrl url = HttpUrl.parse("https://api.openweathermap.org/data/2.5/forecast").newBuilder()
                                .addQueryParameter("lat", String.valueOf(latitude))
                                .addQueryParameter("lon", String.valueOf(longitude))
                                .addQueryParameter("appid", "30b80bfe6e05fbee291d62c4bc25e668")
                                .addQueryParameter("units", "Imperial")
                                .build();

                        Request request = new Request.Builder()
                                .url(url)
                                .build();

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
                                        JSONArray forecastArray = jsonObject.getJSONArray("list");

                                        for(int i = 0; i < forecastArray.length(); i++){
                                            JSONObject forecastArrayObject = forecastArray.getJSONObject(i);
                                            Forecast forecast = new Forecast();
                                            JSONObject forecastTempInfoObject = forecastArrayObject.getJSONObject("main");
                                            JSONArray forecastWeatherArray = forecastArrayObject.getJSONArray("weather");
                                            JSONObject forecastWeatherInfoObject = forecastWeatherArray.getJSONObject(0);

                                            forecast.setDateTime(forecastArrayObject.getString("dt_txt"));
                                            forecast.setTemperature(forecastTempInfoObject.getDouble("temp"));
                                            forecast.setMaxTemp(forecastTempInfoObject.getDouble("temp_max"));
                                            forecast.setMinTemp(forecastTempInfoObject.getDouble("temp_min"));
                                            forecast.setHumidity(forecastTempInfoObject.getInt("humidity"));
                                            forecast.setDescription(forecastWeatherInfoObject.getString("description"));
                                            forecast.setWeatherIcon(forecastWeatherInfoObject.getString("icon"));

                                            forecasts.add(forecast);
                                        }

                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                            }
                                        });

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

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
}