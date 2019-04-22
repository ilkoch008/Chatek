package com.example.chatek;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import android.view.View;
import android.support.v7.widget.RecyclerView;


public class SocketThread extends Thread {

    int i = 0;
    private MyAdapter mAdapter = null;
    private MyMessagesAdapterAdapter mmAdapter = null;
    private Socket fromserver = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String ip = " ";
    private String nickName = " ";
    private String message = " ";
    private String incoming = " ";
    private int command;
    private int id;
    private int companionId;
    private  RecyclerView dialogRecView;
    private  RecyclerView mainRecView;
    ArrayList<Message> messages = new ArrayList<Message>();
    ArrayList<Message> newMessages = new ArrayList<Message>();
    ArrayList<Companion> companions = new ArrayList<Companion>();
    View dialogView = null;
    View mainView = null;
    /*
    1 - connect to server, get id, send nickname
    2 - get list of available companions
    3 - send message
    4 - renew dialog
    5 - wait and get companions
    6 - wait and renew dialog
    7 - check out new messages
    8 - check out new companions
    9 - choose companion
     */

    final static int CONNECT_TO_SERVER = 1;
    final static int GET_COMPANIONS = 2;
    final static int SEND_MESSAGE = 3;
    final static int RENEW_DIALOG = 4;
    final static int WAIT_GET_COMPANIONS = 5;
    final static int WAIT_RENEW_DIALOG = 6;
    final static int CHECK_NEW_MESSAGES = 7;
    final static int CHECK_NEW_COMPANIONS = 8;
    final static int CHOOSE_COMPANION = 9;

    synchronized public void setCommand(int command) {
        this.command = command;
    }
    synchronized public void setMessage(String message){
        this.message = message;
    }
    synchronized public void setIp(String ipAddress){this.ip = ipAddress;}
    synchronized public void setNickName(String nickName){this.nickName = nickName;}
    synchronized public void setmAdapter(MyAdapter mAdapter){this.mAdapter = mAdapter;}
    synchronized public void setMmAdapter(MyMessagesAdapterAdapter mmAdapter){this.mmAdapter = mmAdapter;}
    synchronized public void setDialogView(View dialogView){this.dialogView = dialogView;}
    synchronized public void setMainView(View mainView){this.mainView = mainView;}
    synchronized public void setDialogRecView(RecyclerView view){this.dialogRecView = view;}
    synchronized public void setMainRecView(RecyclerView view){this.mainRecView = view;}
    synchronized public void setCompaionId(Integer compaionId){this.companionId = compaionId;}

