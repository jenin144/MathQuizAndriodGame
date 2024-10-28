package com.example.a1200540_jenin_mansour;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EndActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView textViewResults;
    private EditText editTextPlayerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        ScrollView scrollView=new ScrollView(this);

        dbHelper = new DatabaseHelper(this, "TODO2", null, 1);

        textViewResults = findViewById(R.id.textViewResults);
        editTextPlayerName = findViewById(R.id.editTextPlayerName);


        Button buttonAverageScore = findViewById(R.id.btnAvgeScore);
        buttonAverageScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = dbHelper.selectAverageScore();
                String result = "Average Score: ";
                if (cursor.moveToFirst()) {
                    result += cursor.getFloat(0);
                }
                textViewResults.setText(result);
                cursor.close();
            }
        });



        Button buttonPlayerScore = findViewById(R.id.btnPlayerScores);
        buttonPlayerScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String playerName = editTextPlayerName.getText().toString().trim();
                if (!playerName.isEmpty()) {
                    Cursor cursor = dbHelper.selectPlayerScore(playerName);
                    StringBuilder result = new StringBuilder("Score for " + playerName + ":\n");
                    if (cursor.moveToFirst()) {
                        result.append("Score: ").append(cursor.getInt(0)).append(", Recorded At: ").append(cursor.getString(1)).append("\n");
                    }
                    textViewResults.setText(result.toString());
                    cursor.close();
                } else {
                    textViewResults.setText("Please enter a player's nickname.");
                }
            }
        });



        Button buttonTotalPlayers = findViewById(R.id.btnTotalPlayers);
        buttonTotalPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = dbHelper.selectTotalPlayers();
                String result = "Total Players: ";
                if (cursor.moveToFirst()) {
                    result += cursor.getInt(0);
                }
                cursor.close();
                textViewResults.setText(result);
            }
        });

        Button Top5Scores = findViewById(R.id.btnTop5);
        Top5Scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = dbHelper.selectTop5Scores();
                StringBuilder result = new StringBuilder("Top 5 Scores:\n");
                while (cursor.moveToNext()) {
                    result.append("Player: ").append(cursor.getString(0)).append(", Score: ").append(cursor.getInt(1)).append("\n");
                }
                cursor.close();
                textViewResults.setText(result.toString());
            }
        });



        Button StartNewQuiz = findViewById(R.id.buttonNewQuiz);
        StartNewQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndActivity.this, MainActivity.class);
                // intent.putExtra("USERNAME", usernameEditText.getText().toString());
                EndActivity.this.startActivity(intent);
                finish();
            }
        });


        Button buttonHighestScore = findViewById(R.id.btnHighestScore);
        buttonHighestScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = dbHelper.selectHighestScore();
                String result = "Highest Score:\n";
                if (cursor.moveToFirst()) {
                    result += "Player: " + cursor.getString(0) + ", Score: " + cursor.getInt(1) ;
                }
                textViewResults.setText(result);
                cursor.close();
            }
        });

    }
}
