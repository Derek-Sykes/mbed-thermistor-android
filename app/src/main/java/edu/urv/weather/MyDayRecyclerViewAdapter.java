package edu.urv.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyDayRecyclerViewAdapter extends RecyclerView.Adapter<MyDayRecyclerViewAdapter.ViewHolder> {

    private List<WeatherData> weatherDataList;

    public MyDayRecyclerViewAdapter(List<WeatherData> weatherDataList) {
        this.weatherDataList = weatherDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherData data = weatherDataList.get(position);
        holder.dateTextView.setText(data.getDate());
        holder.descriptionTextView.setText(data.getDescription());
        holder.temperatureTextView.setText(data.getTemperature());
    }

    @Override
    public int getItemCount() {
        return weatherDataList.size();
    }
    public void updateData(List<WeatherData> newData) {
        this.weatherDataList = newData;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView dateTextView;
        public final TextView descriptionTextView;
        public final TextView temperatureTextView;

        public ViewHolder(View view) {
            super(view);
            dateTextView = view.findViewById(R.id.day_date);
            descriptionTextView = view.findViewById(R.id.weather_description);
            temperatureTextView = view.findViewById(R.id.temperature);
        }
    }
}
