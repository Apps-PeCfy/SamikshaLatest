package com.samiksha.ui.progressReport.progressreportweekly;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.samiksha.R;
import com.samiksha.networking.ApiClient;
import com.samiksha.ui.home.main.TrainingCalenderModel;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgressReportWeekly extends AppCompatActivity {

    BarChart chart1;
    BarChart chart2;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;
    List<Integer> entries = new ArrayList<>();
    List<Integer> entries2 = new ArrayList<>();
    List<Integer> entries3 = new ArrayList<>();
    List<String> labels = new ArrayList<>();
    ArrayList<TrainingCalenderModel> calenderList = new ArrayList<>();

    String token, mentalSkillID, FromDate, toDate, userID;
    Boolean isAdded = true;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_report_weekly);

        chart1 = findViewById(R.id.BarChart1);
        chart2 = findViewById(R.id.BarChart2);
        toolbar = findViewById(R.id.weeklytoolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ArrayList<TrainingCalenderModel> myList = (ArrayList<TrainingCalenderModel>) getIntent().getSerializableExtra("calenderList");
        for (int l = 0; l <= 6; l++) {
            labels.add(myList.get(l).getDayDate() + " " + myList.get(l).getMonth());

        }

        token = getIntent().getStringExtra("token");
        mentalSkillID = getIntent().getStringExtra("mentalSkillID");
        FromDate = getIntent().getStringExtra("FromDate");
        toDate = getIntent().getStringExtra("toDate");
        userID = getIntent().getStringExtra("UserID");


        String token1 = "Bearer " + token;

        if (userID != null) {

            Call<WeeklyReportResponsePOJO> call = ApiClient.getClient().weeklyReportWithID(token1, mentalSkillID, FromDate, toDate, userID);

            call.enqueue(new Callback<WeeklyReportResponsePOJO>() {
                @Override
                public void onResponse(Call<WeeklyReportResponsePOJO> call,
                                       Response<WeeklyReportResponsePOJO> response) {


                    int trainingSize = response.body().getData().getTraining().size();
                    for (int l = 0; l <= labels.size() - 1; l++) {

                        isAdded = false;
                        String dateStr = null;


                        for (int i = 0; i < trainingSize; i++) {

                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = dateFormat.parse(response.body().getData().getTraining().get(i).getDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            DateFormat formatter = new SimpleDateFormat("dd MMM");
                            dateStr = formatter.format(date);


                            if (labels.get(l).equals(dateStr)) {
                                if (response.body().getData().getTraining().get(i).getRank().equals("2")) {
                                    isAdded = true;
                                    entries2.add(l, Integer.valueOf((response.body().getData().getTraining().get(i).getAverage())));

                                }
                                if(trainingSize>(i+1)){
                                    String dateStr1 = null;

                                    DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date1 = null;
                                    try {
                                        date1 = dateFormat1.parse(response.body().getData().getTraining().get(i+1).getDate());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    DateFormat formatter1 = new SimpleDateFormat("dd MMM");
                                    dateStr1 = formatter1.format(date1);

                                    if (labels.get(l).equals(dateStr1)) {
                                        if (response.body().getData().getTraining().get(i + 1).getRank().equals("3")) {

                                            isAdded = true;
                                            entries3.add(l, Integer.valueOf((response.body().getData().getTraining().get(i + 1).getAverage())));

                                        } else {
                                            entries3.add(l, 0);
                                        }
                                    }else {
                                        entries3.add(l, 0);
                                    }

                                }else {
                                    entries3.add(l, 0);
                                }
                                break;


                            }

                        }

                        if (!isAdded) {
                            if (trainingSize > 1) {
                                entries2.add(l, 0);
                                entries3.add(l, 0);

                            } else {
                                entries2.add(l, 0);
                                entries3.add(0, 0);

                            }

                        }

                    }

                /*int sportPerformanceSize = response.body().getData().getTraining().getSportPerformance().size();
                for (int l = 0; l <= 6; l++) {

                    String dateStr = null;


                    for (int i = 0; i < sportPerformanceSize; i++) {

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
                        Date date = null;
                        try {
                            date = dateFormat.parse(response.body().getData().getTraining().getSportPerformance().get(i).getCreatedDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat formatter = new SimpleDateFormat("dd MMM");
                        dateStr= formatter.format(date);



                        if (labels.get(l).equals(dateStr)) {
                            entries3.add(response.body().getData().getTraining().getSportPerformance().get(i).getAverage());

                        }else {
                            entries3.add(0);

                        }

                    }






                }
*/

                    create_graph2(labels, entries2);
                    create_graph3(labels, entries3);


                }

                @Override
                public void onFailure(@NotNull Call<WeeklyReportResponsePOJO> call, @NotNull Throwable t) {
                }
            });


        } else {
            Call<WeeklyReportResponsePOJO> call = ApiClient.getClient().weeklyReport(token1, mentalSkillID, FromDate, toDate);

            call.enqueue(new Callback<WeeklyReportResponsePOJO>() {
                @Override
                public void onResponse(Call<WeeklyReportResponsePOJO> call,
                                       Response<WeeklyReportResponsePOJO> response) {


                    int trainingSize = response.body().getData().getTraining().size();
                    for (int l = 0; l <= labels.size() - 1; l++) {

                        isAdded = false;
                        String dateStr = null;


                        for (int i = 0; i < trainingSize; i++) {

                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = dateFormat.parse(response.body().getData().getTraining().get(i).getDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            DateFormat formatter = new SimpleDateFormat("dd MMM");
                            dateStr = formatter.format(date);


                            if (labels.get(l).equals(dateStr)) {
                                if (response.body().getData().getTraining().get(i).getRank().equals("2")) {
                                    isAdded = true;
                                    entries2.add(l, Integer.valueOf((response.body().getData().getTraining().get(i).getAverage())));

                                }




                                if(trainingSize>(i+1)){
                                    String dateStr1 = null;

                                    DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date1 = null;
                                    try {
                                        date1 = dateFormat1.parse(response.body().getData().getTraining().get(i+1).getDate());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    DateFormat formatter1 = new SimpleDateFormat("dd MMM");
                                    dateStr1 = formatter1.format(date1);

                                    if (labels.get(l).equals(dateStr1)) {
                                        if (response.body().getData().getTraining().get(i + 1).getRank().equals("3")) {

                                            isAdded = true;
                                            entries3.add(l, Integer.valueOf((response.body().getData().getTraining().get(i + 1).getAverage())));

                                        } else {
                                            entries3.add(l, 0);
                                        }
                                    }else {
                                        entries3.add(l, 0);
                                    }

                                }else {
                                    entries3.add(l, 0);
                                }




                                break;

                            }

                        }

                        if (!isAdded) {
                            if (trainingSize > 1) {
                                entries2.add(l, 0);
                                entries3.add(l, 0);

                            } else {
                                entries2.add(l, 0);
                                entries3.add(0, 0);

                            }

                        }


                    }

                /*int sportPerformanceSize = response.body().getData().getTraining().getSportPerformance().size();
                for (int l = 0; l <= 6; l++) {

                    String dateStr = null;


                    for (int i = 0; i < sportPerformanceSize; i++) {

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
                        Date date = null;
                        try {
                            date = dateFormat.parse(response.body().getData().getTraining().getSportPerformance().get(i).getCreatedDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat formatter = new SimpleDateFormat("dd MMM");
                        dateStr= formatter.format(date);



                        if (labels.get(l).equals(dateStr)) {
                            entries3.add(response.body().getData().getTraining().getSportPerformance().get(i).getAverage());

                        }else {
                            entries3.add(0);

                        }

                    }






                }
*/

                    create_graph2(labels, entries2);
                    create_graph3(labels, entries3);


                }

                @Override
                public void onFailure(@NotNull Call<WeeklyReportResponsePOJO> call, @NotNull Throwable t) {
                }
            });

        }


    }


    private void create_graph2(List<String> graph_label, List<Integer> userScore) {
        try {

            chart1.getAxisRight().setEnabled(false);
            // chart.getXAxis().setEnabled(false);
            chart1.setDrawBarShadow(false);
            chart1.setDrawValueAboveBar(true);
            chart1.getDescription().setEnabled(false);
            chart1.setPinchZoom(false);
            chart1.setDrawGridBackground(false);


            YAxis yAxis = chart1.getAxisLeft();
            yAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return String.valueOf((int) value);
                }
            });

            yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            yAxis.setGranularity(1);
            yAxis.setGranularityEnabled(true);
            yAxis.setAxisMaximum(6f);
            yAxis.setAxisMinimum(0f);


            chart1.getAxisRight().setEnabled(false);
            XAxis xAxis = chart1.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setCenterAxisLabels(false);
            xAxis.setDrawGridLines(true);

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(graph_label));

            List<BarEntry> yVals1 = new ArrayList<BarEntry>();
            List<Integer> clr = new ArrayList<Integer>();

            for (int i = 0; i < userScore.size(); i++) {
                if (userScore.get(i) < 2) {
                    yVals1.add(new BarEntry(i, userScore.get(i)));
                    clr.add(Color.rgb(255, 0, 0));
                } else if (userScore.get(i) < 4) {
                    yVals1.add(new BarEntry(i, userScore.get(i)));
                    clr.add(Color.rgb(255, 204, 0));
                } else {
                    yVals1.add(new BarEntry(i, userScore.get(i)));
                    clr.add(Color.rgb(75, 170, 79));

                }
            }


            BarDataSet set1;

            if (chart1.getData() != null && chart1.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) chart1.getData().getDataSetByIndex(0);
                set1.setValues(yVals1);
                chart1.getData().notifyDataChanged();
                chart1.notifyDataSetChanged();
            } else {
                // create 2 datasets with different types
                set1 = new BarDataSet(yVals1, "");
                set1.setColors(clr);


                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
                chart1.setData(data);


            }

            chart1.setFitBars(true);

            Legend l = chart1.getLegend();
            l.setFormSize(1f); // set the size of the legend forms/shapes
            l.setForm(Legend.LegendForm.SQUARE); // set what type of form/shape should be used

            l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
            l.setTextSize(10f);
            l.setTextColor(Color.RED);
            l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
            l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis


            chart1.invalidate();
            chart1.animateY(2000);

        } catch (Exception ignored) {
        }
    }

    private void create_graph3(List<String> graph_label, List<Integer> userScore) {
        try {

            chart2.getAxisRight().setEnabled(false);
            // chart.getXAxis().setEnabled(false);
            chart2.setDrawBarShadow(false);
            chart2.setDrawValueAboveBar(true);
            chart2.getDescription().setEnabled(false);
            chart2.setPinchZoom(false);
            chart2.setDrawGridBackground(false);


            YAxis yAxis = chart2.getAxisLeft();
            yAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return String.valueOf((int) value);
                }
            });

            yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            yAxis.setGranularity(1);
            yAxis.setGranularityEnabled(true);
            yAxis.setAxisMaximum(6f);
            yAxis.setAxisMinimum(0f);


            chart2.getAxisRight().setEnabled(false);
            XAxis xAxis = chart2.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setCenterAxisLabels(false);
            xAxis.setDrawGridLines(true);

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(graph_label));

            List<BarEntry> yVals1 = new ArrayList<BarEntry>();
            List<Integer> clrSport = new ArrayList<Integer>();

            for (int i = 0; i < userScore.size(); i++) {
                if (userScore.get(i) < 2) {
                    yVals1.add(new BarEntry(i, userScore.get(i)));
                    clrSport.add(Color.rgb(255, 0, 0));
                } else if (userScore.get(i) < 4) {
                    yVals1.add(new BarEntry(i, userScore.get(i)));
                    clrSport.add(Color.rgb(255, 204, 0));
                } else {
                    yVals1.add(new BarEntry(i, userScore.get(i)));
                    clrSport.add(Color.rgb(75, 170, 79));

                }
            }


            BarDataSet set1;

            if (chart2.getData() != null && chart2.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) chart2.getData().getDataSetByIndex(0);
                set1.setValues(yVals1);
                chart2.getData().notifyDataChanged();
                chart2.notifyDataSetChanged();
            } else {
                // create 2 datasets with different types
                set1 = new BarDataSet(yVals1, "");
                set1.setColors(clrSport);


                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
                chart2.setData(data);


            }

            chart2.setFitBars(true);

            Legend l = chart2.getLegend();
            l.setFormSize(1f); // set the size of the legend forms/shapes
            l.setForm(Legend.LegendForm.SQUARE); // set what type of form/shape should be used

            l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
            l.setTextSize(10f);
            l.setTextColor(Color.RED);
            l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
            l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis


            chart2.invalidate();
            chart2.animateY(2000);

        } catch (Exception ignored) {
        }
    }


}
