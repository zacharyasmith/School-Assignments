/*
LL(1) GRAMMAR =========================
- LL(1): left to right scan, left-most derivation, 1-token lookahead
- ignore and discard tabs, spaces, newlines, and Awk comments
- Your parser should use a greedy scanner ('++' is ++, not + +)
- \s equates to (\s|\n)+

<expr> ::= <a> <t>
<t>    ::= num | [null]
<a>    ::= <b> <u>
<u>    ::= binop <a> | [null]
<b>    ::= incrop <b> | <c>
<c>    ::= <d> <v>
<v>    ::= incrop <v> | [null]
<d>    ::= $<e> | <e>
<e>    ::= (<expr>) | num
incrop ::=	++ | --
binop  ::=	+ | -
num    ::=	0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9

Examples ==============================

** Ex. 1
INPUT
$1 + (1 - ++$2) $3

 */

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Parse {
    ArrayList<String> tokens;
    ArrayList<ArrayList<String>> AST; // abstract syntax tree
    boolean debug;

    public Parse() throws Exception {
        this.tokens = new ArrayList<>();
        this.debug = true;
        this.scan();
        if (debug) {
            System.out.println("Done scanning tokens.");
            for (String s : tokens)
                System.out.println(s);
            System.out.println("=====================================");
        }
    }

    public static String nextChar(String in, int index) {
        if (index + 1 < in.length()) {
            return in.substring(index, index + 1);
        }
        return null;
    }

    public void parse() {

    }

    public void scan() throws Exception {
        Scanner s = new Scanner(System.in);
        int lineNumber = 0;
        while (s.hasNextLine()) {
            String next = s.nextLine();
            int len = next.length();
            for (int i = 0; i < len; i++) {
                String ch = next.substring(i, i + 1);
                if (Pattern.matches("[\t\n\\s#]", ch)) {
                    // discard tabs, spaces, newlines, and Awk comments
                    if(ch.equals("#"))
                        i = len;
                } else if (ch.equals("+")) {
                    if (nextChar(next, i) != null && nextChar(next, i).equals("+")) {
                        i++;
                        tokens.add("++");
                    } else
                        tokens.add(ch);
                } else if (ch.equals("-")) {
                    if (nextChar(next, i) != null && nextChar(next, i).equals("-")) {
                        i++;
                        tokens.add("--");
                    } else
                        tokens.add(ch);
                } else if (ch.equals("-"))
                    tokens.add(ch);
                else if (ch.equals("$"))
                    tokens.add(ch);
                else if (ch.equals("("))
                    tokens.add(ch);
                else if (ch.equals(")"))
                    tokens.add(ch);
                else if (Pattern.matches("[0-9]", ch))
                    tokens.add(ch);
                else {
                    if (debug) {
                        System.out.println("Exception.");
                        System.out.println("\tIllegal: " + ch);
                        System.out.println("\tFrom: " + next);
                        System.out.println("\tIndex: " + i);
                    }
                    System.out.println("Parse error in line " + lineNumber);
                    throw new InvalidObjectException("");
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Parse p = new Parse();
    }
}
