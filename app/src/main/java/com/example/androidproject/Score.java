package com.example.androidproject;

public class Score implements Comparable<Score>{

    private String email;
    private int score;
    private int easy,medium,hard;

    public Score(){
        this.email = "Max";
    }
    public Score(String email){
        this.email = email;
    }
    public Score(Score score){
        this.email = score.email;
        this.score = score.score;
        this.easy = score.easy;
        this.medium = score.medium;
        this.hard = score.hard;
    }

    // score ++
    public void inc(int lv){
        this.score++;
        switch (lv){
            case 1:
                this.easy++;
                break;
            case 2:
                this.medium++;
                break;
            case 3:
                this.hard++;
                break;
        }
    }

    // copy the score's score, ranks
    public void copyScore(Score score){
        this.score = score.score;
        this.easy = score.easy;
        this.medium = score.medium;
        this.hard = score.hard;
    }

    // get firebase key, can not enter an email with .
    public String getKey(){
        return this.email.replace('.', ',');
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getEasy() {
        return easy;
    }

    public void setEasy(int easy) {
        this.easy = easy;
    }

    public int getMedium() {
        return medium;
    }

    public void setMedium(int medium) {
        this.medium = medium;
    }

    public int getHard() {
        return hard;
    }

    public void setHard(int hard) {
        this.hard = hard;
    }

    @Override
    public int compareTo(Score other){
        return this.score - other.score;
    }

    @Override
    public String toString(){
        return String.format("%s Score: %d [ Easy: %d | Medium: %d | Hard: %d ]",email,score,easy,medium,hard);
    }
}
