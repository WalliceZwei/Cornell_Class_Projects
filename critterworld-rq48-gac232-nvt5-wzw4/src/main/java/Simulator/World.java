package Simulator;

import cms.util.maybe.Maybe;
import controller.Controller;
import model.Constants;
import model.ReadOnlyCritter;
import model.ReadOnlyWorld;

import java.io.*;
import java.util.*;


public class World implements Controller {

    /**
     * The hexagonal tiles of the world
     */
    public Tiles tiles;

    /**
     * True if food naturally spawns in this world, false otherwise
     */
    private boolean enabledMana;

    /**
     * True if a critter's program will mutate every time it finishes its action.
     */
    private boolean enabledForceMutation;

    /**
     * Stores the name of this World
     */
    private String name;

    /**
     * Instance variable for access to private values (nested class)
     */
    private final WorldVarAccess readOnlyWorld = new WorldVarAccess();

    /**
     * Stores EVERY critter in the program. Class Invariant: Must contain 2 null at any time
     * <br>While step() isn't running, this list should look like
     * <br> c1 - c2 - c3 - ... - null - null.
     * <br>When step() is running, this list should look like
     * <br> c3 - c4 - c5 - null - newborn1 - newborn2 - null - c1 - c2,
     * <br> where newborn1 & 2 are newborn critters sandwiched between two nulls,
     * c1 & 2 are critters that took their turn,
     * and c3~c5 are critters that haven't taken their turn yet
     */
    public LinkedList<Critter> critterLinkedList = new LinkedList<>();

    /**
     * Stores all critters attempting to mate in the program.
     */
    private final HashMap<Critter,Critter> matingCritters = new HashMap<>();

    /**
     * The number of elapsed time steps, negative one if the world hasn't been initialized yet
     */
    private int time = -1;

    /** whether critters can mutate after bud or mate */

    private boolean mutateCritter = true;

    /** Constructs an instance of a randomly generated world */
    public World() {
        newWorld();
        time = 0;
    }

    public void disableMutation(){
        mutateCritter=false;
    }

    /**
     * Constructs an instance of a world parsed from file {@code filename}
     */
    public World(String filename, boolean enabledMana, boolean enabledForceMutation) {
        loadWorld(filename, enabledMana, enabledForceMutation);
    }

    @Override
    public ReadOnlyWorld getReadOnlyWorld() {
        return readOnlyWorld;
    }

    @Override
    public void newWorld() {
        tiles = new Tiles();
        critterLinkedList.clear();
        critterLinkedList.add(null);
        critterLinkedList.add(null);
        matingCritters.clear();
        name = "";
        time = 0;
        enabledMana = true;
        enabledForceMutation = true;
    }

