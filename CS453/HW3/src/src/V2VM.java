import V2VM.CFG.CFG;
import V2VM.CFGVisitor;
import cs132.util.ProblemException;
import cs132.vapor.ast.VBuiltIn;
import cs132.vapor.ast.VBuiltIn.Op;
import cs132.vapor.ast.VFunction;
import cs132.vapor.ast.VInstr;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.parser.VaporParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class V2VM {
    public static void main(String[] args) {
        VaporProgram v;
        boolean debug = false;
        if (args.length > 0) {
            if (args[0].equals("--manual")) {
                Scanner reader = new Scanner(System.in);
                System.out.print("File: testVapor/");
                v = parseVapor("testVapor/" + reader.next());
            } else
                v = parseVapor(args[0]);
            debug = true;
        }
        else
            v = parseVapor(System.in, System.err);

        // generate CFGs
        ArrayList<CFG> cfgs = new ArrayList<>();
        for (VFunction f : v.functions) {
            CFG cfg = new CFG(f.vars, f);
            CFGVisitor cfgv = new CFGVisitor(cfg);
            for (VInstr i : f.body) {
                i.accept(cfgv);
            }
            cfg.normalize();
            if (debug)
                System.out.println(cfg);
            // add it to the list
            cfgs.add(cfg);
        }

        // TODO print vaporm
    }

    public static VaporProgram parseVapor(String file) {
        try {
            return parseVapor(new FileInputStream(file), System.err);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static VaporProgram parseVapor(InputStream in, PrintStream err) {
        VBuiltIn.Op[] ops = {
                Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS,
                Op.PrintIntS, Op.HeapAllocZ, Op.Error,
        };
        boolean allowLocals = true;
        String[] registers = null;
        boolean allowStack = false;

        VaporProgram program;
        try {
            program = VaporParser.run(new InputStreamReader(in), 1, 1,
                    java.util.Arrays.asList(ops),
                    allowLocals, registers, allowStack);
        } catch (IOException | ProblemException ex) {
            err.println(ex.getMessage());
            return null;
        }

        return program;
    }
}
