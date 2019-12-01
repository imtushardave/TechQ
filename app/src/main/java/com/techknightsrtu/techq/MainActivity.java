package com.techknightsrtu.techq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity {

    FButton startQuiz,profile,quit;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        quit = (FButton)findViewById(R.id.quitButton);
        startQuiz =(FButton)findViewById(R.id.playGame);
        profile = (FButton)findViewById(R.id.profileButton);

        resetColor();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Profile Activity will be updated soon", Toast.LENGTH_LONG).show();
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                overridePendingTransition(0,0);

            }
        });

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainQuizActivity.class);
                startActivity(intent);

            }
        });

    }

    public void resetColor() {

        startQuiz.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        profile.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        quit.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.red));

    }

}
