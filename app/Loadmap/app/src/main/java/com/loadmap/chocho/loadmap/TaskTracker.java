package com.loadmap.chocho.loadmap;

import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

@RequiresApi(api = Build.VERSION_CODES.N)
public class TaskTracker extends Fragment {

    Course[] courses;

    int year, month, day, hour, minute;
    static int taskstatus =0;
    long taskdurationinmilli;

    View rootView;
    TextView timeView,dateView;

    Button startButton;
    Button pauseButton;
    Button resumeButton;
    Button stopButton;

    String resultText;
    String resultText2;
    Course selectedCourse;
    String selectedDate, selectedTime;
    Date date;
    static Long startDateTime;
    static Long finishDateTime;
    String serverURL = "http://52.78.52.132:3000";

    String finedMinute;

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss (z Z)");
    RelativeLayout date_change, time_change;
    SessionManager session;

    String name;
    String username;

    static Calendar c = Calendar.getInstance();

    private static final String ARG_SECTION_NUMBER = "section_number";
    SharedPreferences pref;

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


        String[] option = getResources().getStringArray(R.array.spinnerArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), R.layout.spinner_item, option);


//        과목명 Array에 담아서 받아야 함
//        String[] option2 = getResources().getStringArray(R.array.spinnerArray2);

        final String[] courseNames = getCourseFromServer();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>
                (getContext(), R.layout.spinner_item_dark, courseNames);

        Spinner spinner = (Spinner)rootView.findViewById(R.id.spinner);
        Spinner spinner2 = (Spinner)rootView.findViewById(R.id.spinner2);

        dateView = (TextView)rootView.findViewById(R.id.dateView);
        timeView = (TextView)rootView.findViewById(R.id.timeView);

        startButton = (Button)rootView.findViewById(R.id.startButton);
        pauseButton = (Button)rootView.findViewById(R.id.pauseButton);
        resumeButton = (Button)rootView.findViewById(R.id.resumeButton);
        stopButton = (Button)rootView.findViewById(R.id.stopButton);

        date_change = (RelativeLayout) rootView.findViewById(R.id.date_change);
        time_change = (RelativeLayout)rootView.findViewById(R.id.time_change);

        TimeZone tz;
        date = new Date();
        selectedDate = getMyDate(df.format(date));
        selectedTime = getMyTime(df.format(date));
        tz = TimeZone.getTimeZone("Asia/Seoul"); df.setTimeZone(tz);

        pref = getContext().getSharedPreferences("pref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);

        String strTimeMilli = String.format("%tQ",date);
        Log.d("@@@@@@strTimeMilli : ",strTimeMilli);
        startDateTime = c.getTimeInMillis();
        Long alwaysCurrentTime = c.getTimeInMillis();
        Long startDateTimeKorea = alwaysCurrentTime;
        Date initialDateTime = new Date(startDateTimeKorea);
        Log.d("@@@@@@startDateTime : ",Long.toString(c.getTimeInMillis()));
        finishDateTime = c.getTimeInMillis();


        year = initialDateTime.getYear()+1900;
        month = initialDateTime.getMonth();
        day = initialDateTime.getDate();
        hour = initialDateTime.getHours();
        minute = initialDateTime.getMinutes();


//        month = c.get(Calendar.MONTH);
//        day = c.get(Calendar.DAY_OF_MONTH);
//        hour = c.get(Calendar.HOUR_OF_DAY);
//        minute = c.get(Calendar.MINUTE);
        Log.d("variables : ", "year:"+year+"month:"+month+"day:"+day+"hour:"+hour+"minute:"+minute);
        dateView.setText(year+"년 "+(month+1)+"월 "+day+"일");
        timeView.setText(hour+"시 "+minute+"분");

//        dateView.setText(getMyDate(df.format(date)));
//        timeView.setText(getMyTime(df.format(date)));

        if(!(pref.getString("username", "").equals(""))) {
            username = pref.getString("username", "");
            resultText = pref.getString("tasktype", "");
//            new Gson().toJsonTree(selectedCourse) = pref.getString("subject", null);
            startDateTime = Long.parseLong(pref.getString("datetime", ""));
            taskstatus = Integer.parseInt(pref.getString("taskstatus", ""));

            editor.clear();
            editor.commit();

            startButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
        }

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
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
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

        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
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
                Log.d("NOTHING SELECTED", "NOTHINGGG");
                selectedCourse = courses[0];
                 resultText2 = courseNames[0];
                Toast.makeText(getContext(),courseNames[0]= "courseNames[0]", Toast.LENGTH_SHORT).show();
            }
        });



            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startButton.setVisibility(View.GONE);
