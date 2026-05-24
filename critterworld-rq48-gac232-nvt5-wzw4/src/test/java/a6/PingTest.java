package a6;

import Simulator.Food;
import Simulator.Rock;
import Simulator.Tiles;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PingTest {

    /** tests {@link Tiles#ping} according to Figure 5 in Project Specification */
    @Test
    public void testGetPingFigure5() {
        Tiles tiles = new Tiles(5, 9,false);

        tiles.place(new Food(),1,3);
        assertEquals(11,tiles.ping(2,2,0));
        tiles.remove(1,3);

        tiles.place(new Food(),2,4);
        assertEquals(0,tiles.ping(2,2,0));
        tiles.remove(2,4);

        tiles.place(new Food(),3,3);
        assertEquals(13,tiles.ping(2,2,0));
        tiles.remove(3,3);

        tiles.place(new Food(),1,5);
        tiles.place(new Rock(),1,3);
        assertEquals(40,tiles.ping(2,2,0));
        tiles.remove(1,5);
        tiles.remove(1,3);

        tiles.place(new Food(),2,6);
        assertEquals(30,tiles.ping(2,2,0));
        tiles.remove(2,6);

        tiles.place(new Food(),3,5);
        assertEquals(40,tiles.ping(2,2,0));
        tiles.remove(3,5);

    }

    /** tests {@link Tiles#ping} according to Figure 6 in Project Specification */
    @Test
    public void testGetPingFigure6() {
        Tiles tiles = new Tiles(6, 14,false);

        tiles.place(new Rock(),2,2);
        tiles.place(new Rock(),1,3);
        tiles.place(new Rock(),1,5);
        tiles.place(new Rock(),2,6);
        tiles.place(new Rock(),1,11);
        tiles.place(new Rock(),3,7);
        tiles.place(new Rock(),4,2);
        tiles.place(new Rock(),5,3);
        tiles.place(new Rock(),5,5);
        tiles.place(new Rock(),5,7);
        tiles.place(new Rock(),3,11);
        tiles.place(new Rock(),4,10);
        tiles.place(new Rock(),5,11);

        tiles.place(new Food(),2,0);
        assertEquals(52,tiles.ping(3,3,0));
        tiles.remove(2,0);

        tiles.place(new Food(),3,1);
        assertEquals(33, tiles.ping(3, 3, 0));
        tiles.remove(3,1);

        tiles.place(new Food(),4,0);
        assertEquals(52,tiles.ping(3,3,0));
        tiles.remove(4,0);

        tiles.place(new Food(),2,4);
        assertEquals(11,tiles.ping(3,3,0));
        tiles.remove(2,4);

        tiles.place(new Food(),3,5);
        assertEquals(0,tiles.ping(3,3,0));
        tiles.remove(3,5);

        tiles.place(new Food(),4,4);
        assertEquals(13,tiles.ping(3,3,0));
        tiles.remove(4,4);

        tiles.place(new Food(),4,6);
        assertEquals(40,tiles.ping(3,3,0));
        tiles.remove(4,6);

        tiles.place(new Food(),4,8);
        assertEquals(80, tiles.ping(3, 3, 0));
        tiles.remove(4,8);

        tiles.place(new Food(),5,9);
        assertEquals(120, tiles.ping(3, 3, 0));
        tiles.remove(5,9);

        tiles.place(new Food(),3,9);
        assertEquals(120, tiles.ping(3, 3, 0));
        tiles.remove(3,9);

        tiles.place(new Food(),2,8);
        assertEquals(160, tiles.ping(3, 3, 0));
        tiles.remove(2,8);

        tiles.place(new Food(),1,9);
        assertEquals(190, tiles.ping(3, 3, 0));
        tiles.remove(1,9);

        tiles.place(new Food(),2,10);
        assertEquals(150, tiles.ping(3, 3, 0));
        tiles.remove(2,10);

        tiles.place(new Food(),2,12);
        assertEquals(190, tiles.ping(3, 3, 0));
        tiles.remove(2,12);

        tiles.place(new Food(),3,13);
        assertEquals(230, tiles.ping(3, 3, 0));
        tiles.remove(3,13);

        tiles.place(new Food(),4,12);
        assertEquals(270, tiles.ping(3, 3, 0));
        tiles.remove(4,12);
    }

    /** Tests that insertion works on unique items and that size increases every time an item is inserted */
    @Test
    public void testBinaryHeapInsert() {
        BinaryHeap<Integer,Integer> heap = new BinaryHeap<>((n1,n2)->n2-n1);
        assertTrue(heap.classInv());
        assertTrue(heap.isEmpty());

        for(int i=1;i<1000;i++){
            heap.insert(i,i);
            assertEquals(i,heap.size());
            assertTrue(heap.classInv());
        }
    }

    /**
     * tests that {@link BinaryHeap#peek} & {@link BinaryHeap#poll} returns the highest priority item
     * <br> Requires {@link BinaryHeap#insert} to be working
     */
    @Test
    public void testBinaryHeapPollandPeek(){
        BinaryHeap<Integer,Integer> heap = new BinaryHeap<>((n1,n2)->n2-n1);
        assertTrue(heap.classInv());

        for(int i=1;i<1000;i++){
            heap.insert(i,i);
            assertTrue(heap.classInv());
            assertEquals(1,heap.peek());
        }

        for(int i=1;i<1000;i++){
            assertTrue(heap.classInv());
            assertEquals(i,heap.poll());
        }
    }

    /**
     * Tests that the newly increased priority will satisfy the classInv and
     * also be returned from {@link BinaryHeap#peek} if it's first
     */
    @Test
    public void testBinaryHeapIncreasePriority(){
        BinaryHeap<Integer,Integer> heap = new BinaryHeap<>((n1,n2)->n2-n1);
        assertTrue(heap.classInv());

        for(int i=1;i<1000;i++)
            heap.insert(i,i);


        for(int i=1;i<1000;i++){
            assertTrue(heap.classInv());
            heap.increasePriority(i,0);
            assertTrue(heap.classInv());

            assertEquals(i,heap.peek());

            heap.poll();
            assertTrue(heap.classInv());
        }
    }
}
