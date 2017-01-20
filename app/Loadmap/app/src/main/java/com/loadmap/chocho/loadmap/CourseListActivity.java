package com.loadmap.chocho.loadmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by q on 2017-01-20.
 */

public class CourseListActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_course_list);

        TextView statusView = (TextView) findViewById(R.id.status);

        Intent i = getIntent();
        statusView.setText(i.getStringExtra("status"));
    }
}
