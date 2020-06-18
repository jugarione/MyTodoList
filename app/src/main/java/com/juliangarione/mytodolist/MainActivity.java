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
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.juliangarione.mytodolist.RecyclerView.TaskAdapter;
import com.juliangarione.mytodolist.RecyclerView.TaskViewModel;
import com.juliangarione.mytodolist.RoomDatabase.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddTask = findViewById(R.id.button_add_task);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
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
                }
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);

                intent.putExtra(AddEditTaskActivity.EXTRA_ID, task.getId());
                intent.putExtra(AddEditTaskActivity.EXTRA_TITLE, task.getTaskTitle());
                intent.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, task.getTaskDescription());
                startActivityForResult(intent, EDIT_TASK_REQUEST);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);

            Task task = new Task(title, description);
            taskViewModel.insert(task);
            Toast.makeText(this, "Task saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditTaskActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Task can't be updated", Toast.LENGTH_SHORT).show();
            }

        String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
        String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);

        Task task = new Task(title, description);
        task.setId(id);
        taskViewModel.update(task);

        Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();

    } else {
            Toast.makeText(this, "Task not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_tasks:
                taskViewModel.deleteAllTask();
                Toast.makeText(MainActivity.this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

