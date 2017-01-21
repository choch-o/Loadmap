package com.loadmap.chocho.loadmap;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by q on 2017-01-19.
 */

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;



    // taskstatus 0 = 작업 종료, taskstatus 1 = 작업 중, taskstatus 2 = 작업 일시 정지
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

//        session = new SessionManager(getApplicationContext());
//
//        session.checkLogin();
//        HashMap<String, String> user = session.getUserDetails();
//        String name = user.get(SessionManager.KEY_NAME);
//        String username = user.get(SessionManager.KEY_USERNAME);
//
//        logoutButton = (Button) findViewById(R.id.logout_button);
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                session.logoutUser();
//            }
//        });
//
//        String[] option = getResources().getStringArray(R.array.spinnerArray);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                (this, android.R.layout.simple_spinner_dropdown_item, option);
//
//        Spinner spinner = (Spinner) findViewById(R.id.spinner);
//
//        dateView = (TextView) findViewById(R.id.dateView);
//        timeView = (TextView)findViewById(R.id.timeView);
//
//        startButton = (Button)findViewById(R.id.startButton);
//        pauseButton = (Button)findViewById(R.id.pauseButton);
//        resumeButton = (Button)findViewById(R.id.resumeButton);
//        stopButton = (Button)findViewById(R.id.stopButton);
//
//        date_change = (LinearLayout)findViewById(R.id.date_change);
//        time_change = (LinearLayout)findViewById(R.id.time_change);
//
//        TimeZone tz;
//        Date date = new Date();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss (z Z)");
//        tz = TimeZone.getTimeZone("Asia/Seoul"); df.setTimeZone(tz);
//
//        dateView.setText(getDate(df.format(date)));
//        timeView.setText(getTime(df.format(date)));
//
//        date_change.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm1 = getSupportFragmentManager();
//                MyDialogFragment1 dialogFragment1 = new MyDialogFragment1();
//                dialogFragment1.getActivity();
//                dialogFragment1.show(fm1, "fragment_date");
//            }
//        });
//
//        time_change.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm2 = getSupportFragmentManager();
//                MyDialogFragment2 dialogFragment2 = new MyDialogFragment2();
//                dialogFragment2.getActivity();
//                dialogFragment2.show(fm2, "fragment_time");
//            }
//        });



//        spinner.setAdapter(adapter);
//        getSpinner(R.id.spinner).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedView,
//                                       int position, long id) {
//                printChecked(selectedView, position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//
//            }
//        });
//
//        startButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startButton.setVisibility(View.GONE);
//                pauseButton.setVisibility(View.VISIBLE);
//                stopButton.setVisibility(View.VISIBLE);
//                taskstatus = 1;
//                randomKey = Math.random();
//                setVariables();
//                //서버로 {과목, 종류, 날짜, 시간, 시작/일시정지/재개/종료} 전송
//            }
//       });
//
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
//                pauseButton.setVisibility(View.VISIBLE);
//                stopButton.setVisibility(View.VISIBLE);
//                taskstatus = 1;
//                setVariables();
//                //서버로 {과목, 종류, 날짜, 시간, 시작/일시정지/재개/종료} 전송
//            }
//        });
//
//        stopButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startButton.setVisibility(View.VISIBLE);
//                pauseButton.setVisibility(View.GONE);
//                stopButton.setVisibility(View.GONE);
//                taskstatus = 0;
//                setVariables();
//                //서버로 {과목, 종류, 날짜, 시간, 시작/일시정지/재개/종료} 전송
//            }
//        });
//
//
//
//    }
//    public void printChecked(View v, int position){
//        Spinner spinnerPCheck = (Spinner)findViewById(R.id.spinner);
//        resultText="";
//        if(spinnerPCheck.getSelectedItemPosition()>0){
//            resultText=(String)spinnerPCheck.getAdapter().getItem(spinnerPCheck.getSelectedItemPosition());
//        }
//        if(resultText !=""){
////            ((TextView)findViewById(R.id.textView1)).setText(resultText);
//        }
//    }
//    public Spinner getSpinner(int objId){
//        return (Spinner) findViewById(objId);
//    }
//
//    public void setVariables() {
//        JsonObject json = new JsonObject();
//        try {
//            json.addProperty("randomkey", randomKey);
////            json.addProperty("IDnumber", MainActivity.IDnumber);
////            json.addProperty("username", MainActivity.username);
////            json.addProperty("semester", MainActivity.semester);
////            json.addProperty("professor", MainActivity.professor);
////            json.addProperty("subjectCode", MainActivity.subjectCode);
////            json.addProperty("subjectName", MainActivity.subjectName);
//            json.addProperty("tasktype", URLEncoder.encode(resultText, "utf-8"));
//            json.addProperty("date", date);
//            json.addProperty("time", time);
//            json.addProperty("taskstatus", taskstatus);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Ion.with(this.getApplication()).load(serverURL + "/task/data")
//                .setJsonObjectBody(json).asJsonObject()
//                .setCallback(new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//                        Toast.makeText(MainActivity.this, "SERVER CONNECTION is DONE", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    public void setDate(int yearOfCentury, int monthOfYear, int dayOfMonth) {
//        Log.d("setDate", "#" + yearOfCentury + "#" + monthOfYear + "#" + dayOfMonth);
//        this.year = yearOfCentury;
//        this.month = monthOfYear+1;
//        this.day = dayOfMonth;
//
//        dateView.setText("" + year + ". " + month + ". " + day + ".");
//    }
//
//    public void setTime(int hr, int mn) {
//        Log.d("setHour", "#" + hr + "#" + mn);
//        this.hour = hr;
//        this.minute = mn;
//        finedMinute = String.format("%02d", minute);
//
//        timeView.setText("" + hour + " : " + finedMinute);
//    }

