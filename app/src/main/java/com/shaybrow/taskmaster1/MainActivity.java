package com.shaybrow.taskmaster1;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.LoginTaskUser;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TaskListAdapter.ClickOnTaskAble {
    private static final int QUERYRESULT = 7;
    public static String TAG = "shayapp.main";
    static String OPENEDAPP = "OPENED_APP";
    //    TaskDatabase taskDatabase;
    public List<com.amplifyframework.datastore.generated.model.Task> tasks = new ArrayList<>();
    public List<Team> teamList = new ArrayList<>();
    Handler mainHandler;
    Date resumedTime;
    FusedLocationProviderClient fusedLocationProviderClient;
    Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reqPermissions();
        loadLocationClient();
        AmplifyConfig.configureAmplify(getApplication(), getApplicationContext());

        registerWithFirebaseAndPinpoint();

        AnalyticsEvent event = AnalyticsEvent.builder()
                .name(OPENEDAPP)
                .addProperty("shay", "is a pretty cool guy")

                .build();

        Amplify.Analytics.recordEvent(event);
        if (Amplify.Auth.getCurrentUser() != null) {
            String id = Amplify.Auth.getCurrentUser().getUserId();
            com.amplifyframework.analytics.UserProfile userProfile = com.amplifyframework.analytics.UserProfile.builder()
                    .email(Amplify.Auth.getCurrentUser().getUsername())
                    .build();
            Amplify.Analytics.identifyUser(id, userProfile);
        }
        AnalyticsEvent e = AnalyticsEvent.builder().name(OPENEDAPP).addProperty("name", "value").build();
        Amplify.Analytics.recordEvent(e);


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        //        for creating a layout view on a different page
        RecyclerView rv = findViewById(R.id.taskListView2);
        rv.setLayoutManager(new LinearLayoutManager(this));
//        List<Task> tasks = taskDatabase.taskDao().findAll();
        rv.setAdapter(new TaskListAdapter(this, tasks));
//      to do connect aws db


        mainHandler = new Handler(this.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message message) {
                super.handleMessage(message);
                if (message.what == 7) {
                    rv.getAdapter().notifyDataSetChanged();
                }
                if (message.what == 8) {
                    Amplify.API.query(
                            ModelQuery.list(Task.class, Task.TEAM.contains(teamList.get(0).getId())),
                            response -> {
                                for (Task task : response.getData()) {
                                    tasks.add(task);
                                }
                                mainHandler.sendEmptyMessage(7);
                            },
                            response -> {
                            }
                    );

                }
                if (message.what == 200)
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        };


        String team = pref.getString("team", null);
        if (team != null) {

            Amplify.API.query(
                    ModelQuery.list(Team.class, Team.NAME.contains(team)),
                    response -> {

                        for (Team team1 : response.getData()) {
                            Log.i(TAG, "onCreate: " + team1);
                            teamList.add(team1);
                        }
                        mainHandler.sendEmptyMessage(8);
                    },
                    response -> {
                    }
            );

        } else {
            Amplify.API.query(
                    ModelQuery.list(Task.class),
                    response -> {
                        for (Task task : response.getData()) {
                            tasks.add(task);
                        }
                        mainHandler.sendEmptyMessage(7);
                    },
                    response -> {
                    }
            );
        }


//          til you need both responses otherwise you'll get silly errors


//            taskDatabase = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "shaybrow_task_db")
//
//                    .allowMainThreadQueries()
//                    .build();
        //                    allowing main thread queries overrides main thread protection for this expensive operation


        String username = pref.getString("username", null);
        if (username != null)
            ((TextView) findViewById(R.id.usernameDisplay)).setText(String.format(Locale.ENGLISH, username + "'s tasks"));

// button onclick
//        get by id
//        add listener
//        callback
//        dostuff

//        Amplify.Storage.uploadFile("key", file, r->{},r-{})
        Button potatoAddTask = findViewById(R.id.addTask);
//        returns the type of the thing that it finds

        potatoAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goToAddTasks = new Intent(MainActivity.this, addTask.class);
//                where we're coming from, where we are going
                startActivity(goToAddTasks);


            }
        });


        Button allTasks = findViewById(R.id.allTasks);

        allTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAllTasks = new Intent(MainActivity.this, allTasks.class);
                startActivity(goToAllTasks);
            }
        });


        Button userProfile = findViewById(R.id.userProfileButton);
        userProfile.setOnClickListener(view -> {
            Intent goToUserProfile = new Intent(this, UserProfile.class);
            startActivity(goToUserProfile);
        });

        Button signup = findViewById(R.id.signupButtonLink);
        signup.setOnClickListener(view -> {
            Intent goToSignup = new Intent(this, Signup.class);
            startActivity(goToSignup);
        });
        Button login = findViewById(R.id.buttonLoginLink);
        login.setOnClickListener(view -> {
            Intent goToLogin = new Intent(this, LoginTaskUser.class);
            startActivity(goToLogin);
        });

    }
