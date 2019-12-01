package com.techknightsrtu.techq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import info.hoang8f.widget.FButton;

public class FinishActivity extends AppCompatActivity {

    FButton finishButton;
    TextView wrongAnsText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        //Initialize
        finishButton = (FButton) findViewById(R.id.FinishButton);
        finishButton.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        wrongAnsText = (TextView)findViewById(R.id.wrongAns);

        //play again button onclick listener
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

