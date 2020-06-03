package com.juliangarione.mytodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.juliangarione.mytodolist.RoomDatabase.Task;
import com.juliangarione.mytodolist.RoomDatabase.TaskDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskDatabase db = Room.databaseBuilder(getApplicationContext(),
                TaskDatabase.class, "task-database").build();


    }
}
