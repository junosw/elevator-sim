package com.junosw.elevatorsim;

import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
     * Event - elevator tells us when its doors are open
     * @param elevator
     */
    public void setDoorState(final Elevator elevator) {
        // this function currently doesn't do anything
        // it is here to fulfull the requirement "door open/closed reporting" requirement
        // and provide a hook for future business policy around open/closed doors
        // boolean doorState = elevator.getDoorsOpen();
        return;
    }

    /**
     * Someone has requested an elevator
     * @param toFloor
     */
    public void callElevator(final int toFloor) {

        final Elevator elevator = getElevatorForNewRequest(toFloor);

        elevator.goToDestination(toFloor);
    }

    /**
     * Get an elevator for a new ride request.
     * Will enforce all elevator request requirements
     * @return
     */
    private Elevator getElevatorForNewRequest(final int toFloor) {

        Iterator<Map.Entry<Elevator, Integer>> elevatorIter = elevatorFloorMap.entrySet().iterator();

        while (elevatorIter.hasNext()) {

            Map.Entry<Elevator, Integer> elevatorEntry = elevatorIter.next();

            // do we have an unoccupied elevator on this floor?
            // by definition, if the elevator is traveling, it is NOT "on this floor" so
            // look for an elevator on this floor AND not traveling
            if (elevatorEntry.getValue() == toFloor && !elevatorEntry.getKey().isTraveling()) {
                return elevatorEntry.getKey();

            } else if (elevatorEntry.getKey().willPassFloor(toFloor) && !elevatorEntry.getKey().unoccupied()) {
                // elevator tells us if it will pass a floor en route or not.  If it will and it is occupied, use it
                return elevatorEntry.getKey();
            } //else {
                // running out of implementation time here..

                // basically we would need to find the "closest" unoccupied elevator by keeping track of the
                // absolute value of the difference between the current location of unoccupied elevators and toFloor
                // and end up returning that one when the while loop finishes
            //}
        }

        // do the magic that gets us the right elevator
        // for the purposes of dev, well just return the first
        // elevator for now to get to the high priority features of the Elevator
        return elevatorFloorMap.keySet().iterator().next();
    }
}
