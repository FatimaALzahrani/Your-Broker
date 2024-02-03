package com.market.your_broker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Onboarding1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding1);
    }
    public void ToDef2(View view) {
        finish();
        Intent intent = new Intent(this, Onboarding2.class);
        startActivity(intent);
    }
}