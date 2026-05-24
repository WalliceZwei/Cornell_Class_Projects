package model;

/**
 * NEVER remove or change any constants in this file except reformatting. Feel free to add
 * additional constants in this file.
 */
public final class Constants {

    private Constants() {}

    public static final int BASE_DAMAGE = 100;
    public static final double DAMAGE_INC = .2;
    public static final int ENERGY_PER_SIZE = 500;
    public static final int FOOD_PER_SIZE = 200;
    public static final int MAX_SMELL_DISTANCE = 10;
    public static final int ROCK_VALUE = -1;
    public static final int WIDTH = 50;
    public static final int HEIGHT = 87;
    public static final int MAX_RULES_PER_TURN = 999;
    public static final int SOLAR_FLUX = 1;
    public static final int MOVE_COST = 3;
    public static final int ATTACK_COST = 5;
    public static final int GROW_COST = 1;
    public static final int BUD_COST = 9;
    public static final int MATE_COST = 5;
    public static final int RULE_COST = 2;
    public static final int ABILITY_COST = 25;
    public static final int INITIAL_ENERGY = 250;
    public static final int MIN_MEMORY = 7;
    public static final int MANNA_COUNT = 10;
    public static final int MANNA_AMOUNT = 10;
    public static final int PING_STRENGTH = 15;

    public static final int CANVAS_PADDING = 5;
    public static final int MAX_FPS = 60;
    public static final double MIN_ZOOM = 0;
    public static final double MAX_ZOOM = 10;
    public static final int HEX_RADIUS = 30;
    public static final int MAX_CRITTER_LENGTH = 57;
    public static final int MIN_CRITTER_LENGTH = 40;
    public static final int MIN_STAGE_WIDTH = 560;
    public static final int MIN_STAGE_HEIGHT = 350;
    public static final int MAX_CRITTER_SPECIES_FONT_SIZE = 11;
    public static final int MIN_CRITTER_SPECIES_FONT_SIZE = 6;
    public static double getCritterLength(int size) {
        return (Constants.MAX_CRITTER_LENGTH-Constants.MIN_CRITTER_LENGTH) * (1 - Math.exp(-0.1*size))+Constants.MIN_CRITTER_LENGTH;
    }
    public static double getCritterSpeciesFontSize(int size){
        return (Constants.MAX_CRITTER_SPECIES_FONT_SIZE-Constants.MIN_CRITTER_SPECIES_FONT_SIZE) * (1 - Math.exp(-0.1*size))+Constants.MIN_CRITTER_SPECIES_FONT_SIZE;
    }
}
