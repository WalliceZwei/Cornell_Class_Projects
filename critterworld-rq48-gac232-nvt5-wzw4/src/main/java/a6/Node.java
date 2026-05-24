package a6;

import java.util.Arrays;

/** Stores x, y, dir, and priority. Hashable. Used in Dijkstra's algorithm for Ping */
public class Node {
    /** data[0] & data[1] are coords, data[2] is dir */
    private final int[] data;

    public Node(int[] coords, int dir) {
        this.data = new int[]{coords[0], coords[1],dir};
    }

    public int getDir(){return data[2];}
    public int getX(){return data[0];}
    public int getY(){return data[1];}

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Node)) return false;
        return Arrays.equals(data,((Node)o).data);
    }

    @Override
    public int hashCode(){
        return Arrays.hashCode(data);
    }

    @Override
    public String toString(){
        return Arrays.toString(data);
    }
}
