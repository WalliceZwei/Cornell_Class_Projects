package main;


import ast.Mutation;
import ast.MutationFactory;
import ast.Program;
import ast.ProgramImpl;
import ast.mutations.*;
import cms.util.maybe.Maybe;
import exceptions.SyntaxError;
import parse.Parser;
import parse.ParserFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/*
src/test/resources/files/draw_critter.txt
src/test/resources/files/example-rules.txt
src/test/resources/files/mutated_critter_1.txt
src/test/resources/files/mutated_critter_2.txt
src/test/resources/files/mutated_critter_3.txt
src/test/resources/files/mutated_critter_4.txt
src/test/resources/files/mutated_critter_5.txt
src/test/resources/files/mutated_critter_6.txt
src/test/resources/files/unmutated_critter.txt

--mutate 19 src/test/resources/files/draw_critter.txt
--mutate 31 src/test/resources/files/example-rules.txt
--mutate 15 src/test/resources/files/mutated_critter_1.txt
--mutate 9 src/test/resources/files/mutated_critter_2.txt
--mutate 11 src/test/resources/files/mutated_critter_3.txt
--mutate 13 src/test/resources/files/mutated_critter_4.txt
--mutate 15 src/test/resources/files/mutated_critter_5.txt
--mutate 18 src/test/resources/files/mutated_critter_6.txt
--mutate 2 src/test/resources/files/unmutated_critter.txt

--mutate -3 src/test/resources/files/draw_critter.txt
--mutate -5 src/test/resources/files/example-rules.txt
--mutate -21394 src/test/resources/files/mutated_critter_1.txt

--mutate 9 src/test/resources/files/mutated_crit.txt
--mutate 11 src/test/resources/files/mutatedrtr_3.txt
--mutate 13 src/test/resources/files/gibberish.txt
*/

public class ParseAndMutateApp {
    public static void main(String[] args) {
        int n = 0;
        String file = null;
        try {
            if (args.length == 1) {
                file = args[0];
            } else if (args.length == 3 && args[0].equals("--mutate")) {
                n = Integer.parseInt(args[1]);
                if (n < 0) throw new IllegalArgumentException();
                file = args[2];
            } else {
                throw new IllegalArgumentException();
            }
            ProgramImpl program;
            try {
                program = (ProgramImpl) ParserFactory.getParser().parse(new FileReader(file));
            } catch (FileNotFoundException | SyntaxError e) {
                throw new IllegalArgumentException();
            }
            int mutationCount = n;

            System.out.println("Pretty-printed result of file '"+file+"'\n"+program);

            while (n-- > 0) {
                //chooses random mutation
                Mutation m = new Mutation[]{
                        MutationFactory.getTransform(),
                        MutationFactory.getSwap(),
                        MutationFactory.getRemove(),
                        MutationFactory.getReplace(),
                        MutationFactory.getInsert(),
                        MutationFactory.getDuplicate()}
                        [(int)(Math.random() * 6)];

                //chooses random valid node for mutation
                int indexOfMutation;
                do{
                    indexOfMutation = (int)(Math.random()*program.size());
                }while(!m.canApply(program.nodeAt(indexOfMutation)));

                //mutate program
                program = (ProgramImpl) program.mutate(indexOfMutation,m).orElse(program);

                System.out.println("Mutation -> " + switch (m) {
                    case Duplicate duplicate -> "duplicate";
                    case Transform transform -> "transform";
                    case Insert insert -> "insert";
                    case Remove remove -> "remove";
                    case Replace replace -> "replace";
                    case Swap swap -> "swap";
                    case null, default -> throw new RuntimeException("Encountered Unknown mutation");
                });
                System.out.println("Pretty-printed result of file '"+file+"' after "+(mutationCount-n)+" mutation(s)\n"+program+"\n");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Usage:\n  <input_file>\n  --mutate <n> <input_file>");
        }
    }
}
