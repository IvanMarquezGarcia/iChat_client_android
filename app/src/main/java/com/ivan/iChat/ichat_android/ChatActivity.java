package com.ivan.iChat.ichat_android;


import static com.ivan.iChat.ichat_android.utils.Constants.SEND_BYE;
import static com.ivan.iChat.ichat_android.utils.Constants.STATUS_CONNECTED;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import com.ivan.iChat.ichat_android.model.Mensaje;
import com.ivan.iChat.ichat_android.model.ReaderRunnable;
import com.ivan.iChat.ichat_android.model.User;
import com.ivan.iChat.ichat_android.model.UserSocket;

import com.ivan.iChat.ichat_android.utils.Core;
import com.ivan.iChat.ichat_android.utils.MssgsAdapter;


public class ChatActivity extends AppCompatActivity {

    private int connectionStatus;
    private Socket socket;
    private ObjectOutputStream output;
    private User user;
    private TextView error_text;
    private EditText input;
    private RecyclerView mssgsList;
    private ArrayList<Mensaje> messages = new ArrayList<>();
    private Thread readerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // initialize components
        HashMap<String, String> userData = Core.strToHashMap(getIntent().getStringExtra("userData"));
        user = new User(Integer.parseInt(userData.get("id")), userData.get("username"), userData.get("language"));
        ((TextView) findViewById(R.id.chat_username_textview)).setText(user.getUsername());
        connectionStatus = STATUS_CONNECTED;
        error_text = findViewById(R.id.chat_error_textview);
        input = findViewById(R.id.chat_input_edittext);
        mssgsList = findViewById(R.id.chat_mssgs_recyclerview);
        mssgsList.setLayoutManager(new LinearLayoutManager(this));
        MssgsAdapter mssgsAdapter = new MssgsAdapter(messages, user);
        mssgsList.setAdapter(mssgsAdapter);
        socket = UserSocket.getInstance();
        new Thread(() -> {
            try {
                output = new ObjectOutputStream(socket.getOutputStream());
                readerThread = new Thread(new ReaderRunnable(this));
                startReader();
            } catch (IOException e) { e.printStackTrace(); }
        }).start();
    }

    public int getConnectionStatus() {
        return connectionStatus;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public User getUser() {
        return user;
    }

    public TextView getErrorText() {
        return error_text;
    }

    public ArrayList<Mensaje> getMessages() {
        return messages;
    }

    public MssgsAdapter getMssgsAdapter() {
        return (MssgsAdapter) getMssgsList().getAdapter();
    }

    public RecyclerView getMssgsList() {
        return mssgsList;
    }

    private void startReader() {
        if (!readerThread.isAlive()) {
            readerThread.start();
            System.out.println("the listening has been initialize");
        }
        else
            System.out.println("the listening is already working");
    }

    @Override
    protected void onDestroy() {
        new Thread(() -> {
            try {
                // send bye to server
                output.writeObject(new Mensaje(SEND_BYE, user.getLanguage(), user.getId()));

                // close the socket
                socket.close();

                if (socket.isClosed())
                    System.out.println("the client socket has been closed");
                else
                    System.out.println("cant close the socket");

                // go back to login
                finish();
            } catch(IOException ioe) {
                System.out.println("---------------------------------------------------------------------");
                //ioe.printStackTrace();
                System.out.println("Error al cerrar el socket");
                System.out.println("---------------------------------------------------------------------");
            }
        }).start();

        boolean closed = false;
        while(closed == false) {
            if (socket.isClosed())
                closed = true;
        }
        super.onDestroy();
    }

    public void send(View view) {
        error_text.setVisibility(View.INVISIBLE);
        if (connectionStatus == STATUS_CONNECTED) {
            String content = input.getText().toString().trim();

            if (content.length() > 0) {
                Mensaje message = new Mensaje(content, user.getLanguage(), user.getId());
                // send mssg to server
                if (output != null) {
                    new Thread(() -> {
                        try {
                            output.writeObject(message);
                            output.flush(); // Clear output stream
                        } catch(IOException ioe) {
                            String errorMsg = "Error al enviar mensaje";

                            System.out.println("---------------------------------------------------------------------");
                            //ioe.printStackTrace();
                            System.out.println(errorMsg);
                            System.out.println("---------------------------------------------------------------------");

                            runOnUiThread(() -> {
                                error_text.setText(errorMsg);
                                error_text.setVisibility(View.VISIBLE);
                            });
                        }
                    }).start();
                    input.setText(""); // Clear EditText text input
                }
                else {
                    System.out.println("Flujo de salida nulo");
                }
                messages.add(message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getMssgsAdapter().notifyDataSetChanged();
                        mssgsList.scrollToPosition(mssgsList.getAdapter().getItemCount() - 1);
                    }
                });
            }
        }
    }
}
