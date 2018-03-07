import java.io.*;
import java.util.*;
import syntaxtree.*;

public class J2V {
    private static void _J2V() {
        _J2V("", false);
    }

    private static void _J2V(String file, boolean debug) {
        Node root = null;
        try {
            // Stream start, execute parser
            if(debug) {
                System.out.println("Running J2V on " + file);
                System.setIn(new FileInputStream(file));
            }
            root = new MiniJavaParser(System.in).Goal();

            // Build symbol sigt
            SymTableVisitor stv = new SymTableVisitor();
            root.accept(stv, null);
            // Helper for type check visitor
            SymbolHelper tch = new SymbolHelper(stv.symt, stv.sigt, stv.objs);
            // normalize inheritance
            tch.normalize();
            if (debug)
                System.out.println(tch);

            // Java -> Vapor
        } catch (ParseException | FileNotFoundException e) {
            System.err.println(e.toString());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        if (args.length > 0)
            if (args[0].equals("--manual")) {
                Scanner reader = new Scanner(System.in);
                System.out.print("File: testFiles/");
                _J2V("testFiles/" + reader.next(), true);
            } else
                _J2V(args[0], true);
        else
            _J2V();
    }
}
