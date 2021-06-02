package com.shaybrow.taskmaster1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;

import com.shaybrow.taskmaster1.models.Task;

import java.util.List;

public class TaskList extends AppCompatActivity implements TaskListAdapter.ClickOnTaskAble{
//    TaskDatabase taskDatabase;
//    List<Task> tasks = taskDatabase.taskDao().findAll();
//TaskListAdapter taskListAdapter = new TaskListAdapter(this, tasks);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
//        taskDatabase = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "shaybrow_task_db").allowMainThreadQueries().build();
//        List<Task> tasks = taskDatabase.taskDao().findAll();
////        this creates our recyclerview
//        RecyclerView recyclerView = findViewById(R.id.taskListRecycle);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(taskListAdapter);

    }

    @Override
    public void handleClickOnTask(TaskListAdapter.TaskViewHolder taskViewHolder) {
//        do something here

    }
}