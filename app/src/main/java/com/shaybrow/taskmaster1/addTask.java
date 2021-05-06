package com.shaybrow.taskmaster1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class addTask extends AppCompatActivity {
  static String TAG = "s.addTask";
    TaskDatabase taskDatabase;

    static List<Team> teams = new ArrayList<>();
    //    int count =0;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Spinner spinner = findViewById(R.id.teamSpinnerAdd);
        String[] items = new String []{"The Powder Puff People", "Guardians of the Garage", "The Power Ragers"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);


//        taskDatabase = Room.databaseBuilder(getApplicationContext(),TaskDatabase.class, "shaybrow_task_db" ).allowMainThreadQueries().build();
//        popUp = new PopupWindow(this);
//        TextView textView = new TextView(this);
        Button button = findViewById(R.id.submitTask);

        button.setOnClickListener( view -> {
            String team = spinner.getSelectedItem().toString();
            Team team1 = Team.builder().name("The Powder Puff People").build();



            String title = ((EditText) findViewById(R.id.taskTitle)).getText().toString();
            String body = ((EditText)findViewById(R.id.importTaskBody)).getText().toString();
            Task task = Task.builder()
            .title(title)
            .body(body).team(team1)
                    .build();

//            taskDatabase.taskDao().insert(task);

//            count ++ ;
//            String title = ((EditText)findViewById(R.id.taskTitle)).getText().toString();
//            String body = ((EditText)findViewById(R.id.taskBody)).getText().toString();
            ((TextView)findViewById(R.id.submitted)).setVisibility(View.VISIBLE);
//            ((TextView) findViewById(R.id.taskTotal)).setText(String.format(Locale.ENGLISH, "Total Tasks" + count));
            Amplify.API.mutate(ModelMutation.create(task),
                    response -> {
                        Log.i(TAG, "onCreate: added");
                    }, response ->{
                        Log.i(TAG, "onCreate: miss");
                    });

//            Intent intent = new Intent(addTask.this, allTasks.class);
//            intent.putExtra("taskTitle", title);
//            intent.putExtra("taskBody", body);
//            startActivity(intent);


        });
    }
    @Override
    protected void onResume (){
        super.onResume();
//        ((TextView) findViewById(R.id.taskTotal)).setText(String.format(Locale.ENGLISH, "Total Tasks" + count));
    }
}