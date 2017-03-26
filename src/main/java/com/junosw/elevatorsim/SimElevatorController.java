package com.junosw.elevatorsim;

import java.util.HashMap;

/**
 * The brains of the operation
 * The sim will make requests through the controller
 * The controller "owns" the elevators and decides which
 * will respond to requests, etc.
 */
public class SimElevatorController implements ElevatorController {

    private HashMap<Elevator, Integer> elevatorFloorMap = new HashMap<>();

    /**
     * Create a new controller
     * @param numberOfElevators - number of elevators in the building
     * @param numberOfFloors - number of floors in the building
     */
    public SimElevatorController(final int numberOfElevators, final int numberOfFloors) {

        for (int i = 0; i < numberOfElevators; i++) {
            elevatorFloorMap.put(new Elevator(numberOfFloors, this), Elevator.GROUND_FLOOR);
        }
    }

    /**
     * Elevators report back to us what their state is e.g. what floor they're on, if they are traveling, etc.
     * @param elevator
     */
    public void setElevatorState(final Elevator elevator) {

    }
}
