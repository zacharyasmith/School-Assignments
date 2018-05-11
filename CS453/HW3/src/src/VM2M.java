import VM2M.elements.Element;
import cs132.util.ProblemException;
import cs132.vapor.ast.*;
import cs132.vapor.ast.VBuiltIn.Op;
import cs132.vapor.parser.VaporParser;
import VM2M.ElementsVisitor;
import VM2M.MipsFunction;

import java.io.*;
import java.util.ArrayList;
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
        ArrayList<MipsFunction> mfs = new ArrayList<>();
        for (VFunction f : v.functions) {
            ElementsVisitor ev = new ElementsVisitor(new MipsFunction(f));
            for (VInstr i : f.body)
                i.accept(ev);
            mfs.add(ev.f);
        }


        /**
         * TO MIPS
         */
        StringBuilder mips = new StringBuilder();

        // Copying function tables
        mips.append(".data\n\n");
        for (VDataSegment d : v.dataSegments) {
            mips.append(d.ident + ":\n");
            System.out.println(d.values[0].getClass());
            for (VOperand.Static s : d.values)
                mips.append("  " + ((VLabelRef)s).ident + "\n");
            mips.append('\n');
        }

        // Begin execution section
        mips.append(".text\n\n  jal Main\n  li $v0 10\n  syscall\n\n");

        // Process each function
        for (MipsFunction f : mfs) {
            mips.append(f.f.ident + ":\n");
            mips.append(f.prologue());

            // print statements
            for (Element e : f.elements)
                mips.append(e.toMIPS(f));

            // extra line
            mips.append('\n');
        }

        // Helper functions
        mips.append("_print:\n  li $v0 1\n  syscall\n  la $a0 _newline\n  li $v0 4\n  syscall\n  jr $ra\n\n" +
                "_error:\n  la $a0 _err\n  li $v0 4\n  syscall\n  li $v0 10\n  syscall\n\n" +
                "_heapAlloc:\n  li $v0 9\n  syscall\n  jr $ra\n\n" +
                ".data\n.align 0\n_newline: .asciiz \"\\n\"\n_err: .asciiz \"null pointer\\n\"\n");

        if (debug) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("tmp.s"));
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
