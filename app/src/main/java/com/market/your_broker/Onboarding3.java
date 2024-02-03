package com.market.your_broker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Onboarding3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding3);
    }

    public void ToDef2(View view) {
        finish();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}