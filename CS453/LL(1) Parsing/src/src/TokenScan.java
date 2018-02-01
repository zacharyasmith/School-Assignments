import java.io.IOException;
import java.util.Scanner;
import java.util.regex.*;

public class TokenScan {
    public static void main (String[] args) throws IOException {
        Scanner s = new Scanner(System.in);
        while (s.hasNextLine()) {
            String line = s.nextLine();
            for (int i = 0; i < line.length(); ++i) {
                String ch = line.substring(i, i+1);

                if (Pattern.matches("[a-zA-Z]", ch)) {
                    System.out.println("<Letter," + ch + ">");
                } else if (Pattern.matches("[0-9]", ch)) {
                    System.out.println("<Digit," + ch + ">");
                } else if (Pattern.matches("[^0-9a-zA-Z]", ch)) {
                    System.out.println("<Error," + ch + ">");
                }
            }
        }

        s.close();
    }
}
