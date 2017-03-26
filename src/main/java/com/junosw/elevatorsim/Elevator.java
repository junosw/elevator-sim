package com.junosw.elevatorsim;

import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an Elevator in our sim.
 */
public class Elevator {

    private static Logger logger = LoggerFactory.getLogger(Elevator.class);

    // Elevator constraint - lowest floor is 1
    public static final int GROUND_FLOOR = 1;
    // static int values to represent traveling state
    private static final int TRAVELING_DOWN = 0;
    private static final int NOT_TRAVELING = 1;
    private static final int TRAVELING_UP = 2;
    // travel time MS floor to floor
    private static final int TRAVEL_TIME_MS = 5000;
    // statics for making door setter state more readable
    // managing door state
    private static final boolean DOORS_OPEN = true;
    private static final boolean DOORS_CLOSED = false;

    // number of floors in the building
    private final int numberOfFloors;
    // the controller we report too
    private final ElevatorController controller;
    // timer is managed while elevator is "traveling"
    private Timer travelTimer = new Timer();
    // current floor we are on
    // if we're traveling, this will be the floor we left
    private int currentFloor = GROUND_FLOOR;
    // when traveling, this is the floor we are currently scheduled to travel too
    private int destinationFloor = GROUND_FLOOR;
    // traveling state; 0 = traveling down, 1 = not traveling, 2 = traveling up
    private int traveling = NOT_TRAVELING;
    // doors open?
    private boolean doorsOpen = false;


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
    }

    /**
     * Tell this elevator to pick up a passenger on the specified floor
     * @param floor
     */
    public void goToDestination(final int floor) {

        if (floor < GROUND_FLOOR || floor > numberOfFloors) {
            throw new InvalidParameterException("'floor' param out of bounds. Must be between "
                    + GROUND_FLOOR + " and " + numberOfFloors);
        }
        if (currentFloor > floor) {
            traveling = TRAVELING_DOWN;

        } else if (currentFloor < floor) {
            traveling = TRAVELING_UP;

        } else if (currentFloor == floor) {
            // sanity checking - are we already there?
            arrivedAtDestination();
            return;
        }

        destinationFloor = floor;
        // close the doors and get ready to travel
        setDoorState(DOORS_CLOSED);

        // set up the travel timer scheduled task
        // policy: it takes 5 seconds to move from one floor to the next
        travelTimer.cancel();
        travelTimer.schedule(new ElevatorTravelTask(this), TRAVEL_TIME_MS);
    }

    /**
     * Hook for timer task to tell us we've arrived at a new floor
     */
    public void arrivedAtNewFloorEvent() {

        if (traveling == TRAVELING_UP) {
            currentFloor++;
        } else if (traveling == TRAVELING_DOWN) {
            currentFloor--;
        }

        try {
            // we report back to controller
            controller.setCurrentFloor(this, currentFloor);

            if (currentFloor == destinationFloor) {
                arrivedAtDestination();
            }

        } catch (InvalidKeyException ike) {
            logger.error("Internal error: controller didn't recognize this elevator");
        }
    }

    /**
     * Elevator has finished traveling to a floor
     */
    public void arrivedAtDestination() {
        // no longer traveling
        travelTimer.cancel();
        traveling = NOT_TRAVELING;

        // open the doors!
        setDoorState(DOORS_OPEN);
    }

    /**
     * Since we always want to report to controller when our state changes, this method should always be used
     * internally to set the state of our doors
     * @param state
     */
    private void setDoorState(final boolean state) {
        doorsOpen = state;
        controller.setDoorState(this);
    }

    /**
     * Are the elevator doors open?
     * @return
     */
    public boolean getDoorsOpen() {
        return doorsOpen;
    }
}
