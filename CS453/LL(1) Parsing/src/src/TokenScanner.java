import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TokenScanner {

    private ArrayList<String> tokens;
    private int pointer;
    private boolean debug;

    public TokenScanner(boolean debug) throws Exception {
        this.debug = debug;
        this.tokens = new ArrayList<>();
        scan();
        if (debug) {
            System.out.println("Done scanning tokens.");
            for (String s : tokens)
                System.out.print(s + ", ");
            System.out.println("\n==========================");
        }
    }

    public boolean eof() { return pointer == tokens.size(); }

    public String peek() {
        if (!eof())
            return tokens.get(pointer);
        return "eof!";
    }

    public String consume() {
        String ret = peek();
        pointer++;
        return ret;
    }

    private static String nextChar(String in, int index) {
        if (index + 1 < in.length())
            return in.substring(index + 1, index + 2);
        return null;
    }

    private void scan() throws Exception {
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
                } else if (ch.equals("$"))
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
}
