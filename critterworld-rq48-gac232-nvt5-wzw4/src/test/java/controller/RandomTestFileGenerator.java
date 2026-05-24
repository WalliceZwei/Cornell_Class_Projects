package controller;

import Simulator.Critter;
import Simulator.Rock;
import Simulator.Tiles;
import Simulator.World;
import exceptions.SyntaxError;
import org.junit.jupiter.api.RepeatedTest;
import parser.RandomASTGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A Class that generates Random Critter and World Files, used for randomized test cases
 * Contains 2 Test that tests the validity of the Critter and World Files written by this class
 */
public class RandomTestFileGenerator {

    /** Returns a FilePath String pointing to a randomly generated Critter file */
    public static String writeRandomCritterFile() throws IOException {
        File f = new File("src/test/resources/A5files/random_critter.txt");
        if(f.exists()) f.delete();
        f.createNewFile();
        Writer w = new FileWriter(f);
        writeRandomCritterToWriter(w);
        w.write(RandomASTGenerator.generateRandomAST(10,10).toString());
        w.close();
        return "src/test/resources/A5files/random_critter.txt";
    }

    public static String writeRandomWorldFile() throws IOException {
        for(int i=0;i<10;i++){
            File f = new File("src/test/resources/A5files/randomWorldCritterFiles/"+i+".txt");
            if(f.exists()) f.delete();
            f.createNewFile();
            Writer w = new FileWriter(f);
            writeRandomCritterToWriter(w);
            w.write(RandomASTGenerator.generateRandomAST(30,10).toString());
            w.close();
        }

        File f = new File("src/test/resources/A5files/random_world.txt");
        if(f.exists()) f.delete();
        f.createNewFile();
        Writer w = null;
        //retry mechanism for if multiple process try to access same file at once
        //threw an error whilst testing for some reason, added for safety maybe
        while(w == null) try{ w= new FileWriter(f);} catch(IOException ignored) {};
        w.write("name random_world\n");
        w.write("size 1000 1000\n");
        int numCritters = (int)(Math.random()*10);
        Tiles tiles = new Tiles(1000,1000,false);
        for(int i=0;i<numCritters;i++) {
            int[] coords = tiles.getRandomEmptyTile();
            tiles.place(new Rock(),coords[0],coords[1]);
            w.write("critter randomWorldCritterFiles/"+i+".txt "+coords[0]+" "+coords[1]+" "+((int)(Math.random()*6)+"\n"));
            if(Math.random()<0.5){
                coords = tiles.getRandomEmptyTile();
                tiles.place(new Rock(),coords[0],coords[1]);
                w.write("rock "+coords[0]+" "+coords[1]+"\n");
            }if(Math.random()<0.5){
                coords = tiles.getRandomEmptyTile();
                tiles.place(new Rock(),coords[0],coords[1]);
                w.write("food "+coords[0]+" "+coords[1]+" "+(int)(Math.random()*199+1)+"\n");
            }
        }
        w.close();

        return "src/test/resources/A5files/random_world.txt";
    }

    private static void writeRandomCritterToWriter(Writer w) throws IOException {
        w.write("species: random critter\n");
        w.write("memsize: "+((int)(Math.random()*100)+7)+"\n");
        w.write("defense: "+((int)(Math.random()*100)+1)+"\n");
        w.write("offense: "+((int)(Math.random()*100)+1)+"\n");
        w.write("size: "+((int)(Math.random()*100)+1)+"\n");
        w.write("energy: "+((int)(Math.random()*300)+200)+"\n");
        w.write("posture: "+((int)(Math.random()*100))+"\n");
    }

    @RepeatedTest(1000)
    void writeRandomCritterFileTest() throws IOException, SyntaxError {
        Tiles tiles = new Tiles(1,1,false);
        Critter critter = new Critter(writeRandomCritterFile(),tiles,0,0,0);
        tiles.place(critter,0,0);
        assertTrue(critter.classInv());
    }

    @RepeatedTest(1000)
    void writeRandomWorldFileTest() throws IOException {
        World w = new World(writeRandomWorldFile(),true,true);
        assertTrue(w.advanceTime(1));
        assertTrue(w.classInv());
    }
}
