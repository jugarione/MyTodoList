package com.juliangarione.mytodolist.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.juliangarione.mytodolist.R;
import com.juliangarione.mytodolist.RoomDatabase.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private List<Task> tasks = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.textViewTitle.setText(currentTask.getTaskTitle());
        holder.textViewDescription.setText(currentTask.getTaskDescription());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public Task getTaskAt(int position) {
        return tasks.get(position);
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.task_title);
            textViewDescription = itemView.findViewById(R.id.task_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(tasks.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
