package edu.urv.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder> {

    private final List<HourlyWeatherData> hourlyData;

    public HourlyForecastAdapter(List<HourlyWeatherData> hourlyData) {
        this.hourlyData = hourlyData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_forecast_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyWeatherData data = hourlyData.get(position);
        holder.timeTextView.setText(data.getTime());
        holder.descriptionTextView.setText(data.getDescription());
        holder.temperatureTextView.setText(data.getTemperature());
    }

    @Override
    public int getItemCount() {
        return hourlyData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView timeTextView;
        final TextView descriptionTextView;
        final TextView temperatureTextView;

        public ViewHolder(View view) {
            super(view);
            timeTextView = view.findViewById(R.id.time);
            descriptionTextView = view.findViewById(R.id.description);
            temperatureTextView = view.findViewById(R.id.temperature);
        }
    }
}
