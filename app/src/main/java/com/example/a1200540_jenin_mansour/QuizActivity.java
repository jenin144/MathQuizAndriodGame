package com.example.a1200540_jenin_mansour;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.os.CountDownTimer;

public class QuizActivity extends AppCompatActivity {

    private List<Question> randomQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int correctAnswers , incorrectAnswers = 0;

    private TextView summaryTextView; // TextView for summary
    private TextView correctAnswersTextView; // TextView for correct answers
    private TextView incorrectAnswersTextView; // TextView for incorrect answers
    private TextView scoreTextView;

    private CountDownTimer timer;
    private String username;
    DatabaseHelper databaseHelper = new DatabaseHelper(this, "TODO2", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        // Retrieve the username from the intent
        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        Toast.makeText(this, "Welcome " + username + "!", Toast.LENGTH_SHORT).show();

        if (databaseHelper.isTableEmpty("QUESTIONS"))
            loadQuestionsFromCSV();

        // Select random questions
        randomQuestions = databaseHelper.getRandomQuestions(5);

        summaryTextView = findViewById(R.id.summaryTextView);
        correctAnswersTextView = findViewById(R.id.correctAnsTextView);
        incorrectAnswersTextView = findViewById(R.id.incorrectAnsTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setVisibility(View.INVISIBLE);

        // Display the first question
        if (!randomQuestions.isEmpty()) {
            displayQuestion(randomQuestions.get(currentQuestionIndex));
        }

        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(view -> {
            finishQuiz();
            Intent intent1 = new Intent(QuizActivity.this, EndActivity.class);
            QuizActivity.this.startActivity(intent1);
            finish();
        });
    }

    private void loadQuestionsFromCSV() {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        try {
            // Open the CSV file from the raw resources
            InputStream f = getResources().openRawResource(R.raw.questions);
            BufferedReader reader = new BufferedReader(new InputStreamReader(f));

            String line;
            // Skip the first header row
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");

                if (columns.length < 2) {
                    continue;
                }

                String questionText = columns[0].trim(); // Get the question
                int correctAnswer = Integer.parseInt(columns[1].trim()); // Get the correct answer

                List<Integer> options = new ArrayList<>();
                Random random = new Random(System.currentTimeMillis());

                // Add the correct answer
                options.add(correctAnswer);

                // Generate two  random options
                while (options.size() < 3) {
                    int option = correctAnswer + (random.nextInt(11) - 5);
                    if (!options.contains(option) && option != correctAnswer) {
                        options.add(option);
                    }
                }
                java.util.Collections.shuffle(options);
                databaseHelper.insertQuestion(questionText, correctAnswer, options.get(0), options.get(1), options.get(2));
            }
            reader.close();
        } catch (Exception e) {
            Log.e("QuizActivity", "Error loading questions from CSV", e);
        } finally {
            db.close();
        }
    }

    private void displayQuestion(Question question) {
        TextView questionTextView = findViewById(R.id.questionTextView);
        questionTextView.setText(question.getQuestionText());

        // Generate answer options
        String correctAnswer = question.getCorrectAnswer();

        Button optionButton1 = findViewById(R.id.optButton1);
        optionButton1.setText(question.getOption1());
        optionButton1.setOnClickListener(view -> {
            checkOption(question.getOption1(), correctAnswer);
        });


        Button optionButton2 = findViewById(R.id.optButton2);
        optionButton2.setText(question.getOption2());
        optionButton2.setOnClickListener(view -> {
            checkOption(question.getOption2(), correctAnswer);
        });


        Button optionButton3 = findViewById(R.id.optButton3);
        optionButton3.setText(question.getOption3());
        optionButton3.setOnClickListener(view -> {
            checkOption(question.getOption3(), correctAnswer);
        });

        TextView timerTextView = findViewById(R.id.timerTextView);

        // Start the countdown timer
        timerTextView.setText("10");

        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                // Timer finished, move to the next question
                Toast.makeText(QuizActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                incorrectAnswers++; // Increment incorrect answers
                incorrectAnswersTextView.setText("Incorrect Answers: " + incorrectAnswers);
                currentQuestionIndex++;
                if (currentQuestionIndex < 5) {
                    displayQuestion(randomQuestions.get(currentQuestionIndex)); // Use selected questions
                } else {
                    finishQuiz();
                }
            }
        }.start(); // Start the countdown timer
    }
    private void checkOption(String opt, String correctAnswer) {
        if (timer != null) {
            timer.cancel();
        }

        if (opt.equals(correctAnswer)) {
            correctAnswers++;
            correctAnswersTextView.setText("Correct Answers: " + correctAnswers);
            Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();
        } else {
            incorrectAnswers++; // Increment score for incorrect answer
            incorrectAnswersTextView.setText("Incorrect Answers: " + incorrectAnswers);
            Toast.makeText(this, "Incorrect Answer!", Toast.LENGTH_SHORT).show();
        }

        currentQuestionIndex++;
        if (currentQuestionIndex < 5) {
            displayQuestion(randomQuestions.get(currentQuestionIndex)); // Use selected questions
        } else {
            finishQuiz();
        }
    }

    private void finishQuiz() {
        // Disable option buttons
        findViewById(R.id.optButton1).setEnabled(false);
        findViewById(R.id.optButton2).setEnabled(false);
        findViewById(R.id.optButton3).setEnabled(false);

        if (timer != null) {
            timer.cancel();
        }

        scoreTextView.setVisibility(View.VISIBLE);

        correctAnswersTextView.setText("Correct Answers: " + correctAnswers);
        incorrectAnswersTextView.setText("Incorrect Answers: " + incorrectAnswers);
        int score = (-incorrectAnswers + correctAnswers);
        if (score < 0) score = 0;
        scoreTextView.setText("Score : " + score);

        databaseHelper.insertScore(username, score);
        Toast.makeText(this, "Score saved successfully! for player " + username, Toast.LENGTH_SHORT).show();
    }
}
