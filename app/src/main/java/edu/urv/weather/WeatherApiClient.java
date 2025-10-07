package edu.urv.weather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.URL;
import java.net.HttpURLConnection;

import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import org.json.JSONObject;
import org.json.JSONArray;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Locale;



public class WeatherApiClient {


    public interface WeatherApiCallback {
        void onWeatherDataFetched(List<WeatherData> result, String location);
        void onHourlyWeatherDataFetched(List<HourlyWeatherData> hourlyWeatherData); // For hourly forecasts
        void onError(String errorMessage);
        void onWeatherDataFetched(String errorMessage); // Optional: for error handling
    }

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void fetchWeatherData(String city, final WeatherApiCallback callback) {
        executor.submit(new Runnable() {
            String apiKey = "a3cd313a283ae987be7366270e1fbd15";
            String urlString = "https://api.openweathermap.org/data/2.5/forecast/daily?q=" + city + "&cnt=16&units=metric&appid=" + apiKey;

            @Override
            public void run() {
                try {
                    // Replace with your actual API endpoint
                    URL url = new URL(urlString);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Read the input stream and build response string
                        InputStream inputStream = urlConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder responseBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }

                        // Convert the response to a string
                        final String result = responseBuilder.toString();

                        // Parse the JSON response (Assuming you know the structure of the JSON)
                        JSONObject jsonResponse = new JSONObject(result);
                        Log.i("TAG", String.valueOf(jsonResponse));
                        final String location = jsonResponse.getJSONObject("city").getString("name");
                        JSONArray weatherArray = jsonResponse.getJSONArray("list");

                        // Create a list to hold WeatherData objects
                        List<WeatherData> weatherDataList = new ArrayList<>();

                        // Iterate over the list to get weather data for each day

                        for (int i = 0; i < weatherArray.length(); i++) {
                            JSONObject weatherData = weatherArray.getJSONObject(i);

                            // Get the date (convert from timestamp)
                            long timestamp = weatherData.getLong("dt");
                            Date date = new Date(timestamp * 1000); // Convert seconds to milliseconds
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String dateStr = dateFormat.format(date);

                            // Get the temperature (use "day" temperature)
                            JSONObject tempData = weatherData.getJSONObject("temp");
                            String temperature = tempData.getString("day");

                            // Get the weather description
                            JSONArray weatherDetails = weatherData.getJSONArray("weather");
                            String description = weatherDetails.getJSONObject(0).getString("description");

                            // Create a WeatherData object for this day
                            WeatherData data = new WeatherData(dateStr, description, temperature);

                            // Add to the list
                            weatherDataList.add(data);
                            }
                        // Call back on the main thread with the list of WeatherData objects
                        final List<WeatherData> finalWeatherDataList = weatherDataList;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onWeatherDataFetched(finalWeatherDataList, location);
                            }
                        });
                    } else {
                        final String result = "Failed to fetch weather data";
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onWeatherDataFetched(result);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    final String result = "Error occurred: " + e.getMessage();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onWeatherDataFetched(result);
                        }
                    });
                }
            }
        });
    }

    public static void fetchHourlyWeatherData(String city, WeatherApiCallback callback) {
        executor.submit(() -> {
            String apiKey = "a3cd313a283ae987be7366270e1fbd15";
            String urlString = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&units=metric&appid=" + apiKey;

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    JSONObject jsonResponse = new JSONObject(result.toString());
                    JSONArray list = jsonResponse.getJSONArray("list");

                    List<HourlyWeatherData> hourlyForecast = new ArrayList<>();
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject weatherData = list.getJSONObject(i);

                        // Extract relevant data
                        long timestamp = weatherData.getLong("dt");
                        Date date = new Date(timestamp * 1000);
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        String time = sdf.format(date);

                        String temp = weatherData.getJSONObject("main").getString("temp");
                        String description = weatherData.getJSONArray("weather").getJSONObject(0).getString("description");

                        // Add data to the list
                        hourlyForecast.add(new HourlyWeatherData(time, description, temp + "°C"));
                    }

                    new Handler(Looper.getMainLooper()).post(() -> callback.onHourlyWeatherDataFetched(hourlyForecast));
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onError("Error fetching weather data."));
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> callback.onError("An error occurred: " + e.getMessage()));
            }
        });
    }



}
