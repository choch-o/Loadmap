package com.loadmap.chocho.loadmap;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by q on 2017-01-20.
 */

public class CourseListActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button confirmButton;

    private String mUsername;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_course_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        Intent intent = getIntent();
        mName = intent.getStringExtra("name");
        mUsername = intent.getStringExtra("username");
        Parcelable[] pCourses = intent.getParcelableArrayExtra(LoginActivity.PAR_KEY);

        final Course[] courses = new Course[pCourses.length];
        for (int i = 0; i < courses.length; i++) {
            courses[i] = (Course) pCourses[i];
            System.out.println(courses[i].getName());
        }
        mAdapter = new CourseAdapter(courses);
        mRecyclerView.setAdapter(mAdapter);

        confirmButton = (Button) findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCourseInfo(courses);
                Intent i = new Intent(CourseListActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    void postCourseInfo(Course[] courses) {
        String url = "http://52.78.52.132:3000" + "/courses";
        OkHttpHandler handler = new OkHttpHandler();
        String result = null;
        JSONObject obj = new JSONObject();
        JSONObject courseObj;
        try {
            for (int i = 0; i < courses.length; i++) {
                courseObj = new JSONObject();
                courseObj.put("name", courses[i].getName());
                courseObj.put("professor", courses[i].getProfessor());
                courseObj.put("code", courses[i].getCode());
                courseObj.put("semester", courses[i].getSemester());
                obj.accumulate("courses", courseObj);
                Log.d("COURSE INFO", obj.toString());
            }
            obj.put("username", mUsername);
            obj.put("name", mName);

            Log.d("COURSES IN JSON", obj.toString());
            result = handler.execute(url, "POST", obj.toString()).get();
            Log.d("RESULT", result);
            // JSONObject obj2 = new JSONObject(result);
            // Log.d("RESULT", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
