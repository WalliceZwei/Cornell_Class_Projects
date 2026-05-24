package a6;

import java.util.Arrays;

/** Stores cost, dist, initialDir. Used in Dijkstra's for Ping. Immutable */
public class Priority {

    /** data[0] is cost, data[1] is dist, data[2] is move */
    private final int[] data;

    public Priority(int cost,int dist, int move) {
        data = new int[]{cost,dist, move};
    }

    public int getCost(){
        return data[0];
    }

    public int getDist(){
        return data[1];
    }

    public int getMove(){
        return data[2];
    }

    /** Returns a negative, 0, or positive number if the cost is less than, equal to, or bigger than {@code other}. */
    public int compare(Priority other){
        return getCost() - other.getCost();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Priority)) return false;
        return Arrays.equals(data,((Priority)o).data);
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
