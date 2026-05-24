package Simulator;

public class Rock implements Tile {
    @Override
    public boolean classInv() {
        return true;
    }

    @Override
    public Tile clone() {
        return new Rock();
    }
}
