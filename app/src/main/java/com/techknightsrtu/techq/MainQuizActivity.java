package com.techknightsrtu.techq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.forms.sti.progresslitieigb.Inteface.IProgressLoadingIGB;
import com.forms.sti.progresslitieigb.Model.JSetting;
import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import info.hoang8f.widget.FButton;

public class MainQuizActivity extends AppCompatActivity {

    private static final String TAG = "MainQuizActivity";

    FButton buttonA, buttonB, buttonC, buttonD;
    RelativeLayout quizLoading;
    TextView questionText, timeText, resultText;
    QuizQuestion currentQuestion;
    List<QuizQuestion> list;
    int qid = 0;

    int score = 0;
    CountDownTimer countDownTimer;


    //Firebase Variables
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);

        quizLoading = (RelativeLayout)findViewById(R.id.quizLoading);
        quizLoading.setVisibility(View.VISIBLE);



//        ProgressLoadingJIGB.setupLoading = new IProgressLoadingIGB() {
//            @Override
//            public void body(JSetting setup) {
//                setup.srcLottieJson = R.raw.quiz_loading; // Tour Source JSON Lottie
//                setup.message = "Loading Quiz!";//  Center Message
//                setup.timer = 0;   // Time of live for progress.
//
//            }
//        };
//
//        ProgressLoadingJIGB.startLoading(MainQuizActivity.this);

        //Initializing variables
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("quizzes").child("quiz-1");

        currentQuestion = new QuizQuestion();
        list = new ArrayList<>();


        questionText = (TextView) findViewById(R.id.triviaQuestion);
        buttonA = (FButton) findViewById(R.id.buttonA);
        buttonB = (FButton) findViewById(R.id.buttonB);
        buttonC = (FButton) findViewById(R.id.buttonC);
        buttonD = (FButton) findViewById(R.id.buttonD);
        timeText = (TextView) findViewById(R.id.timeText);
        resultText = (TextView) findViewById(R.id.resultText);

        resetColor();


        //add questions from firebase database into list
        readFromFirebase();

        Log.d(TAG, "onCreate: " + list);

        //Setup Countdown Timer for the question
        setupTimer();

    }

    public void readFromFirebase() {
        // Read from the database
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "onDataChange:  " + dataSnapshot);

                for (DataSnapshot ques : dataSnapshot.getChildren()) {

                   //Storing Values form the Database into Temporary Object of QuizQuestion class

                    Log.d(TAG, "onDataChange: " + ques);
                    //currentQuestion.setId((Integer)ques.child("id").getValue());

                    currentQuestion.setId(1);
                    currentQuestion.setQuestion((String) ques.child("question").getValue());
                    currentQuestion.setOptA((String) ques.child("opta").getValue());
                    currentQuestion.setOptB((String) ques.child("optb").getValue());
                    currentQuestion.setOptC((String) ques.child("optc").getValue());
                    currentQuestion.setOptD((String) ques.child("optd").getValue());
                    currentQuestion.setAnswer((String) ques.child("answer").getValue());

                    Log.d(TAG, "onDataChange: checking the status of " + currentQuestion);

                    //Add the Question into list
                    list.add(new QuizQuestion(currentQuestion));
                }

                Log.d(TAG, "onDataChange: " + list);

                //Now we gonna shuffle the elements of the list so that we will get questions randomly
                Collections.shuffle(list);

                //currentQuestion will hold the que, 4 option and ans for particular id
                currentQuestion = list.get(qid);

                //This method will set the que and four options
                updateQueAndOptions();

                //loading screen visibility gone
                quizLoading.setVisibility(View.GONE);

                //Quiz loaded and Now timer will start
                countDownTimer.start();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void setupTimer() {

        //countDownTimer
        countDownTimer = new CountDownTimer(20000, 1000) {
            public void onTick(long millisUntilFinished) {

                //here you can have your logic to set text to timeText
                setTimerFormat(millisUntilFinished/1000);

            }

            //Now user is out of time
            public void onFinish() {
                //We will navigate him to the time up activity using below method
                disableButton();
                timeUp();
            }
        };

    }

    /*
    This function converts seconds into the correct format of the timer
     */
    public void setTimerFormat(long sec){

        int p1 = (int) sec % 60;
        long p2 = sec / 60;
        int p3 = (int) p2 % 60;
        p2 = p2 / 60;

        String hrs = String.format("%02d",p2); // Format the number into two digit format
        String min = String.format("%02d",p3);
        String seco = String.format("%02d",p1);

        timeText.setText(hrs + ":" + min + ":" + seco);

    }

    public void updateQueAndOptions() {

        //This method will setText for que and options
        questionText.setText(currentQuestion.getQuestion());
        buttonA.setText(currentQuestion.getOptA());
        buttonB.setText(currentQuestion.getOptB());
        buttonC.setText(currentQuestion.getOptC());
        buttonD.setText(currentQuestion.getOptD());

        //Now since the user has ans correct just reset timer back for another que- by cancel and start



    }

    //Onclick listener for first button
    public void buttonA(View view) {
        //compare the option with the ans if yes then make button color green
        if (currentQuestion.getOptA().equals(currentQuestion.getAnswer())) {
            //Now since user has ans correct increment the coinvalue
            score++;

        }

        //Check if user has not exceeds the que limit
        if (qid < list.size()-1) {
            //Now disable all the option button since user choice is recorded
            //user won't be able to press another option button after pressing one button
            disableButton();

            //Show the dialog that ans is correct
            //correctDialog();

            qid++;

            //get the que and 4 option and store in the currentQuestion
            currentQuestion = list.get(qid);

            Log.d(TAG, "buttonA: "+ currentQuestion.getQuestion());

            //Now this method will set the new que and 4 options
            updateQueAndOptions();

            //reset the color of buttons back to white
            resetColor();

            //Enable button - remember we had disable them when user ans was correct in there particular button methods
            enableButton();
        }
        //If user has exceeds the que limit just navigate him to GameWon activity
        else {
            gameWon();
        }
    }

    //Onclick listener for sec button
    public void buttonB(View view) {
        if (currentQuestion.getOptB().equals(currentQuestion.getAnswer())) {
            //Now since user has ans correct increment the coinvalue
            score++;
        }
        if (qid < list.size()-1) {
            disableButton();
            qid++;
            currentQuestion = list.get(qid);
            Log.d(TAG, "buttonA: "+ currentQuestion.getQuestion());
            updateQueAndOptions();
            resetColor();
            enableButton();
        } else {
            gameWon();
        }
    }

    //Onclick listener for third button
    public void buttonC(View view) {
        if (currentQuestion.getOptC().equals(currentQuestion.getAnswer())) {
            //Now since user has ans correct increment the coinvalue
            score++;

        }
        if (qid < list.size()-1) {
            disableButton();
            qid++;
            currentQuestion = list.get(qid);
            Log.d(TAG, "buttonA: "+ currentQuestion.getQuestion());
            updateQueAndOptions();
            resetColor();
            enableButton();
        } else {
            gameWon();
        }
    }

    //Onclick listener for fourth button
    public void buttonD(View view) {
        if (currentQuestion.getOptD().equals(currentQuestion.getAnswer())) {
            //Now since user has ans correct increment the coinvalue
            score++;
        }
        if (qid < list.size()-1) {
            disableButton();
            qid++;
            currentQuestion = list.get(qid);
            Log.d(TAG, "buttonA: "+ currentQuestion.getQuestion());
            updateQueAndOptions();
            resetColor();
            enableButton();
        } else {
            gameWon();
        }
    }


    //This method will navigate from current activity to GameWon
    public void gameWon() {
        countDownTimer.cancel();
        Intent intent = new Intent(this, FinishActivity.class);
        startActivity(intent);
        finish();
    }

    //This method is called when time is up
    //this method will navigate user to the activity Time_Up
    public void timeUp() {
        countDownTimer.cancel();
        Intent intent = new Intent(this, TimeupActivity.class);
        startActivity(intent);
        finish();
    }


    //This method will make button color white again since our one button color was turned green
    public void resetColor() {
        buttonA.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        buttonB.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        buttonC.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        buttonD.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
    }

    //This method will disable all the option button
    public void disableButton() {
        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);
    }

    //This method will all enable the option buttons
    public void enableButton() {
        buttonA.setEnabled(true);
        buttonB.setEnabled(true);
        buttonC.setEnabled(true);
        buttonD.setEnabled(true);
    }


    //If user press home button and come in the game from memory then this
    //method will continue the timer from the previous time it left
    @Override
    protected void onRestart() {
        super.onRestart();
        countDownTimer.start();
    }

    //When activity is destroyed then this will cancel the timer
    @Override
    protected void onStop() {
        super.onStop();
        countDownTimer.cancel();
    }

    //This will pause the time
    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    //On BackPressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
