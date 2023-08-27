package com.example.telegram_analytics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.telegram_analytics.entities.AnalyticsDTO;

import java.util.List;

public class AnalyticsAdapter extends ArrayAdapter<AnalyticsDTO> {

    Context context;
    int resource;


    public AnalyticsAdapter(Context context, int resource, List<AnalyticsDTO> analyticsDTOs) {
        super(context, resource, analyticsDTOs);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        AnalyticsDTO analyticsDTO = getItem(position);

        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(context);
            convertView = inflator.inflate(resource, parent, false);
        }

        if (analyticsDTO != null) {
            TextView nameView = convertView.findViewById(R.id.name);
            nameView.setText(analyticsDTO.name);

            TextView mesNumberView = convertView.findViewById(R.id.numberOfMessages);
            mesNumberView.setText(Integer.toString(analyticsDTO.numberOfMessages));

            TextView reactionTimeView = convertView.findViewById(R.id.reactionTime);
            reactionTimeView.setText(analyticsDTO.reactionTime + " s");
        }

        return convertView;
    }
}