package com.loadmap.chocho.loadmap;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
/**
 * Created by q on 2017-01-19.
 */

public class MainActivity extends AppCompatActivity {

    TimePicker timePicker;
    int hour, minute;
    String yearStr, monthStr, dayStr;
    EditText editDate1;
    EditText editDate2;
    EditText editDate3;
    int currentYear;
    int currentMonth;
    int currentDay;
    Button startButton;
    Button pauseButton;
    Button resumeButton;
    Button stopButton;
    String resultText;
    String date, time;
    int taskstatus =0;
    String serverURL = "http://52.78.101.202:3000";
    double randomKey;

    Button logoutButton;
    SessionManager session;

    // taskstatus 0 = 작업 종료, taskstatus 1 = 작업 중, taskstatus 2 = 작업 일시 정지
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity);

        session = new SessionManager(getApplicationContext());

        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        String name = user.get(SessionManager.KEY_NAME);
        String username = user.get(SessionManager.KEY_USERNAME);

        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                session.logoutUser();
            }
        });

        String[] option = getResources().getStringArray(R.array.spinnerArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_dropdown_item, option);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        editDate1 = (EditText)findViewById(R.id.editDate1);
        editDate2 = (EditText)findViewById(R.id.editDate2);
        editDate3 = (EditText)findViewById(R.id.editDate3);

        startButton = (Button)findViewById(R.id.startButton);
        pauseButton = (Button)findViewById(R.id.pauseButton);
        resumeButton = (Button)findViewById(R.id.resumeButton);
        stopButton = (Button)findViewById(R.id.stopButton);

        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get((Calendar.MONTH));
        currentDay = c.get(Calendar.DAY_OF_MONTH);

        editDate1.setText(Integer.toString(currentYear));
        editDate2.setText(Integer.toString(currentMonth)+1);
        editDate3.setText(Integer.toString(currentDay));

        yearStr = editDate1.getText().toString();
        monthStr = editDate2.getText().toString();
        dayStr = editDate3.getText().toString();

        date =  yearStr+"."+monthStr+"."+dayStr;

        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour((c.get(Calendar.HOUR_OF_DAY))+9);
        timePicker.setCurrentMinute((c.get(Calendar.MINUTE)));
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minuteOfHour) {
                timePicker.setHour(hourOfDay);
                timePicker.setMinute(minuteOfHour);
                hour = hourOfDay;
                minute = minuteOfHour;
                time = Integer.toString(hour)+":"+Integer.toString(minute);
            }
        });
        spinner.setAdapter(adapter);
        getSpinner(R.id.spinner).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedView,
                                       int position, long id) {
                printChecked(selectedView, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                taskstatus = 1;
                randomKey = Math.random();
                setVariables();
                //서버로 {과목, 종류, 날짜, 시간, 시작/일시정지/재개/종료} 전송
            }
       });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseButton.setVisibility(View.GONE);
                resumeButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.GONE);
                taskstatus = 2;
                setVariables();
                //서버로 {과목, 종류, 날짜, 시간, 시작/일시정지/재개/종료} 전송
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                taskstatus = 1;
                setVariables();
                //서버로 {과목, 종류, 날짜, 시간, 시작/일시정지/재개/종료} 전송
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.GONE);
                taskstatus = 0;
                setVariables();
                //서버로 {과목, 종류, 날짜, 시간, 시작/일시정지/재개/종료} 전송
            }
        });



    }
    public void printChecked(View v, int position){
        Spinner spinnerPCheck = (Spinner)findViewById(R.id.spinner);
        resultText="";
        if(spinnerPCheck.getSelectedItemPosition()>0){
            resultText=(String)spinnerPCheck.getAdapter().getItem(spinnerPCheck.getSelectedItemPosition());
        }
        if(resultText !=""){
//            ((TextView)findViewById(R.id.textView1)).setText(resultText);
        }
    }
    public Spinner getSpinner(int objId){
        return (Spinner) findViewById(objId);
    }

    public void setVariables() {
        JsonObject json = new JsonObject();
        try {
            json.addProperty("randomkey", randomKey);
//            json.addProperty("subject", MainActivity.subject);
            json.addProperty("tasktype", URLEncoder.encode(resultText, "utf-8"));
            json.addProperty("date", date);
            json.addProperty("time", time);
            json.addProperty("taskstatus", taskstatus);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Ion.with(this.getApplication()).load(serverURL + "/task/data")
                .setJsonObjectBody(json).asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Toast.makeText(MainActivity.this, "SERVER CONNECTION is DONE", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
