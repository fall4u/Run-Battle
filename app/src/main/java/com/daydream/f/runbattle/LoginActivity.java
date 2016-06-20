package com.daydream.f.runbattle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button weChatLoginButton = (Button) findViewById(R.id.weChatLogin);
        weChatLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // use weChat ID to login
                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
