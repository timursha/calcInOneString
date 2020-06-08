package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        while (true) {
            System.out.println("Input");
            Scanner scanner = new Scanner(System.in);
            String tapedValue = scanner.nextLine();
            List<Lexeme> lexemes = analyse(tapedValue);
            LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
            checkNumberWithRome(lexemes);
            System.out.println("Output");
            int output = expr(lexemeBuffer);
            if (LexemeBuffer.isRomaValue == true) {
                System.out.println(getRoman(output));
                LexemeBuffer.isRomaValue = false;

            } else {
                System.out.println(output);
                LexemeBuffer.isRomaValue = false;


            }
        }
    }

    public static List<Lexeme> analyse(String parsedLine) {
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int countChar = 0;
        while (countChar < parsedLine.length()) {
            char ch = parsedLine.charAt(countChar);
            switch (ch) {
                case '+':
                    lexemes.add(new Lexeme(LexemeType.OP_PLUS, ch));
                    countChar++;
                    continue;
                case '-':
                    lexemes.add(new Lexeme(LexemeType.OP_MINUS, ch));
                    countChar++;
                    continue;
                case '*':
                    lexemes.add(new Lexeme(LexemeType.OP_MUL, ch));
                    countChar++;
                    continue;
                case '/':
                    lexemes.add(new Lexeme(LexemeType.OP_DIV, ch));
                    countChar++;
                    continue;
                default:
                    if ((ch == 'I') | (ch == 'V') | (ch == 'X')) {
                        StringBuilder sbr = new StringBuilder();
                        do {
                            sbr.append(ch);
                            countChar++;
                            if (countChar >= parsedLine.length()) {
                                break;
                            }
                            ch = parsedLine.charAt(countChar);
                        } while ((ch == 'I') | (ch == 'V') | (ch == 'X'));
                        lexemes.add(new Lexeme(LexemeType.ROM, sbr.toString()));
                    } else if (ch <= '9' && ch >= '0') {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(ch);
                            countChar++;
                            if (countChar >= parsedLine.length()) {
                                break;
                            }
                            ch = parsedLine.charAt(countChar);
                        } while (ch <= '9' && ch >= '0');
                        lexemes.add(new Lexeme(LexemeType.NUMBER, sb.toString()));
                    } else {
                        if (ch != ' ') {
                            throw new RuntimeException("Unexpected character: " + ch);
                        }
                        countChar++;
                    }
            }
        }
        lexemes.add(new Lexeme(LexemeType.EOF, ""));

        return lexemes;
    }

    public static int expr(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexemeType.EOF) {
            return 0;
        } else {
            lexemes.back();
            return plusminus(lexemes);
        }
    }

    public static int plusminus(LexemeBuffer lexemes) {
        int value = multdiv(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case OP_PLUS:
                    value += multdiv(lexemes);
                    break;
                case OP_MINUS:
                    value -= multdiv(lexemes);
                    break;
                case EOF:
                    lexemes.back();
                    if (lexemes.lexemes.size() > 4) {
                        throw new RuntimeException("too much value " + lexemes.lexemes.size());
                    }
                    if (lexemes.lexemes.get(0).type.toString() == "ROM") {
                        LexemeBuffer.isRomaValue = true;
                    }
                    return value;

                default:
                    throw new RuntimeException("Unexpected token: " + lexeme.value
                            + " at position: " + lexemes.getPos());
            }
        }
    }

    public static int multdiv(LexemeBuffer lexemes) {
        int value = factor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case OP_MUL:
                    value *= factor(lexemes);
                    break;
                case OP_DIV:
                    value /= factor(lexemes);
                    break;
                case EOF:
                case OP_PLUS:
                case OP_MINUS:
                    lexemes.back();
                    return value;
                default:
                    throw new RuntimeException("Unexpected values: " + lexeme.value
                            + " at position: " + lexemes.getPos());
            }
        }
    }

    public static int factor(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();

        switch (lexeme.type) {
            case ROM:
                switch (lexeme.value) {
                    case "I":
                        return 1;
                    case "II":
                        return 2;
                    case "III":
                        return 3;
                    case "IV":
                        return 4;
                    case "V":
                        return 5;
                    case "VI":
                        return 6;
                    case "VII":
                        return 7;
                    case "VIII":
                        return 8;
                    case "IX":
                        return 9;
                    case "X":
                        return 10;
                    default:
                        throw new RuntimeException("Unexpected values");

                }
            case NUMBER:
                if (Integer.parseInt(lexeme.value) > 10) {
                    throw new RuntimeException("Unexpected values");
                }
                return Integer.parseInt(lexeme.value);

            default:
                throw new RuntimeException("Unexpected values");
        }
    }

    public static void checkNumberWithRome(List<Lexeme> lexemes) {
        int isRom = 0;
        int isNumber = 0;
        for (int i = 0; i < lexemes.size(); i++) {
            switch (lexemes.get(i).type.toString()) {
                case "ROM":
                    isRom++;
                    continue;
                case "NUMBER":
                    isNumber++;
                    continue;
                default:
                    if (isNumber > 0 && isRom > 0) {
                        throw new RuntimeException("Unexpected values");
                    }
            }
        }
    }


    public static String getRoman(int number) {

        String riman[] = {"M", "XM", "CM", "D", "XD", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int arab[] = {1000, 990, 900, 500, 490, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (number > 0 || arab.length == (i - 1)) {
            while ((number - arab[i]) >= 0) {
                number -= arab[i];
                result.append(riman[i]);
            }
            i++;
        }
        return result.toString();
    }
}