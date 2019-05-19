package com.example.chatek;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


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
    private String password = " ";
    private int command;
    private int command_from_server;
    public int id;
    private int companionId;
    private  RecyclerView dialogRecView;
    private  RecyclerView mainRecView;
    private boolean connected = false;
    private int chosenCompanion=-1;
    ArrayList<Message> messages = new ArrayList<Message>();
    ArrayList<Message> newMessages = new ArrayList<Message>();
    ArrayList<Companion> companions = new ArrayList<Companion>();
    View dialogView = null;
    View mainView = null;
    MainActivity mainActivity = null;
    Context currentContext = null;
    SocketThread socketThread = this;
    Router router = null;
    public static CheckForMessagesThread checkForMessagesThread = null;
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

    final static int WAIT = 0;
    final static int CONNECT_TO_SERVER = 1;
    final static int LOG_IN = 11;
    final static int REGISTER = 12;
    final static int THIS_NAME_ALREADY_EXISTS = 13;
    final static int ACCOUNT_IS_NOT_FOUND = 14;
    final static int WRONG_PASSWORD = 15;
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
    synchronized public void setPassword(String password){this.password = password;}
    synchronized public void setMainActivity(MainActivity mainActivity){this.mainActivity = mainActivity;}
    synchronized public void setRouter(Router router){this.router = router;}
    synchronized public void setContext(Context context){this.currentContext = context;}
    synchronized public boolean isConnected(){return connected;}
    synchronized public void setTheChosenOne(Integer chosen){this.chosenCompanion = chosen;}

    @Override
    public void run() {
        while (true) {
            if (Thread.interrupted()) {
                break;
            }
            JSONObject away = new JSONObject();
            JSONObject jIncoming = null;
            JSONArray jIncomingMessages = null;
            switch (command){
                case WAIT:
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case CONNECT_TO_SERVER:
                    try{
                        fromserver = new Socket(ip, 4444);
                        in = new BufferedReader(new InputStreamReader(fromserver.getInputStream()));
                        out = new PrintWriter(fromserver.getOutputStream(), true);
                        connected = true;
                        final LogInFragment logInFragment = new LogInFragment();
                        logInFragment.set_SocketThread(socketThread);
                        router.navigateTo(false, logInFragment);
                        Thread.sleep(5);
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //mainActivity.makeToast("Connected successfully");
                                Toast.makeText(logInFragment.requireContext(), "Connected successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                        command = WAIT;
                    }
                        catch (IOException e){e.printStackTrace(); }
                        catch (InterruptedException e){e.printStackTrace();}
                    break;
                case LOG_IN:
                    try {
                        away.put("command", LOG_IN);
                        away.put("nickName", nickName);
                        away.put("password", password);
                        out.println(away.toString());
                        incoming = in.readLine();
                        jIncoming = new JSONObject(incoming);
                        command_from_server = jIncoming.getInt("command");
                        if (command_from_server == 0) {
                            id = jIncoming.getInt("clientId");
                            MainFragment mainFragment = new MainFragment();
                            mainFragment.set_SocketThread(socketThread);
                            router.navigateTo(false, mainFragment);
                            command = GET_COMPANIONS;
                            checkForMessagesThread = new CheckForMessagesThread(ip, password, nickName);
                            checkForMessagesThread.setMainActivity(mainActivity);
                            checkForMessagesThread.setTypeOfRun(checkForMessagesThread.RUN_IN_APP);
                            checkForMessagesThread.start();
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //mainActivity.makeToast("Logged in");
                                    Toast.makeText(currentContext, "Logged in", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        Thread.yield();
                        if (command_from_server == ACCOUNT_IS_NOT_FOUND){
                            command = WAIT;
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //mainActivity.makeToast("Account is not found");
                                    Toast.makeText(currentContext, "Account is not found", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (command_from_server == WRONG_PASSWORD){
                            command = WAIT;
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //mainActivity.makeToast("Wrong password");
                                    Toast.makeText(currentContext, "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    break;
                case REGISTER:
                    try {
                        away.put("command", REGISTER);
                        away.put("nickName", nickName);
                        away.put("password", password);
                        out.println(away.toString());
                        incoming = in.readLine();
                        jIncoming = new JSONObject(incoming);
                        command_from_server = jIncoming.getInt("command");
                        if(command_from_server == THIS_NAME_ALREADY_EXISTS) {
                            command = WAIT;
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //mainActivity.makeToast("This name already exists");
                                    Toast.makeText(currentContext, "This name already exists", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Thread.yield();
                        }
                        if(command_from_server == 0) {
                            MainFragment mainFragment = new MainFragment();
                            mainFragment.set_SocketThread(socketThread);
                            router.navigateTo(false, mainFragment);
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //mainActivity.makeToast("Registered");
                                    Toast.makeText(currentContext, "Registered", Toast.LENGTH_SHORT).show();
                                }
                            });
                            id = jIncoming.getInt("clientId");
                            checkForMessagesThread = new CheckForMessagesThread(ip, password, nickName);
                            checkForMessagesThread.setMainActivity(mainActivity);
                            checkForMessagesThread.setTypeOfRun(checkForMessagesThread.RUN_IN_APP);
                            checkForMessagesThread.start();
                            command = GET_COMPANIONS;
                            Thread.yield();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
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
                        ArrayList<Integer> oldNumbersOfMessages = new ArrayList<>();
                        for (Companion i: companions) {
                            oldNumbersOfMessages.add(i.getOldNumberOfMessages());
                        }
                        Integer maxOld = oldNumbersOfMessages.size();
                        companions.clear();
                        for (int j = 0; j < mmm; j++) {
                            companions.add(new Companion(
                                    jIncomingMessages.getJSONObject(j).getString("name"),
                                    jIncomingMessages.getJSONObject(j).getInt("id"),
                                    jIncomingMessages.getJSONObject(j).getBoolean("availability"),
                                    jIncomingMessages.getJSONObject(j).getInt("numberOfMessages")));
                            if(j < maxOld){
                                companions.get(j).setOldNumberOfMessages(oldNumbersOfMessages.get(j));
                            }
                        }
                        if(companions != null && mAdapter != null) {
                            mAdapter.listt_define(companions);
                            mainView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mainRecView.getRecycledViewPool().clear();
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                        if(chosenCompanion>=0) {
                            companions.get(chosenCompanion).overlook();
                            chosenCompanion = -1;
                        }
                        Thread.yield();
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
                        for (int i = jIncomingMessages.length() - 1; i >= 0; i--) {
                            messages.add(new Message.Builder()
                                    .idIs(jIncomingMessages.getJSONObject(i).getInt("id"))
                                    .messageIs(jIncomingMessages.getJSONObject(i).getString("message"))
                                    .ownerIs(jIncomingMessages.getJSONObject(i).getString("owner"))
                                    .timeIs(jIncomingMessages.getJSONObject(i).getString("time"))
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
                        int diff = messages.size();
                        newMessages.clear();
                        messages.clear();
                        for (int i = jIncomingMessages.length() - 1; i >= 0; i--) {
                            newMessages.add(new Message.Builder()
                                    .idIs(jIncomingMessages.getJSONObject(i).getInt("id"))
                                    .messageIs(jIncomingMessages.getJSONObject(i).getString("message"))
                                    .ownerIs(jIncomingMessages.getJSONObject(i).getString("owner"))
                                    .timeIs(jIncomingMessages.getJSONObject(i).getString("time"))
                                    .build());
                        }
                        diff = newMessages.size() - diff;
                        checkForMessagesThread.incrementOld(diff);
                        messages = newMessages;
                        if(chosenCompanion>=0) {
                            companions.get(chosenCompanion).overlook();
                        }
                        if (mmAdapter != null) {
                            mmAdapter.listt_define(messages);
                            dialogView.post(new Runnable() {
                                @Override
                                public void run() {
                                    dialogRecView.getRecycledViewPool().clear();
                                    mmAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                        //System.out.println();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //catch (InterruptedException e){e.printStackTrace();}
                    if (command != SEND_MESSAGE && command != GET_COMPANIONS) {
                        command = CHOOSE_COMPANION;
                    }
                    break;
                case WAIT_GET_COMPANIONS:
                    i++;
                    if(i<400) {
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        i=0;
                        if(command != RENEW_DIALOG && command != WAIT_RENEW_DIALOG) {
                            command = GET_COMPANIONS;
                        }
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
                        if (command != SEND_MESSAGE && command != GET_COMPANIONS) {
                            command = RENEW_DIALOG;
                        }
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
