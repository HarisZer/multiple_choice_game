package com.uniwa.multiplechoice;

public class Result {
    private String name;
    private String am;
    private String age;
    private int score;
    private String dateTime;

    public Result(String name, String am, String age, int score, String dateTime) {
        this.name = name;
        this.am = am;
        this.age = age;
        this.score = score;
        this.dateTime = dateTime;
    }


    public String getName() { return name; }
    public String getAm() { return am; }
    public String getAge() { return age; }
    public int getScore() { return score; }
    public String getDateTime() { return dateTime; }


    public void setName(String name) { this.name = name; }
    public void setAm(String am) { this.am = am; }
    public void setAge(String age) { this.age = age; }
    public void setScore(int score) { this.score = score; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
}
