package com.daydream.f.runbattle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {

    boolean isPass = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button weChatLoginButton = (Button) findViewById(R.id.Login);
        weChatLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // use weChat ID to login
                EditText editText = (EditText)findViewById(R.id.Email);
                String account = editText.getText().toString();

                editText = (EditText)findViewById(R.id.passWord);
                String passWord = editText.getText().toString();

                // TODO: verify account and password in server
                if(isPass){
                    Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                    intent.putExtra("account", account);
                    startActivity(intent);
                    //jump to user information UI
                }
                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
