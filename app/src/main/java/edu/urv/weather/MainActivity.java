package edu.urv.weather;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView temperatureTextView;
    private Handler handler;
    private Runnable updateTemperatureRunnable;
    private RecyclerView hourlyRecyclerView;
    private static final int PERMISSION_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText newCityEdit = findViewById(R.id.newCityEdit);
        Button searchCityBtn = findViewById(R.id.searchCityBtn);
// Initialize the RecyclerView
        hourlyRecyclerView = findViewById(R.id.recycler_view_hourly);

        // Set the LayoutManager
        hourlyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Optionally, add an adapter (will implement later)
        hourlyRecyclerView.setAdapter(new HourlyForecastAdapter(new ArrayList<>()));
        String defaultCity = "Tarragona";
        WeatherApiClient.fetchHourlyWeatherData(defaultCity, new WeatherApiClient.WeatherApiCallback() {
            public void onWeatherDataFetched(List<WeatherData> result, String location) {
                // nothing
            }

            @Override
            public void onWeatherDataFetched(String errorMessage) {
                // Handle the error
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onHourlyWeatherDataFetched(List<HourlyWeatherData> hourlyWeatherData) {
                Log.i("HourlyWeather", "Data received: " + hourlyWeatherData.size());
                HourlyForecastAdapter hourlyAdapter = new HourlyForecastAdapter(hourlyWeatherData);
                hourlyRecyclerView.setAdapter(hourlyAdapter);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }

        });
        searchCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = newCityEdit.getText().toString().trim();

                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                    return;
                }

                WeatherApiClient.fetchHourlyWeatherData(city, new WeatherApiClient.WeatherApiCallback() {
                    public void onWeatherDataFetched(List<WeatherData> result, String location) {
                        // nothing
                    }

                    @Override
                    public void onWeatherDataFetched(String errorMessage) {
                        // Handle the error
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onHourlyWeatherDataFetched(List<HourlyWeatherData> hourlyWeatherData) {
                        Log.i("HourlyWeather", "Data received: " + hourlyWeatherData.size());
                        HourlyForecastAdapter hourlyAdapter = new HourlyForecastAdapter(hourlyWeatherData);
                        hourlyRecyclerView.setAdapter(hourlyAdapter);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                });
                // Fetch weather data for the entered city
                WeatherApiClient.fetchWeatherData(city, new WeatherApiClient.WeatherApiCallback() {
                    public void onWeatherDataFetched(List<WeatherData> result, String location) {
                        // Handle the successful result
                        updateLocation(location);

                        // Pass data to your fragment or update the UI
                        Fragment dayFragment = getSupportFragmentManager().findFragmentById(R.id.day_fragment);
                        if (dayFragment instanceof DayFragment) {
                            ((DayFragment) dayFragment).updateWeatherData(result);
                        }
                    }

                    @Override
                    public void onWeatherDataFetched(String errorMessage) {
                        // Handle the error
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onHourlyWeatherDataFetched(List<HourlyWeatherData> hourlyWeatherData) {
                        // nothing
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                });

            }
        });
        if (!hasBluetoothPermissions()) {
            requestBluetoothPermissions();
        } else {
            startBluetoothOperations();
        }
    }
    private void startBluetoothOperations() {
        // Initialize and use BluetoothHandler here
        TextView temperatureTextView = findViewById(R.id.temperature);
        BluetoothHandler bluetoothHandler;
        bluetoothHandler = new BluetoothHandler(this, temperatureTextView);
        String deviceAddress = "98:D3:31:F7:68:0A";
        bluetoothHandler.connectToDevice(deviceAddress);
        bluetoothHandler.receiveData();
    }



    public void updateLocation(String location) {
        TextView locationTextView = findViewById(R.id.locationTextView);
        locationTextView.setText(location);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop updating the temperature when the activity is destroyed
        handler.removeCallbacks(updateTemperatureRunnable);
    }

    // Method to simulate generating a random temperature
    private double generateRandomTemperature() {
        Random random = new Random();
        return 18.5 + (random.nextDouble() * 0.2); // Simulate a temperature between 20.0 and 30.0°C
    }
    private boolean hasBluetoothPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestBluetoothPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[] {
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN
                },
                PERMISSION_REQUEST_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startBluetoothOperations();
            } else {
                // Handle the case where permissions are not granted
                System.out.println("Bluetooth permissions not granted.");
            }
        }
    }
}


