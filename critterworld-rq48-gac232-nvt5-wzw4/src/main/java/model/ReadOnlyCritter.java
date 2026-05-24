package model;

import cms.util.maybe.Maybe;

/**
 * NEVER remove or change any constants in this file except reformatting. Feel free to add
 * additional constants in this file.
 */

public interface ReadOnlyCritter {
    /** @return critter species. */
    String getSpecies();

    /**
     * Hint: you should consider making a defensive copy of the array.
     *
     * @return an array representation of critter's memory.
     */
    int[] getMemory();

    /** @return current program string of the critter. */
    String getProgramString();

    /**
     * @return last rule executed by the critter on its previous turn, or {@code Maybe.none()} if it has not
     *     executed any.
     */
    Maybe<String> getLastRuleString();

    /**
     * @return the orientation of the critter. An integer from 0 to 5 inclusive.
     */
    int getOrientation();
}
