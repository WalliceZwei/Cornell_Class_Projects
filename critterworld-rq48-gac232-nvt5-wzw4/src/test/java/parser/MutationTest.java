package parser;

import ast.*;
import ast.mutations.*;
import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import exceptions.SyntaxError;
import org.junit.jupiter.api.RepeatedTest;
import parse.Parser;
import parse.ParserFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A Class that tests for all types of mutations<br>
 * IMPORTANT: For some mutations, there's a small likelihood that the mutated AST will be identical to the previous AST
 * in which case, set assertNotEquals to false and check output console for report.
 */
public class MutationTest {

    @RepeatedTest(100)
    void simpleTestDuplicate() {
        simpleMutationTest(MutationFactory.getDuplicate());
    }
    @RepeatedTest(100)
    void simpleTestInsert() {
        simpleMutationTest(MutationFactory.getInsert());
    }
    @RepeatedTest(100)
    void simpleTestRemove() {
        simpleMutationTest(MutationFactory.getRemove());
    }
    @RepeatedTest(100)
    void simpleTestReplace() {
        simpleMutationTest(MutationFactory.getReplace());
    }
    @RepeatedTest(100)
    void simpleTestSwap() {
        simpleMutationTest(MutationFactory.getSwap());
    }
    @RepeatedTest(100)
    void simpleTestTransform() {
        simpleMutationTest(MutationFactory.getTransform());
    }

    @RepeatedTest(1000)
    void testDuplicate(){ //all works as intended
        testMutation(MutationFactory.getDuplicate(),true);
    }

    @RepeatedTest(10000)
    void testInsert(){ //all works as intended
        testMutation(MutationFactory.getInsert(),true);
    }

    @RepeatedTest(1000)
    void testRemove(){ //all works as intended
        testMutation(MutationFactory.getRemove(),true);
    }

    @RepeatedTest(1000)
    void testReplace(){ //all works as intended
        testMutation(MutationFactory.getReplace(),false);
    }

    @RepeatedTest(1000)
    void testSwap(){ //all works as intended
        testMutation(MutationFactory.getSwap(),false);
    }

    @RepeatedTest(1000)
    void testTransform(){ //all works as intended
        testMutation(MutationFactory.getTransform(),false);
    }

    /**
     * Tests the mutate function of ProgramImpl. The result after mutating must be a well-formed AST and
     *  should not be equal to the original AST. Some mutations could have the mutated AST remain identical as the
     *  un-mutated AST, in this case, user must set {@code assertNotEquals} to the correct type
     * @param m the mutation being tested
     * @param assertNotEquals if true, this test case asserts that mutated program must not be equal to un-mutated program.
     *                     otherwise, this test case simply prints out 'equal' or 'not equal' to console.
     */
    private void testMutation(Mutation m,boolean assertNotEquals){
        Program program = RandomASTGenerator.generateRandomAST(50,10);
        assertTrue(program.classInv(), "class invariant must be true for random AST before applying mutation");
        ArrayList<Integer> validNodeIndices = new ArrayList<>();
        for(int i=0;i<program.size();i++) if(m.canApply(program.nodeAt(i))) validNodeIndices.add(i);

        if(validNodeIndices.isEmpty()) return;

        Maybe<Program> maybeProgram;

        int validNodeIndex;
        do {
            validNodeIndex = validNodeIndices.get((int)(Math.random() * validNodeIndices.size()));
            maybeProgram = program.mutate(validNodeIndex, m);
        } while(maybeProgram.isEmpty());

        Program newProgram;
        try{newProgram = maybeProgram.get();}
        catch (NoMaybeValue e) {throw new RuntimeException(e);}

        assertTrue(newProgram.classInv(),"class invariant must be true for random AST after applying mutation");

        if(assertNotEquals) {
            assertNotEquals(program, newProgram);
        }else if(program.equals(newProgram))
            System.out.println("equal");
        else
            System.out.println("not equal");
    }