    @Override
    public void run() {
        while (true) {
            if (Thread.interrupted()) {
                interrupt();
            }
            JSONObject away = new JSONObject();
            JSONObject jIncoming = null;
            JSONArray jIncomingMessages = null;
            switch (command){
                case CONNECT_TO_SERVER:
                    try{
                        fromserver = new Socket(ip, 4444);
                        in = new BufferedReader(new InputStreamReader(fromserver.getInputStream()));
                        out = new PrintWriter(fromserver.getOutputStream(), true);
                        away.put("command", CONNECT_TO_SERVER);
                        away.put("data", nickName);
                        out.println(away.toString());
                        incoming = in.readLine();
                        jIncoming = new JSONObject(incoming);
                        id = jIncoming.getInt("data");
                        command = WAIT_GET_COMPANIONS; }
                        catch (IOException e){e.printStackTrace(); }
                        catch (JSONException e){e.printStackTrace();}
                    break;
                case GET_COMPANIONS:
                    try{
                        away.put("command", GET_COMPANIONS);
                        away.put("data", "get companions");
                        out.println(away.toString());
                        incoming = in.readLine();
                        jIncoming = new JSONObject(incoming);
                        jIncomingMessages = jIncoming.getJSONArray("companions");
                        int mmm = jIncomingMessages.length();
                        companions.clear();
                        for (int j = 0; j < mmm; j++) {
                            companions.add(new Companion(
                                    jIncomingMessages.getJSONObject(j).getString("name"),
                                    jIncomingMessages.getJSONObject(j).getInt("id"),
                                    jIncomingMessages.getJSONObject(j).getBoolean("availability")));
                        }
                        mAdapter.listt_define(companions);
                        mainView.post(new Runnable() {
                            @Override
                            public void run() {
                                mainRecView.getRecycledViewPool().clear();
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (IOException e){e.printStackTrace();}
                    catch (JSONException e){e.printStackTrace();}
                    command = WAIT_GET_COMPANIONS;
                    break;
                case SEND_MESSAGE:
                    String ans = " ";
                    try {
                        away.put("command", command);
                        away.put("message", message);
                        away.put("owner", nickName);
                        away.put("id", id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    out.println(away.toString());
                    try {
                        messages.clear();
                        incoming = in.readLine();
                        jIncomingMessages = new JSONArray(incoming);
                        for (int i = jIncomingMessages.length()-1; i >= 0; i--){
                            messages.add(new Message.Builder()
                            .idIs(jIncomingMessages.getJSONObject(i).getInt("id"))
                            .messageIs(jIncomingMessages.getJSONObject(i).getString("message"))
                            .ownerIs(jIncomingMessages.getJSONObject(i).getString("owner"))
                            .build());
                        }


                        mmAdapter.listt_define(messages);
                        dialogView.post(new Runnable() {
                            @Override
                            public void run() {
                                dialogRecView.getRecycledViewPool().clear();
                                mmAdapter.notifyDataSetChanged();
                            }
                        });
                        System.out.println();
                    } catch (IOException e) { e.printStackTrace(); }
                    catch (JSONException e){ e.printStackTrace(); }
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    command = RENEW_DIALOG;

                    break;
                case RENEW_DIALOG:
                    try {
                        away.put("command", command);
                        away.put("data", "renew dialog");
                        out.println(away.toString());
                        incoming = in.readLine();
                        jIncomingMessages = new JSONArray(incoming);
                        newMessages.clear();
                        messages.clear();
                        for (int i = jIncomingMessages.length()-1; i >= 0; i--){
                            newMessages.add(new Message.Builder()
                                    .idIs(jIncomingMessages.getJSONObject(i).getInt("id"))
                                    .messageIs(jIncomingMessages.getJSONObject(i).getString("message"))
                                    .ownerIs(jIncomingMessages.getJSONObject(i).getString("owner"))
                                    .build());
                        }
                        //if (mmAdapter.get_listt_size() != newMessages.size()) {
                            messages = newMessages;
                            mmAdapter.listt_define(messages);
                            dialogView.post(new Runnable() {
                                @Override
                                public void run() {
                                    dialogRecView.getRecycledViewPool().clear();
                                    mmAdapter.notifyDataSetChanged();
                                }
                            });
                        //}
                        //Thread.sleep(25);
                        System.out.println();
                    }
                        catch (JSONException e) { e.printStackTrace(); }
                        catch (IOException e) { e.printStackTrace(); }
                        //catch (InterruptedException e){e.printStackTrace();}
                        command = CHOOSE_COMPANION;
                    break;
                case WAIT_GET_COMPANIONS:
                    i++;
                    if(i<200) {
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        i=0;
                        command = GET_COMPANIONS;
                    }
                    break;
                case WAIT_RENEW_DIALOG:
                    i++;
                    if(i<200) {
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        i=0;
                        command = RENEW_DIALOG;
                    }
                    break;
                case CHOOSE_COMPANION:
                    try {
                        away.put("command", CHOOSE_COMPANION);
                        away.put("companionId", companionId);
                        out.println(away.toString());
                        incoming = in.readLine();
                        command = WAIT_RENEW_DIALOG;
                    } catch (JSONException e) { e.printStackTrace(); }
                    catch (IOException e) { e.printStackTrace(); }
                    break;
            }
        }

    }
}
