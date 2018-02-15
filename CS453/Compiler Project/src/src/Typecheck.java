import java.io.*;
import java.util.*;
import visitor.*;
import syntaxtree.*;

public class Typecheck {
    public static void main(String[] args) {
        Node root = null;
        try {
            root = new MiniJavaParser(System.in).Goal();

            // Pretty-print
            PPrinter<Void> pp = new PPrinter();
            System.out.println("Starting...");
            root.accept(pp, "");
        } catch (ParseException e) {
            System.out.println(e.toString());
            System.exit(1);
        }
    }
}
