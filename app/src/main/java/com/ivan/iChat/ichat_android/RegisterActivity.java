package com.ivan.iChat.ichat_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText confirm_password;
    private ImageButton register_imagebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize components
        username = findViewById(R.id.register_username_edittext);
        password = findViewById(R.id.register_password_edittextpassword);
        confirm_password = findViewById(R.id.register_confirm_password_edittextpassword);
    }

    private HashMap<String, Boolean> checkInput() {
        HashMap<String, Boolean> checks = new HashMap<String, Boolean>();

        checks.put("username", username.getText().toString().matches("regex"));

        if (password.getText().toString().equals(confirm_password.getText().toString()))
            checks.put("password", password.getText().toString().matches("regex"));
        else
            checks.put("password", false);

        return checks;
    }

    public void register(View view) {
        HashMap<String, Boolean> checks = checkInput();

        if (checks.get("fullname") &&
                checks.get("username") &&
                checks.get("password")) {
            // send data to server to insert in db
        }
        else {
            // indicar error
        }
    }

}