package com.example.todo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.AddNewTask;
import com.example.todo.MainActivity;
import com.example.todo.ModelTodo;
import com.example.todo.R;
import com.example.todo.Utils.DatabaseHandler;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private List<ModelTodo> todoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public TodoAdapter(DatabaseHandler db,MainActivity activity){
        this.db=db;
        this.activity = activity;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_layout,parent,false);
        return new ViewHolder(itemView);
    }
    public void onBindViewHolder(ViewHolder holder,int position){
        db.openDatabase();
        ModelTodo item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }
    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<ModelTodo> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        ModelTodo item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ModelTodo item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.checkBox);
        }
    }
}