//                pauseButton.setVisibility(View.VISIBLE);
                    stopButton.setVisibility(View.VISIBLE);
                    taskstatus = 1;
//                c.set(Calendar.SECOND,0);
//                c.set(Calendar.MILLISECOND,0);
//                    setVariables();


                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username", username);
                    editor.putString("tasktype", resultText);
                    editor.putString("subject", (new Gson().toJsonTree(selectedCourse)).toString());
                    editor.putString("datetime", Long.toString(startDateTime));
                    editor.putString("taskstatus", Integer.toString(taskstatus));
                    editor.putString("duration", "");
                    editor.commit();

                    Log.d("username : ", username);
                    Log.d("username : ", pref.getString("username", ""));
                    Log.d("SAVEDtasktype : ", resultText);
                    Log.d("SAVEDtasktype : ", pref.getString("tasktype", ""));
                    Log.d("subject : ", (new Gson().toJsonTree(selectedCourse)).toString());
                    Log.d("SAVEDstartdatetime : ", (new Date(startDateTime)).toString());
                    Log.d("SAVEDstartdatetime : ", new Date(Long.parseLong(pref.getString("datetime",""))).toString());
                    Log.d("taskstatus : ", Integer.toString(taskstatus));
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
                    taskdurationinmilli = finishDateTime - startDateTime;
                    Log.d("@@@@@@@@@@@@duration: ", Long.toString(taskdurationinmilli));
