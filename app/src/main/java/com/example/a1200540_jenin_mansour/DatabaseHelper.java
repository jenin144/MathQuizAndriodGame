package com.example.a1200540_jenin_mansour;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE PLAYERS(ID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME TEXT UNIQUE NOT NULL, EMAIL TEXT NOT NULL,DOB TEXT NOT NULL)");
        sqLiteDatabase.execSQL("CREATE TABLE SCORES(ID INTEGER PRIMARY KEY AUTOINCREMENT,NICKNAME TEXT UNIQUE NOT NULL,SCORE INTEGER NOT NULL,TIMESTAMP TEXT NOT NULL)");
        sqLiteDatabase.execSQL("CREATE TABLE QUESTIONS(ID INTEGER PRIMARY KEY AUTOINCREMENT,QUESTION TEXT NOT NULL,CORRECT_ANSWER INTEGER NOT NULL,OPTION1 INTEGER NOT NULL,OPTION2 INTEGER NOT NULL, OPTION3 INTEGER NOT NULL)");

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public boolean  insertPlayer(Player player) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USERNAME", player.getUserName());
        contentValues.put("Email", player.getEmail());
        contentValues.put("DOB", player.getDOB());

        //check the uniqueness of The UserName
        try {
            sqLiteDatabase.insertOrThrow("PLAYERS", null, contentValues);
            return true;  // If successful, return true
        } catch (SQLiteConstraintException e) {
            return false;
        } finally {
            sqLiteDatabase.close();
        }
    }

    public Cursor getAllplayers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM PLAYERS", null);
    }
    public Cursor getAllQ() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM QUESTIONS", null);
    }
    public Cursor getAllscores() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM SCORES", null);
    }

    //*
    public void insertQuestion( String questionText, int correctAnswer,int opt1, int opt2, int opt3 ) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("QUESTION", questionText);
        values.put("CORRECT_ANSWER", correctAnswer);
        values.put("OPTION1", opt1);
        values.put("OPTION2", opt2);
        values.put("OPTION3", opt3);

        sqLiteDatabase.insert("QUESTIONS", null, values);
        sqLiteDatabase.close();

    }


    public void insertScore( String UserName , int score ) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NICKNAME", UserName);
        values.put("SCORE", score);
        String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis());
        values.put("TIMESTAMP", currentTimestamp);

        sqLiteDatabase.insert("SCORES", null, values);
        sqLiteDatabase.close();

    }


    public Cursor selectTop5Scores() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT NICKNAME, SCORE FROM SCORES ORDER BY SCORE DESC LIMIT 5", null);
    }

    public Cursor selectTotalPlayers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT COUNT(DISTINCT ID) AS total FROM PLAYERS", null);
    }

    public Cursor selectPlayerScore(String nickname) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT SCORE, TIMESTAMP FROM SCORES WHERE NICKNAME = ?", new String[]{nickname});
    }


    public Cursor selectAverageScore() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT AVG(SCORE) AS avg FROM SCORES", null);
    }


    public Cursor selectHighestScore() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT NICKNAME, SCORE , TIMESTAMP FROM SCORES ORDER BY SCORE DESC ", null);
    }

    public List<Question> getRandomQuestions(int questionCount) {
        List<Question> questionsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM QUESTIONS ORDER BY RANDOM() LIMIT ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(questionCount)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String questionText = cursor.getString(1);
                String correctAnswer = cursor.getString(2);
                String option1 = cursor.getString(3);
                String option2 = cursor.getString(4);
                String option3 = cursor.getString(5);

                // Create a Question object and add it to the list
                questionsList.add(new Question(questionText, correctAnswer, option1, option2, option3));
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return questionsList;
    }
    public boolean isTableEmpty(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }



}

