package com.junosw.elevatorsim;

import java.security.InvalidKeyException;
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
     * Elevators report back to us what floor they're on when they are traveling
     * @param elevator
     */
    public void setCurrentFloor(final Elevator elevator, final int floor) throws InvalidKeyException {

        // general sanity check - we should not have non-existent or elevators we didn't create reporting to us
        if (!elevatorFloorMap.containsKey(elevator)) {
            throw new InvalidKeyException("Elevator not found in controller map");
        }

        elevatorFloorMap.put(elevator, floor);
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
