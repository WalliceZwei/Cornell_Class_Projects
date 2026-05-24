package controller;

import Simulator.World;
import model.Constants;
import model.ReadOnlyCritter;
import model.ReadOnlyWorld;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatorTest {

    @Test
    void testLoadWorld() {
        World w = new World();
        String[] correctFiles = new String[]{
                "src/test/resources/A5files/space_world.txt",
                "src/test/java/controller/testFiles/empty_world.txt",
                "src/test/java/controller/testFiles/walworld.txt.txt",
                "src/test/resources/A5files/mediumworld.txt",
                "src/test/resources/A5files/random_world.txt",
                "src/test/resources/A5files/small_world.txt",
        };

        String[] incorrectFiles = new String[]{
                "src/test/java/controller/testFiles/testParseWorld1.txt",
                "src/test/java/controller/testFiles/testParseWorld2.txt",
                "src/test/java/controller/testFiles/testParseWorld3.txt",
                "src/test/java/controller/testFiles/testParseWorld4.txt",
                "src/test/resources/gibberish"
        };

        for(String file : correctFiles) {
            System.out.println(file);
            w = new World(file,true,true);
            assertTrue(w.advanceTime(1));
            assertTrue(w.classInv());
        }

        for(String file : incorrectFiles) {
            System.out.println(file);
            w = new World(file,true,true);
            assertTrue(w.advanceTime(1)); //even if file is technically invalid, execution should still proceed
            assertTrue(w.classInv());
        }
    }

//    @RepeatedTest(1000)
//    void testLoadRandomWorld() throws IOException {
//        World h = new World(RandomTestFileGenerator.writeRandomWorldFile(), false, false);
////        h.printWorld(System.out);
//        assertTrue(h.advanceTime(1));
//        assertTrue(h.classInv());
//    }


    @Test
    void testStepOneCritterOnce(){ //space critter always waits, 1 test is enough
        World w = new World();
        assertTrue(w.loadCritters("src/test/resources/A5files/space_critter.txt",1));
        assertTrue(w.advanceTime(1));
        assertTrue(w.classInv());
    }

    @Test
    void testStepOneCritter() {
        World w = new World(); //space critter always waits, 1 test is enough
        assertTrue(w.loadCritters("src/test/resources/A5files/space_critter.txt",1));
        assertTrue(w.advanceTime(1000));
        assertTrue(w.classInv());
    }

    @RepeatedTest(1000) //ran for 100,000 iterations, working so far o_o
    void testStepOneRandomCritterOnce() throws IOException {
        World w = new World();
        assertTrue(w.loadCritters(RandomTestFileGenerator.writeRandomCritterFile(),1));
        assertTrue(w.advanceTime(1));
        assertTrue(w.classInv());
    }

    @RepeatedTest(1000) //works so far, test more
    void testStepOneRandomCritter() throws IOException {
        World w = new World();
        assertTrue(w.loadCritters(RandomTestFileGenerator.writeRandomCritterFile(),1));
        assertTrue(w.advanceTime(100));
        assertTrue(w.classInv());
    }

    @RepeatedTest(100)//works so far
    void testStepManyRandomCritterOnce() throws IOException {
        World w = new World();
        int randomCritters = (int)(Math.random() * 10);
        for(int i=0;i<randomCritters;i++) assertTrue(w.loadCritters(RandomTestFileGenerator.writeRandomCritterFile(),(int)(Math.random() * 10)));
        assertTrue(w.advanceTime(1));
        assertTrue(w.classInv());
    }

    @RepeatedTest(100) //works so far (ran 1000 times)
    void testStepManyRandomCritter() throws IOException {
        World w = new World();
        int randomCritters = (int)(Math.random() * 10);
        for(int i=0;i<randomCritters;i++) assertTrue(w.loadCritters(RandomTestFileGenerator.writeRandomCritterFile(),(int)(Math.random() * 3)));
        assertTrue(w.advanceTime(100));
        assertTrue(w.classInv());
    }

    @RepeatedTest(10) //works!!!
    void testStepRandomWorld() throws IOException {
        World w = new World(RandomTestFileGenerator.writeRandomWorldFile(), Math.random()>0.5, Math.random()>0.5);
        assertTrue(w.advanceTime(1000));
        assertTrue(w.classInv());
    }

    @RepeatedTest(1)
    void CritterProgramfromfilewithcrits(){
        World h = new World("src/test/resources/A5files/mediumworld.txt",false,false);
        assertTrue(h.loadCritters("src/test/resources/A5files/space_critter.txt", 10));
        h.printWorld(System.out);
        assertTrue(h.advanceTime(1000));
    }

    @Test
    void getTerrainInfoTest(){
        World h = new World("src/test/resources/A5files/mediumworld.txt",false,false);
        ReadOnlyWorld b = h.getReadOnlyWorld();
        // Check this out
        assertEquals(b.getTerrainInfo(1,1),0);
        assertEquals(b.getTerrainInfo(1,2),-1);
        assertEquals(b.getTerrainInfo(0,6),-13);
        assertEquals(b.getTerrainInfo(0,2),-1);
        assertEquals(b.getTerrainInfo(0,8),3);
    }

    @Test
    void getReadOnlyCritterTest(){
        World h = new World("src/test/resources/A5files/mediumworld.txt",false,false);
        ReadOnlyWorld b = h.getReadOnlyWorld();
        try {
            ReadOnlyCritter ty = ((b.getReadOnlyCritter(0, 8)).get());
            assertEquals(ty.getOrientation(),2);
            assertEquals(ty.getSpecies(), "space critter");
            assertEquals(ty.getProgramString(), "1 = 1 --> wait;\n");
            assertEquals(ty.getMemory()[0], 7);
            assertEquals(ty.getMemory()[1], 1);
            assertEquals(ty.getMemory()[2], 1);
            assertEquals(ty.getMemory()[3], 1);
            assertEquals(ty.getMemory()[4], 500);
            assertEquals(ty.getMemory()[6], 0);
        }
        catch(Exception e){
            System.err.println("bruh");
        }

        try {
            ReadOnlyCritter ty2 = b.getReadOnlyCritter(0,2).get();
        }
        catch(Exception e){
            assertTrue(true);
        }


    }

    // This test case tests the boundaries and normal actions in bud, should only be 12 total, starts with 10 critters, but only 6 successfully bud, the other 3 run into rocks, food, or boundary, or other newborn critters
    @Test
    void budSimulator(){
        World b = new World("src/test/resources/A5files/walworld.txt",false,false);
        b.printWorld(System.out);
        assertTrue(b.advanceTime(1));
        assertEquals(b.getReadOnlyWorld().getNumberOfAliveCritters(),12);
        b.printWorld(System.out);
        assertTrue(b.classInv());
    }

    @Test
    void attackCritterFalse(){
        //size of 1
        World b = new World("src/test/resources/A5files/attackworldfalse.txt",false,false);
        b.printWorld(System.out);
        int healtha = 0; int healthb = 0; int health1 = 0; int health2=0;
        try{healtha = b.getReadOnlyWorld().getReadOnlyCritter(2, 2).get().getMemory()[4];}
        catch(Exception e){}

        try{healthb = b.getReadOnlyWorld().getReadOnlyCritter(2, 4).get().getMemory()[4];}
        catch(Exception e){}

        assertTrue(b.advanceTime(1));
        try {health1 = b.getReadOnlyWorld().getReadOnlyCritter(2, 2).get().getMemory()[4];}
        catch (Exception e){}

        try {health2 = b.getReadOnlyWorld().getReadOnlyCritter(2, 4).get().getMemory()[4];}
        catch (Exception e){}

        assertNotEquals(healtha,0);
        assertNotEquals(healthb,0);
        assertNotEquals(health1,0);
        assertNotEquals(health2,0);
        assertEquals(healtha-2* Constants.ATTACK_COST,health1);
        assertEquals(healthb-2*Constants.ATTACK_COST,health2);

        b.printWorld(System.out);
        assertTrue(b.classInv());
    }

    @Test
    void attackCritterOneAttack(){
        World b = new World("src/test/resources/A5files/attackworldonecrit.txt",false,false);
        b.printWorld(System.out);
        int healtha = 0; int healthb = 0; int health1 = 0; int health2=0;
        try{healtha = b.getReadOnlyWorld().getReadOnlyCritter(2, 2).get().getMemory()[4];}
        catch(Exception e){}

        try{healthb = b.getReadOnlyWorld().getReadOnlyCritter(2, 0).get().getMemory()[4];}
        catch(Exception e){}

        System.out.println(healtha);
        System.out.println(healthb);
        assertTrue(b.advanceTime(1));
        try {health1 = b.getReadOnlyWorld().getReadOnlyCritter(2, 2).get().getMemory()[4];}
        catch (Exception e){}

        try {health2 = b.getReadOnlyWorld().getReadOnlyCritter(2, 0).get().getMemory()[4];}
        catch (Exception e){}

        assertNotEquals(healtha,0);
        assertNotEquals(healthb,0);
        assertNotEquals(health1,0);
        assertNotEquals(health2,0);
        assertEquals(healtha-Constants.ATTACK_COST,health1);
        assertTrue(healthb-Constants.ATTACK_COST>health2);

        b.printWorld(System.out);
        assertTrue(b.classInv());
    }




    @Test
    void mateSimulator(){
        World b = new World("src/test/resources/A5files/mateworld.txt",false,false);
        assertTrue(b.advanceTime(1));
        assertEquals(b.getReadOnlyWorld().getNumberOfAliveCritters(),3);
        assertTrue(b.advanceTime(1));
        assertEquals(b.getReadOnlyWorld().getNumberOfAliveCritters(),3);
        b.printWorld(System.out);
    }

    @Test
    void mateSimulatorfail(){
        World b = new World("src/test/resources/A5files/barrierworld.txt",false,false);
        assertTrue(b.advanceTime(5));
        assertEquals(b.getReadOnlyWorld().getNumberOfAliveCritters(),2);
        assertTrue(b.classInv());
        b.printWorld(System.out);
    }

    @Test
    void EatTest(){
        World b = new World("src/test/resources/A5files/eatworld.txt",false,false);
        ReadOnlyWorld rworld = b.getReadOnlyWorld();
        assertTrue(b.advanceTime(1));
        b.printWorld(System.out);
        assertEquals(0,rworld.getTerrainInfo(1,5));
        int z = 0; int y = 0; int x = 0;
        try{z = rworld.getReadOnlyCritter(1, 3).get().getMemory()[4];}
        catch (Exception e){System.err.println("error");}

        try{y = rworld.getReadOnlyCritter(0, 4).get().getMemory()[4];}
        catch (Exception e){System.err.println("error");}

        try{x = rworld.getReadOnlyCritter(3, 5).get().getMemory()[4];}
        catch (Exception e){System.err.println("error");}

        assertEquals(500,z);
        assertEquals(499,y);
        assertTrue(rworld.getTerrainInfo(4,6)<-1);
        assertEquals(500,x);
        assertTrue(b.classInv());
    }

}
