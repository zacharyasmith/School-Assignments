import java.io.*;
import java.util.*;
import syntaxtree.*;

public class Typecheck {
    private static void typecheck() {
        typecheck("", false);
    }

    private static void typecheck(String file, boolean debug) {
        Node root = null;
        try {
            // Stream start, execute parser
            if(debug)
                System.setIn(new FileInputStream(file));
            root = new MiniJavaParser(System.in).Goal();

            // Pretty print parser output
//            if (debug) {
//                PPrinter pp = new PPrinter();
//                System.out.println("Starting...");
//                root.accept(pp, "");
//            }

            // Build symbol table
            SymTableVisitor<Void> stv = new SymTableVisitor();
            root.accept(stv, "");

            // Build method table
            SignatureVisitor<Void> sv = new SignatureVisitor();
            root.accept(sv, "");

            // Helper for type check visitor
            TypeCheckHelper tch = new TypeCheckHelper(stv.table, sv.table);
            if (debug)
                System.out.println(tch);

            // Type check
            TypeCheckVisitor<Void> tcv = new TypeCheckVisitor(debug, tch);
            root.accept(tcv, null);
        } catch (ParseException | FileNotFoundException e) {
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
