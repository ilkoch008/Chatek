package com.example.chatek;

import android.support.design.widget.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.example.chatek.SocketThread.*;

public class CheckForMessagesThread extends Thread {
    final int RUN_IN_APP = 1;
    final int RUN_IN_BACKGROUND = 2;

    private Socket fromserver = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String ip = " ";
    private String incoming = " ";
    MainActivity mainActivity = null;

    private String password;
    private String nickName;

    private int oldNumberOfMessages = 0;
    private int newNumberOfMessages = 0;
    private int typeOfRun;

    public CheckForMessagesThread(String ip, String password, String nickName){
        this.ip = ip;
        this.password = password;
        this.nickName = nickName;
    }

    public void setTypeOfRun(int typeOfRun){
        this.typeOfRun = typeOfRun;
    }

    public void overlook(){
        oldNumberOfMessages = newNumberOfMessages;
    }

    public void incrementOld(int i){
        oldNumberOfMessages += i;
    }

    synchronized public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void run(){
        try {
            fromserver = new Socket(ip, 4444);
            in = new BufferedReader(new InputStreamReader(fromserver.getInputStream()));
            out = new PrintWriter(fromserver.getOutputStream(), true);
            JSONObject away = new JSONObject();
            JSONObject jIncoming = null;
            JSONArray jIncomingMessages = null;
            away.put("command", LOG_IN);
            away.put("nickName", nickName);
            away.put("password", password);
            out.println(away.toString());
            incoming = in.readLine();
            while (!isInterrupted()) {
                away.put("command", GET_DIFFERENCE_IN_DIALOGS);
                away.put("oldNum", oldNumberOfMessages);
                out.println(away.toString());
                incoming = in.readLine();
                jIncoming = new JSONObject(incoming);
                int diff = jIncoming.getInt("diff");

                if (diff > 0) {
                    // switch (typeOfRun) {
                    //case RUN_IN_APP:
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(mainActivity.findViewById(R.id.fragment_container), "Вам пришли новые сообщения :)", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                    //overlook();
                    //break;
                    //case RUN_IN_BACKGROUND:
                    mainActivity.createNotification();
                    oldNumberOfMessages+=diff;
                    //break;
                }
                Thread.sleep(2000);
            }


        //}

        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
