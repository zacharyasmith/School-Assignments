import java.io.*;
import java.util.*;
import visitor.*;
import syntaxtree.*;

public class Typecheck {
    private static void typecheck() {
        typecheck("", false);
    }

    private static void typecheck(String file, boolean debug) {
        Node root = null;
        try {
            if(debug)
                System.setIn(new FileInputStream(file));
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

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("--manual")) {
                Scanner reader = new Scanner(System.in);
                System.out.println("File: ");
                typecheck(reader.next(), true);
            } else
                for (String f : args)
                    typecheck(f, true);
            return;
        }
        typecheck();
    }
}
