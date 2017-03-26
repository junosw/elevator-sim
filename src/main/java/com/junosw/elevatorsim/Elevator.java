package com.junosw.elevatorsim;

import java.security.InvalidParameterException;
import java.util.Timer;

/**
 * Represents an Elevator in our sim.
 */
public class Elevator {

    // Elevator constraint - lowest floor is 1
    public static final int GROUND_FLOOR = 1;

    // number of floors in the building
    private final int numberOfFloors;
    // the controller we report too
    private final ElevatorController controller;

    Timer travelTimer;
    /**
     * Pojo c'tor for creating a new Elevator
     * @param numberOfFloors - number of floors in the building.
     */
    public Elevator(final int numberOfFloors, final ElevatorController controller) {

        if (numberOfFloors <= GROUND_FLOOR) {
            throw new InvalidParameterException("numFloors must be greater than " + GROUND_FLOOR);
        }

        this.numberOfFloors = numberOfFloors;
        this.controller = controller;

        // set up the travel timer scheduled task
        // policy: it takes 5 seconds to move from one floor to the next
        travelTimer = new Timer();

        travelTimer.schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    // your code here
                }
            },
            5000
        );
    }

    /**
     * Tell this elevator to pick up a passenger on the specified floor
     * @param floor
     */
    public void goToPickup(final int floor) {

    }


}
