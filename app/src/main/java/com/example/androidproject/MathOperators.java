package com.example.androidproject;

public enum MathOperators {

    PLUS('+'),MINUS('-'),TIMES('x');

    public final char sign;

    private MathOperators(char ch){
        this.sign = ch;
    }

    public int getResult(int a,int b){
        switch (this.sign){
            case '+': return a+b;
            case '-': return a-b;
            case 'x':
            case 'X':
            case '*': return a*b;
        }
        throw new NullPointerException("Invalid enum");
    }

}
