package com.example.a1200540_jenin_mansour;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, birthdateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        birthdateEditText = findViewById(R.id.birthdateEditText);

        // Initialize the Database Helper
        DatabaseHelper dataBaseHelper = new DatabaseHelper(this, "TODO2", null, 1);

        Button addCustomerButton = findViewById(R.id.startButton);
        addCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player newplayer = new Player();

                if (usernameEditText.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(), "Please enter a username.", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!isValidEmail(emailEditText.getText().toString())) {
                    Toast.makeText(view.getContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidDate(birthdateEditText.getText().toString())) {
                    Toast.makeText(view.getContext(), "Please enter a valid birthdate (DD/MM/YYYY).", Toast.LENGTH_SHORT).show();
                    return;
                }


                newplayer.setUserName(usernameEditText.getText().toString());
                newplayer.setEmail(emailEditText.getText().toString());
                newplayer.setDOB(birthdateEditText.getText().toString());

                // add the new player
                boolean added = dataBaseHelper.insertPlayer(newplayer);
                if (added) {
                    Toast.makeText(view.getContext(), "Registration successful! Starting Quiz...", Toast.LENGTH_SHORT).show();
                    // to quiz game activity
                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                    intent.putExtra("USERNAME", usernameEditText.getText().toString());
                    MainActivity.this.startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(view.getContext(), "Username already exists.choose another one", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Validate email format
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidDate(String date) {
        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate parsedDate = LocalDate.parse(date, formatter);

            if (parsedDate.getYear() >= 2024) {
                return false;
            }
            if (parsedDate.getYear() <= 1900) {
                return false;
            }

            return true; // Valid date
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}