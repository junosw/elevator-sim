package com.junosw.elevatorsim;

import java.util.HashMap;

/**
 * The brains of the operation
 * The sim will make requests through the controller
 * The controller "owns" the elevators and decides which
 * will respond to requests, etc.
 */
public class SimElevatorController implements ElevatorController {

    // our map of Elevators and what floor they are currently on
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

    /**
     * Someone has requested an elevator
     * @param toFloor
     */
    public void callElevator(final int toFloor) {

        final Elevator elevator = getElevatorForNewRequest();

        elevator.goToPickup(toFloor);
    }

    /**
     * Get an elevator for a new ride request.
     * Will enforce all elevator request requirements
     * @return
     */
    private Elevator getElevatorForNewRequest() {
        // do the magic that gets us the right elevator
        // for the purposes of dev, well just return the first
        // elevator for now to get to the high priority features of the Elevator
        return elevatorFloorMap.keySet().iterator().next();
    }
}
