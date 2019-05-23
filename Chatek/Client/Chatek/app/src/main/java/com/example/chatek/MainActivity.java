package com.example.chatek;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MainActivity extends AppCompatActivity {
    String CHANNEL_ID = "888";
    final int RUN_IN_APP = 1;
    final int RUN_IN_BACKGROUND = 2;
    public static final int NOTIFICATION_ID = 888;
    private NotificationManagerCompat mNotificationManagerCompat;
    final SocketThread socketThread = new SocketThread();

    final Router router = new Router(this, R.id.fragment_container);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        createNotificationChannel();
        super.onCreate(savedInstanceState);


        socketThread.setMainActivity(this);


        //final Router router = new Router(this, R.id.fragment_container);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        ProgressBar progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.INVISIBLE);
        socketThread.setProgressBar2(progressBar2);

        final HelloFragment hellofragment = new HelloFragment();
        router.navigateTo(false, hellofragment);
        final GetIpAndConnectFragment getIpAndConnectFragment = new GetIpAndConnectFragment();
        getIpAndConnectFragment.set_SocketThread(socketThread);
        getIpAndConnectFragment.set_mainActivity(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                router.navigateTo(false, getIpAndConnectFragment);
                socketThread.setRouter(router);
                getSupportActionBar().show();
            }
        }, 2000);
    }

//    public void makeToast(String toast){
//        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "ТЫК", Toast.LENGTH_SHORT).show();
            SettingsFragment settingsFragment = new SettingsFragment();
            router.navigateTo(true, settingsFragment);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void createNotification(){

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Chatek")
                .setContentText("У вас новые сообщения")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        Notification notification = builder.build();
        mNotificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

//    @Override
    public void onPause(){
        Integer isSaved = null;
        try {
            isSaved = this.getApplicationContext().openFileInput("isSaved").read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        String str = gson.toJson(socketThread.companions);
        if (isSaved == 1) {
            try {
                this.getApplicationContext().openFileOutput("companions.txt", Context.MODE_PRIVATE)
                        .write(gson.toJson(socketThread.companions).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onPause();
    }
//    @Override
//    public void onResume(){
//        super.onResume();
//        //SocketThread.checkForMessagesThread.setTypeOfRun(RUN_IN_APP);
//    }
    @Override
    public void onStop(){
        Integer isSaved = null;
        try {
            isSaved = this.getApplicationContext().openFileInput("isSaved").read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        String str = gson.toJson(socketThread.companions);
        if (isSaved == 1) {
            try {
                this.getApplicationContext().openFileOutput("companions.txt", Context.MODE_PRIVATE)
                        .write(gson.toJson(socketThread.companions).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onStop();

    }

    @Override
    public void onDestroy(){
        Integer isSaved = null;
        try {
            isSaved = this.getApplicationContext().openFileInput("isSaved").read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        String str = gson.toJson(socketThread.companions);
        if (isSaved == 1) {
            try {
                this.getApplicationContext().openFileOutput("companions.txt", Context.MODE_PRIVATE)
                        .write(gson.toJson(socketThread.companions).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
//    @Override
//    public void onStart(){
//        super.onStart();
//        //SocketThread.checkForMessagesThread.setTypeOfRun(RUN_IN_APP);
//    }
}
