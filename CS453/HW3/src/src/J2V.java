import java.io.*;
import java.util.*;

import context.SymTableVisitor;
import context.SymbolHelper;
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
            SymbolHelper sh = new SymbolHelper(stv.symt, stv.sigt, stv.objs);
            // normalize inheritance
            sh.normalize();
            if (debug)
                System.out.println(sh);

            // Java -> Vapor
            VaporVisitor vv = new VaporVisitor(sh, debug);
            root.accept(vv, null);
            String vapor = vv.toVapor();
            System.out.println(vapor);
            if (debug) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("tmp.vapor"));
                    writer.write(vapor);
                    writer.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
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
