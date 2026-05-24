package Simulator;

import ast.*;
import cms.util.maybe.Maybe;
import exceptions.SyntaxError;
import model.Constants;
import model.ReadOnlyCritter;
import parse.ParserFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Critter implements Tile {

    /** The coordinate of this Critter */
    private int row = 0, col = 0;

    /**
     * The direction of this Critter
     * Class Invariant: 0 <= {@code orientation} <= 5
     */
    private int orientation;

    /** The 'species' of this Critter, as read from the Critter File */
    private String species = "default_name";

    /**
     * Stores critter memory, as follows:<br>
     * mem[0]: the length of the critter’s memory (immutable, always at least 7)<br>
     * mem[1]: defensive ability (immutable, ≥ 1)<br>
     * mem[2]: offensive ability (immutable, ≥ 1)<br>
     * mem[3]: size (variable, but cannot be assigned directly, ≥ 1)<br>
     * mem[4]: energy (variable, but cannot be assigned directly, ≥ 1)<br>
     * mem[5]: pass number, explained below (variable, but cannot be assigned directly, ≥ 1).<br>
     * mem[6]: posture (assignable only to values between 0 and 99).<br>
     * mem[7...n] are general-purpose entries
     */
    private int[] mem;

    /** The root of the Critter's AST */
    private ProgramImpl rootAST;

    /** Keeps track of the index of the last executed Rule object from ASTInterpreter. Is {@code -1} if no rules were executed */
    private int lastExecutedRuleIndex = -1;

    /** The Interpreter for this Critter's AST */
    private ASTInterpreter astInterpreter;

    /** Instance variable for access to private values (nested class) */
    private final ReadOnlyCritter readOnlyCritter = new CritterVarAccess();

    /** Returns the ReadOnlyCritter object */
    public ReadOnlyCritter getReadOnlyCritter() {
        return readOnlyCritter;
    }

    /** Temporary Critter Initializer for Testing purposes */
    public Critter() {
        this(null);
    }

    /** Temporary Critter Initializer for Testing purposes */
    public Critter(Program ast) {
        Tiles tiles = new Tiles(1, 1, false);
        tiles.place(this, 0, 0);
        astInterpreter = new ASTInterpreter(tiles);
        row = 0;
        col = 0;
        orientation = 0;
        species = "space critter";
        mem = new int[]{7, 1, 1, 1, 500, 1, 0};
        rootAST = (ProgramImpl) ast;
    }

    /**
     * Parses a new instance of Critter from {@code fileName}. Prints out warning messages when Critter File has invalid inputs
     * @param fileName the file containing a valid Critter file
     * @param tiles the tiles instance this Critter exists in
     * @throws IOException if an Exception is thrown when reading {@code fileName}
     * @throws IllegalArgumentException if the expected Critter File is Invalid, World should catch this Exception and
     * print a warning message to the user
     * @throws SyntaxError if the expected Critter File is Invalid, World should catch this Exception and
     * print a warning message to the user
     */
    public Critter(String fileName, Tiles tiles, int col, int row, int direction) throws IOException, IllegalArgumentException {
        this.row = row;
        this.col = col;
        this.astInterpreter = new ASTInterpreter(tiles);

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;

        // Read and validate each line in order.
        while ((line = br.readLine()) != null) {
            if (line.isEmpty() || line.startsWith("//")) continue;
            else if (line.startsWith("species: ")) {
                species = line.substring(9).trim();
            } else if (line.startsWith("memsize: ")) {
                try {
                    int memLength = Integer.parseInt(line.substring(9));
                    if (memLength < Constants.MIN_MEMORY) {
                        System.err.println(mem == null ? "Critter File missing memsize value" : "Critter File invalid memsize value {" + mem.length + "}");
                        mem = new int[Constants.MIN_MEMORY];
                    } else mem = new int[memLength];
                    mem[0] = mem.length;

                    mem[5] = 1; //pass should initialize to 1 always, since rule processing always start at 1
                } catch (NumberFormatException e) {
                    System.err.println("Critter Invalid number format for 'memsize'");
                }
            } else if (line.startsWith("defense: ")) {
                try {
                    mem[1] = Integer.parseInt(line.substring(9));
                } catch (NumberFormatException e) {
                    System.err.println("Critter File invalid number format for 'defense'");
                }
            } else if (line.startsWith("offense: ")) {
                try {
                    mem[2] = Integer.parseInt(line.substring(9));
                } catch (NumberFormatException e) {
                    System.err.println("Critter File invalid number format for 'offense'");
                }
            } else if (line.startsWith("size: ")) {
                try {
                    mem[3] = Integer.parseInt(line.substring(6));
                } catch (NumberFormatException e) {
                    System.err.println("Critter File invalid number format for 'size'");
                }
            } else if (line.startsWith("energy: ")) {
                try {
                    mem[4] = Integer.parseInt(line.substring(8));
                } catch (NumberFormatException e) {
                    System.err.println("Critter File invalid number format for 'energy'");
                }
            } else if (line.startsWith("posture: ")) {
                try {
                    mem[6] = Integer.parseInt(line.substring(9));
                } catch (NumberFormatException e) {
                    System.err.println("Critter File invalid number format for 'posture'");
                }
            } else {
                String restOfFile = line + System.lineSeparator() + br.lines().collect(Collectors.joining(System.lineSeparator()));;
                try {
                    this.rootAST = (ProgramImpl) ParserFactory.getParser().parse(new StringReader(restOfFile));
                    if (!rootAST.classInv()) throw new SyntaxError(-1, "AST violates its Class Invariant");
                } catch (SyntaxError e) {
                    throw new IllegalArgumentException("Encountered unknown line '" + line + "', attempted to parse rest of file as Critter Program and failed\n" + restOfFile + "\n" + e.toString());
                }
            }
        }
        if (mem == null || mem.length < Constants.MIN_MEMORY) {
            System.err.println(mem == null ? "Critter File missing memsize value" : "Critter File invalid memsize value {" + mem.length + "}");
            mem = new int[Constants.MIN_MEMORY];
            mem[0] = mem.length;
            mem[5] = 1;
        }
        if (mem[1] < 1) {
            System.err.println("Invalid defense value {" + mem[1] + "}");
            mem[1] = 1;
        }
        if (mem[2] < 1) {
            System.err.println("Critter File invalid offense value {" + mem[2] + "}");
            mem[2] = 1;
        }
        if (mem[3] < 1) {
            System.err.println("Critter File invalid size value {" + mem[3] + "}");
            mem[3] = 1;
        }
        if (mem[4] < 1 || mem[4] > mem[3] * Constants.ENERGY_PER_SIZE) {
            System.err.println("Critter File invalid energy value {" + mem[4] + "}");
            mem[4] = Math.clamp(mem[4], 1, mem[3] * Constants.ENERGY_PER_SIZE);
        }
        if (mem[6] < 0 || mem[6] > 99) {
            System.err.println("Critter File invalid posture value {" + mem[6] + "}");
            mem[6] = 0;
        }

        this.orientation = direction;
    }


    /**
     * Creates a new instance of Critter from Budding {@code parent}
     * @param parent the parent of this Critter
     * @param tiles the Tiles instance this Critter exists in
     * @param row the row this critter will be in
     * @param col the col this critter will be in
     */
    public Critter(Critter parent, Tiles tiles, int col, int row, boolean mutation) {
        this.astInterpreter = new ASTInterpreter(tiles);
        this.rootAST = (ProgramImpl) parent.rootAST.clone();

        this.mem = new int[parent.mem[0]];
        mem[0] = parent.mem[0]; //mem array size
        mem[1] = parent.mem[1]; //defense copied
        mem[2] = parent.mem[2]; //offense copied
        mem[3] = 1; //size
        mem[4] = Constants.INITIAL_ENERGY; //energy
        mem[5] = 1; //pass number
        mem[6] = 0; //posture

        this.row = row;
        this.col = col;
        this.species = parent.species;
        this.orientation = (parent.orientation + 3) % 6;
        if (mutation) {
            mutate();
        }
    }

    /**
     * Creates a new instance of Critter from Mating {@code parent1} and {@code parent2}
     * @param parent1 one of the parent of this Critter
     * @param parent2 the other parent of this Critter
     * @param tiles the Tiles instance this Critter exists in
     * @param row the row this Critter is in, must be behind one parent
     * @param col the col this Critter is in, must be behind one parent
     * @param orientation the orientation of this Critter, should be facing the opposite way of whichever parent it's behind
     */
    public Critter(Critter parent1, Critter parent2, Tiles tiles, int col, int row, int orientation,boolean mutation) {
        this.astInterpreter = new ASTInterpreter(tiles);
        this.row = row;
        this.col = col;
        this.orientation = orientation;

        this.species = Math.random() > 0.5 ? parent1.species : parent2.species; //species
        mem = new int[Math.random() > 0.5 ? parent1.mem.length : parent2.mem.length]; //mem length
        mem[0] = mem.length; //mem length
        mem[1] = (Math.random() > 0.5 ? parent1 : parent2).mem[1]; //defense
        mem[2] = (Math.random() > 0.5 ? parent1 : parent2).mem[2]; //offense
        mem[3] = 1; //size
        mem[4] = Constants.INITIAL_ENERGY; //energy
        mem[5] = 1; //pass number
        mem[6] = 0; //posture

        //mix and match rulesets of each parent
        List<Node> ruleSet1 = parent1.rootAST.getChildren(), ruleSet2 = parent2.rootAST.getChildren();
        int ruleLength = (Math.random() > 0.5 ? ruleSet1 : ruleSet2).size();
        List<Rule> newRuleSet = new ArrayList<>();
        for (int i = 0; i < ruleLength; i++) {
            if (i >= ruleSet1.size()) newRuleSet.add((Rule) ruleSet2.get(i).clone());
            else if (i >= ruleSet2.size()) newRuleSet.add((Rule) ruleSet1.get(i).clone());
            else newRuleSet.add((Rule) (Math.random() > 0.5 ? ruleSet1.get(i) : ruleSet2.get(i)).clone());
        }
        this.rootAST = new ProgramImpl(newRuleSet);
        if (mutation) {
            mutate();
        }
    }

    /**
     * Attempts to step the critter forward one time step
     */
    public CritterAction step() {
        lastExecutedRuleIndex = astInterpreter.interpret();
        if (lastExecutedRuleIndex == -1) {
            return CritterAction.wait;
        }
        assert lastExecutedRuleIndex >= 0 && lastExecutedRuleIndex < rootAST.getChildren().size();

        Rule executedRule = (Rule) rootAST.getChildren().get(lastExecutedRuleIndex);

        assert executedRule.getChildren().getLast() instanceof Action;
        Action action = (Action) executedRule.getChildren().getLast();

        return astInterpreter.interpretAction(action);
    }

    /** Sets the Coordinate of this Critter instance */
    public void setCoord(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Updates the Critter's energy by {@code deltaEnergy}
     * returns true if Critter's energy is below 1 (dies), false otherwise
     */
    public boolean updateEnergy(int deltaEnergy) {
        mem[4] = Math.min(mem[3] * Constants.ENERGY_PER_SIZE, mem[4] + deltaEnergy);
        return mem[4] < 1;
    }

    /** Returns the index at {@link #mem} array of this Critter */
    public int getMem(int index) {
        return mem[index];
    }

    /** Increases its size variable by one */
    public void grow() {
        mem[3]++;
    }

    /** Returns the complexity of this critter object. */
    public int getComplexity() {
        return rootAST.getChildren().size() * Constants.RULE_COST + (mem[1] + mem[2]) * Constants.ABILITY_COST;
    }

    /** @return the column of the tile */
    public int getCol() {
        return col;
    }

    /** @return the row of the tile */
    public int getRow() {
        return row;
    }

    /** Returns whether this class instance follows its class invariant or not */
    public boolean classInv() {
        boolean memCheck = mem != null && mem.length == mem[0] && mem[0] >= 7 && mem[1] >= 1 && mem[2] >= 1 && mem[3] >= 1 && mem[4] >= 1 && mem[5] >= 1 && mem[6] >= 0 && mem[6] <= 99 && mem[4] <= mem[3] * Constants.ENERGY_PER_SIZE;
        boolean ASTCheck = rootAST != null && rootAST.classInv();
        boolean dirCheck = orientation >= 0 && orientation <= 5;
        boolean interpreterCheck = astInterpreter != null && astInterpreter.classInv();
        boolean tilesCheck = astInterpreter != null && astInterpreter.tiles.getTile(col, row) == this;
        return memCheck && ASTCheck && dirCheck && interpreterCheck && tilesCheck;
    }

    /**
     * Returns a deep-copy of this Critter
     */
    public Tile clone(Tiles tiles) {
        Critter c = new Critter();
        c.rootAST = (ProgramImpl) rootAST.clone();
        c.mem = new int[mem.length];
        System.arraycopy(mem, 0, c.mem, 0, mem.length);
        c.orientation = orientation;
        c.row = row;
        c.col = col;
        c.species = species;
        c.lastExecutedRuleIndex = lastExecutedRuleIndex;
        c.astInterpreter = new ASTInterpreter(tiles);
        return c;
    }

    /** Mutates both the Critter's attributes and program. Different from {@link #mutateProgram()} */
    private void mutate() {
        while (Math.random() < 0.25) {
            if (Math.random() < 0.5)
                rootAST = (ProgramImpl) rootAST.mutate();
            else {
                int random = (int) (Math.random() * 3);
                switch (random) {
                    case 0: { //change memsize
                        int[] newMem = new int[(mem.length >= 8 && Math.random() < 0.5) ? mem.length - 1 : mem.length + 1];
                        System.arraycopy(mem, 0, newMem, 0, Math.min(mem.length,newMem.length));
                        mem = newMem;
                        break;
                    }
                    case 1: { //change defense
                        mem[1] = (mem[1] >= 2 && Math.random() < 0.5) ? mem[1] - 1 : mem[1] + 1;
                        break;
                    }
                    case 2: { //change offense
                        mem[2] = (mem[2] >= 2 && Math.random() < 0.5) ? mem[2] - 1 : mem[2] + 1;
                        break;
                    }
                }
            }
        }
    }

    /** Mutates the critter's program a singular time */
    public void mutateProgram() {
        rootAST = (ProgramImpl) rootAST.mutate();
    }

    /** Returns the integer representation of the appearance of this Critter */
    public int getAppearance() {
        return mem[3] * 1000 + mem[6] * 10 + orientation;
    }

    /**
     * Turns the critter left or right by 60 degrees
     * @param left true if turning left, false otherwise
     */
    public void turn(boolean left) {
        if (left) orientation--;
        else orientation++;
        if (orientation < 0) orientation += 6;
        else if (orientation > 5) orientation -= 6;
    }

    private class CritterVarAccess implements ReadOnlyCritter {
        @Override
        public String getSpecies() {
            return species;
        }

        @Override
        public int[] getMemory() {
            int[] memCopy = new int[mem.length];
            System.arraycopy(mem, 0, memCopy, 0, mem.length);
            return memCopy;
        }

        /** Gets the pretty-printed AST */
        @Override
        public String getProgramString() {
            return rootAST.toString();
        }

        @Override
        public Maybe<String> getLastRuleString() {
            if (lastExecutedRuleIndex == -1) return Maybe.none();
            return Maybe.some(rootAST.getChildren().get(lastExecutedRuleIndex).toString());
        }

        @Override
        public int getOrientation() {
            return orientation;
        }
    }

    //changed to public class for testing purposes
    public class ASTInterpreter {

        private final Integer[] nearby = new Integer[6];
        /** Default value: -1 */
        private int smell = -1;
        /** Default value: Integer.MIN_VALUE */
        private int ping = Integer.MIN_VALUE;

        /** A pointer to the World instance this Critter exists in, used for sensory information */
        private final Tiles tiles;

        //changed to public for testing purposes
        public ASTInterpreter(Tiles tiles) {
            this.tiles = tiles;
        }

        private boolean classInv() {
            return tiles != null && tiles.getTile(col, row) == Critter.this;
        }

        /** Interprets the Action AST object into a CritterAction that can easily be processed by {@code World} */
        public CritterAction interpretAction(Action action) {
            return switch (action.getValue()) {
                case "wait" -> CritterAction.wait;
                case "forward" -> CritterAction.forward;
                case "backward" -> CritterAction.backward;
                case "left" -> CritterAction.turnLeft;
                case "right" -> CritterAction.turnRight;
                case "eat" -> CritterAction.eat;
                case "attack" -> CritterAction.attack;
                case "grow" -> CritterAction.grow;
                case "bud" -> CritterAction.bud;
                case "mate" -> CritterAction.mate;
                case "serve" -> CritterAction.serve.setVal(interpretExpr((Expr) action.getChildren().getFirst()));
                default -> throw new RuntimeException("Unexpected CritterAction type: " + action.getValue());
            };
        }

        /**
         * Interprets the Critter AST and manipulates the Critter's Memory array. The Interpreter rule is as follows:<br>
         * - Finds the first rule whose condition is true<br>
         * - Perform all Updates in that rule<br>
         * - If that rule has no Action, restart the process from the first rule, keeping all changes to mem<br>
         * - If that rule has an Action, return the index of the Rule object in the Program root
         * - If no Action is returned after {@link Constants#MAX_RULES_PER_TURN} restarts, return -1
         * @return the index of the successful rule with an Action, or -1 if no successful rules are found
         */
        public int interpret() {
            //This method should start by rewriting every instance variable (nearby & smell) to the newest information
            //in this round. Allows for easy access to all variables so no re-calculations are needed
            // Ex: (if nearby[3] are called twice in the same AST)
            smell = -1;
            ping = Integer.MIN_VALUE;
            Arrays.fill(nearby, null);

            ArrayList<Update> updates = new ArrayList<>();

            for (int i = 0; i < Constants.MAX_RULES_PER_TURN; i++) {
                mem[5] = i + 1;
                for (int j = 0; j < rootAST.getChildren().size(); j++) {
                    updates.clear();
                    Rule r = (Rule) rootAST.getChildren().get(j);
                    Node child = r.getChildren().getFirst();
                    if (interpretCondition((Condition) child)) {
                        for (Node n : r.getChildren()) {
                            if (n instanceof Update) {
                                updates.add((Update) n);
                            }
                        }
                        processUpdates(updates);
                        if (r.getChildren().getLast() instanceof Action) {
                            return j;
                        }
                    }
                }
            }
            return -1;
        }

        /**
         * Processes all updates to modify the Critter Memory
         * @param updates the list of {@link Update} objects to process
         */
        private void processUpdates(ArrayList<Update> updates) {
            for (Update up : updates) {
                int index = interpretExpr((Expr) up.getChildren().getFirst());
                int updateVal = interpretExpr((Expr) up.getChildren().getLast());
                //Conditions: index can't be 0~5. index must be within bounds of mem array.
                //index = 6 implies that updateVal is valid for posture (0~99)
                if (index >= 6 && index < mem.length && (index != 6 || (updateVal >= 0 && updateVal <= 99))) {
                    mem[index] = updateVal;
                }
            }
        }

        /**
         * Interprets a {@link Condition} class in the Critter AST
         * @param condition the Condition Object
         * @return true if the Condition is true, false otherwise
         */
        private boolean interpretCondition(Condition condition) {
            String op = condition.getValue();
            boolean left;
            boolean right;

            if (condition instanceof BinaryCondition) {
                left = interpretCondition((Condition) condition.getChildren().getFirst());
                right = interpretCondition((Condition) condition.getChildren().getLast());
                return switch (op) {
                    case "and" -> left && right;
                    case "or" -> left || right;
                    default -> throw new IllegalArgumentException("Unknown operator: " + op);
                };
            } else {
                int leftCondition = interpretExpr((Expr) condition.getChildren().getFirst());
                int rightCondition = interpretExpr((Expr) condition.getChildren().getLast());
                return switch (condition.getValue()) {
                    case ">" -> leftCondition > rightCondition;
                    case "<" -> leftCondition < rightCondition;
                    case "=" -> leftCondition == rightCondition;
                    case ">=" -> leftCondition >= rightCondition;
                    case "<=" -> leftCondition <= rightCondition;
                    case "!=" -> leftCondition != rightCondition;
                    default -> throw new IllegalArgumentException("Unknown operator: " + condition.getValue());
                };
            }
        }

        /**
         * Interprets the Expr and converts it into a number format
         */
        private int interpretExpr(Expr expr) {
            if (expr instanceof Term) {
                int left = interpretExpr((Expr) expr.getChildren().getFirst());
                int right = interpretExpr((Expr) expr.getChildren().getLast());
                return switch (expr.getValue()) {
                    case "+" -> left + right;
                    case "-" -> left - right;
                    case "*" -> left * right;
                    case "/" -> right == 0 ? 0 : Math.floorDiv(left, right);
                    case "mod" -> right == 0 ? 0 : Math.floorMod(left, right);
                    default -> throw new RuntimeException("Invalid Term in AST " + expr.getValue());
                };
            } else {
                Factor factor = (Factor) expr;
                switch (factor.getNumType()) {
                    case inv:
                        return -interpretExpr((Expr) factor.getChildren().getFirst());
                    case num:
                        try {
                            return Integer.parseInt(factor.getValue());
                        } catch (NumberFormatException e) {
                            throw new RuntimeException("Invalid Factor in AST");
                        }
                    case args:
                        if (factor.getValue().equals("smell")) return fetchSmell();
                        else if(factor.getValue().equals("ping")) return fetchPing();
                        int val = interpretExpr((Expr) factor.getChildren().getFirst());
                        switch (factor.getValue()) {
                            case "nearby":
                                while (val < 0) val += 6;
                                return fetchNearby(val % 6);
                            case "ahead":
                                if (val <= 0) return getAppearance();
                                return fetchAhead(val);
                            case "random":
                                if (val < 2) return 0;
                                return fetchRandom(val);
                            case "mem":
                                if (val < 0 || val >= mem.length) return 0;
                                return mem[val];
                            default:
                        }
                    default:
                        throw new RuntimeException("Unknown numType in Factor of type " + factor.getNumType());
                }
            }
        }

        /**
         * observes the hex in direction {@code dir} and returns the following:<br>
         * - 0 if the hex is completely empty.<br>
         * - n > 0 if the hex contains a critter with appearance n (see Project Spec Section 8).<br>
         * – n < −1 if the hex contains some food, with total energy value (−n) − 1.<br>
         * – n = −1 if the hex contains a rock.
         */
        private int fetchNearby(int dir) {
            if (nearby[dir] == null) nearby[dir] = tiles.nearby(col, row, dir);
            return nearby[dir];
        }

        /** Returns the integer representation of whatever the Critter sees {@code numSteps} distance ahead of itself */
        private int fetchAhead(int dist) {
            return tiles.ahead(col, row, orientation, dist);
        }

        /**
         * generates a random integer from 0 up to one less than the value of {@code num}.<br>
         * Ex: random[2] gives either 0 or 1 randomly. Always return zero when {@code num} < 2
         */
        private int fetchRandom(int num) {
            if (num < 0) return 0;
            return (int) (Math.random() * num);
        }

        /**
         * returns the distance to the nearest food or 1,000,000 if no food is within a distance of {@code MAX_SMELL_DISTANCE}
         */
        private int fetchSmell() {
            if (smell == -1) smell = tiles.smell(col, row, orientation);
            return smell;
        }

        /**
         * returns the energy cost and direction of the path to the nearest food, returns -1 if there is no food within a distance of {@code PING_STRENGTH}
         */
        private int fetchPing() {
            if (ping == Integer.MIN_VALUE) ping = tiles.ping(col, row, orientation);
            return ping;
        }
    }
}
