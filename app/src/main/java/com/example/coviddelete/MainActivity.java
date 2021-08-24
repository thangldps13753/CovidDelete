package com.example.coviddelete;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coviddelete.api.ApiUtilities;
import com.example.coviddelete.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView totalConfirm, totalActive, totalDeath, totalTest, totalRecovered;
    TextView todayConfirm, todayRecovered, todayDeath, date, cname;
    List<CountryData> list;
    ValueLineChart mCubicValueLineChart;

    String country = "Vietnam";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(getIntent().getStringArrayExtra("country") != null)
            country = getIntent().getStringExtra("country");

        TextView cname = findViewById(R.id.cname);
        cname.setText(country);
         cname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CountryActivity.class));
            }
        });

        list = new ArrayList<>();
        init();

        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getCountry().equals("Vietnam")) {
                                int confirm = Integer.parseInt(list.get(i).getCases());
                                int active = Integer.parseInt(list.get(i).getActive());
                                int recovered = Integer.parseInt(list.get(i).getRecovered());
                                int death = Integer.parseInt(list.get(i).getDeaths());
                                int test = Integer.parseInt(list.get(i).getTests());

                                totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                                totalActive.setText(NumberFormat.getInstance().format(active));
                                totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                                totalDeath.setText(NumberFormat.getInstance().format(death));
                                totalTest.setText(NumberFormat.getInstance().format(test));


                                setText(list.get(i).getUpdated());


                                todayConfirm.setText("Today " + NumberFormat.getInstance().
                                        format(Integer.parseInt(list.get(i).getTodayCases())));
                                todayDeath.setText("Today " + NumberFormat.getInstance().
                                        format(Integer.parseInt(list.get(i).getTodayDeaths())));
                                todayRecovered.setText("Today " + NumberFormat.getInstance().
                                        format(Integer.parseInt(list.get(i).getTodayRecovered())));

//
//                                series.addPoint(new ValueLinePoint("Jan", 2.4f));
//                                series.addPoint(new ValueLinePoint("Feb", 3.4f));
//                                series.addPoint(new ValueLinePoint("Mar", .4f));
//                                series.addPoint(new ValueLinePoint("Apr", 1.2f));

                                ValueLineSeries series = new ValueLineSeries();
                                series.setColor(R.color.orange);
//                                series.addPoint(new ValueLinePoint("Confirm", confirm));
//                                series.addPoint(new ValueLinePoint("Confirm", confirm));
//                                series.addPoint(new ValueLinePoint("Confirm", confirm));
//                                series.addPoint(new ValueLinePoint("Confirm", confirm));
//                                series.addPoint(new ValueLinePoint("Confirm", confirm));
//                                series.addPoint(new ValueLinePoint("Confirm", confirm));
                                series.addPoint(new ValueLinePoint("", 0f));
                                series.addPoint(new ValueLinePoint("Confirm", confirm));

                                series.addPoint(new ValueLinePoint("Active", active));
                                series.addPoint(new ValueLinePoint("Recovered", recovered));
                                series.addPoint(new ValueLinePoint("Death", death));

                                series.addPoint(new ValueLinePoint("", 0f));

                                mCubicValueLineChart.addSeries(series);
                                mCubicValueLineChart.startAnimation();
//                                pieChart.addPieSlice(new PieModel("Active", active,getResources()
//                                        .getColor(R.color.blue)));
//                                pieChart.addPieSlice(new PieModel("Recovered", recovered,getResources()
//                                        .getColor(R.color.green_pie)));
//                                pieChart.addPieSlice(new PieModel("Death", death,getResources()
//                                        .getColor(R.color.red_pie)));


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Erorr: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void setText(String updated) {
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        long millisecond = Long.parseLong(updated);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);
        date.setText("Update at " + format.format(calendar.getTime()));
    }

    private void init() {
        totalConfirm = findViewById(R.id.totalConfirm);
        totalActive = findViewById(R.id.totalActive);
        totalDeath = findViewById(R.id.totalDeath);
        totalTest = findViewById(R.id.totalTest);
        totalRecovered = findViewById(R.id.totalRecovered);

        todayConfirm = findViewById(R.id.todayConfirm);
        todayRecovered = findViewById(R.id.todayRecovered);
        todayDeath = findViewById(R.id.todayDeath);


        date = findViewById(R.id.date);
        mCubicValueLineChart = findViewById(R.id.cubiclinechart);

//        pieChart = findViewById(R.id.piechart);


        cname = findViewById(R.id.cname);

    }
}