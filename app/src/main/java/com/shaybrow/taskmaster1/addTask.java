package com.shaybrow.taskmaster1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Message;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class addTask extends AppCompatActivity {
    static List<Team> teamList = new ArrayList<>();
    static String TAG = "s.addTask";
    File fileToUpload;
    TaskDatabase taskDatabase;
    Handler addHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        AmplifyConfig.configureAmplify(getApplication(), getApplicationContext());
        try {
            checkIntentData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Spinner spinner = findViewById(R.id.teamSpinnerAdd);
        addHandler = new Handler(this.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message message) {
                super.handleMessage(message);
                if (message.what == 8) {
                    ArrayAdapter<Team> adapter = new ArrayAdapter<>(addTask.this, android.R.layout.simple_spinner_dropdown_item, teamList);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                }
            }
        };
        Amplify.API.query(ModelQuery.list(Team.class),
                r -> {
                    for (Team t : r.getData()) {
                        teamList.add(t);
                    }
                    addHandler.sendEmptyMessage(8);
                },
                r -> {
                });


//        taskDatabase = Room.databaseBuilder(getApplicationContext(),TaskDatabase.class, "shaybrow_task_db" ).allowMainThreadQueries().build();

        Button button = findViewById(R.id.submitTask);

        button.setOnClickListener(view -> {
            String team = spinner.getSelectedItem().toString();
            Team t = (Team) spinner.getSelectedItem();


            String title = ((EditText) findViewById(R.id.taskTitle)).getText().toString();
            String body = ((EditText) findViewById(R.id.importTaskBody)).getText().toString();
            Task task = Task.builder()
                    .title(title)
                    .body(body).team(t)
                    .build();
            saveFile(fileToUpload, task.getId());

//            taskDatabase.taskDao().insert(task);

            findViewById(R.id.submitted).setVisibility(View.VISIBLE);
            Amplify.API.mutate(ModelMutation.create(task),
                    response -> {
//                        Log.i(TAG, "onCreate: added");
                    }, response -> {
//                        Log.i(TAG, "onCreate: miss");
                    });


        });
        Button button1 = findViewById(R.id.buttonAddImage);

        button1.setOnClickListener(view -> {

            getImageFromPhone();


        });
    }

    void getImageFromPhone() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*"); //single type
//        i.putExtra(Intent.EXTRA_MIME_TYPES, new String []{".jpg", ".png", ".pdf"}); // multiple file types
        startActivityForResult(i, 9);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9) {
            fileToUpload = new File(getApplicationContext().getFilesDir(), "uploadingfile");
            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                FileUtils.copy(is, new FileOutputStream(fileToUpload));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void saveFile(File file, String key) {
        Amplify.Storage.uploadFile(key, file,
                r -> {

                },
                r -> {
                });
    }

//    void downloadFile(String key) {
//        Amplify.Storage.downloadFile(key, new File(getApplicationContext().getFilesDir(), key),
//                r -> {
//                    ImageView i = findViewById(R.id.testImage);
//                    i.setImageBitmap(BitmapFactory.decodeFile(r.getFile().getPath()));
//                    r.getFile();
//                },
//                r -> {
//                });
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        ((TextView) findViewById(R.id.taskTotal)).setText(String.format(Locale.ENGLISH, "Total Tasks" + count));
    }


    void checkIntentData() throws IOException {
        Intent i = getIntent();
        if (i.getType().startsWith("image/")) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            Uri uri = i.getParcelableExtra(Intent.EXTRA_STREAM);
//            todo load image from intent
            loadImageFromIntent(uri);
        }
        }else if (i.getType().startsWith("test/plain")) {
            String text = i.getStringExtra(Intent.EXTRA_TEXT);
            ((EditText) findViewById(R.id.taskTitle)).setText(text);

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    void loadImageFromIntent (Uri uri) throws IOException {
        fileToUpload = new File(getApplicationContext().getFilesDir(), "fileName");

        try{
            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileUtils.copy(inputStream, new FileOutputStream(fileToUpload));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}