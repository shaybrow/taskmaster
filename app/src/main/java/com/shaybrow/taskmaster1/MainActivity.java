package com.shaybrow.taskmaster1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TaskListAdapter.ClickOnTaskAble {
    public static  String TAG = "shayapp.main";
    private static final int QUERYRESULT = 7;
//    TaskDatabase taskDatabase;
    public List<com.amplifyframework.datastore.generated.model.Task>tasks = new ArrayList<>();
    public List<Team> teamList = new ArrayList<>();
    Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        //        for creating a layout view on a different page
        RecyclerView rv = findViewById(R.id.taskListView2);
        rv.setLayoutManager(new LinearLayoutManager(this));
//        List<Task> tasks = taskDatabase.taskDao().findAll();
        rv.setAdapter(new TaskListAdapter(this, tasks));
//      to do connect aws db


        mainHandler = new Handler(this.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message message) {
                super.handleMessage(message);
                if (message.what == 7){
                    rv.getAdapter().notifyDataSetChanged();
                }
                if (message.what == 8){
                    Amplify.API.query(
                ModelQuery.list(Task.class, Task.TEAM.contains(teamList.get(0).getId())),
                response -> {
          for (Task task : response.getData()){
              tasks.add(task);
          }
          mainHandler.sendEmptyMessage(7);
                },
                response -> {}
        );

                }
            }
        };

//        Amplify.API.query(
//                ModelQuery.list(Task.class, Task.TEAM.contains(teamList.get(0).getId())),
//                response -> {
//          for (Task task : response.getData()){
//              tasks.add(task);
//          }
//          mainHandler.sendEmptyMessage(7);
//                },
//                response -> {}
//        );

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());


//            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
//            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
//        Amplify.API.query(
//                ModelQuery.list(Team.class, Team.NAME.contains(team)),
//                response -> {
//                    for (Team team1 : response.getData()){
//                        teams.add(team1);
//                    }
//
//                },
//                response -> {}
//        );
//
        String team = pref.getString("team", null);
        if (team != null){

            Log.i(TAG, "onCreate: "+ team);
        Amplify.API.query(
                ModelQuery.list(Team.class, Team.NAME.contains(team)),
                response -> {
                    Log.i(TAG, "onCreate: " + response.toString());
                    for (Team team1 : response.getData()){
                        Log.i(TAG, "onCreate: "+ team1);
                        teamList.add(team1);
                    }
                    mainHandler.sendEmptyMessage(8);
                },
                response -> {}
        );

        } else {
            Amplify.API.query(
                    ModelQuery.list(Task.class),
                    response -> {
                        for (Task task : response.getData()){
                            tasks.add(task);
                        }
                        mainHandler.sendEmptyMessage(7);
                    },
                    response -> {}
            );
        }


//          til you need both responses otherwise you'll get silly errors




//            taskDatabase = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "shaybrow_task_db")
//
//                    .allowMainThreadQueries()
//                    .build();
        //                    allowing main thread queries overrides main thread protection for this expensive operation


        String username = pref.getString("username", null);
        if (username != null) ((TextView)findViewById(R.id.usernameDisplay)).setText(String.format(Locale.ENGLISH, username + "'s tasks"));

// button onclick
//        get by id
//        add listener
//        callback
//        dostuff

        Button potatoAddTask = findViewById(R.id.addTask);
//        returns the type of the thing that it finds

        potatoAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i(TAG, "This is how we long in 2021");
                Intent goToAddTasks = new Intent(MainActivity.this, addTask.class);
//                where we're coming from, where we are going
                startActivity(goToAddTasks);



            }
        });


        Button allTasks = findViewById(R.id.allTasks);

        allTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAllTasks = new Intent(MainActivity.this, allTasks.class );
                startActivity(goToAllTasks);
            }
        });



//        Button recycle = findViewById(R.id.recyclerView);
//
//        recycle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, allTasks.class );
//                startActivity(intent);
//            }
//        });


        Button userProfile = findViewById(R.id.userProfileButton);
        userProfile.setOnClickListener(view ->{
            Intent goToUserProfile = new Intent(this, UserProfile.class);
            startActivity(goToUserProfile);
        });

        Button getAJob = findViewById(R.id.getAJob);
        getAJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToGetAJob = new Intent(MainActivity.this, TaskDetails.class);
                String title = ((Button)findViewById(R.id.getAJob)).getText().toString();
                goToGetAJob.putExtra("taskTitle", title);
                startActivity(goToGetAJob);



            }
        });
        Button mealPrep = findViewById(R.id.mealPrep);
        mealPrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMealPrep = new Intent(MainActivity.this, TaskDetails.class);
                String title = ((Button)findViewById(R.id.mealPrep)).getText().toString();
                goMealPrep.putExtra("taskTitle", title);
                startActivity(goMealPrep);


            }
        });
        Button sleep = findViewById(R.id.sleep);
        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSleep = new Intent(MainActivity.this, TaskDetails.class);
                String title = ((Button)findViewById(R.id.sleep)).getText().toString();
                goSleep.putExtra("taskTitle", title);
                startActivity(goSleep);


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    public void handleClickOnTask(TaskListAdapter.TaskViewHolder taskViewHolder) {
//        Toast.makeText(this, taskViewHolder.design, Toast.LENGTH_SHORT).show();
        Task task = taskViewHolder.task;

        Intent intent = new Intent(MainActivity.this, TaskDetails.class);
//        Log.i("something", " " + taskViewHolder);
//        intent.putExtra("taskTitle", taskViewHolder.task.getTitle());
//        intent.putExtra("taskBody", taskViewHolder.task.getBody());
//        intent.putExtra("taskState", taskViewHolder.task.getState());
        startActivity(intent);

    }
}