//                getDuration(new Date(), );
                    editVariables();
                    editor.clear();
                    editor.commit();

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
            selectedCourse = courses[spinnerPCheck2.getSelectedItemPosition()];
        }
        else if (spinnerPCheck2.getSelectedItemPosition() == 0){
            selectedCourse = courses[0];
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
            json.add("subject", new Gson().toJsonTree(selectedCourse));
            json.addProperty("datetime", startDateTime);
            json.addProperty("taskstatus", 1);
            json.addProperty("duration", 0);
            Log.d("JSON INFO", json.toString());
            Log.d("startDateTime : ",(new Date(startDateTime)).toString());
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
        JsonObject json = new JsonObject();
        try {
            json.addProperty("username", username);
            json.addProperty("tasktype", resultText);
            json.add("subject", new Gson().toJsonTree(selectedCourse));
            json.addProperty("datetime", startDateTime);
            json.addProperty("taskstatus", 0);
            json.addProperty("duration", taskdurationinmilli);
            Log.d("JSON INFO", json.toString());
            Log.d("startDateTime : ",(new Date(startDateTime)).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Ion.with(rootView.getContext()).load(serverURL + "/task/data")
                .setJsonObjectBody(json).asJsonObject()
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
        this.month = monthOfYear;
        this.day = dayOfMonth;
        selectedDate = year+"년 "+month+"월 "+day+"일";
        dateView.setText("" + year + ". " + month+1 + ". " + day + ".");
    }

    public void setTime(int hr, int mn) {
        Log.d("setHour", "#" + hr + "#" + mn);
        this.hour = hr;
        this.minute = mn;
        finedMinute = String.format("%02d", minute);
        selectedTime = hour+":"+minute;
        timeView.setText(hour + "시 " + finedMinute+ "분" );
    }

    public static class MyDialogFragment1 extends DialogFragment {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_date, container, false);

            final DatePicker datePicker = (DatePicker) v.findViewById(R.id.datePicker);

//            datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener()
            datePicker.init(((TaskTracker)getTargetFragment()).year, ((TaskTracker)getTargetFragment()).month, ((TaskTracker)getTargetFragment()).day, new DatePicker.OnDateChangedListener(){
                @Override
                public void onDateChanged(DatePicker view, int yearOfCentury, int monthOfYear, int dayOfMonth) {
                    ((TaskTracker)getTargetFragment()).setDate(yearOfCentury, monthOfYear, dayOfMonth);
                    ((TaskTracker)getTargetFragment()).year = yearOfCentury;
                    ((TaskTracker)getTargetFragment()).month = monthOfYear;
                    ((TaskTracker)getTargetFragment()).day = dayOfMonth;
                    c.set(yearOfCentury,monthOfYear,dayOfMonth);
                    c.set(Calendar.HOUR_OF_DAY, ((TaskTracker)getTargetFragment()).hour);
                    c.set(Calendar.MINUTE,((TaskTracker)getTargetFragment()).minute);
                    Log.d("@Calendar in Millis : ",Long.toString(c.getTimeInMillis()));
                    Log.d("month at dialog1 : ",Integer.toString(((TaskTracker) getTargetFragment()).month));
//                    c.set(Calendar.HOUR_OF_DAY, 23);
//                    c.set(Calendar.MINUTE,0);
//                    c.set(Calendar.SECOND,0);
//                    c.set(Calendar.MILLISECOND,0);
//                    Log.d("@Calendar after fine: ",Long.toString(c.getTimeInMillis()));

                    if(taskstatus == 1) {
                        Log.d("taskstaus must be 1", Integer.toString(taskstatus));
                        finishDateTime = c.getTimeInMillis()-32400000;
                        Log.d("FINISHSIMPLEFORMAT : ", (new Date(finishDateTime)).toString());
                                            }
                    else {
                        Log.d("taskstaus must be 0", Integer.toString(taskstatus));
                        startDateTime = c.getTimeInMillis()-32400000;
                        Log.d("STARTSIMPLEFORMAT : ", (new Date(startDateTime)).toString());
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
            Calendar c2 = Calendar.getInstance();
            if(c2.get(Calendar.HOUR_OF_DAY)>=15){
                timePicker.setCurrentHour(c2.get(Calendar.HOUR_OF_DAY)-15);
            }
            else{
                timePicker.setCurrentHour(c2.get(Calendar.HOUR_OF_DAY)+9);
            }
            timePicker.setCurrentMinute(c2.get(Calendar.MINUTE));
            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minuteOfhour) {
                    ((TaskTracker) getTargetFragment()).setTime(hourOfDay, minuteOfhour);
                    c.set(((TaskTracker) getTargetFragment()).year, ((TaskTracker) getTargetFragment()).month, ((TaskTracker) getTargetFragment()).day, hourOfDay, minuteOfhour,0);
                    Log.d("month at dialog2 : ",Integer.toString(((TaskTracker) getTargetFragment()).month));
                    ((TaskTracker) getTargetFragment()).hour = hourOfDay;
                    ((TaskTracker) getTargetFragment()).minute = minuteOfhour;

                    if (taskstatus == 1) {
                        Log.d("taskstaus must be 1", Integer.toString(taskstatus));
                        finishDateTime = c.getTimeInMillis()-32400000;
//                        finishDateTime = finishDateTime + (c.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (c.get(Calendar.MINUTE) * 60 * 1000)+32400000;

                        Log.d("STARTSIMPLEFORMAT : ", (new Date(startDateTime)).toString());
                        Log.d("FINISHSIMPLEFORMAT : ", (new Date(finishDateTime)).toString());
                        Log.d("finishDateTime : ", Long.toString(c.getTimeInMillis()));
                        Log.d("durationInMillis : ", Long.toString(finishDateTime-startDateTime));
                    }
                    else{
                        Log.d("taskstaus must be 0", Integer.toString(taskstatus));
                        startDateTime = c.getTimeInMillis()-32400000;
                        Log.d("STARTSIMPLEFORMAT : ", Long.toString(startDateTime));
                        Log.d("STARTSIMPLEFORMAT : ", (new Date(startDateTime)).toString());
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

    public static String getMyDate(String playtime) {
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

        String strDate = strYear+". "+strMonth + ". " + strDay;
        return strDate;
    }

    public static String getMyTime(String playtime) {
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
        String[] names;
        try {
            result = handler.execute(serverURL + "/courses/" + username).get();
            Log.d("GET COURSES RESULT", result);
            JSONObject userObj = new JSONObject(result);
            JSONArray courseArr = userObj.getJSONArray("courses");
            courses = new Course[courseArr.length()];
            names = new String[courseArr.length()];
            for (int i = 0; i < courseArr.length(); i++) {
                courses[i] = new Course();
                JSONObject obj = courseArr.getJSONObject(i);
                names[i] = obj.getString("name");
                courses[i].setName(obj.getString("name"));
                courses[i].setProfessor(obj.getString("professor"));
                courses[i].setCode(obj.getString("code"));
                courses[i].setSemester(obj.getString("semester"));
            }
            return names;
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0];
        }
    }
}

