package com.example.todo;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Adapter.TodoAdapter;
import com.example.todo.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView taskRecycle;
    private TodoAdapter tasksAdapter;
    private List<ModelTodo> task_List;
    private DatabaseHandler db;
    private FloatingActionButton fab;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        db.openDatabase();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        task_List = new ArrayList<>();
        taskRecycle = findViewById(R.id.recycleView);
        taskRecycle.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new TodoAdapter(db, MainActivity.this);
        taskRecycle.setAdapter(tasksAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecycle);
        fab = findViewById(R.id.fa);
        fab.setOnClickListener(v -> AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG));
        loadTasks();
    }

    private void loadTasks() {
        task_List = db.getAllTasks();
        Collections.reverse(task_List);
        tasksAdapter.setTasks(task_List);
        tasksAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        loadTasks();
    }
}
