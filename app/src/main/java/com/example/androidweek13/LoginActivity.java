package com.example.androidweek13;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText txt;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txt = findViewById(R.id.id);
        handler = new Handler();
        findViewById(R.id.save_btn).setOnClickListener(v -> new Thread(this::click).start());
    }

    protected void click() {
        String id = String.valueOf(txt.getText());
        try {
            URL url = new URL("https://solved.ac/api/v3\n" +
                    "/user/show?handle="+id);
            url.openStream();
            SharedPreferences preferences = this.getSharedPreferences("test", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("id", id);
            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            handler.post(() -> startActivity(intent));
        } catch (FileNotFoundException fe) {
            handler.post(() ->Toast.makeText(getApplicationContext(), "잘못된 아이디입니다. 다시 입력하세요.", Toast.LENGTH_LONG).show());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}