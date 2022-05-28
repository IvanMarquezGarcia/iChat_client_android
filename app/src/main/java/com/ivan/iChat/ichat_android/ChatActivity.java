package com.ivan.iChat.ichat_android;


import static com.ivan.iChat.ichat_android.utils.Constants.SEND_BYE;
import static com.ivan.iChat.ichat_android.utils.Constants.STATUS_CONNECTED;

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
import com.ivan.iChat.ichat_android.model.UserSocket;


public class ChatActivity extends AppCompatActivity {

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
        socket = UserSocket.getInstance();
        try {
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectionStatus = STATUS_CONNECTED;
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

    @Override
    public void onBackPressed() { // check if goes to login by default
        new Thread(() -> {
            try {
                // send bye to server
                output.writeUTF(SEND_BYE);

                // close the socket
                UserSocket.getInstance().close();
                if (UserSocket.getInstance().isClosed())
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

        return;
    }

    public void send(View view) {
        error_text.setVisibility(View.INVISIBLE);
        if (connectionStatus == STATUS_CONNECTED) {
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
