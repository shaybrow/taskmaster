package com.shaybrow.taskmaster1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.MenuPopupWindow;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity {
    public static  String TAG = "shayapp.user";
//    public List<String> teamList = new ArrayList<>();
//    Handler userHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEditor = pref.edit();

        String username = pref.getString("username", "Guest");
        ((TextView)findViewById(R.id.usernameInput)).setText(pref.getString("username",username));
        ((TextView) findViewById(R.id.currentTeam)).setText("Current Team: " + pref.getString("team", "none"));
        Spinner spinner = findViewById(R.id.teamSpinner);
        String[] items = new String []{"The Powder Puff People", "Guardians of the Garage", "The Power Ragers"};
//        for (int i = 0; i < teamList.size(); i++) {
//            items[i]= teamList.get(i);
//            Log.i(TAG, "onCreate: ");
//        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        findViewById(R.id.usernameSubmit).setOnClickListener(view ->{
            String username1 = ((TextView)findViewById(R.id.usernameInput)).getText().toString();

            prefEditor.putString("username", username1);

            String team = spinner.getSelectedItem().toString();
            ((TextView) findViewById(R.id.currentTeam)).setText("Current Team: " + team);
            prefEditor.putString("team", team);

            prefEditor.apply();
            ((TextView) findViewById(R.id.usernameInput)).setText(username1);
        });
        Button goHome = findViewById(R.id.returnHome);

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserProfile.this, MainActivity.class);
//                where we're coming from, where we are going
                startActivity(intent);



            }
        });


//        userHandler = new Handler(this.getMainLooper()){
//            @Override
//            public void handleMessage(@NonNull Message message) {
//                super.handleMessage(message);
//                if (message.what == 6){
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        };
//        Amplify.API.query(
//                ModelQuery.list(Team.class),
//                response -> {
//                    for (Team team: response.getData()){
//                        teamList.add(team.getName());
//                    }
//
//                    userHandler.sendEmptyMessage(6);
//                },
//                response -> {}
//        );



    }
}