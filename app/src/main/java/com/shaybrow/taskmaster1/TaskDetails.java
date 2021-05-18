package com.shaybrow.taskmaster1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

import java.io.File;

public class TaskDetails extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        AmplifyConfig.configureAmplify(getApplication(), getApplicationContext());

        Intent loadIntent = getIntent();
        ((TextView)findViewById(R.id.importTaskTitle)).setText(loadIntent.getStringExtra("taskTitle"));
        ((TextView)findViewById(R.id.importTaskBody)).setText(loadIntent.getStringExtra("taskBody"));
        ((TextView)findViewById(R.id.importTaskState)).setText(loadIntent.getStringExtra("taskState"));
        String id = loadIntent.getStringExtra("taskId");
        downloadFile(id);

    }
    void downloadFile (String key){
        Amplify.Storage.downloadFile(key, new File(getApplicationContext().getFilesDir(), key),
                r->{
                    ImageView i = findViewById(R.id.imageTaskDetail);
                    if (r.getFile() != null){
                        i.setImageBitmap(BitmapFactory.decodeFile(r.getFile().getPath()));
                        r.getFile();
                    }

                },
                r->{});
    }
}