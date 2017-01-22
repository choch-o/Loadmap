package com.loadmap.chocho.loadmap;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.N)
public class TaskTracker extends Fragment {

    int year, month, day, hour, minute;
    static int taskstatus =0;
    long taskdurationinmilli;

    View rootView;
    TextView timeView,dateView;

    Button logoutButton;
    Button startButton;
    Button pauseButton;
    Button resumeButton;
    Button stopButton;

    String resultText;
    String resultText2;
    String selectedDate, selectedTime;
    Date date;
    static Long startDateTime;
    static Long finishDateTime;
    String serverURL = "http://52.78.52.132:3000";

    String finedMinute;

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss (z Z)");
    LinearLayout date_change, time_change;
    SessionManager session;

    String name;
    String username;

    static Calendar c = Calendar.getInstance();

    private static final String ARG_SECTION_NUMBER = "section_number";

    public TaskTracker() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TaskTracker newInstance(int sectionNumber) {
        TaskTracker fragment = new TaskTracker();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_task_tracker, container, false);

        session = new SessionManager(getActivity());

        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManager.KEY_NAME);
        username = user.get(SessionManager.KEY_USERNAME);

        logoutButton = (Button)rootView.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                session.logoutUser();
            }
        });

        String[] option = getResources().getStringArray(R.array.spinnerArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, option);


//        과목명 Array에 담아서 받아야 함
//        String[] option2 = getResources().getStringArray(R.array.spinnerArray2);

        final String[] courses = getCourseFromServer();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, courses);

        Spinner spinner = (Spinner)rootView.findViewById(R.id.spinner);
        Spinner spinner2 = (Spinner)rootView.findViewById(R.id.spinner2);

        dateView = (TextView)rootView.findViewById(R.id.dateView);
        timeView = (TextView)rootView.findViewById(R.id.timeView);

        startButton = (Button)rootView.findViewById(R.id.startButton);
        pauseButton = (Button)rootView.findViewById(R.id.pauseButton);
        resumeButton = (Button)rootView.findViewById(R.id.resumeButton);
        stopButton = (Button)rootView.findViewById(R.id.stopButton);

        date_change = (LinearLayout)rootView.findViewById(R.id.date_change);
        time_change = (LinearLayout)rootView.findViewById(R.id.time_change);

        TimeZone tz;
        date = new Date();
        selectedDate = getDate(df.format(date));
        selectedTime = getTime(df.format(date));

        tz = TimeZone.getTimeZone("Asia/Seoul"); df.setTimeZone(tz);


        startDateTime = c.getTimeInMillis()+32400000;
//        finishDateTime = c.getTimeInMillis()+32400000;



        dateView.setText(getDate(df.format(date)));
        timeView.setText(getTime(df.format(date)));

        date_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm1 = getFragmentManager();
                MyDialogFragment1 dialogFragment = new MyDialogFragment1();
                dialogFragment.setTargetFragment(TaskTracker.this, 0);
                dialogFragment.show(fm1, "fragment_date");
            }
        });

        time_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm2 = getFragmentManager();
                MyDialogFragment2 dialogFragment2 = new MyDialogFragment2();
                dialogFragment2.setTargetFragment(TaskTracker.this, 0);
                dialogFragment2.show(fm2, "fragment_time");

            }
        });

        spinner.setAdapter(adapter);
        getSpinner(rootView, R.id.spinner).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected
                    (AdapterView<?> parentView, View selectedView,
                                       int position, long id) {
                printChecked(selectedView, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                resultText = "숙제";
            }
        });

        spinner2.setAdapter(adapter2);
        getSpinner(rootView, R.id.spinner2).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected
                    (AdapterView<?> parentView, View selectedView,
                     int position, long id) {
                printChecked(selectedView, position);
                Toast.makeText(getContext(),"Something Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                resultText2 = courses[0];
                Toast.makeText(getContext(),courses[0]= "courses[0]", Toast.LENGTH_SHORT).show();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setVisibility(View.GONE);
//                pauseButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                taskstatus = 1;
                setVariables();
                //서버로 {과목, 종류, 날짜, 시간, 시작/일시정지/재개/종료} 전송
            }
        });

