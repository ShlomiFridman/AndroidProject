package com.example.androidproject;

public class Game {

    private int min,max,lv;
    private int num1,num2,res;
    private Score score,maxScore;
    private MathOperators op;
    private MathOperators setOp;

    public Game(Score score, Score max){
        this.maxScore = max;
        this.score = score;
        this.maxScore.setEmail("Max");
        this.op = MathOperators.PLUS;
        this.setLevel(1);
    }

    /**
     * return a number in range, min - max-1
     * @param min the min number
     * @param max the max number, not included
     * @return int (from min to max-1)
     */
    private int genNum(int min,int max){
        return (int) (Math.floor(Math.random()*(max-min))+min);
    }

    public void setMax(Score max){
        this.maxScore = max;
        this.maxScore.setEmail("Max");
    }

    // set the level, easy,medium,hard
    public void setLevel(int lv){
        this.lv = lv;
        switch (lv){
            case 1:
                this.setRange(2,11);
                break;
            case 2:
                this.setRange(11,21);
                break;
            case 3:
                this.setRange(21,41);
                break;
        }
    }
    /**
     * set the range (min - max-1)
     * @param min the min num
     * @param max the max num
     * @return true
     */
    private boolean setRange(int min,int max){
        this.min = min;
        this.max = max;
        return true;
    }

    public void newRound(){
        do{
            num1 = genNum(min,max);
            num2 = genNum(min,max);
        } while (num1==num2);
        if (setOp==null)
            op = MathOperators.values()[genNum(0,3)];
        res = op.getResult(num1,num2);
    }

    /**
     * check if the guess was current, if advance equals true will advance to the next round
     * @param guess the given guess
     * @param advance if true will advance to the next round and up the score
     * @return boolean
     */
    public boolean guess(int guess,boolean advance){
        if (advance && guess == res){
            this.scoreUp();
            newRound();
        }
        return guess == res;
    }

    /**
     * up the score by one, update max if needed
     * @return
     */
    public boolean scoreUp(){
        this.score.inc(lv);
        if (this.score.getScore()>this.maxScore.getScore())
            this.maxScore.copyScore(score);
        return this.score.getScore()>=this.maxScore.getScore();
    }

    /**
     * returns the current question, Ex: 1 + 3
     * @return String
     */
    public String getQuestion(){
        String str = this.toString();
        return str.substring(0,str.lastIndexOf(32)+1);
    }

    @Override
    public String toString(){
        return String.format("%d %c %d = %d",num1,op.sign,num2,res);
    }

    public int getRes() {
        return res;
    }

    public void setSetOp(MathOperators setOp) {
        this.setOp = this.op = setOp;
    }
}
