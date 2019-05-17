package com.example.chatek;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

    final Router router = new Router(this, R.id.fragment_container);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
}
