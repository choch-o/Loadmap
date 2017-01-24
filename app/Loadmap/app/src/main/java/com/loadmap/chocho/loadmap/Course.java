package com.loadmap.chocho.loadmap;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by q on 2017-01-21.
 */

public class Course implements Parcelable {
    private String name;
    private String code;
    private String professor;
    private String semester;
    private int period;
    private TaskType[] taskTypes;
    private long totalDuration;
    private String _id;

    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }
    public String getName() {
        return name;
    }
    public void setName(String name) {this.name = name;}
    public int getPeriod() {
        return period;
    }
    public void setPeriod(int period) {
        this.period = period;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getProfessor() {
        return professor;
    }
    public void setProfessor(String professor) {
        this.professor = professor;
    }
    public String getSemester() {
        return semester;
    }
    public void setSemester(String semester) {
        this.semester = semester;
    }
    public TaskType[] getTasks() { return taskTypes; }
    public void setTasks(TaskType[] tasks) { this.taskTypes = taskTypes; }
    public long getTotalDuration() { return totalDuration; }
    public void setTotalDuration(long totalDuration) { this.totalDuration = totalDuration; }

    public static final Parcelable.Creator<Course> CREATOR
            = new Creator<Course>() {
        public Course createFromParcel(Parcel source) {
            Course mCourse = new Course();
            mCourse.name = source.readString();
            mCourse.professor = source.readString();
            mCourse.code = source.readString();
            mCourse.semester = source.readString();
            return mCourse;
        }

        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(professor);
        parcel.writeString(code);
        parcel.writeString(semester);
    }

}
