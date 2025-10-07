package edu.urv.weather;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import java.util.Locale;

/**
 * A fragment representing a list of Items.
 */
public class DayFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DayFragment() {
    }
    public interface LocationCallback {
        void onLocationFetched(String location);
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DayFragment newInstance(int columnCount) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }



    private RecyclerView recyclerView;
    private MyDayRecyclerViewAdapter adapter;
    private List<WeatherData> weatherDataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment (fragment_day_list.xml)
        View rootView = inflater.inflate(R.layout.fragment_day_list, container, false);

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Fetch weather data
        fetchWeatherData();

        return rootView;
    }

    String defaultCity = "Tarragona"; // Default city to fetch data for
    private void fetchWeatherData() {
        // Call the WeatherApiClient to fetch weather data
        WeatherApiClient.fetchWeatherData(defaultCity, new WeatherApiClient.WeatherApiCallback() {
            @Override
            public void onWeatherDataFetched(final List<WeatherData> result, final String location) {
                // Update the UI with the fetched weather data on the main thread
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        if (recyclerView != null) {
                            adapter = new MyDayRecyclerViewAdapter(result);
                            recyclerView.setAdapter(adapter);
                        }
                        // Pass the location to the MainActivity
                        // Pass the location to MainActivity via a callback or directly update UI
                        if (getActivity() instanceof MainActivity) {
                            Log.i("TAg", "Updating location");
                            ((MainActivity) getActivity()).updateLocation(location);
                            Log.i("TAg", location);
                        }
                    }
                });
            }

            @Override
            public void onWeatherDataFetched(String errorMessage) {
                // Handle error case
            }

            @Override
            public void onHourlyWeatherDataFetched(List<HourlyWeatherData> hourlyWeatherData) {
                // Handle hourly weather data (optional in this fragment)
                // You can choose to leave this empty or log the result if needed.
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error case
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    public void updateWeatherData(List<WeatherData> weatherDataList) {
        if (recyclerView != null && adapter != null) {
            adapter.updateData(weatherDataList); // Add an `updateData` method in your adapter
        }
    }

    private String getFormattedDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM d", Locale.getDefault());
        return sdf.format(new Date(timestamp * 1000)); // Multiply by 1000 to convert seconds to milliseconds
    }

}