import V2VM.CFG.CFG;
import V2VM.CFGVisitor;
import V2VM.RegisterAllocator;
import cs132.util.ProblemException;
import cs132.vapor.ast.*;
import cs132.vapor.ast.VBuiltIn.Op;
import cs132.vapor.parser.VaporParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class V2VM {
    public static void main(String[] args) {
        VaporProgram v;
        boolean debug = false;

        // parse the vapor code
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
            for (VInstr i : f.body)
                i.accept(cfgv);
            cfg.normalize();
            cfgs.add(cfg);
        }

        // allocate registers for each function
        String[] registers = {"t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "t8"};
        ArrayList<String> regs = new ArrayList<>(Arrays.asList(registers));
        for (CFG cfg : cfgs) {
            RegisterAllocator reg_alloc = new RegisterAllocator(cfg.vars, regs);
            reg_alloc.LinearScanRegisterAllocation();
        }

//        String[] registers = {
//            "v0", "v1", // for return and stack pop temporary
//            "a0", "a1", "a2", "a3", // argument passing
//            "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "t8"
//            "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7"
//        };
        // TODO copy function tables
        StringBuilder vaporm = new StringBuilder();
        for (VDataSegment d : v.dataSegments) {
            vaporm.append("const " + d.ident + "\n");
            for (VOperand.Static n : d.values)
                vaporm.append("  " + n + "\n");
            vaporm.append('\n');
        }
        for (CFG cfg : cfgs) {
            vaporm.append("func " + cfg.fname + " []\n");
            for (int i = 0; i < cfg.size; i++) {
                vaporm.append(cfg.get(i).element.toVapor(cfg));
            }
            vaporm.append("\n");
        }
        System.out.println(vaporm);
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