//    void configure(){
//        try {
//
//            Amplify.addPlugin(new AWSApiPlugin());
//            Amplify.addPlugin(new AWSCognitoAuthPlugin());
//            Amplify.addPlugin(new AWSS3StoragePlugin());
//            Amplify.addPlugin(new AWSPinpointAnalyticsPlugin(getApplication()));
//            Amplify.configure(getApplicationContext());
//
//
//        } catch (AmplifyException error) {
//
//        }
//    }


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
            File f = new File(getApplicationContext().getFilesDir(), "uploadingfile");
            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                FileUtils.copy(is, new FileOutputStream(f));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //    void saveFile (File file, String name){
//        Amplify.Storage.uploadFile(name, file,
//                r->{
//
//                },
//                r->{});
//    }
//    void downloadFile (String key){
//        Amplify.Storage.downloadFile(key, new File(getApplicationContext().getFilesDir(), key),
//                r->{
//            ImageView i = findViewById(R.id.testImage);
//            i.setImageBitmap(BitmapFactory.decodeFile(r.getFile().getPath()));
//            r.getFile();
//                },
//                r->{});
//    }
    void registerWithFirebaseAndPinpoint() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {


                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<String> task) { //TODO: make sure this is the non taskmaster Task
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        } else {
                            Log.i(TAG, "onComplete: firbaSE GOT A TOKEN");
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        resumedTime = new Date();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        AuthUser prince = Amplify.Auth.getCurrentUser();
        if (prince != null) {
            Button login = findViewById(R.id.buttonLoginLink);
            login.setOnClickListener(null);
            login.setVisibility(View.INVISIBLE);
            Button signup = findViewById(R.id.signupButtonLink);
            signup.setOnClickListener(null);
            signup.setVisibility(View.INVISIBLE);

            String email = prince.getUsername();
            ((TextView) findViewById(R.id.userEmailDisplay)).setText(email);
            Button logout = findViewById(R.id.buttonLogout);
            logout.setVisibility(View.VISIBLE);
            logout.setOnClickListener(v -> {

                Amplify.Auth.signOut(
                        () -> mainHandler.sendEmptyMessage(200),
                        error -> mainHandler.sendEmptyMessage(201)
                );
            });

        } else {
            Button logout = findViewById(R.id.buttonLogout);
            logout.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        AnalyticsTracking.getAnalyticsTracking().timeSpentOnPage(resumedTime, new Date(), "Main Activity");
    }

    @Override
    public void handleClickOnTask(TaskListAdapter.TaskViewHolder taskViewHolder) {
//        Toast.makeText(this, taskViewHolder.design, Toast.LENGTH_SHORT).show();
        Task task = taskViewHolder.task;

        Intent intent = new Intent(MainActivity.this, TaskDetails.class);
//        Log.i("something", " " + taskViewHolder);
        intent.putExtra("taskTitle", taskViewHolder.task.getTitle());
        intent.putExtra("taskBody", taskViewHolder.task.getBody());
        intent.putExtra("taskState", taskViewHolder.task.getState());
        intent.putExtra("taskId", taskViewHolder.task.getId());
        startActivity(intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void configureNotificationChannel() {
        String CHANNEL_ID = "100";
        NotificationChannel c = new NotificationChannel(CHANNEL_ID, "Taskmaster", NotificationManager.IMPORTANCE_DEFAULT);
        c.setDescription("it is what it is");
        NotificationManager n = getSystemService(NotificationManager.class);
        n.createNotificationChannel(c);
    }

    public void testNotification() {
//        Notification n = new Notification();
        NotificationCompat.Builder b = new NotificationCompat.Builder(this, "100").setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("FDSFDS").setContentText("Fsdfsdfds").setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, b.build());


    }

    public void reqPermissions() {
//        permissions in manifest
//        request
//        load FusedLocationProviderClient
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, 0);
    }

    void loadLocationClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
    }

    void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(loc -> { if (loc != null)Log.i(TAG, "getLocation: "+ loc);
            try {
                List <Address> address = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        })
        .addOnCanceledListener(() -> Log.i(TAG, "cancelled")).addOnFailureListener(e -> Log.i(TAG, "failed:" + e));
    }
    void getLocationUpdates(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                
                Log.i(TAG, "onLocationResult: "+ );
            }

        };

    }
}