    @Override
    public boolean loadWorld(String filename, boolean enableManna, boolean enableForcedMutation) {
        critterLinkedList.clear();
        critterLinkedList.add(null);
        critterLinkedList.add(null);
        matingCritters.clear();
        tiles = new Tiles();
        name = "default_name";
        time = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String worldName = reader.readLine();
            worldName = worldName==null ? "" : worldName.replaceAll("[\\t\\n\\r]", "");

            if (worldName.startsWith("name")) {
                name = worldName.substring(5);
                System.out.println(name);
            } else {
                System.err.println("Invalid World File Argument: expected 'name <world name>', got " + worldName);
            }

            String size = reader.readLine();
            size = size==null ? "" : size.replaceAll("[\\t\\n\\r]", "");

            if (size.startsWith("size")) {
                String[] sizelist = size.split(" ");
                try{
                    tiles = new Tiles(Integer.parseInt(sizelist[1]), Integer.parseInt(sizelist[2]), false);
                }catch (NumberFormatException e){
                    System.err.println("Invalid number on line "+size);
                }
            } else {
                System.err.println("Invalid World File Argument: expected 'size <width> <height>', got " + size);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("[\\t\\n\\r]", "");
                if(line.isEmpty() || line.startsWith("//")) continue;

                String[] listy = line.split(" ");
                if (listy.length == 0)
                    System.err.println("Unexpected World File Argument '" + line + "'");
                else switch (listy[0]) {
                    case "rock": {
                        if (listy.length != 3)
                            System.err.println("Invalid World File Argument: expected 'rock <column> <row>', got " + line);
                        else try{
                            if(!tiles.place(new Rock(), Integer.parseInt(listy[1]), Integer.parseInt(listy[2])))
                                System.err.println("Invalid Rock Placement "+listy[1] + "," + listy[2]);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number on line "+line);
                        }
                        break;
                    } case "food": {
                        if (listy.length != 4)
                            System.err.println("Invalid World File Argument: expected 'food <column> <row> <amount>', got " + line);
                        else try {
                            int foodVal = Integer.parseInt(listy[3]);
                            if (foodVal <= 0) {
                                System.err.println("Invalid Food energy value {" + foodVal + "}, needs to be greater than 0");
                                break;
                            }

                            Food newFood = new Food(foodVal);
                            if(!tiles.place(newFood, Integer.parseInt(listy[1]), Integer.parseInt(listy[2])))
                                System.err.println("Invalid Food Placement "+listy[1] + "," + listy[2]);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number on line "+line);
                        }
                        break;
                    } case "critter": {
                        if (listy.length != 5)
                            System.err.println("Invalid World File Argument: expected 'critter <critter file> <column> <row> <direction>', got " + line);
                        else try {
                            int x = Integer.parseInt(listy[2]), y = Integer.parseInt(listy[3]), dir = Integer.parseInt(listy[4]);

                            File f = new File(new File(filename).getParent(), listy[1]);
                            if (!f.exists()){
                                System.err.println("Invalid Critter File '" + listy[1] + "'\nFile does not exist");
                                break;
                            }

                            Critter critter = new Critter(f.getAbsolutePath(), tiles, x, y, dir);

                            if (tiles.place(critter, x, y)) {
                                critterLinkedList.addFirst(critter);
                                assert critter.classInv();
                            }else{
                                System.err.println("Invalid Critter Placement "+x + "," + y);
                            }
                        }catch (NumberFormatException e){
                            System.err.println("Invalid number on line "+line);
                        }
                        break;
                    }
                    default:
                        System.err.println("Invalid World File Argument: expected 'rock', 'food', or 'critter', got '" + listy[0] + "' from line '" + line + "'");
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (IOException e) {
            System.err.println("IOException occurred whilst reading world file '" + filename + "'");
            e.printStackTrace();
            return false;
        }
        assert classInv();
        enabledMana = enableManna;
        enabledForceMutation = enableForcedMutation;
        return true;
    }

    public boolean loadCritter(String filename, int x, int y){
        try {
            if (tiles.getTile(x,y) == null){
                Critter critter = new Critter(filename, tiles, x,y, (int) (Math.random() * 6));
                tiles.place(critter, x, y);
                critterLinkedList.addFirst(critter);
                assert critter.classInv();
            }
            else{
                throw new IllegalArgumentException("Either out of bounds, or not an empty tile");
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (IOException e) {
            System.err.println("IOException occurred whilst reading file '" + filename);
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public boolean loadCritters(String filename, int n) {
        try {
            for (int i = 0; i < n; i++) {
                int[] coords = tiles.getRandomEmptyTile();
                if (coords == null) throw new IllegalArgumentException("World has no more empty tiles");

                int x = coords[0], y = coords[1];
                Critter critter = new Critter(filename, tiles, x,y, (int) (Math.random() * 6));

                tiles.place(critter, x, y);
                critterLinkedList.addFirst(critter);
                assert critter.classInv();
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (IOException e) {
            System.err.println("IOException occurred whilst reading file '" + filename + "' on iteration " + n);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void printWorld(PrintStream out) {
        if(time==-1){
            System.err.println("World hasn't been initialized yet");
            return;
        }
        for (int y = 0; y < tiles.getHeight(); y++) {
            for (int x = 0; x < tiles.getWidth(); x++) {
                Tile tiletype = tiles.getTile(x, tiles.getHeight() - 1 - y);
                boolean h = !tiles.isValid(x, tiles.getHeight() - 1 - y);
                if (tiletype instanceof Food) {
                    out.print("F");
                } else if (tiletype instanceof Rock) {
                    out.print("#");
                } else if (tiletype instanceof Critter) {
                    out.print(((Critter) tiletype).getReadOnlyCritter().getOrientation());
                } else if (h) {
                    out.print(" ");
                } else {
                    out.print("-");
                }
            }
            out.print("\n");
        }
        out.flush();
    }

    @Override
    public boolean advanceTime(int n) {
        if (n < 0 || time == -1){
            System.err.println(time == -1 ? "World hasn't been initialized yet" : "Invalid parameter "+n);
            return false;
        }
        for (int i = 0; i < n; i++) {
            step();
        }
        time += n;
        return true;
    }

    /**
     * Adds manna/energy to the world.
     */
    private void mannaAddition() {
        int totalsize = (tiles.getWidth() / 2) * (tiles.getHeight() / 2) + ((tiles.getWidth() + 1) / 2) * ((tiles.getHeight() + 1) / 2);
        for (int i = 0; i < (Constants.MANNA_COUNT * totalsize) / 1000; i++) {
            int[] coords = tiles.getRandomTile();
            int x = coords[0], y = coords[1];

            if (tiles.getTile(x, y) == null)
                tiles.place(new Food(), x, y);
            else if (tiles.getTile(x, y) instanceof Food)
                ((Food) tiles.getTile(x, y)).updateEnergy(Constants.MANNA_AMOUNT);
        }
    }

    /**
     * Advances the time in the game by one step.
     */
    private void step() {
        Critter critter;
        assert classInv();
        while ((critter = critterLinkedList.removeFirst()) != null) {
            assert critter.classInv();

            CritterAction action = critter.step();

            if (enabledForceMutation) critter.mutateProgram();

            if(!executeCritterAction(critter,action)){
                critterLinkedList.addLast(critter);
                assert critter.classInv();
            }

            //fixed enabledManna
            if (enabledMana && Math.random() < (1.0 / critterLinkedList.size())) {
                mannaAddition();
            }
        }

        //add all newly born critter to the end of the new critterLinkedList stack
        while((critter = critterLinkedList.removeFirst()) != null){
            critterLinkedList.addLast(critter);
        }

        //adds the two nulls back to maintain class invariant, except now
        // it reverts back to how it should look like while step() isn't running
        critterLinkedList.addLast(null);
        critterLinkedList.addLast(null);

        assert classInv();

        // unsuccessful mating for all leftover single critters :(
        for(Critter c : matingCritters.values()){
            if(c.updateEnergy(-c.getMem(3))) unalive(c);
        }
        matingCritters.clear();
    }

    /**
     * Attempts to execute a CritterAction given a critter.
     * <br> Returns if {@code critter} dies after taking the action
     */
    private boolean executeCritterAction(Critter critter,CritterAction action){
        switch(action){
            case wait -> critter.updateEnergy(Constants.SOLAR_FLUX * critter.getMem(3));
            case forward -> {
                if(critter.updateEnergy(-Constants.MOVE_COST * critter.getMem(3))){
                    unalive(critter);
                    return true;
                }

                int[] forwardCoords = getCritterDir(critter,0);
                if(tiles.isValid(forwardCoords) && tiles.getTile(forwardCoords[0], forwardCoords[1]) == null){
                    boolean asserts = tiles.move(critter,critter.getCol(),critter.getRow(),forwardCoords[0],forwardCoords[1]);
                    assert asserts;
                    critter.setCoord(forwardCoords[1],forwardCoords[0]);
                }
            } case backward -> {

                if(critter.updateEnergy(-Constants.MOVE_COST * critter.getMem(3))){
                    unalive(critter);
                    return true;
                }

                int[] backwardCoords = getCritterDir(critter,3);
                if(tiles.isValid(backwardCoords) && tiles.getTile(backwardCoords[0], backwardCoords[1]) == null){
                    boolean asserts =  tiles.move(critter,critter.getCol(),critter.getRow(), backwardCoords[0], backwardCoords[1]);
                    assert asserts;
                    critter.setCoord(backwardCoords[1],backwardCoords[0]);
                }
            } case turnLeft -> {
                if(critter.updateEnergy(-critter.getMem(3))){
                    unalive(critter);
                    return true;
                }

                critter.turn(true);
            } case turnRight -> {
                if(critter.updateEnergy(-critter.getMem(3))){
                    unalive(critter);
                    return true;
                }
                critter.turn(false);
            } case eat ->{
                if(critter.updateEnergy(-critter.getMem(3))){
                    unalive(critter);
                    return true;
                }

                int[] forwardCoordinates = getCritterDir(critter,0);
                if(!(tiles.isValid(forwardCoordinates))) break;
                Tile forwardTile = tiles.getTile(forwardCoordinates[0],forwardCoordinates[1]);

                if(forwardTile instanceof Food){
                    int consumedEnergy = critter.getMem(3) * Constants.ENERGY_PER_SIZE - critter.getMem(4);
                    int availableEnergy = ((Food)forwardTile).getEnergy();
                    int absorbedEnergy = Math.min(availableEnergy,consumedEnergy);

                    critter.updateEnergy(absorbedEnergy);
                    if(((Food)forwardTile).updateEnergy(-consumedEnergy)) tiles.remove(forwardCoordinates[0], forwardCoordinates[1]);
                }
            }
            case serve -> {
                //energyCost will never be bigger critter's own energy and no less than 0 (inclusive)
                int energyCost = Math.clamp(action.getVal()+critter.getMem(3),0,critter.getMem(4));

                int[] forwardCoordinates = getCritterDir(critter,0);

                if(tiles.isValid(forwardCoordinates)){
                    Tile tile = tiles.getTile(forwardCoordinates[0],forwardCoordinates[1]);
                    int amountServed = Math.max(0, energyCost -critter.getMem(3));

                    if(tile == null) {
                        if(amountServed !=0) tiles.place(new Food(amountServed), forwardCoordinates[0], forwardCoordinates[1]);
                    }else if(tile instanceof Food){
                        ((Food)tile).updateEnergy(amountServed);
                    }else break;

                    if(critter.updateEnergy(-energyCost)) {
                        unalive(critter);
                        return true;
                    }
                }
            } case attack -> {
                if(critter.updateEnergy(-critter.getMem(3) * Constants.ATTACK_COST)) {
                    unalive(critter);
                    return true;
                }

                int[] forwardCoordinates = getCritterDir(critter,0);
                if(!(tiles.isValid(forwardCoordinates))) break;
                Tile forwardTile = tiles.getTile(forwardCoordinates[0],forwardCoordinates[1]);
                if(forwardTile instanceof Critter c2){
                    int s1 = critter.getMem(3);
                    double x = Constants.DAMAGE_INC * (s1 * critter.getMem(2) - c2.getMem(3) * c2.getMem(1)), logisticFunc = 1 / (1 + Math.exp(-x));
                    int damageDealt = (int)Math.round(Constants.BASE_DAMAGE * s1 * logisticFunc);

                    if(c2.updateEnergy(-damageDealt)){
                        unalive(c2);
                        assert critterLinkedList.contains(c2);
                        critterLinkedList.remove(c2);
                    }
                }
            } case grow -> {
                int growthCost = critter.getMem(3) * critter.getComplexity() * Constants.GROW_COST;
                if(critter.updateEnergy(-growthCost)){
                    unalive(critter);
                    return true;
                }
                critter.grow();
            } case bud -> {
                if(critter.updateEnergy(-critter.getComplexity() * Constants.BUD_COST)){
                    unalive(critter);
                    return true;
                }

                int[] backwardCoords = getCritterDir(critter,3);
                if(!(tiles.isValid(backwardCoords))) break;
                if(tiles.getTile(backwardCoords[0], backwardCoords[1]) == null){
                    Critter newBorn = new Critter(critter,tiles, backwardCoords[0], backwardCoords[1],mutateCritter);
                    boolean asserts = tiles.place(newBorn, backwardCoords[0], backwardCoords[1]);
                    assert asserts;
                    critterLinkedList.add(critterLinkedList.indexOf(null)+1,newBorn);
                }
            } case mate -> {
                //critter's energy must be at least as much as unsuccessful mating cost
                if(critter.getMem(4) < critter.getMem(3)){
                    unalive(critter);
                    return true;
                }

                //checks if there is a potential mate directly in front of this critter, if not, unsuccessful mate instantly
                int[] forwardCoords = getCritterDir(critter,0);
                Tile critterInFront;
                if(!tiles.isValid(forwardCoords) || !((critterInFront = tiles.getTile(forwardCoords[0], forwardCoords[1])) instanceof Critter)) {
                    if (critter.updateEnergy(-critter.getMem(3))){
                        unalive(critter);
                        return true;
                    }
                    break;
                }

                //checks if there is a valid mate right now
                Critter c2 = matingCritters.get((Critter)critterInFront);
                if(c2 != null && forwardCoords[0]==c2.getCol() && forwardCoords[1]==c2.getRow()){
                    matingCritters.remove(c2);

                    boolean c1 = false,emptySpace = true;
                    int[] c1Behind = getCritterDir(critter,3),c2Behind = getCritterDir(c2,3);
                    if(!tiles.isValid(c1Behind) || tiles.getTile(c1Behind[0],c1Behind[1]) != null){
                        if(!tiles.isValid(c2Behind) || tiles.getTile(c2Behind[0],c2Behind[1]) != null){
                            emptySpace = false;
                        }else{
                            c1 = false;
                        }
                    }else{
                        c1 = (tiles.isValid(c2Behind) && tiles.getTile(c2Behind[0],c2Behind[1]) == null) ? Math.random()>0.5 : true;
                    }

                    //parent's behind is always valid
                    if(emptySpace) {
                        //todo There is one required command-line option. If the flag --disable-mutation is provided, critters should
                        //todo reproduce without any mutation. All further user interaction should be done through the graphical user
                        //todo interface
                        int[] coords = c1 ? c1Behind : c2Behind;
                        Critter newBorn = new Critter(critter, c2, tiles, coords[0], coords[1], c1 ? c2.getReadOnlyCritter().getOrientation() : critter.getReadOnlyCritter().getOrientation(),mutateCritter);
                        boolean asserts = tiles.place(newBorn, coords[0], coords[1]);
                        assert asserts;
                        critterLinkedList.add(critterLinkedList.indexOf(null)+1,newBorn);
                    }

                    //subtract energy
                    if(critter.updateEnergy(-Constants.MATE_COST * critter.getComplexity())) unalive(critter);
                    if(c2.updateEnergy(-Constants.MATE_COST * c2.getComplexity())) unalive(c2);
                } else matingCritters.put(critter, critter);
            } default -> throw new RuntimeException("Unknown CritterAction "+action);
        }
        return false;
    }

    /**
     * Returns the coordinates of the tile directly next to this Critter
     * at critter's current direction (changeable by {@code deltaDir}) or null if it does not exist
     */
    private int[] getCritterDir(Critter c,int deltaDir){
        int x = c.getCol(), y = c.getRow(), dir = c.getReadOnlyCritter().getOrientation();
        return tiles.getDirCoordinates(x,y,(dir+deltaDir)%6,1);
    }

    /**
     * Removes the Critter from {@link #tiles} and replace it with food proportional to its size
     */
    private void unalive(Critter c) {
        assert tiles.isValid(c.getCol(), c.getRow()) && tiles.getTile(c.getCol(), c.getRow()) == c;
        Tile removed = tiles.remove(c.getCol(), c.getRow());

        assert removed == c;
        boolean asserts = tiles.place(new Food(Constants.FOOD_PER_SIZE * c.getMem(3)), c.getCol(), c.getRow());
        assert asserts;
    }

    /**
     * Returns whether this class instance follows the following class invariant:
     * - The number of critters in the world is equal to the number of critters in the critters list.
     * - All tiles are either empty, contain a critter, food, or a rock.
     * - The coordinates of all objects are valid
     * - All objects of the world follow their respective class invariants.
     *
     * @return true if the class invariant is maintained, false otherwise.
     */
    public boolean classInv() {
        //world hasn't been initialized yet, classInv holds vacuously
        if(tiles == null && time == -1) return true;
        //xor since we checked the and condition above ^^
        else if(tiles == null || time == -1) return false;

        //if critterLinkedList doesn't contain exactly 2 nulls as stated by critterLinkedList class invariant
        if(critterLinkedList.stream().filter(Objects::isNull).count()!=2)
            return false;

        int critterCount = 0;
        for (int x = 0; x < tiles.getWidth(); x++) {
            for (int y = 0; y < tiles.getHeight(); y++) {
                Tile tile = tiles.getTile(x, y);
                if (tile == null) continue;
                if (!tiles.isValid(x, y)) {
                    return false;
                }
                if (tile instanceof Critter) {
                    critterCount++;
                    if (!critterLinkedList.contains(tile)) {
                        return false;
                    }
                }
                if (!tile.classInv()) {
                    return false;
                }
            }
        }
        if (critterCount != critterLinkedList.size()-2) {
            return false;
        }
        return true;
    }

    /**
     * Returns a read-only, deep-copy of this World Model
     * <br>Similar to {@link Object#clone()}, however, this returns the read-only version of this instance
     * and can't actually step world by itself
     */
    public ReadOnlyWorld getReadOnlyCopy(){
        World w = new World();
        w.tiles = tiles.clone();
        w.time = time;
        w.critterLinkedList = new LinkedList<>(critterLinkedList);
        w.enabledMana = enabledMana;
        w.name = name;
        w.enabledForceMutation = enabledForceMutation;
        return w.getReadOnlyWorld();
    }

    private class WorldVarAccess implements ReadOnlyWorld {

        @Override
        // Time Elapsed
        public int getSteps() {
            return time;
        }

        @Override
        public int getNumberOfAliveCritters() {
            return critterLinkedList.size()-2;
        }

        @Override
        public int getNumberOfRocks() {
            return tiles.getNumRocks();
        }

        @Override
        public int getNumberOfFood() {
            return tiles.getNumFood();
        }

        @Override
        public int getNumEmptyTiles() {
            return tiles.numEmpty;
        }

        @Override
        public Maybe<ReadOnlyCritter> getReadOnlyCritter(int c, int r) {
            Tile tile = tiles.getTile(c, r);
            if (tile instanceof Critter) return Maybe.some(((Critter) tile).getReadOnlyCritter());
            return Maybe.none();
        }

        @Override
        public int getTerrainInfo(int c, int r) {
            Tile tile = tiles.getTile(c, r);
            if (!(tiles.isValid(c, r))) {
                return -1;
            }
            return switch (tile) {
                case Rock ignored -> -1;
                case Food food -> -1 * (food.getEnergy() + 1);
                case Critter critter -> critter.getReadOnlyCritter().getOrientation() + 1;
                case null, default -> 0;
            };
        }

        @Override
        public int getWidth() {
            return tiles.width;
        }

        @Override
        public int getHeight() {
            return tiles.height;
        }
    }
}