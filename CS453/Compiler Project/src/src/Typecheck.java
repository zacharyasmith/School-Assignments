import java.io.*;
import java.util.*;
import visitor.*;
import syntaxtree.*;

public class Typecheck {
    public static void main(String[] args) {
        Node root = null;
        boolean debug = args.length > 0;
        try {
            if(debug)
                System.setIn(new FileInputStream(args[0]));
            root = new MiniJavaParser(System.in).Goal();
            if (debug) {
                // Pretty-print
                PPrinter<Void> pp = new PPrinter();
                System.out.println("Starting...");
                root.accept(pp, "");
            }
        } catch (ParseException e) {
            System.out.println(e.toString());
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
            System.exit(1);
        }
    }
}
