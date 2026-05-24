package controller;

import Simulator.*;
import Simulator.Tiles;
import exceptions.SyntaxError;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TilesTest {

    @Test
    void testGetCoordinates() {
        Tiles tiles = new Tiles(6, 10, true);

        // even rows and columns
        int[] coords1 = tiles.getCoordinates(true, 0, 0);
        assertEquals(0, coords1[0]);
        assertEquals(0, coords1[1]);

        int[] coords2 = tiles.getCoordinates(true, 1, 1);
        assertEquals(2, coords2[0]);
        assertEquals(2, coords2[1]);

        // odd rows and columns
        int[] coords3 = tiles.getCoordinates(false, 1, 0);
        assertEquals(3, coords3[0]);
        assertEquals(1, coords3[1]);

        int[] coords4 = tiles.getCoordinates(false, 2, 1);
        assertEquals(5, coords4[0]);
        assertEquals(3, coords4[1]);

        // edge cases
        Tiles tilesEdge = new Tiles(1, 1, true);
        int[] coordsEdge = tilesEdge.getCoordinates(true, 0, 0);
        assertEquals(0, coordsEdge[0]);
        assertEquals(0, coordsEdge[1]);

        Tiles tilesOdd = new Tiles(5, 5, false);
        int[] coordsOdd = tilesOdd.getCoordinates(true, 2, 2);
        assertEquals(4, coordsOdd[0]);
        assertEquals(4, coordsOdd[1]);
    }

    @Test
    void testIsValid_OutOfBounds() {
        Tiles tiles = new Tiles(6, 10,true);

        // Negative coordinates
        assertFalse(tiles.isValid(-1, 0));
        assertFalse(tiles.isValid(0, -1));
        assertFalse(tiles.isValid(-1, -1));

        // Beyond width and height
        assertFalse(tiles.isValid(6, 0));
        assertFalse(tiles.isValid(0, 10));
    }

    @Test
    void testIsValid_Even() {
        Tiles tiles = new Tiles(6, 10,true);

        // Valid even-column, even-row
        assertTrue(tiles.isValid(0, 0));
        assertTrue(tiles.isValid(2, 0));
        assertTrue(tiles.isValid(4, 0));

        // Valid odd-column, odd-row
        assertTrue(tiles.isValid(1, 1));
        assertTrue(tiles.isValid(3, 1));
        assertTrue(tiles.isValid(5, 1));

        // Invalid even, odd
        assertFalse(tiles.isValid(1, 0));
        assertFalse(tiles.isValid(2, 1));
    }

    @Test
    void testIsValid_OddHeight() {
        Tiles tiles = new Tiles(6, 11,true);

        // Valid extra even row for odd height
        assertTrue(tiles.isValid(0, 10));
        assertTrue(tiles.isValid(2, 10));

        // Invalid extra odd row for odd height
        assertFalse(tiles.isValid(1, 10));
        assertFalse(tiles.isValid(3, 10));
    }

    @Test
    void testIsValid_OddWidth() {
        Tiles tiles = new Tiles(7, 10,true);

        // Valid even-column, even-row coordinates
        assertTrue(tiles.isValid(0, 0));
        assertTrue(tiles.isValid(2, 0));
        assertTrue(tiles.isValid(4, 0));
        assertTrue(tiles.isValid(6, 0)); // Extra column for odd width

        // Valid odd-column, odd-row coordinates
        assertTrue(tiles.isValid(1, 1));
        assertTrue(tiles.isValid(3, 1));
        assertTrue(tiles.isValid(5, 1));

        // Invalid even-column, odd-row coordinates
        assertFalse(tiles.isValid(1, 0));
        assertFalse(tiles.isValid(2, 1));
        assertFalse(tiles.isValid(6, 1)); // Extra column for odd width, should be invalid if row is odd
    }

    @Test
    void testGetTile_RockTile() {
        Tiles tiles = new Tiles(6, 10,false);
        assertTrue(tiles.place(new Rock(), 0, 0));

        // Valid coordinates with Rock tile
        assertTrue(tiles.getTile(0, 0) instanceof Rock);
    }

    @Test
    void testGetTile_FoodTile() {
        Tiles tiles = new Tiles(6, 10,false);
        assertTrue(tiles.place(new Food(), 1, 3));

        // Valid coordinates with Food tile
        assertTrue(tiles.getTile(1, 3) instanceof Food);
    }

    @Test
    void testGetTile_EmptyTile() {
        Tiles tiles = new Tiles(6, 10,false);

        // Valid coordinates with no tile
        assertNull(tiles.getTile(0, 0));
    }

    @Test
    void testGetTile_OutOfBounds() {
        Tiles tiles = new Tiles(6, 10, true);

        // Coordinates out of bounds
        assertNull(tiles.getTile(-1, 0));
        assertNull(tiles.getTile(6, 10));
    }

    /**
     * Tests populated Tiles of specified height and width of 1~100 inclusive for methods {@link Tiles#ahead}
     */
    @Test
    void testGetAhead() throws SyntaxError, IOException {
        Tiles tiles = new Tiles(6, 10,false);

        //rock and null placed
        tiles.place(new Rock(), 0, 2);
        tiles.place(new Food(), 0, 6);

        Critter c = new Critter("src/test/resources/A5files/space_critter.txt", tiles,0, 8, 0);
        //place critter
        tiles.place(c, 0, 8);
        assertTrue(c.classInv());

        //assert placed into right places
        assertTrue(tiles.getTile(0, 2) instanceof Rock);
        assertTrue(tiles.getTile(0,8) instanceof Critter);
        assertNull(tiles.getTile(0,4));
        assertTrue(tiles.getTile(0,6) instanceof Food);

        //check ahead for rock tiles
        assertEquals(-1, tiles.ahead(0,0, 0, 1));
        //check ahead for null tiles
        assertEquals(0, tiles.ahead(0, 0, 0, 2));
        //check ahead for critter tiles
        assertTrue(tiles.ahead(0, 0, 0, 4) > 0);
        //check ahead for food tiles
        assertTrue(tiles.ahead(0, 0, 0, 3) < -1);
    }

    /**
     * populate tiles with Food in 6 cardinal directions to test {@link Tiles#smell} method
     */
    @Test
    void testGetSmellCardinalDir() {
        Tiles tiles = new Tiles(6, 10,false);

        //rock and null placed
        tiles.place(new Food(), 0, 2);
        tiles.place(new Food(), 0, 6);
        tiles.place(new Food(), 2, 0);
        tiles.place(new Food(), 2, 8);
        tiles.place(new Food(), 4, 2);
        tiles.place(new Food(), 4, 6);

        //assert placed into right places
        assertTrue(tiles.getTile(2,8) instanceof Food);
        assertTrue(tiles.getTile(4,6) instanceof Food);
        assertTrue(tiles.getTile(4,2) instanceof Food);
        assertTrue(tiles.getTile(2,0) instanceof Food);
        assertTrue(tiles.getTile(0, 2) instanceof Food);
        assertTrue(tiles.getTile(0,6) instanceof Food);
        assertNull(tiles.getTile(2,4));

        assertEquals(2000,tiles.smell(2,4,0));
        assertEquals(2000,tiles.smell(2,4,1));
        assertEquals(2000,tiles.smell(2,4,2));
        assertEquals(2000,tiles.smell(2,4,3));
        assertEquals(2000,tiles.smell(2,4,4));
        assertEquals(2000,tiles.smell(2,4,5));

        assertTrue(tiles.remove(2,8) instanceof Food);
        assertEquals(2001,tiles.smell(2,4,0));
        assertTrue(tiles.remove(4,6) instanceof Food);
        assertEquals(2002,tiles.smell(2,4,0));
        assertEquals(2001,tiles.smell(2,4,1));
        assertTrue(tiles.remove(4,2) instanceof Food);
        assertEquals(2001,tiles.smell(2,4,2));
        assertEquals(2002,tiles.smell(2,4,1));
        assertTrue(tiles.remove(2,0) instanceof Food);
        assertEquals(2001,tiles.smell(2,4,3));
        assertEquals(2003,tiles.smell(2,4,1));
        assertTrue(tiles.remove(0, 2) instanceof Food);
        assertEquals(2001,tiles.smell(2,4,4));
        assertEquals(2003,tiles.smell(2,4,2));
        assertTrue(tiles.remove(0,6) instanceof Food);
        assertEquals(1000000,tiles.smell(2,4,5));
    }

    /**
     * populate tiles with Food not in the 6 cardinal directions to test {@link Tiles#smell} method
     */
    @Test
    void testGetSmellRandomDir(){
        Tiles tiles = new Tiles(6, 10,false);

        tiles.place(new Food(), 3,7);

        assertTrue(tiles.getTile(3,7) instanceof Food);

        assertTrue(tiles.smell(2,4,0) == 2000 || tiles.smell(2,4,0) == 2001);
        assertTrue(tiles.smell(2,4,5) == 2001 || tiles.smell(2,4,5) == 2002);
        assertTrue(tiles.smell(2,4,4) == 2002 || tiles.smell(2,4,4) == 2003);
    }

    /**
     * Tests populated Tiles of specified height and width of 1~100 inclusive for methods {@link Tiles#nearby}
     */
    @Test
    void testGetNearby() {
        Tiles tiles = new Tiles(6,10,false);

        tiles.place(new Food(),2,6);
        tiles.place(new Rock(),3,5);
        tiles.place(new Critter(),3,3);
        tiles.place(new Food(),1,3);
        tiles.place(new Rock(),1,5);

        assertTrue(tiles.getTile(2,6) instanceof Food);
        assertTrue(tiles.getTile(3,5) instanceof Rock);
        assertTrue(tiles.getTile(3,3) instanceof Critter);
        assertTrue(tiles.getTile(1,3) instanceof Food);
        assertTrue(tiles.getTile(1,5) instanceof Rock);

        assertTrue(tiles.nearby(2,4,0)<-1);
        assertTrue(tiles.nearby(2,4,1)==-1);
        assertTrue(tiles.nearby(2,4,2)>0);
        assertTrue(tiles.nearby(2,4,3)==0);
        assertTrue(tiles.nearby(2,4,4)<-1);
        assertTrue(tiles.nearby(2,4,5)==-1);

        assertTrue(tiles.nearby(2,0,3)==0);
        assertTrue(tiles.nearby(2,0,2)==0);
        assertTrue(tiles.nearby(2,0,4)==0);
        assertTrue(tiles.nearby(5,5,1)==0);
        assertTrue(tiles.nearby(5,5,2)==0);
        assertTrue(tiles.nearby(3,9,0)==0);
        assertTrue(tiles.nearby(3,9,1)==0);
        assertTrue(tiles.nearby(3,9,5)==0);
        assertTrue(tiles.nearby(0,4,4)==0);
        assertTrue(tiles.nearby(0,4,5)==0);
    }

    /**
     * Tests random Tiles of random height and width of 1~100 inclusive for methods {@link Tiles#getTile},{@link Tiles#isValid}, & {@link Tiles#place}
     */
    @RepeatedTest(1000)
    void randomAssertionTests() {
        Tiles tiles = new Tiles((int)(Math.random() * 100)+1, (int)(Math.random() * 100)+1,true);
        int width = tiles.getWidth(), height = tiles.getHeight();

        //tiles within bound must be valid if and only if tiles coord are both even or both odd
        int randomCol = (int)(Math.random() * width), randomRow = (int)(Math.random() * height);
        boolean validCoords = randomRow%2==randomCol%2;
        if (tiles.isValid(randomCol,randomRow) != validCoords) {
            System.out.println("Width: " + width + ", Height: " + height + ", Col: " + randomCol + ", Row: " + randomRow);
        }
        assertEquals(tiles.isValid(randomCol,randomRow),validCoords);


        if (validCoords) { // tiles within bound must return either rock, food or null
            assertTrue(tiles.getTile(randomCol,randomRow) instanceof Rock || tiles.getTile(randomCol,randomRow) instanceof Food || tiles.getTile(randomCol,randomRow) == null);
        } else { // tiles out of bounds must always be null.
            assertNull(tiles.getTile(randomCol, randomRow));
        }

        //negative tiles should always not be valid
        assertFalse(tiles.isValid(-randomCol-1,-randomRow-1));

        if(validCoords && tiles.getTile(randomCol,randomRow) == null) {
            //valid tiles must be able to place rock
            assertTrue(tiles.place(new Rock(), randomCol,randomRow));

            //tile must be rock
            assertTrue(tiles.getTile(randomCol,randomRow) instanceof Rock);
        }
    }
}
