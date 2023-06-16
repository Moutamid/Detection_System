package com.moutamid.controlsapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.controlsapp.Constants;
import com.moutamid.controlsapp.R;
import com.moutamid.controlsapp.databinding.ActivityStatsBinding;

import java.util.ArrayList;
import java.util.List;

public class StatsActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    ActivityStatsBinding binding;
    private LineChart chart, humidityChart;
    Typeface tfRegular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);
        Constants.showDialog();

        chart = binding.tempChart;
        humidityChart = binding.humidityChart;

        binding.humidityChart.setTouchEnabled(true);
        binding.humidityChart.setPinchZoom(true);

        binding.tempChart.setTouchEnabled(true);
        binding.tempChart.setPinchZoom(true);

        binding.back.setOnClickListener(v -> finish());

        tfRegular = Typeface.SANS_SERIF;

        getGas();
        getHumidity();
        getTemp();

        Constants.databaseReference().child(Constants.values).child(Constants.temperature)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    float max = Float.valueOf(snapshot.child("max").getValue(String.class));
                                    float min = Float.valueOf(snapshot.child("min").getValue(String.class));
                                    temperatureChart(max, min);
                                }
                                Constants.dismissDialog();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Constants.dismissDialog();
                                Toast.makeText(StatsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

        Constants.databaseReference().child(Constants.values).child(Constants.humidity)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    float max = Float.valueOf(snapshot.child("max").getValue(String.class));
                                    float min = Float.valueOf(snapshot.child("min").getValue(String.class));
                                   humidityChart(max, min);
                                }
                                Constants.dismissDialog();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Constants.dismissDialog();
                                Toast.makeText(StatsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

    }

    private void humidityChart(float max, float min) {
        humidityChart.getDescription().setEnabled(false);

        // enable touch gestures
        humidityChart.setTouchEnabled(true);

        humidityChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        humidityChart.setDragEnabled(true);
        humidityChart.setScaleEnabled(true);
        humidityChart.setVisibleXRangeMaximum(6);
        humidityChart.setDrawGridBackground(false);
        humidityChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        humidityChart.setPinchZoom(true);

        // set an alternative background color
        humidityChart.setBackgroundColor(getResources().getColor(R.color.back));

        humidityChart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = humidityChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(tfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        XAxis xAxis = humidityChart.getXAxis();
//        xAxis.setTypeface(tfLight);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(getResources().getColor(R.color.dark));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = humidityChart.getAxisLeft();
//        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(getResources().getColor(R.color.purple));
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(true);

        // YAxis rightAxis = chart.getAxisRight();
        //rightAxis.setEnabled(false);
////        rightAxis.setTypeface(tfLight);
//        rightAxis.setTextColor(Color.RED);
//        rightAxis.setAxisMaximum(900);
//        rightAxis.setAxisMinimum(-200);
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setDrawZeroLine(false);
//        rightAxis.setGranularityEnabled(false);

        setDataHumidity();
    }

    private void setDataHumidity() {
        ArrayList<Entry> values1 = new ArrayList<>();
        Constants.databaseReference().child(Constants.graph).child(Constants.humidity)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String temp = "";
                        int ind=0;
                        values1.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            String name = child.getKey();
                            temp = String.valueOf(snapshot.child(name).child("temp").getValue(Integer.class));
                            values1.add(new Entry(ind, Float.parseFloat(temp)));
                            ind=ind+1;

                        }

                        LineDataSet set1;

                        if (humidityChart.getData() != null && humidityChart.getData().getDataSetCount() > 0) {
                            set1 = (LineDataSet) humidityChart.getData().getDataSetByIndex(0);
                            set1.setValues(values1);

                            humidityChart.getData().notifyDataChanged();
                            humidityChart.notifyDataSetChanged();
                            humidityChart.invalidate();
                        } else {
                            // create a dataset and give it a type
                            set1 = new LineDataSet(values1, "Humidity Dataset");
                            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                            set1.setColor(getResources().getColor(R.color.purple));
                            set1.setCircleColor(getResources().getColor(R.color.light));
                            set1.setLineWidth(2f);
                            set1.setCircleRadius(3f);
                            set1.setFillAlpha(65);
                            set1.setHighlightEnabled(true);
                            set1.setFillColor(ColorTemplate.getHoloBlue());
                            set1.setHighLightColor(Color.rgb(244, 117, 117));
                            set1.setDrawCircleHole(false);
                            set1.setDrawFilled(true);
                            // create a data object with the data sets
                            LineData data = new LineData(set1);
                            data.setValueTextColor(getResources().getColor(R.color.dark));
                            data.setValueTextSize(9f);

                            // set data
                            humidityChart.setData(data);

                            List<ILineDataSet> sets = humidityChart.getData()
                                    .getDataSets();

                            for (ILineDataSet iSet : sets) {

                                LineDataSet set = (LineDataSet) iSet;
                                set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
                                        ? LineDataSet.Mode.LINEAR
                                        :  LineDataSet.Mode.CUBIC_BEZIER);
                            }
                            humidityChart.invalidate();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void temperatureChart(float max, float min) {
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);
        chart.setVisibleXRangeMaximum(6);
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(getResources().getColor(R.color.back));

        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(tfLight);
        l.setTextSize(11f);
        l.setTextColor(getResources().getColor(R.color.dark));
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        XAxis xAxis = chart.getXAxis();
//        xAxis.setTypeface(tfLight);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(getResources().getColor(R.color.dark));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(getResources().getColor(R.color.purple));
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(true);

       // YAxis rightAxis = chart.getAxisRight();
        //rightAxis.setEnabled(false);
////        rightAxis.setTypeface(tfLight);
//        rightAxis.setTextColor(Color.RED);
//        rightAxis.setAxisMaximum(900);
//        rightAxis.setAxisMinimum(-200);
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setDrawZeroLine(false);
//        rightAxis.setGranularityEnabled(false);

       setDataTemp();
    }

    private void setDataTemp() {

        ArrayList<Entry> values1 = new ArrayList<>();
        Constants.databaseReference().child(Constants.graph).child(Constants.temperature)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String temp = "";
                int ind=0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    String name = child.getKey();
                    temp = String.valueOf(snapshot.child(name).child("temp").getValue(Integer.class));
                    values1.add(new Entry(ind, Float.parseFloat(temp)));
                    ind=ind+1;

                }

                LineDataSet set1;

                if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
                    set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
                    set1.setValues(values1);

                    chart.getData().notifyDataChanged();
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                } else {
                    // create a dataset and give it a type
                    set1 = new LineDataSet(values1, "Temperature Dataset");
                    set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set1.setColor(getResources().getColor(R.color.purple));
                    set1.setCircleColor(getResources().getColor(R.color.light));
                    set1.setLineWidth(2f);
                    set1.setCircleRadius(3f);
                    set1.setFillAlpha(65);
                    set1.setHighlightEnabled(true);
                    set1.setFillColor(ColorTemplate.getHoloBlue());
                    set1.setHighLightColor(Color.rgb(244, 117, 117));
                    set1.setDrawCircleHole(false);
                    set1.setDrawFilled(true);
                    // create a data object with the data sets
                    LineData data = new LineData(set1);
                    data.setValueTextColor(getResources().getColor(R.color.dark));
                    data.setValueTextSize(9f);

                    // set data
                    chart.setData(data);

                    List<ILineDataSet> sets = chart.getData()
                            .getDataSets();

                    for (ILineDataSet iSet : sets) {

                        LineDataSet set = (LineDataSet) iSet;
                        set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
                                ? LineDataSet.Mode.LINEAR
                                :  LineDataSet.Mode.CUBIC_BEZIER);
                    }
                    chart.invalidate();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTemp() {
        Constants.databaseReference().child(Constants.temperature).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int tem = snapshot.getValue(Integer.class);
                        binding.temp.setText("Temperature : " + tem + "\u00B0 C");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    private void getHumidity() {
        Constants.databaseReference().child(Constants.humidity).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double humi = snapshot.getValue(Double.class);
                        binding.humidity.setText("Humidity : " + String.format("%.2f", humi) + "%");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    private void getGas() {
        Constants.databaseReference().child(Constants.gasLeakage).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean gas = snapshot.getValue(Boolean.class);
                        if (gas) {
                            binding.gas.setText("Gas Leakage : ON");
                        } else {
                            binding.gas.setText("Gas Leakage : OFF");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}