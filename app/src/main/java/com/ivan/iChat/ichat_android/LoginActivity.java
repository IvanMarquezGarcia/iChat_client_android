package com.ivan.iChat.ichat_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private ImageButton loggin_button;
    private TextView register_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize components
        username = findViewById(R.id.login_username_edittext);
        password = findViewById(R.id.login_password_edittextpassword);
        loggin_button = findViewById(R.id.login_imageButton);
        register_textview = findViewById(R.id.login_register_textView);
    }

    public void open_register(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    public static boolean validation() {
        return true;
    }

}