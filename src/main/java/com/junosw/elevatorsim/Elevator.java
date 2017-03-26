package com.junosw.elevatorsim;

import java.security.InvalidParameterException;

/**
 * Represents an Elevator in our sim.
 */
public class Elevator {

    // Elevator constraint - lowest floor is 1
    private static final int GROUND_FLOOR = 1;

    // number of floors in the building
    private final int numberOfFloors;

    /**
     * Pojo c'tor for creating a new Elevator
     * @param numFloors - number of floors in the building.
     */
    public Elevator(final int numFloors) {

        if (numFloors <= GROUND_FLOOR) {
            throw new InvalidParameterException("numFloors must be greater than " + GROUND_FLOOR);
        }

        numberOfFloors = numFloors;
    }


}
