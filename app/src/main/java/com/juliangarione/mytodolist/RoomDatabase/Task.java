package com.juliangarione.mytodolist.RoomDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {

    @PrimaryKey
    private int tid;

    @ColumnInfo(name = "task_title")
    private String taskTitle;

    @ColumnInfo(name = "task_description")
    private String taskDescription;

    public Task(String taskTitle, String taskDescription) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getTid() {
        return tid;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }
}
