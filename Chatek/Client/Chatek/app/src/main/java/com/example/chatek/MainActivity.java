package com.example.chatek;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String CHANNEL_ID = "888";
    final int RUN_IN_APP = 1;
    final int RUN_IN_BACKGROUND = 2;
    public static final int NOTIFICATION_ID = 888;
    private NotificationManagerCompat mNotificationManagerCompat;

    final Router router = new Router(this, R.id.fragment_container);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        createNotificationChannel();
        super.onCreate(savedInstanceState);

        final SocketThread socketThread = new SocketThread();
        socketThread.setMainActivity(this);


        //final Router router = new Router(this, R.id.fragment_container);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.send)
                .setContentTitle("Chatek")
                .setContentText("У вас новые сообщения")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Notification notification = builder.build();
        mNotificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

//    @Override
//    public void onPause(){
//        super.onPause();
//        SocketThread.checkForMessagesThread.setTypeOfRun(RUN_IN_BACKGROUND);
//    }
//    @Override
//    public void onResume(){
//        super.onResume();
//        //SocketThread.checkForMessagesThread.setTypeOfRun(RUN_IN_APP);
//    }
//    @Override
//    public void onStop(){
//        super.onStop();
//        SocketThread.checkForMessagesThread.setTypeOfRun(RUN_IN_BACKGROUND);
//    }
//    @Override
//    public void onStart(){
//        super.onStart();
//        //SocketThread.checkForMessagesThread.setTypeOfRun(RUN_IN_APP);
//    }
}
