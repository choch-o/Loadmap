package com.loadmap.chocho.loadmap;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by q on 2017-01-21.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private Course[] courses;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView, professorView, codeView, semesterView;
        public ViewHolder(View v) {
            super(v);
            nameView = (TextView) v.findViewById(R.id.course_name);
            professorView = (TextView) v.findViewById(R.id.course_professor);
            codeView = (TextView) v.findViewById(R.id.course_code);
            semesterView = (TextView) v.findViewById(R.id.course_semester);
        }
    }
    public CourseAdapter(Course[] courseSet) {
        this.courses = courseSet;
    }

    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Course course = courses[position];
        holder.nameView.setText(course.getName());
        holder.professorView.setText(course.getProfessor());
        holder.codeView.setText(course.getCode());
        holder.semesterView.setText(course.getSemester());
    }

    @Override
    public int getItemCount() {
        return courses.length;
    }
}
