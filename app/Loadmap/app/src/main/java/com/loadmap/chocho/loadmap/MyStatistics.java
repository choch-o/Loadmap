package com.loadmap.chocho.loadmap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MyStatistics extends Fragment {

    private BarChart barChart;
    private PieChart coursePieChart;
    private PieChart tasktypePieChart;
    Course[] courses;
    HashMap<String, TaskType[]> courseTypeHm = new HashMap<String, TaskType[]>();

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

        // barChart = (BarChart) rootView.findViewById(R.id.bar_chart);
        coursePieChart = (PieChart) rootView.findViewById(R.id.course_pie_chart);
        tasktypePieChart = (PieChart) rootView.findViewById(R.id.tasktype_pie_chart);

        session = new SessionManager(getActivity());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        username = user.get(SessionManager.KEY_USERNAME);

        /*
        Button reqButton = (Button) rootView.findViewById(R.id.request_button);
        reqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTasksFromServer();
            }
        });
        */

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
            Log.d("DRAWING TASKS", Long.toString(task.getDateTime()));
            Log.d("DURATION", Long.toString(task.getDuration()));
            // entries.add(new BarEntry(task.getStartTime(), task.getDuration()));
            entries.add(new BarEntry(i, task.getDuration()));
        }
        BarDataSet dataSet = new BarDataSet(entries, "Load");
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate();
    }


    void getTasksFromServer() {
        OkHttpHandler handler = new OkHttpHandler();

        String result = null;
        ArrayList<PieEntry> courseEntries = new ArrayList<PieEntry>();

        try {
            result = handler.execute(serverURL + "/tasks/" + username).get();
            Log.d("GET TASKS RESULT", result);
            JSONArray tasksByCourse = new JSONArray(result);
            courses = new Course[tasksByCourse.length()];
            for (int i = 0; i < tasksByCourse.length(); i++) {
                JSONObject obj = tasksByCourse.getJSONObject(i);
                courseEntries.add(new PieEntry(obj.getLong("totalDuration"), obj.getString("courseName")));
                courses[i] = new Course();
                courses[i].setName(obj.getString("courseName"));
                courses[i].setTotalDuration(obj.getLong("totalDuration"));
                courses[i].setId(obj.getString("_id"));

                JSONArray tasksByTypes = obj.getJSONArray("tasksByType");
                TaskType[] taskTypes = new TaskType[tasksByTypes.length()];
                for (int j = 0; j < tasksByTypes.length(); j++) {
                    taskTypes[j] = new TaskType();
                    JSONObject type = tasksByTypes.getJSONObject(j);
                    taskTypes[j].setTaskType(type.getString("tasktype"));
                    taskTypes[j].setTotalDuration(type.getLong("subtotalDuration"));
                    // taskTypes[i].setTasks(type.get);
                }
                courses[i].setTasks(taskTypes);

                courseTypeHm.put(obj.getString("courseName"), taskTypes);
            }

            drawPieChart(courseEntries, coursePieChart, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void drawPieChart(final ArrayList<PieEntry> pieEntries, PieChart pieChart, boolean course) {

        pieChart.setUsePercentValues(true);
        if (course) {
            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    final ArrayList<PieEntry> taskTypeEntries = new ArrayList<PieEntry>();
                    if (e == null)
                        return;
                    Log.d("ENTRY INFO", e.toString());
                    Log.d("ENT IF", pieEntries.get((int) h.getX()).getLabel());
                    Log.d("GET X", Float.toString(h.getX()));
                    Log.d("GET Y", Float.toString(h.getY()));
                    Log.d("VALUE SELECTED", h.toString());

                    // Log.d("VALUE SELECTED", courseTypeHm.get(pieEntries.get((int) h.getX()).getLabel()).toString());
                    Log.d("LABEL", pieEntries.get((int) h.getX()).getLabel());
                    TaskType[] result = courseTypeHm.get(pieEntries.get((int) h.getX()).getLabel());
                    Log.d("Result length", Integer.toString(result.length));
                    for (TaskType tt : result) {
                        taskTypeEntries.add(new PieEntry(tt.getTotalDuration(), tt.getTaskType()));
                    }
                    tasktypePieChart.invalidate();
                    drawPieChart(taskTypeEntries, tasktypePieChart, false);
                }

                @Override
                public void onNothingSelected() {

                }
            });
        }
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        PieDataSet dataSet = new PieDataSet(pieEntries, "Tasks by Courses");
        // Pie chart styling

        int[] colors = { getResources().getColor(R.color.piecolor2),
                getResources().getColor(R.color.piecolor4),
                getResources().getColor(R.color.piecolor5),
                getResources().getColor(R.color.piecolor1),
                getResources().getColor(R.color.piecolor7),
                getResources().getColor(R.color.piecolor3),
                getResources().getColor(R.color.piecolor6)
        };

        shuffleArray(colors);
        dataSet.setColors(colors);
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PieValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(getResources().getColor(R.color.textcolor));

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(getResources().getColor(R.color.color4));

        pieChart.setTransparentCircleColor(getResources().getColor(R.color.color4));
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(50f);

        pieChart.setHighlightPerTapEnabled(true);

        pieChart.setEntryLabelColor(getResources().getColor(R.color.textcolor));
        pieChart.setEntryLabelTextSize(11f);

        pieChart.setData(data);

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend legend = pieChart.getLegend();


        pieChart.invalidate();
    }

    static void shuffleArray(int[] arr) {
        Random rnd = new Random();
        for (int i = arr.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = arr[index];
            arr[index] = arr[i];
            arr[i] = a;
        }
    }
}