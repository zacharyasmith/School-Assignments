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

            // Build symbol sigt
            SymTableVisitor<Void> stv = new SymTableVisitor();
            root.accept(stv, "");

            // Helper for type check visitor
            TypeCheckHelper tch = new TypeCheckHelper(stv.symt, stv.sigt, stv.objs);
            if (debug)
                System.out.println(tch);

            // Type check
            TypeCheckVisitor tcv = new TypeCheckVisitor(debug, tch);
            root.accept(tcv, null);
            if (tcv.tch.passing) {
                System.out.println("Program type checked successfully");
            } else {
                System.out.println("Type error");
            }
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
