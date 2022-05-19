package com.ivan.iChat.ichat_android;


/*import static com.ivan.iChat.ichat_android.utils.Constants.STATUS_CONNECTED;
import static com.ivan.iChat.ichat_android.utils.Constants.STATUS_DISCONNECTED;
import static com.ivan.iChat.ichat_android.utils.Constants.STATUS_ERROR;*/
import static com.ivan.iChat.ichat_android.utils.Constants.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;
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

    private EditText username_edittext;
    private EditText password_edittext;
    private EditText dev_host;
    private ImageButton loggin_button;
    private TextView register_textview;
    private TextView error_text;
    private Button dev_button;

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
        dev_button = findViewById(R.id.login_dev_button);
        dev_host = findViewById(R.id.login_host_edittext);
    }

    public void open_register(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    public HashMap<String, Boolean> checkInput() {
        HashMap<String, Boolean> checks = new HashMap<String, Boolean>();
        checks.put("username", true);
        checks.put("password", true);

        if (username_edittext.getText().toString().trim().length() <= 0)
            checks.put("username", false);

        if (username_edittext.getText().toString().trim().length() <= 0)
            checks.put("password", false);

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
        error_text.setText("Intentando conectar con el servidor...");
        error_text.setTextColor(getResources().getColor(R.color.gold_light, null));
        error_text.setVisibility(View.VISIBLE);


        HashMap<String, Boolean> checks = checkInput();

        if (checks.get("username") == true && checks.get("password") == true) {
            String username = username_edittext.getText().toString().trim();
            String password = password_edittext.getText().toString().trim();
            int puerto = 1234;

            // Comprobar que el puerto indicado
            // es un puerto registrado
            if (puerto > 1024 && puerto < 49151) {
                new Thread(() -> {
                    String errorMsg = "";
                    Exception e = null;
                    try {
                        // for the lambda capturing standard //
                        String host = "192.168.1.50";
                        if (dev_host.getVisibility() == View.VISIBLE)
                            host = dev_host.getText().toString().trim();

                        Socket socket = new Socket(host, puerto);

                        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                        HashMap<String, String> data = new HashMap<String, String>();
                        data.put("type", "login");
                        data.put("username", username);
                        data.put("password", password);

                        String strData = data.toString().substring(1, data.toString().length() - 1);

                        output.writeUTF(strData); // enviar credenciales al servidor

                        System.out.println(strData); // texto de control

                        System.out.println("-------" + username + "-------");

                        if (checkTry(socket) == STATUS_CONNECTED) {
                            Intent i = new Intent(this, ChatActivity.class);
                            i.putExtra("username", username);
                            SingletonSocket.setInstance(socket);
                            startActivity(i);
                        }
                    }
                    catch (UnknownHostException uhe) {
                        errorMsg = "Host desconocido";
                        e = uhe;
                    }
                    catch (ConnectException ce) {
                        errorMsg = "Error al conectar";
                        e = ce;
                    }
                    catch (NumberFormatException nfe) {
                        errorMsg = "Puerto no válido";
                        e = nfe;
                    }
                    catch (IOException ioe) {
                        errorMsg = "Error de E/S";
                        e = ioe;
                    }
                    finally {
                        if (e != null) {
                            System.out.println("-----------------------------------------------------");
                            //e.printStackTrace();
                            System.out.println(errorMsg);
                            System.out.println("-----------------------------------------------------");

                            String msg = errorMsg;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    error_text.setText(msg);
                                    error_text.setTextColor(getResources().getColor(R.color.red_error, null));
                                    error_text.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }).start();
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        error_text.setTextColor(getResources().getColor(R.color.red_error,null));
                        error_text.setText("Puerto no válido");
                        error_text.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    error_text.setTextColor(getResources().getColor(R.color.red_error,null));
                    error_text.setText("Nombre o contraseña inválidos");
                    error_text.setVisibility(View.VISIBLE);
                }
            });
        }
    }


    public void activate_dev(View view) {
        if (dev_host.getVisibility() == View.VISIBLE)
            dev_host.setVisibility(View.INVISIBLE);
        else
            dev_host.setVisibility(View.VISIBLE);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                error_text.setVisibility(View.INVISIBLE);
            }
        });

        int estado = STATUS_DISCONNECTED;
        String errorMsg = "";
        Exception e = null;

        try {
            // Solicitar "hueco" en el servidor
            String mensaje = new DataInputStream(socket.getInputStream()).readUTF();

            System.out.println("server response: " + mensaje);

            if (mensaje.equals(RESPONSE_SERVER_FULL)) {		            // server full
                System.out.println("server full");
                errorMsg = "Servidor lleno";
            }
            else if (mensaje.equals(RESPONSE_INCORRECT_PASSWORD)) {	    // incorrect password
                System.out.println("incorrect password");
                errorMsg = "Contraseña incorrecta";
            }
            else if (mensaje.equals(RESPONSE_UNREGISTERED)) {		    // unregistered
                System.out.println("unregistered");
                errorMsg = "¡Ups!, no estás registrado";
            }
            else if (mensaje.equals(RESPONSE_DB_UNABLE_CONNECTION)) {	// not access to db
                System.out.println("not access to db");
                errorMsg = "Base de datos innacesible";
            }
            else if (mensaje.equals(RESPONSE_ERROR)) {				    // error in login
                System.out.println("error in login");
                errorMsg = "Error al inciciar sesión";
            }
            else if (mensaje.equals(RESPONSE_OK))			            // ok
                estado = STATUS_CONNECTED;

        } catch (IOException ex) {
            estado = STATUS_ERROR;
            errorMsg = "error connecting to the server";
            e = ex;
        }
        finally {
            if (errorMsg.length() > 0) {
                if (e != null) {
                    System.out.println("-----------------------------------------------------------");
                    //e.printStackTrace();
                    System.out.println(errorMsg);
                    System.out.println("-----------------------------------------------------------");
                }

                String msg = errorMsg;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        error_text.setTextColor(getResources().getColor(R.color.red_error, null));
                        error_text.setText(msg);
                        error_text.setVisibility(View.VISIBLE);
                    }
                });
            }
        }

        return estado;
    }

}