package com.example.todoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTask;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.Task;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> tasks = new ArrayList<>();
    private MainActivity activity;
    private DatabaseHandler db;

    public TaskAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasks_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        db.openDatabase();
        Task task = tasks.get(position);
        holder.task.setText(task.getTaskName());
        holder.task.setChecked(toBoolean(task.getStatus()));
        holder.task.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                db.updateStatus(task.getId(), 1);
            } else {
                db.updateStatus(task.getId(), 0);
            }
        });
    }

    private boolean toBoolean(int n) {
        return  n != 0;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return tasks != null ? tasks.size() : 0;
    }

    public Context getContext() {
        return activity;
    }

    public void deleteItem(int position) {
        Task task = tasks.get(position);
        db.deleteTask(task.getId());
        tasks.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        Task task = tasks.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", task.getId());
        bundle.putString("task", task.getTaskName());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
