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

    static List<Team> teamList = new ArrayList<>();
    Handler addHandler;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Spinner spinner = findViewById(R.id.teamSpinnerAdd);
        addHandler = new Handler(this.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message message) {
                super.handleMessage(message);
                if (message.what == 8){
                    ArrayAdapter<Team> adapter = new ArrayAdapter<>(addTask.this, android.R.layout.simple_spinner_dropdown_item, teamList);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                }
            }
        };
            Amplify.API.query(ModelQuery.list(Team.class),
                    r -> {
                        for (Team t : r.getData()){
                            teamList.add(t);
                        }
                        addHandler.sendEmptyMessage(8);
                    },
                    r->{});



//        taskDatabase = Room.databaseBuilder(getApplicationContext(),TaskDatabase.class, "shaybrow_task_db" ).allowMainThreadQueries().build();

        Button button = findViewById(R.id.submitTask);

        button.setOnClickListener( view -> {
            String team = spinner.getSelectedItem().toString();
            Team t = (Team)spinner.getSelectedItem();



            String title = ((EditText) findViewById(R.id.taskTitle)).getText().toString();
            String body = ((EditText)findViewById(R.id.importTaskBody)).getText().toString();
            Task task = Task.builder()
            .title(title)
            .body(body).team(t)
                    .build();
            

//            taskDatabase.taskDao().insert(task);

            ((TextView)findViewById(R.id.submitted)).setVisibility(View.VISIBLE);
//            ((TextView) findViewById(R.id.taskTotal)).setText(String.format(Locale.ENGLISH, "Total Tasks" + count));
            Amplify.API.mutate(ModelMutation.create(task),
                    response -> {
//                        Log.i(TAG, "onCreate: added");
                    }, response ->{
//                        Log.i(TAG, "onCreate: miss");
                    });



        });
    }
    @Override
    protected void onResume (){
        super.onResume();
//        ((TextView) findViewById(R.id.taskTotal)).setText(String.format(Locale.ENGLISH, "Total Tasks" + count));
    }
}