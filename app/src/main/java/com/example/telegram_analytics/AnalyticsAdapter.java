package com.example.telegram_analytics;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.telegram_analytics.entities.AnalyticsDTO;
import com.example.telegram_analytics.entities.TimePointData;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

public class AnalyticsAdapter extends ArrayAdapter<AnalyticsDTO> {

    Context context;
    int resource;
    final int LIGHT_BLUE = -16746548;
    AnalyticsDTO analyticsDTO;
    DataPoint[] messagesDataPoints;
    DataPoint[] reactionDataPoints;


    public AnalyticsAdapter(Context context, int resource, List<AnalyticsDTO> analyticsDTOs) {
        super(context, resource, analyticsDTOs);
        this.context = context;
        this.resource = resource;
    }

    private void processData() {
        List<TimePointData> timePointDataList = analyticsDTO.timePointDataList;
        reactionDataPoints = new DataPoint[timePointDataList.size() * 2];
        messagesDataPoints = new DataPoint[timePointDataList.size() * 2];
        long prevPointTime = timePointDataList.get(0).getUnixTimeInMs();

        analyticsDTO.tempNumberOfMessages = 0;
        analyticsDTO.tempReactionTime = 0;

        for (int i = 0; i < timePointDataList.size(); i++) {
            TimePointData timePointData = timePointDataList.get(i);
            int doubledIterator = i * 2;
            reactionDataPoints[doubledIterator + 1] = new DataPoint(timePointData.getUnixTimeInMs(), timePointData.getReactionTime());
            messagesDataPoints[doubledIterator + 1] = new DataPoint(timePointData.getUnixTimeInMs(), timePointData.getNumberOfMessages());

            reactionDataPoints[doubledIterator] = new DataPoint(prevPointTime, reactionDataPoints[doubledIterator + 1].getY());
            messagesDataPoints[doubledIterator] = new DataPoint(prevPointTime, messagesDataPoints[doubledIterator + 1].getY());

            analyticsDTO.tempNumberOfMessages += timePointData.getNumberOfMessages();
            analyticsDTO.tempReactionTime += timePointData.getReactionTime();

            prevPointTime = timePointData.getUnixTimeInMs();
        }

        analyticsDTO.tempReactionTime /= timePointDataList.size();

    }

    private void configureGraph(View convertView) {
        LineGraphSeries<DataPoint> reactionSeries = new LineGraphSeries<>(reactionDataPoints);
        LineGraphSeries<DataPoint> messageSeries = new LineGraphSeries<>(messagesDataPoints);
        List<TimePointData> timePointDataList = analyticsDTO.timePointDataList;
        GraphView graph = convertView.findViewById(R.id.graph);
        Viewport viewport = graph.getViewport();

        graph.removeAllSeries();
        graph.getSecondScale().removeAllSeries();

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4);

        viewport.setScrollable(true);
        viewport.setScalable(true);

        viewport.setMinX(timePointDataList.get(0).getUnixTimeInMs());
        viewport.setMaxX(timePointDataList.get(3).getUnixTimeInMs());
        viewport.setXAxisBoundsManual(true);

        viewport.setMinY(0);
        viewport.setMaxY(reactionSeries.getHighestValueY());
        viewport.setYAxisBoundsManual(true);

        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.addSeries(reactionSeries);

        graph.getSecondScale().addSeries(messageSeries);
        graph.getSecondScale().setMinY(0);
        graph.getSecondScale().setMaxY(messageSeries.getHighestValueY());

        messageSeries.setColor(Color.RED);
        graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);
        graph.getGridLabelRenderer().setVerticalLabelsColor(LIGHT_BLUE);

        reactionSeries.setTitle("reaction time");
        messageSeries.setTitle("number of messages");
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setTextSize(25);
        graph.getLegendRenderer().setVisible(true);
    }

    @NonNull
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        analyticsDTO = getItem(position);

        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(context);
            convertView = inflator.inflate(resource, parent, false);
        }

        if (analyticsDTO != null) {
            processData();
            configureGraph(convertView);

            TextView nameView = convertView.findViewById(R.id.name);
            nameView.setText(analyticsDTO.name);

            TextView mesNumberView = convertView.findViewById(R.id.numberOfMessages);
            mesNumberView.setText(Integer.toString(analyticsDTO.tempNumberOfMessages));

            TextView reactionTimeView = convertView.findViewById(R.id.reactionTime);
            reactionTimeView.setText(analyticsDTO.tempReactionTime + " s");

            TextView numberOfStartedConversesView = convertView.findViewById(R.id.numberOfStartedConverses);
            numberOfStartedConversesView.setText(Integer.toString(analyticsDTO.numberOfStartedConverses));

            TextView numberOfSymbols = convertView.findViewById(R.id.numberOfSymbols);
            numberOfSymbols.setText(String.valueOf(analyticsDTO.numberOfSymbols));
        }

        return convertView;
    }
}