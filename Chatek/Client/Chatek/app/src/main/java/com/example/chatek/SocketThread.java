package com.example.chatek;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketThread extends Thread {

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
    /*
    1 - connect to server
    2 - get list of available companions
    3 - send message
    4 - renew dialog
    5 - wait
    6 - get id, send nickname
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

    @Override
    public void run() {
        while (true) {
            if (Thread.interrupted()) {
                interrupt();
            }
            JSONObject away = new JSONObject();
            JSONObject jIncoming = null;
            switch (command){
                case 1:
                    try{
                        fromserver = new Socket(ip, 4444);
                        in = new BufferedReader(new InputStreamReader(fromserver.getInputStream()));
                        out = new PrintWriter(fromserver.getOutputStream(), true);
                        away.put("command", 6);
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
                        incoming = in.readLine();
                        jIncoming = new JSONObject(incoming);
                        System.out.println();
                    } catch (IOException e) { e.printStackTrace(); }
                    catch (JSONException e){ e.printStackTrace(); }
                    try {
                        Thread.sleep(500);
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
                        jIncoming = new JSONObject(incoming);
                        System.out.println();
                    }
                        catch (JSONException e) { e.printStackTrace(); }
                        catch (IOException e) { e.printStackTrace(); }
                    break;
                case 5:
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    }
}
