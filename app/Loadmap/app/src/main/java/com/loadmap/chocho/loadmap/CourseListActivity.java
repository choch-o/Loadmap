package com.loadmap.chocho.loadmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by q on 2017-01-20.
 */

public class CourseListActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        Parcelable[] pCourses = getIntent().getParcelableArrayExtra(LoginActivity.PAR_KEY);
        Course[] courses = new Course[pCourses.length];
        for (int i = 0; i < courses.length; i++) {
            courses[i] = (Course) pCourses[i];
            System.out.println(courses[i]);
        }
        mAdapter = new CourseAdapter(courses);
        mRecyclerView.setAdapter(mAdapter);


    }
}
