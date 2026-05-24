package controller;

import Simulator.Food;
import Simulator.Tiles;
import Simulator.World;
import Simulator.*;
import exceptions.SyntaxError;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CritterTest {

    /**
     * Tests if loading valid Critter Files doesn't throw an Exception
     */
    @Test
    void testLoadFile() {
        World world = new World();
        String[] correctFiles = new String[]{
            "src/test/java/controller/testFiles/bud_critter.txt",
                "src/test/resources/A5files/space_critter.txt",
                "src/test/java/controller/testFiles/testParseCritter2.txt",
                "src/test/java/controller/testFiles/testParseCritter1.txt",
                "src/test/java/controller/testFiles/testParseCritter4.txt",
                "src/test/java/controller/testFiles/spiraling_critter.txt"
        };
        int critterCount = 0;
        for (String file : correctFiles) {
            System.out.println(file);
            int newCritters = (int) (9 * Math.random() + 1);
            critterCount += newCritters;
            assertTrue(world.loadCritters(file, newCritters));
            assertEquals(world.getReadOnlyWorld().getNumberOfAliveCritters(), critterCount);
        }

        String[] incorrectFiles = new String[]{
                "src/test/resources/gibberish",
                "src/test/java/controller/testFiles/testParseCritter3.txt"
        };
        for (String file : incorrectFiles) {
            System.out.println(file);
            int newCritters = (int) (9 * Math.random() + 1);
            assertFalse(world.loadCritters(file, newCritters));
            assertEquals(world.getReadOnlyWorld().getNumberOfAliveCritters(), critterCount);
        }
    }

    @Test
    void testLoadRandomFile() throws IOException {
        World world = new World();
        int critterCount = 0;
        for (int i = 0; i < 100; i++) {
            int newCritters = (int) (10 * Math.random());
            critterCount += newCritters;
            assertTrue(world.loadCritters(RandomTestFileGenerator.writeRandomCritterFile(), newCritters));
            assertEquals(world.getReadOnlyWorld().getNumberOfAliveCritters(), critterCount);
        }
    }

    @Test
    void CritterProgramfromfile() {
        // idc about tiles
        Critter testcrit = null;
        try {
            testcrit = new Critter("src/test/resources/A5files/space_critter.txt", new Tiles(), 0, 0, 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1000,testcrit.getAppearance());
    }

    /*
    Testing stepping the critter
     */

    @Test
    void testStepCritterWithUpdates() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter1.txt", w.tiles, 0, 0, 0);
        w.tiles.place(critter, 0,0);
        w.critterLinkedList.addFirst(critter);
        assertEquals(10, ((Critter) w.tiles.getTile(0, 0)).getMem(6));
        assertTrue(w.advanceTime(1));
        assertEquals(20, ((Critter)w.tiles.getTile(0,0)).getMem(6));
        assertEquals(10, critter.getMem(7));
    }


    @Test
    void testStepCritterWait() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter2.txt", w.tiles, 0, 0, 0);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,0);
        w.advanceTime(1);
        assertEquals(3010, ((Critter)w.tiles.getTile(0,0)).getMem(4));
    }
    
    @Test
    void testStepCritterLeft() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter3.txt", w.tiles, 0, 0, 0);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,0);
        w.advanceTime(1);
        assertEquals(2990, ((Critter)w.tiles.getTile(0,0)).getMem(4));
    }
    
    @Test
    void testStepCritterRight() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter4.txt", w.tiles, 0, 0, 0);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,0);
        w.advanceTime(1);
        assertEquals(2990, ((Critter)w.tiles.getTile(0,0)).getMem(4));
    }

    //test case when there is no food in front of critter
    @Test
    void testStepCritterEatFail() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter5.txt", w.tiles, 0, 0, 0);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,0);
        w.advanceTime(1);
        assertEquals(2990, ((Critter)w.tiles.getTile(0,0)).getMem(4));
    }

    //test case when there is food in front of critter but leaves no excess food
    @Test
    void testStepCritterEatSuccess() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter5.txt", w.tiles, 0, 0, 0);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,0);
        w.tiles.place(new Food(1010),0,2);
        assertTrue(w.tiles.getTile(0,2) instanceof Food);
        w.advanceTime(1);
        assertEquals(4000, ((Critter)w.tiles.getTile(0,0)).getMem(4));
        assertNull(w.tiles.getTile(0,2));
    }

    //test case critters eats food but leaves excess food
    @Test
    void testStepCritterEatSuccessExcess() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter5.txt", w.tiles, 0, 0, 0);
        Food food = new Food(2020);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(food, 0, 2);
        w.tiles.place(critter, 0,0);
        assertTrue(w.tiles.getTile(0,2) instanceof Food);
        w.advanceTime(1);
        assertTrue(w.tiles.getTile(0,2) instanceof Food);
        assertEquals(5000, ((Critter)w.tiles.getTile(0,0)).getMem(4));
        assertEquals(10, ((Food)w.tiles.getTile(0,2)).getEnergy());
    }

    @Test
    void testStepCritterForward() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter6.txt", w.tiles, 0, 0, 0);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,0);
        w.advanceTime(1);
        assertEquals(2970, ((Critter)w.tiles.getTile(0,2)).getMem(4));
        assertTrue(critter.classInv());
        assertNull(w.tiles.getTile(0,0));
        assertEquals(w.tiles.getTile(0,2),critter);
    }

    @Test
    void testStepCritterBackwardFail() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter7.txt", w.tiles, 0, 0, 0);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,0);
        w.advanceTime(1);
        assertEquals(2970, ((Critter)w.tiles.getTile(0,0)).getMem(4));
        assertEquals(w.tiles.getTile(0,0),critter);
    }

    @Test
    void testStepCritterBackwardSuccess() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter7.txt", w.tiles, 0, 0, 3);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,0);
        w.advanceTime(1);
        assertNull(w.tiles.getTile(0,0));
        assertEquals(w.tiles.getTile(0,2),critter);
        assertEquals(2970, ((Critter)w.tiles.getTile(0,2)).getMem(4));
    }

    //test case critter unalives itself
    @Test
    void testStepCritterServeDie() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter8.txt", w.tiles, 0, 0, 0);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,0);
        w.advanceTime(1);
        assertEquals(0, w.getReadOnlyWorld().getNumberOfAliveCritters());
        assertEquals(200 * 10, ((Food) w.tiles.getTile(0, 0)).getEnergy());
    }

    //test case critter doesn't die and serves food to a null hex in front of it
    @Test
    void testStepCritterServeNull() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter13.txt", w.tiles, 0, 0, 0);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,0);
        w.advanceTime(1);
        assertEquals(2980, ((Critter)w.tiles.getTile(0,0)).getMem(4));
        assertEquals(10, ((Food) w.tiles.getTile(0, 2)).getEnergy());
    }

    //test case critter doesn't die and serves food to food in front of it
    @Test
    void testStepCritterServeFood() throws IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter13.txt", w.tiles, 0, 0, 0);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,0);
        w.tiles.place(new Food(10),0,2);
        w.advanceTime(1);
        assertEquals(2980, ((Critter)w.tiles.getTile(0,0)).getMem(4));
        assertEquals(20, ((Food) w.tiles.getTile(0, 2)).getEnergy());
    }

    @Test
    void testStepCritterAttackFail() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter9.txt", w.tiles, 0, 0, 0);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,0);
        w.advanceTime(1);
        assertEquals(2950, ((Critter)w.tiles.getTile(0,0)).getMem(4));
    }

    @Test
    void testStepCritterAttackSuccess() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter9.txt", w.tiles, 0, 0, 0);
        Critter critter2 = new Critter("src/test/java/controller/testFiles/dummy.txt", w.tiles, 0,2, 0);
        w.critterLinkedList.addFirst(critter);
        w.critterLinkedList.addFirst(critter2);
        w.tiles.place(critter, 0,0);
        w.tiles.place(critter2, 0,2);
        assertTrue(w.advanceTime(1)); //equation = round(2^-91) = 0
        assertEquals(2950, ((Critter)w.tiles.getTile(0,0)).getMem(4));
        assertEquals(372, ((Critter)w.tiles.getTile(0,2)).getMem(4)); //action = wait so energy += size. 340 + 32 = 372
    }

    @Test
    void testStepCritterGrow() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter10.txt", w.tiles, 0, 0, 0);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,0);
        w.advanceTime(1);
        assertEquals(2480, ((Critter)w.tiles.getTile(0,0)).getMem(4));
        assertTrue(((Critter)w.tiles.getTile(0,0)).getMem(3) == 11);
    }

    @Test
    void testStepCritterBudFail() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter11.txt", w.tiles, 0, 0, 0);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,0);
        w.advanceTime(1);
        assertEquals(2532, ((Critter)w.tiles.getTile(0,0)).getMem(4));
    }

    @Test
    void testStepCritterBudSuccess() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter11.txt", w.tiles, 0, 2, 0);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,2);
        w.advanceTime(1);
        assertEquals(2532, ((Critter)w.tiles.getTile(0,2)).getMem(4));
        assertEquals(250, ((Critter)w.tiles.getTile(0, 0)).getMem(4));
        assertEquals(1, ((Critter)w.tiles.getTile(0, 0)).getMem(3));
        assertEquals(0, ((Critter)w.tiles.getTile(0, 0)).getMem(6));
        assertEquals(0,((Critter)w.tiles.getTile(0, 0)).getMem(7));
        assertEquals(0,((Critter)w.tiles.getTile(0, 0)).getMem(8));
        assertEquals(0,((Critter)w.tiles.getTile(0, 0)).getMem(9));
    }

    @Test
    void testStepCritterMateFail() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter = new Critter("src/test/java/controller/testFiles/og_critter12.txt", w.tiles, 0, 0, 0);
        w.critterLinkedList.addFirst(critter);
        w.tiles.place(critter, 0,0);
        w.advanceTime(1);
        assertEquals(2990, ((Critter)w.tiles.getTile(0,0)).getMem(4));
    }

    @Test
    void testStepCritterMateSuccess1() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter1 = new Critter("src/test/java/controller/testFiles/og_critter12.txt", w.tiles, 0, 0, 0);
        Critter critter2 = new Critter("src/test/java/controller/testFiles/og_critter12.txt", w.tiles, 0, 2, 3);
        w.critterLinkedList.addFirst(critter1);
        w.critterLinkedList.addFirst(critter2);
        assertTrue(w.tiles.place(critter1, 0,0));
        assertTrue(w.tiles.place(critter2, 0,2));
        assertTrue(w.advanceTime(1));
        assertEquals(250,((Critter)w.tiles.getTile(0,4)).getMem(4));
        assertEquals(1, (((Critter) w.tiles.getTile(0, 4)).getMem(3)));
        assertEquals(2740,((Critter)w.tiles.getTile(0,2)).getMem(4));
        assertEquals(2740, ((Critter)w.tiles.getTile(0,0)).getMem(4));

        assertTrue(w.classInv());
    }

    @Test
    void testStepCritterMateSuccess2() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter1 = new Critter("src/test/java/controller/testFiles/og_critter12.txt", w.tiles, 2, 4, 0);
        Critter critter2 = new Critter("src/test/java/controller/testFiles/og_critter12.txt", w.tiles, 2, 6, 3);
        w.critterLinkedList.addFirst(critter1);
        w.critterLinkedList.addFirst(critter2);
        assertTrue(w.tiles.place(critter1, 2,4));
        assertTrue(w.tiles.place(critter2, 2,6));
        assertTrue(w.advanceTime(1));
        Critter newBorn = w.tiles.getTile(2,8) instanceof Critter ? (Critter)w.tiles.getTile(2,8) : (Critter)w.tiles.getTile(2,2);

        assertEquals(250,newBorn.getMem(4));
        assertEquals(1, newBorn.getMem(3));
        assertEquals(2740,((Critter)w.tiles.getTile(2,4)).getMem(4));
        assertEquals(2740, ((Critter)w.tiles.getTile(2,6)).getMem(4));

        assertTrue(w.classInv());
    }

    @Test
    void testStepCritterMateFail1() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter1 = new Critter("src/test/java/controller/testFiles/og_critter12.txt", w.tiles, 0, 0, 0);
        Critter critter2 = new Critter("src/test/java/controller/testFiles/og_critter12.txt", w.tiles, 0, 2, 3);
        w.critterLinkedList.addFirst(critter1);
        w.critterLinkedList.addFirst(critter2);
        assertTrue(w.tiles.place(critter1, 0,0));
        assertTrue(w.tiles.place(critter2, 0,2));
        assertTrue(w.tiles.place(new Rock(),0,4));
        assertTrue(w.advanceTime(1));
        assertEquals(2740,((Critter)w.tiles.getTile(0,2)).getMem(4));
        assertEquals(2740, ((Critter)w.tiles.getTile(0,0)).getMem(4));

        assertTrue(w.classInv());
    }

    @Test
    void testStepCritterMateFail2() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter1 = new Critter("src/test/java/controller/testFiles/og_critter12.txt", w.tiles, 4,0, 1);
        Critter critter2 = new Critter("src/test/java/controller/testFiles/og_critter12.txt", w.tiles, 5, 1, 4);
        w.critterLinkedList.addFirst(critter1);
        w.critterLinkedList.addFirst(critter2);
        assertTrue(w.tiles.place(critter1, 4,0));
        assertTrue(w.tiles.place(critter2, 5,1));
        assertTrue(w.advanceTime(1));
        assertEquals(2740,((Critter)w.tiles.getTile(4,0)).getMem(4));
        assertEquals(2740, ((Critter)w.tiles.getTile(5,1)).getMem(4));

        assertTrue(w.classInv());
    }


    @Test
    void testStepCritterMateFail3() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter1 = new Critter("src/test/java/controller/testFiles/og_critter12.txt", w.tiles, 2, 4, 0);
        Critter critter2 = new Critter("src/test/java/controller/testFiles/og_critter12.txt", w.tiles, 2, 6, 3);
        w.critterLinkedList.addFirst(critter1);
        w.critterLinkedList.addFirst(critter2);
        assertTrue(w.tiles.place(critter1, 2,4));
        assertTrue(w.tiles.place(critter2, 2,6));
        assertTrue(w.tiles.place(new Rock(),2,2));
        assertTrue(w.tiles.place(new Rock(),2,8));
        assertTrue(w.advanceTime(1));

        assertTrue(w.tiles.getTile(2,8) instanceof Rock);
        assertTrue(w.tiles.getTile(2,2) instanceof Rock);
        assertEquals(2740,((Critter)w.tiles.getTile(2,4)).getMem(4));
        assertEquals(2740, ((Critter)w.tiles.getTile(2,6)).getMem(4));

        assertTrue(w.classInv());
    }

    //test case where two critters are facing the same direction but one doesn't want to mate.wld in same time step
    @Test
    void testStepCritterMateFailSameDirDiffActions() throws SyntaxError, IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);
        Critter critter1 = new Critter("src/test/java/controller/testFiles/og_critter12.txt", w.tiles, 0, 0, 0);
        Critter critter2 = new Critter("src/test/java/controller/testFiles/og_critter2.txt", w.tiles, 0, 2, 3);
        w.critterLinkedList.addFirst(critter1);
        w.critterLinkedList.addFirst(critter2);
        assertTrue(w.tiles.place(critter1, 0,0));
        assertTrue(w.tiles.place(critter2, 0,2));
        assertTrue(w.advanceTime(1));
        assertEquals(2990,((Critter)w.tiles.getTile(0,0)).getMem(4));
        assertEquals(3010, ((Critter)w.tiles.getTile(0,2)).getMem(4));

        assertTrue(w.classInv());
    }

    @Test
    void testWrittenProblem3() throws IOException {
        World w = new World("src/test/resources/A5files/empty.wld", false, false);

        Critter critter1 = new Critter("src/test/java/controller/testFiles/sitting_critter.txt", w.tiles, 2,4, 0);

        w.critterLinkedList.addFirst(critter1);
        assertTrue(w.tiles.place(critter1, 2,4));
        assertTrue(w.tiles.place(new Food(),2,2));

        w.printWorld(System.out);
        assertTrue(w.advanceTime(1));
        assertTrue(w.classInv());

        w.printWorld(System.out);
        assertTrue(w.advanceTime(1));
        assertTrue(w.classInv());

        w.printWorld(System.out);
        assertTrue(w.advanceTime(1));
        assertTrue(w.classInv());

        w.printWorld(System.out);
        assertTrue(w.advanceTime(1));
        assertTrue(w.classInv());

        w.printWorld(System.out);
        assertTrue(w.advanceTime(1));
        assertTrue(w.classInv());

        w.printWorld(System.out);
        assertTrue(w.advanceTime(1));
        assertTrue(w.classInv());

        w.printWorld(System.out);
        assertTrue(w.advanceTime(1));
        assertTrue(w.classInv());
    }
}










