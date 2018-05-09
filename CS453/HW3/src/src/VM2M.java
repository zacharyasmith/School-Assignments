import VM2M.PrettyPrintVisitor;
import cs132.util.ProblemException;
import cs132.vapor.ast.VBuiltIn.Op;
import cs132.vapor.ast.VFunction;
import cs132.vapor.ast.VInstr;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.parser.VaporParser;

import java.io.*;
import java.util.Scanner;

import static java.lang.System.exit;

public class VM2M {
    public static void main(String[] args) {
        boolean debug = false;

        // parse the vapor code
        VaporProgram v = null;
        if (args.length > 0) {
            if (args[0].equals("--manual")) {
                Scanner reader = new Scanner(System.in);
                System.out.print("File: testVaporM/");
                try {
                    v = parseVapor(new FileReader("testVaporM/" + reader.next()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    exit(1);
                }
            } else {
                try {
                    v = parseVapor(new FileReader(args[0]));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    exit(1);
                }
            }
            debug = true;
        }
        else
            v = parseVapor(new InputStreamReader(System.in));

        /**
         * Build AST
         */
        for (VFunction f : v.functions) {
            System.out.println(f.ident);
            System.out.println("in " + f.stack.in);
            System.out.println("out " + f.stack.out);
            System.out.println("local " + f.stack.local);
            System.out.println("INSTRUCTIONS");
            PrettyPrintVisitor ppv = new PrettyPrintVisitor(f);
            for (VInstr i : f.body)
                i.accept(ppv);
            System.out.println();
        }


        /**
         * TO MIPS
         */
        StringBuilder mips = new StringBuilder();

        // Copying function tables


        // Process each function

        // print statements

        // extra line

        if (debug) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("tmp.vaporm"));
                writer.write(mips.toString());
                writer.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(mips);
    }

    public static VaporProgram parseVapor(Reader in) {
        // VaporM operations
        Op[] ops = {
                Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS,
                Op.PrintIntS, Op.HeapAllocZ, Op.Error,
        };
        String[] registers = {
                "v0", "v1",
                "a0", "a1", "a2", "a3",
                "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7",
                "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7",
                "t8",
        };
        boolean allowStack = true;
        boolean allowLocals = false;

        try {
            return VaporParser.run(in, 1, 1, java.util.Arrays.asList(ops),
                    allowLocals, registers, allowStack);
        } catch (ProblemException | IOException e) {
            e.printStackTrace();
            exit(1);
            return null;
        }
    }

}
