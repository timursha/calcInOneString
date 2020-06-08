package com.company;

import java.util.List;

public class LexemeBuffer {

    private int countChar;
    public List<Lexeme> lexemes;
    protected static boolean isRomaValue = false;

    public LexemeBuffer(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
    }

    public Lexeme next() {
        return lexemes.get(countChar++);
    }

    public void back() {
        countChar--;
    }

    public int getPos() {
        return countChar;
    }
}
