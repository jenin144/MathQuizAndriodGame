package com.example.a1200540_jenin_mansour;

// Question.java
public class Question {
    private String questionText;
    private String correctAnswer;
    private String option1;
    private String option2;
    private String option3;

    public Question(String questionText, String correctAnswer) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }
    public Question(String questionText, String correctAnswer, String option1, String option2, String option3) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }
}
