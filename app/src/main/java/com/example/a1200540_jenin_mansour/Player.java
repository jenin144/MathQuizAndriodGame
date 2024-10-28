package com.example.a1200540_jenin_mansour;

public class Player {
    private String UserName;
    private String Email;
    private String DOB;



    public Player() {
    }
    public Player(String UserName ,String Email,String DOB)
    {
        this.UserName = UserName;
        this.Email = Email;
        this.DOB = DOB;


    }

    public String getUserName() {
        return UserName;
    }
    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }
    @Override
    public String toString() {
        return "Customer{" +
                "\n, UserName='" + UserName + '\'' +
                "\n, Email='" + Email + '\'' +
                "\n, DOB='" + DOB + '\'' +
                "\n}\n\n";
    }
}