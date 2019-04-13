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
    ArrayList<Message> messages = new ArrayList<>();
    ArrayList<Message> newMessages = new ArrayList<>();
    ArrayList<Companion> companions = new ArrayList<>();
    View dialogView = null;
    View mainView = null;
    /*
    1 - connect to server, get id, send nickname
    2 - get list of available companions
    3 - send message
    4 - renew dialog
    5 - wait
    6 - wait and renew dialog
    7 - check out new messages
    8 - check out new companions
    9 - choose companion
     */

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
                case 1:
                    try{
                        fromserver = new Socket(ip, 4444);
                        in = new BufferedReader(new InputStreamReader(fromserver.getInputStream()));
                        out = new PrintWriter(fromserver.getOutputStream(), true);
                        away.put("command", 1);
                        away.put("data", nickName);
                        out.println(away.toString());
                        incoming = in.readLine();
                        jIncoming = new JSONObject(incoming);
                        id = jIncoming.getInt("data");
                        command = 5; }
                        catch (IOException e){e.printStackTrace(); }
                        catch (JSONException e){e.printStackTrace();}
                    break;
                case 2:
                    try{
                        away.put("command", 2);
                        away.put("data", "get companions");
                        out.println(away.toString());
                        incoming = in.readLine();
                        jIncomingMessages = new JSONArray(incoming);
                        int mmm = jIncomingMessages.length();
                        for (int j = 0; j < mmm; j++) {
                            companions.add(new Companion(
                                    jIncomingMessages.getJSONObject(i).getString("name"),
                                    jIncomingMessages.getJSONObject(i).getInt("id")));
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
                    break;
                case 3:
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
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    command = 4;

                    break;
                case 4:
                    try {
                        away.put("command", command);
                        away.put("data", "renew dialog");
                        out.println(away.toString());
                        incoming = in.readLine();
                        jIncomingMessages = new JSONArray(incoming);
                        newMessages.clear();
                        for (int i = jIncomingMessages.length()-1; i >= 0; i--){
                            newMessages.add(new Message.Builder()
                                    .idIs(jIncomingMessages.getJSONObject(i).getInt("id"))
                                    .messageIs(jIncomingMessages.getJSONObject(i).getString("message"))
                                    .ownerIs(jIncomingMessages.getJSONObject(i).getString("owner"))
                                    .build());
                        }
                        if (mmAdapter.get_listt_size() != newMessages.size()) {
                            messages = newMessages;
                            mmAdapter.listt_define(messages);
                            dialogView.post(new Runnable() {
                                @Override
                                public void run() {
                                    dialogRecView.getRecycledViewPool().clear();
                                    mmAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                        Thread.sleep(25);
                        System.out.println();
                    }
                        catch (JSONException e) { e.printStackTrace(); }
                        catch (IOException e) { e.printStackTrace(); }
                        catch (InterruptedException e){e.printStackTrace();}
                        command = 6;
                    break;
                case 5:
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    i++;
                    if(i<40) {
                        try {
                            Thread.sleep(25);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        i=0;
                        command = 4;
                    }
                    break;
                case 9:
                    try {
                        away.put("command", 9);
                        away.put("companionId", companionId);
                        out.println(away.toString());
                        incoming = in.readLine();
                        command = 4;
                    } catch (JSONException e) { e.printStackTrace(); }
                    catch (IOException e) { e.printStackTrace(); }
                    break;
            }
        }

    }
}
