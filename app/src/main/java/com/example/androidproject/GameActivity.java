package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class GameActivity extends AppCompatActivity {

    private FirebaseModule db;
    private Game game;
    private View view;
    private TextView text;
    private TextView scoreText,maxText;
    private Score score,max;
    private Button easy,medium,hard,guessBtn;
    private Button onlyPlus,onlyMinus,onlyTimes,anyOp;
    private EditText guess;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setup();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.startGame();
    }

    private void setup(){
        this.img = findViewById(R.id.imgMap);
        this.text = findViewById(R.id.gameQuestion);
        this.scoreText = findViewById(R.id.scoreText);
        this.maxText = findViewById(R.id.maxText);
        this.guess = findViewById(R.id.gameGuessInput);
        this.guessBtn = findViewById(R.id.gameGuessBtn);
        this.easy = findViewById(R.id.gameEasyMode);
        this.medium = findViewById(R.id.gameMediumMode);
        this.hard = findViewById(R.id.gameHardMode);
        this.guessBtn = findViewById(R.id.gameGuessBtn);
        this.onlyPlus = findViewById(R.id.gameOnlyPlus);
        this.onlyMinus = findViewById(R.id.gameOnlyMinus);
        this.onlyTimes = findViewById(R.id.gameOnlyTimes);
        this.anyOp = findViewById(R.id.gameAnyOp);

        this.db = FirebaseModule.getInstance();
        if (db.img!=null)
            this.img.setImageBitmap(db.img);
        this.startGame();
    }


    private void startGame(){
        this.guess.setVisibility(View.INVISIBLE);
        this.score = new Score(db.getUser().getEmail());
        this.max = new Score();
        this.db.getDatabase().getReference("scores").child(score.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                max = snapshot.getValue(Score.class);
                if (max==null) {    // if there isn't a score in the database
                    FirebaseModule.getInstance().getDatabase().getReference("scores").child(score.getKey()).setValue(score);
                    max = new Score();
                }
                game.setMax(max);
                maxText.setText(max.toString());
                guess.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                max = new Score();
            }
        });
        this.maxText.setText(max.toString());
        this.scoreText.setText(score.toString());
        this.game = new Game(score,max);
        this.game.newRound();
        this.updateQuestion();
        // set the mode btn events
        this.easy.setOnClickListener(view->{
            this.game.setLevel(1);
            this.game.newRound();
            this.resetGuess();
            this.updateQuestion();
        });
        this.medium.setOnClickListener(view->{
            this.game.setLevel(2);
            this.game.newRound();
            this.resetGuess();
            this.updateQuestion();
        });
        this.hard.setOnClickListener(view->{
            this.game.setLevel(3);
            this.game.newRound();
            this.resetGuess();
            this.updateQuestion();
        });

        // set the only buttons
        this.onlyPlus.setOnClickListener(view->{
            this.game.setSetOp(MathOperators.PLUS);
            this.newRound();
        });
        this.onlyMinus.setOnClickListener(view->{
            this.game.setSetOp(MathOperators.MINUS);
            this.newRound();
        });
        this.onlyTimes.setOnClickListener(view->{
            this.game.setSetOp(MathOperators.TIMES);
            this.newRound();
        });
        this.anyOp.setOnClickListener(view->{
            this.game.setSetOp(null);
            this.newRound();
        });

        // set the guess event
        this.guessBtn.setOnClickListener(view->{
            this.makeGuess();
        });
        this.guess.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE)
                makeGuess();
            return actionId == EditorInfo.IME_ACTION_DONE;
        });
    }

    public void makeGuess(){
        int res = 0;
        try {
            res = Integer.parseInt(this.guess.getText().toString());
        } catch (Exception ex){
            this.makeToast("Invalid guess");
            // TODO game over, show leaderboard,credit, yours score
            return;
        }
        if (this.game.guess(res,false)){
            this.makeToast("Correct");
            if (this.game.scoreUp()) {
                this.maxText.setText(max.toString());
                this.db.getDatabase().getReference("scores").child(score.getKey()).setValue(score);
            }
            this.scoreText.setText(score.toString());
        }
        else {
            this.makeToast("Incorrect, The answer was " + this.game.getRes());
            this.db.setScore(score);
            this.db.setMax(max);
            Intent intent = new Intent(this,EndActivity.class);
            startActivity(intent);
        }
        this.newRound();
    }

    private void newRound(){
        this.game.newRound();
        this.updateQuestion();
        this.resetGuess();

    }
    private void updateQuestion(){
        this.text.setText(this.game.getQuestion());
    }
    private void resetGuess(){
        this.guess.setText("0");
        this.guess.getText().clear();
    }
    private void makeToast(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

}