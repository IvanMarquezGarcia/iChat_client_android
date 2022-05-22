package com.ivan.iChat.ichat_android;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ivan.iChat.ichat_android.utils.Constants.*;


public class RegisterActivity extends AppCompatActivity {

    private EditText devHostEdittext;
    private EditText usernameEdittext;
    private EditText passwordEdittext;
    private EditText confirmPasswordEdittext;
    private Spinner languagesSpinner;
    private TextView errorText;
    private ImageButton registerImagebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize components
        devHostEdittext = findViewById(R.id.register_dev_host_edittext);
        usernameEdittext = findViewById(R.id.register_username_edittext);
        passwordEdittext = findViewById(R.id.register_password_edittextpassword);
        confirmPasswordEdittext = findViewById(R.id.register_password_edittextpassword2);
        languagesSpinner = findViewById(R.id.register_lang_spinner);
        errorText = findViewById(R.id.register_error_textView);
        registerImagebutton = findViewById(R.id.register_imageButton);

        List<String> languages =  new ArrayList<String>();
        languages.add("es");
        languages.add("en");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, languages);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        languagesSpinner.setAdapter(adapter);
    }

    public void activate_dev(View view) {
        if (devHostEdittext.getVisibility() == View.VISIBLE)
            devHostEdittext.setVisibility(View.INVISIBLE);
        else
            devHostEdittext.setVisibility(View.VISIBLE);
    }

    private HashMap<String, Boolean> checkInput() {
        /*
          username requirements: ^[A-zñ\-\/@#$<>.:,;]{3,}$
            three letters at least
            no numbers
          password requirements: ^[A-zñ\-\/@#$<>.:,;]{6,}$
            longer than 6 chars
            numbers
        */
        HashMap<String, Boolean> checks = new HashMap<String, Boolean>();

        checks.put("username", usernameEdittext.getText().toString().trim().matches("^[A-zñ\\-@#$<>.:,;]{3,}$"));

        if (passwordEdittext.getText().toString().trim().equals(confirmPasswordEdittext.getText().toString().trim()))
            checks.put("password", passwordEdittext.getText().toString().trim().matches("^[A-zñ\\-@#$<>.:,;]{6,}$"));
        else
            checks.put("password", false);

        return checks;
    }

    public void register(View view) {
        errorText.setVisibility(View.INVISIBLE);
        HashMap<String, Boolean> checks = checkInput();

        if (!checks.get("username")) {                  // invalid username
            System.out.println("invalid username");
            errorText.setTextColor(getResources().getColor(R.color.red_error, null));
            errorText.setText("Username incorrecto");
            errorText.setVisibility(View.VISIBLE);
        }
        else if (!checks.get("password")) {              // invalid password
            System.out.println("invalid password");
            errorText.setTextColor(getResources().getColor(R.color.red_error, null));
            errorText.setText("Contraseña incorrecta");
            errorText.setVisibility(View.VISIBLE);
        }
        else {
            errorText.setTextColor(getResources().getColor(R.color.gold_light, null));
            errorText.setText("Procesando registro");
            errorText.setVisibility(View.VISIBLE);
            sendLogup();
        }
    }

    public void sendLogup() {
        String host = devHostEdittext.getText().toString().trim();
        if (host.length() == 0)
            host = "192.168.1.37";
        int port = 1234;

        if (usernameEdittext.getText().toString().trim().length() <= 0)
            usernameEdittext.setHint("rellena username");
        else if (passwordEdittext.getText().toString().trim().length() <= 0)
            passwordEdittext.setHint("rellena password");
        else {
            String username = usernameEdittext.getText().toString().trim();
            String password = passwordEdittext.getText().toString().trim();
            String language = languagesSpinner.getSelectedItem().toString().trim();

            String hostAux = host;
            new Thread(() -> {
                String response = "";
                try {
                    System.out.println("u are going to connect at " + hostAux + ":" + port);

                    Socket s = new Socket(hostAux, port);

                    String request = "type=logup, password=" + password + ", username=" + username + ", language=" + language;
                    System.out.println(request);

                    new DataOutputStream(s.getOutputStream()).writeUTF(request);
                    response = new DataInputStream(s.getInputStream()).readUTF();

                    if (response.equals(RESPONSE_OK))                           // logup complete
                        response += " - logged";
                    else if (response.equals(ALREADY_EXISTS)) {					// username already exists
                        response += " - already exists";
                    }
                    else if (response.equals(RESPONSE_DB_UNABLE_CONNECTION))    // database is not accessible
                        response += " - unable access to db";
                    else if (response.equals(RESPONSE_ERROR))                   // error in logup
                        response += " - error";
                    else
                        response += " - not idea";
                } catch(Exception e) {
                    e.printStackTrace();
                    response += " - " + e.getMessage();
                }
                finally {
                    System.out.println("#############################################################################");
                    System.out.println(response);
                    System.out.println("#############################################################################");

                    String responseAux = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (responseAux.contains(RESPONSE_OK)) {
                                errorText.setTextColor(getResources().getColor(R.color.ok_green, null));
                                errorText.setText("Registro completado");
                                errorText.setVisibility(View.VISIBLE);
                            }
                            else {
                                String errorMssg = responseAux.split(" - ")[1];
                                errorText.setTextColor(getResources().getColor(R.color.red_error, null));
                                errorText.setText(errorMssg);
                                errorText.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }).start();
        }
    }

}
