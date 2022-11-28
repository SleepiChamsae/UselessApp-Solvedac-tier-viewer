package com.example.androidweek13;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String id;
    int tier;
    final String[] colors = {"#000000", "#ad5600", "#435f7a", "#ec9a00", "#27e2a4", "#00b4fc", "#ff0062", "#e483c6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        // id 가져와서 txtView가져오기
        SharedPreferences pref = this.getSharedPreferences("test", Context.MODE_PRIVATE);

        try {
            id = pref.getString("id", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (id == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        TextView t = findViewById(R.id.main);
        t.setText(id);

        Thread thread = new Thread(this::getTier);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        findViewById(R.id.back).setBackgroundColor(Color.parseColor(colors[(tier+4)/5]));
    }

    private void getTier() {
        try {
            URL url = new URL("https://solved.ac/api/v3/user/show?handle="+id);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String t;
            while((t = br.readLine()) != null) {
                int idx;
                if((idx = t.indexOf("tier")) != -1)
                    tier = Integer.parseInt(t.substring(idx+6, idx+8));
            }
        } catch (Exception ignored) {

        }
    }

}