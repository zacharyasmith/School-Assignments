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
            // Build symbol table
            SymTableVisitor<Void, Integer> stv = new SymTableVisitor<Void, Integer>();
            root.accept(stv, 0);
            HashMap <String, IdentifierType> symt = stv.table;
            if (debug) {
                System.out.println("\nSymbol table ----------------");
                for (Map.Entry<String, IdentifierType> entry : symt.entrySet()) {
                    String key = entry.getKey();
                    IdentifierType val = entry.getValue();
                    System.out.println(key + " : " + val.type);
                }
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
