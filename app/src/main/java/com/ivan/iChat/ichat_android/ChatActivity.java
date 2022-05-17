package com.ivan.iChat.ichat_android;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;

import com.ivan.iChat.ichat_android.model.ReaderRunnable;
import com.ivan.iChat.ichat_android.model.SingletonSocket;


public class ChatActivity extends AppCompatActivity {

    private final int CONNECTED = 1;
    private final int DISCONNECTED = 0;
    private final int ERROR = -1;

    private int connectionStatus;
    private Socket socket;
    private String username;
    private TextView error_text;
    private RecyclerView mssgsList;
    private Thread readerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // initialize components
        ((TextView) findViewById(R.id.chat_username_textview)).setText(getIntent().getStringExtra("username"));
        socket = SingletonSocket.getInstance();
        connectionStatus = CONNECTED;
        mssgsList = findViewById(R.id.chat_mssgs_recyclerview);
        error_text = findViewById(R.id.chat_error_textview);
        readerThread = new Thread(new ReaderRunnable(this));
    }

    public int getConnectionStatus() {
        return connectionStatus;
    }

    public Socket getSocket() {
        return socket;
    }

    public DataOutputStream getOutput() {
        try {
            return new DataOutputStream(socket.getOutputStream());
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public TextView getErrorText() {
        return error_text;
    }

    public RecyclerView getMssgsList() {
        return mssgsList;
    }

    private void startReader() {
        if (!readerThread.isAlive())
            readerThread.start();
        else
            System.out.println("La escucha ya está habilitada");
    }

}