//        pauseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pauseButton.setVisibility(View.GONE);
//                resumeButton.setVisibility(View.VISIBLE);
//                stopButton.setVisibility(View.GONE);
//                taskstatus = 2;
//                setVariables();
//                //서버로 {과목, 종류, 날짜, 시간, 시작/일시정지/재개/종료} 전송
//            }
//        });
//
//        resumeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                resumeButton.setVisibility(View.GONE);
////                pauseButton.setVisibility(View.VISIBLE);
////                stopButton.setVisibility(View.VISIBLE);
//                taskstatus = 1;
//                setVariables();
//                //서버로 {과목, 종류, 날짜, 시간, 시작/일시정지/재개/종료} 전송
//            }
//        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.GONE);
                taskstatus = 0;
                taskdurationinmilli = finishDateTime-startDateTime ;
                Log.d("@@@@@@@@@@@@duration: ", Long.toString(taskdurationinmilli));
//                getDuration(new Date(), );
                editVariables();
                //서버로 {과목, 종류, 날짜, 시간, 시작/일시정지/재개/종료} 전송
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public void printChecked(View v, int position){
        Spinner spinnerPCheck = (Spinner)getView().getRootView().findViewById(R.id.spinner);
        resultText="";
        if(spinnerPCheck.getSelectedItemPosition()>0){
            resultText=(String)spinnerPCheck.getAdapter().getItem(spinnerPCheck.getSelectedItemPosition());
        }
        else{
            resultText=(String)spinnerPCheck.getAdapter().getItem(0);
//            ((TextView)findViewById(R.id.textView1)).setText(resultText);
        }

        Spinner spinnerPCheck2 = (Spinner)getView().getRootView().findViewById(R.id.spinner2);
        resultText2="";
        if(spinnerPCheck2.getSelectedItemPosition()>0){
            resultText2=(String)spinnerPCheck2.getAdapter().getItem(spinnerPCheck2.getSelectedItemPosition());
        }
        else{
//            resultText2=(String)spinnerPCheck2.getAdapter().getItem(0);
//            ((TextView)findViewById(R.id.textView1)).setText(resultText);
        }
    }

    public Spinner getSpinner(View rootView, int objId){
        return (Spinner) rootView.findViewById(objId);
    }

    public void setVariables() {
        JsonObject json = new JsonObject();
        try {
//            json.addProperty("IDnumber", MainActivity.IDnumber);
//            json.addProperty("semester", MainActivity.semester);
//            json.addProperty("professor", MainActivity.professor);

            json.addProperty("username", username);
            json.addProperty("tasktype", resultText);
            json.addProperty("subject", resultText2);
            json.addProperty("datetime", startDateTime);
            json.addProperty("taskstatus", 1);
            json.addProperty("duration", 0);
            Log.d("JSON INFO", json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Ion.with(rootView.getContext()).load(serverURL + "/task/data")
                .setJsonObjectBody(json).asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Toast.makeText(getContext(), "Task is Created", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void editVariables() {
        JsonObject json2 = new JsonObject();
        try {
            json2.addProperty("username", username);
            json2.addProperty("tasktype", resultText);
            json2.addProperty("subject", resultText2);
            json2.addProperty("datetime", 0);
            json2.addProperty("taskstatus", 0);
            json2.addProperty("duration", taskdurationinmilli);
            Log.d("JSON INFO", json2.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Ion.with(rootView.getContext()).load(serverURL + "/task/data")
                .setJsonObjectBody(json2).asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Toast.makeText(getContext(), "Task is finished", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setDate(int yearOfCentury, int monthOfYear, int dayOfMonth) {
        Log.d("setDate", "#" + yearOfCentury + "#" + monthOfYear + "#" + dayOfMonth);
        this.year = yearOfCentury;
        this.month = monthOfYear+1;
        this.day = dayOfMonth;
        selectedDate = year+"."+month+"."+day;
        dateView.setText("" + year + ". " + month + ". " + day + ".");
    }

    public void setTime(int hr, int mn) {
        Log.d("setHour", "#" + hr + "#" + mn);
        this.hour = hr;
        this.minute = mn;
        finedMinute = String.format("%02d", minute);
        selectedTime = hour+":"+minute;
        timeView.setText("" + hour + " : " + finedMinute);
    }

    public static class MyDialogFragment1 extends DialogFragment {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_date, container, false);

            final DatePicker datePicker = (DatePicker) v.findViewById(R.id.datePicker);
            datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int yearOfCentury, int monthOfYear, int dayOfMonth) {
                    ((TaskTracker)getTargetFragment()).setDate(yearOfCentury, monthOfYear, dayOfMonth);
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    if(taskstatus == 1) {
                        finishDateTime = c.getTimeInMillis();
                    }
                    else {
                        startDateTime = c.getTimeInMillis();
                    }
                }
            });
            Button finishButton = (Button) v.findViewById(R.id.datebutton);
            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            return v;
        }
    }

    public static class MyDialogFragment2 extends DialogFragment {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_time, container, false);

            final TimePicker timePicker = (TimePicker)v.findViewById(R.id.timePicker);
            timePicker.setIs24HourView(true);
            if(c.get(Calendar.HOUR_OF_DAY)>=15){
                timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY)-15);
            }
            else{
                timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY)+9);
            }
            timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minuteOfhour) {
                    ((TaskTracker) getTargetFragment()).setTime(hourOfDay, minuteOfhour);
                    if (taskstatus == 1) {
                        finishDateTime = finishDateTime + (c.get(Calendar.HOUR) * 60 * 60 * 1000) + (c.get(Calendar.MINUTE) * 60 * 1000);
                    }
                    else{
                        startDateTime = startDateTime + (c.get(Calendar.HOUR) * 60 * 60 * 1000) + (c.get(Calendar.MINUTE) * 60 * 1000);
                    }
                }
            });

            Button finishButton2 = (Button)v.findViewById(R.id.timebutton);
            finishButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            return v;
        }
    }

    public static String getDate(String playtime) {
        // TODO : Implement this method
        // 2017-01-10T15:00:00.000Z
        String strYear;
        char[] charYear = new char [4];
        playtime.getChars(0, 4, charYear, 0);
        strYear = new String(charYear, 0, charYear.length);


        String strMonth;
        char[] charMonth = new char [2];
        playtime.getChars(5, 7, charMonth, 0);

        if(charMonth[0] == '0'){
            char finedCharMonth = charMonth[1];
            strMonth = Character.toString(finedCharMonth);
        }
        else{
            strMonth = new String(charMonth, 0, charMonth.length);
        }

        String strDay;
        char[] charDay = new char [2];
        playtime.getChars(8, 10, charDay, 0);

        if(charDay[0] == '0'){
            char finedCharDay = charDay[1];
            strDay = Character.toString(finedCharDay);
        }
        else{
            strDay = new String(charDay, 0, charDay.length);
        }

        String strDate = strYear+"년 "+strMonth + "월 " + strDay + "일";
        return strDate;
    }

    public static String getTime(String playtime) {
        // 2017-01-10T15:00:00.000Z
        char[] charTime = new char [5];
        playtime.getChars(11, 16, charTime, 0);
        String strTime = "";
        strTime = new String(charTime, 0, charTime.length);

        return strTime;
    }

    String[] getCourseFromServer() {
        OkHttpHandler handler = new OkHttpHandler();

        String result = null;
        String[] courses;
        try {
            result = handler.execute(serverURL + "/courses/" + username).get();
            Log.d("GET COURSES RESULT", result);
            JSONObject userObj = new JSONObject(result);
            JSONArray courseArr = userObj.getJSONArray("courses");
            courses = new String[courseArr.length()];
            for (int i = 0; i < courseArr.length(); i++) {

                courses[i] = courseArr.getJSONObject(i).getString("name");
                Log.d("COURSE ARR", courses[i]);
            }

            return courses;
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0];
        }
    }
}

