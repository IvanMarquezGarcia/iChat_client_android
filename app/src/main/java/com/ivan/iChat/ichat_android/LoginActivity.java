package com.ivan.iChat.ichat_android;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import android.os.Parcelable;
import android.os.StrictMode;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ivan.iChat.ichat_android.model.SingletonSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {

    private final int CONNECTED = 1;
    private final int DISCONNECTED = 0;
    private final int ERROR = -1;

    private EditText username_edittext;
    private EditText password_edittext;
    private ImageButton loggin_button;
    private TextView register_textview;
    private TextView error_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize components
        username_edittext = findViewById(R.id.login_username_edittext);
        password_edittext = findViewById(R.id.login_password_edittextpassword);
        loggin_button = findViewById(R.id.login_imageButton);
        register_textview = findViewById(R.id.login_register_textView);
        error_text = findViewById(R.id.login_error_textView);
    }

    public void open_register(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    public HashMap<String, Boolean> checkInput() {
        HashMap<String, Boolean> checks = new HashMap<String, Boolean>();
        checks.put("username", true);
        checks.put("password", true);
        /*
        for logup
            username requirements:
                three letters at least
                no numbers
            password requirements:
                longer than 6 chars
                one number and letter
                at least
        checks.put("username", username_edittext.getText().toString().matches("^[A-z]{3,}$"));
        checks.put("password", password_edittext.getText().toString().matches("^([A-z0-9]+){6,}$"));
        */
        return checks;
    }

    /*
		ESTADO: FUNCIONAL

		DESCRIPCIÓN:
			Conectar con el servidor.
	 */
    public void tryConnection(View view) {
        error_text.setVisibility(View.INVISIBLE);

        HashMap<String, Boolean> checks = checkInput();

        if (checks.get("username") == true && checks.get("password") == true) {
            String username = username_edittext.getText().toString().trim();
            String password = password_edittext.getText().toString().trim();
            int puerto = 1234;

            // Comprobar que el puerto indicado
            // es un puerto registrado
            if (puerto > 1024 && puerto < 49151) {
                new Thread(() -> {
                    try {
                        // for the lambda capturing standard //
                        Socket socket = new Socket("192.168.1.50", puerto);

                        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                        HashMap<String, String> data = new HashMap<String, String>();
                        data.put("type", "login");
                        data.put("username", username);
                        data.put("password", password);

                        String strData = data.toString().substring(1, data.toString().length() - 1);

                        output.writeUTF(strData); // enviar credenciales al servidor

                        System.out.println(strData); // texto de control

                        System.out.println("-------" + username + "-------");

                        if (checkTry(socket) == CONNECTED) {
                            Intent i = new Intent(this, ChatActivity.class);
                            i.putExtra("username", username);
                            SingletonSocket.setInstance(socket);
                            startActivity(i);
                        }
                    }
                    catch (UnknownHostException uhe) {
                        String errorMsg = "Host desconocido";

                        System.out.println("-----------------------------------------------------");
                        //uhe.printStackTrace();
                        System.out.println(errorMsg);
                        System.out.println("-----------------------------------------------------");

                        /*error_text.setText(errorMsg);
                        error_text.setVisibility(View.VISIBLE);*/
                    }
                    catch (ConnectException ce) {
                        String errorMsg = "Error al conectar";

                        System.out.println("-----------------------------------------------------");
                        //ce.printStackTrace();
                        System.out.println(errorMsg);
                        System.out.println("-----------------------------------------------------");

                        /*error_text.setText(errorMsg);
                        error_text.setVisibility(View.VISIBLE);*/
                    }
                    catch (NumberFormatException nfe) {
                        String errorMsg = "Puerto no válido";

                        System.out.println("-----------------------------------------------------");
                        //nfe.printStackTrace();
                        System.out.println(errorMsg);
                        System.out.println("-----------------------------------------------------");

                        /*error_text.setText(errorMsg);
                        error_text.setVisibility(View.VISIBLE);*/

                        System.exit(0);
                    }
                    catch (IOException ioe) {
                        String errorMsg = "Error de E/S";

                        System.out.println("-----------------------------------------------------");
                        //ioe.printStackTrace();
                        System.out.println(errorMsg);
                        System.out.println("-----------------------------------------------------");

                        /*error_text.setText(errorMsg);
                        error_text.setVisibility(View.VISIBLE);*/

                        System.exit(0);
                    }
                }).start();
            }
            else {
                error_text.setText("Puerto no válido");
                error_text.setVisibility(View.VISIBLE);
            }
        }
        else {
            error_text.setText("Nombre o contraseña inválidos");
            error_text.setVisibility(View.VISIBLE);
        }
    }


    /*
    ESTADO: FUNCIONAL

    DESCRIPCIÓN:
        Solicita conexión al servidor y conecta al
        cliente con este.

    RETORNO:
        int indicando el estado de conexión del cliente.
    */
    public int checkTry(Socket socket) {
        error_text.setVisibility(View.INVISIBLE);
        int estado = DISCONNECTED;
        try {
            // Solicitar "hueco" en el servidor
            String mensaje = new DataInputStream(socket.getInputStream()).readUTF();

            System.out.println("server response: " + mensaje);

            if (mensaje.equals("------/n##em/p#t_y_")) {			// server full
                /*error_text.setText("Servidor lleno");
                error_text.setVisibility(View.VISIBLE);*/
                System.out.println("server full");
            }
            else if (mensaje.equals("------***#pass/*word**#")) {	// incorrect password
                /*error_text.setText("Contraseña incorrecta");
                error_text.setVisibility(View.VISIBLE);*/
                System.out.println("incorrect password");
            }
            else if (mensaje.equals("------un##/r//_/_")) {			// unregistered
                /*error_text.setText("�Ups!, no estás registrado");
                error_text.setVisibility(View.VISIBLE);*/
                System.out.println("unregistered");
            }
            else if (mensaje.equals("------db/#una/b_#le_")) {		// not access to db
                /*error_text.setText("Base de datos innacesible");
                error_text.setVisibility(View.VISIBLE);*/
                System.out.println("not access to db");
            }
            else if (mensaje.equals("------e#rr//_")) {				// error in login
                /*error_text.setText("Error al conectar");
                error_text.setVisibility(View.VISIBLE);*/
                System.out.println("error in login");
            }
            else if (mensaje.equals("------#//u_o/k_#"))			// ok
                estado = CONNECTED;
        } catch (IOException ex) {
            estado = ERROR;

            System.out.println("-----------------------------------------------------------");
            //ex.printStackTrace();
            System.out.println("error connecting to the server");
            System.out.println("-----------------------------------------------------------");
        }
        return estado;
    }

}