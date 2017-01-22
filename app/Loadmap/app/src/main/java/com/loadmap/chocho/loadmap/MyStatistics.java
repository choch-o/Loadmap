package com.loadmap.chocho.loadmap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyStatistics extends Fragment {

    private BarChart barChart;

    String serverURL = "http://52.78.52.132:3000";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private SessionManager session;
    private String username;

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
        session = new SessionManager(getActivity());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        username = user.get(SessionManager.KEY_USERNAME);

        getTasksFromServer();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    List<Entry> prepareBarChartEntries() {
        List<Entry> entries = new ArrayList<Entry>();

        return entries;
    }

    void getTasksFromServer() {
        OkHttpHandler handler = new OkHttpHandler();

        String result = null;
        try {
            result = handler.execute(serverURL + "/tasks/" + username).get();
            Log.d("GET TASKS RESULT", result);
            // JSONObject taskObj = new JSONObject(result);

        } catch (Exception e) {

        }
    }
}