package com.loadmap.chocho.loadmap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyStatistics extends Fragment {

    private BarChart barChart;
    private PieChart pieChart;
    ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
    ArrayList<String> pieLabels = new ArrayList<String>();

    String serverURL = "http://52.78.52.132:3000";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private SessionManager session;
    private String username;
    Task[] myTasks;
    public MyStatistics() {
    }

    public static MyStatistics newInstance(int sectionNumber) {
        MyStatistics fragment = new MyStatistics();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_statistics, container, false);

        barChart = (BarChart) rootView.findViewById(R.id.bar_chart);
        pieChart = (PieChart) rootView.findViewById(R.id.pie_chart);
        pieChart.setUsePercentValues(true);
        session = new SessionManager(getActivity());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        username = user.get(SessionManager.KEY_USERNAME);

        Button reqButton = (Button) rootView.findViewById(R.id.request_button);
        reqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTasksFromServer();
            }
        });

        getTasksFromServer();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    void drawBarChart() {
        List<BarEntry> entries = new ArrayList<BarEntry>();
        // for (Task task : myTasks) {
        for (int i = 0; i < myTasks.length; i++) {
            Task task = myTasks[i];
            Log.d("DRAWING TASKS", Long.toString(task.getStartTime()));
            Log.d("DURATION", Long.toString(task.getDuration()));
            // entries.add(new BarEntry(task.getStartTime(), task.getDuration()));
            entries.add(new BarEntry(i, task.getDuration()));
        }
        BarDataSet dataSet = new BarDataSet(entries, "Label");
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate();
    }

    void drawPieChart() {
        PieDataSet dataSet = new PieDataSet(pieEntries, "Tasks by Courses");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.invalidate();
    }
    void getTasksFromServer() {
        OkHttpHandler handler = new OkHttpHandler();

        String result = null;
        try {
            result = handler.execute(serverURL + "/tasks/" + username).get();
            Log.d("GET TASKS RESULT", result);
            JSONArray tasksByCourse = new JSONArray(result);
            for (int i = 0; i < tasksByCourse.length(); i++) {
                JSONObject obj = tasksByCourse.getJSONObject(i);
                pieEntries.add(new PieEntry(obj.getLong("totalDuration"), i));
                pieLabels.add(obj.getString("courseName"));
//                obj.getString("courseName");
//                obj.getLong("totalDuration");
            }
            /*
            JSONArray tasks = new JSONArray(result);
            myTasks = new Task[tasks.length()];
            for (int i = 0; i < tasks.length(); i++) {
                JSONObject task = tasks.getJSONObject(i);
                /* myTasks[i] = new Task(task.getString("username"),
                        task.getString("subject"),
                        task.getString("tasktype"),
                        task.getLong("datetime"),
                        task.getLong("duration"));
                Log.d("SORTED TASKS", Long.toString(myTasks[i].getStartTime()));
            }*/
            drawPieChart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}