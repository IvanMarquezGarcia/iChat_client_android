package com.ivan.iChat.ichat_android;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
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
    private DataOutputStream output;
    private String username;
    private TextView error_text;
    private EditText input;
    private RecyclerView mssgsList;
    private Thread readerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // initialize components
        username = getIntent().getStringExtra("username");
        ((TextView) findViewById(R.id.chat_username_textview)).setText(username);
        socket = SingletonSocket.getInstance();
        try {
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectionStatus = CONNECTED;
        error_text = findViewById(R.id.chat_error_textview);
        input = findViewById(R.id.chat_input_edittext);
        mssgsList = findViewById(R.id.chat_mssgs_recyclerview);
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

    public void send(View view) {
        error_text.setVisibility(View.INVISIBLE);
        if (connectionStatus == CONNECTED) {
            String mssg = input.getText().toString().trim();

            if (mssg.length() > 0) {
                // send mssg to server
                if (output != null) {
                    new Thread(() -> {
                        try {
                            output.writeUTF("[" + username + "] > " + mssg + "");
                            output.flush(); // Clear output stream
                        } catch(IOException ioe) {
                            String errorMsg = "Error al enviar mensaje";

                            System.out.println("---------------------------------------------------------------------");
                            //ioe.printStackTrace();
                            System.out.println(errorMsg);
                            System.out.println("---------------------------------------------------------------------");

                            error_text.setText(errorMsg);
                            error_text.setVisibility(View.VISIBLE);
                        }
                    }).start();

                    input.setText(""); // Clear EditText text input
                }
                else {
                    System.out.println("Flujo de salida nulo");
                }
                /*mensajes_ListView.getItems().add(new Mensaje(mensaje + " < [Tú]", true));
                mensajes_ListView.scrollTo(mensajes_ListView.getItems().size() - 1);*/
            }
        }
    }
}