//    public static class MyDialogFragment1 extends DialogFragment {
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View v = inflater.inflate(R.layout.fragment_date, container, false);
//
//            final DatePicker datePicker = (DatePicker) v.findViewById(R.id.datePicker);
//            Calendar c = Calendar.getInstance();
//            datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
//                @Override
//                public void onDateChanged(DatePicker view, int yearOfCentury, int monthOfYear, int dayOfMonth) {
//                    ((MainActivity)getActivity()).setDate(yearOfCentury, monthOfYear, dayOfMonth);
//                }
//            });
//            Button finishButton = (Button) v.findViewById(R.id.datebutton);
//            finishButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismiss();
//                }
//            });
//            return v;
//        }
//    }
//
//    public static class MyDialogFragment2 extends DialogFragment {
//
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View v = inflater.inflate(R.layout.fragment_time, container, false);
//
//            final TimePicker timePicker = (TimePicker)v.findViewById(R.id.timePicker);
//            timePicker.setIs24HourView(true);
//            Calendar c = Calendar.getInstance();
//            timePicker.setCurrentHour(c.get(Calendar.HOUR));
//            timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
//            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//                @Override
//                public void onTimeChanged(TimePicker view, int hourOfDay, int minuteOfhour) {
//                    ((MainActivity)getActivity()).setTime(hourOfDay, minuteOfhour);
//
//                }
//            });
//
//            Button finishButton2 = (Button)v.findViewById(R.id.timebutton);
//            finishButton2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismiss();
//                }
//            });
//            return v;
//        }
//    }
//
//    public static String getDate(String playtime) {
//        // TODO : Implement this method
//        // 2017-01-10T15:00:00.000Z
//        String strYear;
//        char[] charYear = new char [4];
//        playtime.getChars(0, 4, charYear, 0);
//            strYear = new String(charYear, 0, charYear.length);
//
//
//        String strMonth;
//        char[] charMonth = new char [2];
//        playtime.getChars(5, 7, charMonth, 0);
//
//        if(charMonth[0] == '0'){
//            char finedCharMonth = charMonth[1];
//            strMonth = Character.toString(finedCharMonth);
//        }
//        else{
//            strMonth = new String(charMonth, 0, charMonth.length);
//        }
//
//        String strDay;
//        char[] charDay = new char [2];
//        playtime.getChars(8, 10, charDay, 0);
//
//        if(charDay[0] == '0'){
//            char finedCharDay = charDay[1];
//            strDay = Character.toString(finedCharDay);
//        }
//        else{
//            strDay = new String(charDay, 0, charDay.length);
//        }
//
//
//
//        String strDate = strYear+"년 "+strMonth + "월 " + strDay + "일";
//        return strDate;
//    }

    public static String getTime(String playtime) {
        // 2017-01-10T15:00:00.000Z
        char[] charTime = new char [5];
        playtime.getChars(11, 16, charTime, 0);
        String strTime = "";
        strTime = new String(charTime, 0, charTime.length);

        return strTime;
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).


            if (position == 0) {
                return TaskTracker.newInstance(position+1);
            }
//
//            if (position == 1) {
//                return ImageList.newInstance();
//            }
            return PlaceholderFragment.newInstance(position+1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Task Tracking";
                case 1:
                    return "Statistics";
            }
            return null;
        }
    }
}