    // simple mutation tests to check if mutations produce expected results
    private void simpleMutationTest(Mutation m) {
        Parser parser = ParserFactory.getParser();
        File f = new File("src/test/resources/A4files/unmutated_critter.txt");
        ProgramImpl program;
        try {program = (ProgramImpl) parser.parse(new FileReader(f));}
        catch (FileNotFoundException | SyntaxError e) {throw new RuntimeException("file not found!");}
        ArrayList<Integer> validNodeIndices = new ArrayList<>();
        for(int i=0;i<program.size();i++) if(m.canApply(program.nodeAt(i))) validNodeIndices.add(i);
        if(validNodeIndices.isEmpty()) return;

        int validNodeIndex;
        validNodeIndex = validNodeIndices.get((int)(Math.random() * validNodeIndices.size()));
        Program p = (Program) program.clone();
        try {
            if (m instanceof Replace) {
                simpleReplaceTest(p,validNodeIndex,m);
            }
            if (m instanceof Remove) {
                simpleRemoveTest(p,validNodeIndex,m);
            }
            if (m instanceof Insert) {
                simpleInsertTest(p,validNodeIndex,m);
            }
            if (m instanceof Swap) {
                simpleSwapTest(p,validNodeIndex,m);
            }
            if (m instanceof Transform) {
                simpleTransformTest(p,validNodeIndex,m);
            }
            if (m instanceof Duplicate) {
                simpleDuplicateTest(p,validNodeIndex,m);
            }
        } catch (NoMaybeValue e) {
            System.out.println("null");
        }
    }
    // mutated node is of same category
    private void simpleReplaceTest(Program p, int i, Mutation m) throws NoMaybeValue {
        NodeCategory oldCategory = p.nodeAt(i).getCategory();
        p = p.mutate(i, m).get();
        NodeCategory newCategory = p.nodeAt(i).getCategory();
        assertSame(oldCategory, newCategory);
    }
    // node's old indexed spot is now used by another node
    private void simpleRemoveTest(Program p, int i, Mutation m) throws NoMaybeValue {
        List<Node> possibleValues = new ArrayList<>();
        for (int k = i+1; k < p.size(); k++) {
            possibleValues.add(p.nodeAt(k));
        }
        Program new_p = p.mutate(i, m).get();
        assertTrue(possibleValues.contains(new_p.nodeAt(i)));
    }
    // node's new children and old children contain each other
    private void simpleSwapTest(Program p, int i, Mutation m) throws NoMaybeValue {
        List<Node> children = p.nodeAt(i).getChildren();
        p = p.mutate(i, m).get();
        List<Node> new_children = p.nodeAt(i).getChildren();
        assertTrue(children.size() == new_children.size() && children.containsAll(new_children) && new_children.containsAll(children));
    }
    // mutated node has same category and children
    private void simpleTransformTest(Program p, int i, Mutation m) throws NoMaybeValue {
        NodeCategory oldCategory = p.nodeAt(i).getCategory();
        List<Node> children = p.nodeAt(i).getChildren();
        p = p.mutate(i, m).get();
        List<Node> new_children = p.nodeAt(i).getChildren();
        NodeCategory newCategory = p.nodeAt(i).getCategory();
        assertSame(oldCategory, newCategory);
        assertEquals(children, new_children);
    }
    // mutated node has one additional child
    private void simpleDuplicateTest(Program p, int i, Mutation m) throws NoMaybeValue {
        List<Node> children = p.nodeAt(i).getChildren();
        p = p.mutate(i, m).get();
        List<Node> new_children = p.nodeAt(i).getChildren();
        assertEquals(children.size() + 1, new_children.size());
    }
    // node is contained within the inserted node's children
    private void simpleInsertTest(Program p, int i, Mutation m) throws NoMaybeValue {
        AbstractNode new_p = (AbstractNode) p.mutate(i, m).get();
        assertTrue(new_p.nodeAt(i).getChildren().contains(p.nodeAt(i)));
    }
}
