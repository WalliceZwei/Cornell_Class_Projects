package model;

import cms.util.maybe.Maybe;

import java.util.List;

/**
 * NEVER remove or change any constants in this file except reformatting. Feel free to add
 * additional constants in this file.
 */
public interface ReadOnlyWorld {
    /** @return number of steps */
    int getSteps();

    /** @return number of alive critters. */
    int getNumberOfAliveCritters();

    /** @return number of rocks. */
    int getNumberOfRocks();

    /** @return number of food. */
    int getNumberOfFood();

    /** @return number of empty tiles. */
    int getNumEmptyTiles();

    /**
     * @param c column id.
     * @param r row id.
     * @return the critter at the specified hex.
     */
    Maybe<ReadOnlyCritter> getReadOnlyCritter(int c, int r);

    /**
     * @param c column id.
     * @param r row id.
     * @return 0 if the cell is empty.
     * <br>-1 if it is rock,
     * <br>-(X+1) if it is X food,
     * <br>X+1 if it contains a critter facing in direction X.
     * <br>Treat out-of-bound or invalid hex as rock.
     */
    int getTerrainInfo(int c, int r);

    /** Returns the width of this World */
    int getWidth();

    /** Returns the height of this World */
    int getHeight();
}
