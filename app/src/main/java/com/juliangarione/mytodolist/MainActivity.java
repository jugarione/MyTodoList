package com.juliangarione.mytodolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.juliangarione.mytodolist.RecyclerView.TaskAdapter;
import com.juliangarione.mytodolist.RecyclerView.TaskViewModel;
import com.juliangarione.mytodolist.RoomDatabase.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_TASK_REQUEST = 1;
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddTask = findViewById(R.id.button_add_task);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, ADD_TASK_REQUEST);
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        TaskAdapter adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getAllTask().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                // update RecyclerView
                adapter.setTasks(tasks);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Task Deleted", Toast.LENGTH_SHORT).show();
                break;
                    case ItemTouchHelper.RIGHT:
                        //TODO dar la funcion de enviar al dia siguiente.
                        Toast.makeText(MainActivity.this, "Ya va a volver, no pacho nada", Toast.LENGTH_SHORT).show();
                        break;
                }}}).attachToRecyclerView(recyclerView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddTaskActivity.EXTRA_DESCRIPTION);

            Task task = new Task(title, description);
            taskViewModel.insert(task);
            Toast.makeText(this, "Task saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
