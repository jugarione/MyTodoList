package com.juliangarione.mytodolist.RoomDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {

    @PrimaryKey
    public int tid;

    @ColumnInfo(name = "task_title")
    public String taskTitle;

    @ColumnInfo(name = "task_description")
    public String taskDescription;
}
