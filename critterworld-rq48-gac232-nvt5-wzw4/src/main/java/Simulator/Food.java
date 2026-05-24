package Simulator;

import model.Constants;

public class Food implements Tile {

    /*
     * Food must be above 0, or else it shouldn't be a Food tile, but empty
     */

    /** Amount of energy inside the food */
    private int energy;

    public Food(){this.energy = Constants.MANNA_AMOUNT;}

    public Food(int energy) {
        assert energy > 0;
        this.energy = energy;
    }

    /**
     * Updates the Food's energy by {@code deltaEnergy}
     * returns true if Food's energy is below or equal to 0, false otherwise.
     */
    public boolean updateEnergy(int deltaEnergy){
        energy += deltaEnergy;
        return energy <= 0;
    }

    public int eatFood(){
        energy = 0;
        return energy;
    }

    public int getEnergy(){
        return energy;
    }

    @Override
    public boolean classInv() {
        if (energy <= 0) return false;
        return true;
    }

    @Override
    public Tile clone() {
        return new Food(energy);
    }
}
