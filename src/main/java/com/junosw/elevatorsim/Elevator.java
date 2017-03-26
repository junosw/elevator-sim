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

    // not implemented due to time constraints but the elevator would keep track of trips and floors passed here
    // private int trips = 0;
    // private int floors = 0;
    // private static final int TRIPS_UNTIL_SERVICE_REQUIRED = 100;
    // private boolean serviceRequired = false;
    //
    // then we would raise an event to the controller when service is required as well as "disable itself"
    // by setting serviceRequired to TRUE and checking this flag where appropriate e.g. goToDestination


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
     *
     * Naturally, this isn't currently keeping track of more than one destination.
     * We would implement an ordered stack for that where the closest floor in the "current direction" would
     * be the next destination then pop the next destination off the stack once we arrive and doors close again
     *
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
     * Tests whether or not a traveling elevator will pass a floor
     *
     * @param floor - floor to test against
     * @return TRUE if it will pass the floor en route, false otherwise
     */
    public boolean willPassFloor(final int floor) {
        if (!isTraveling()) {
            return false;
        }

        if (traveling == TRAVELING_UP && floor > currentFloor && floor < destinationFloor) {
            return true;
        } else if (traveling == TRAVELING_DOWN && floor < currentFloor && floor > destinationFloor) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return whether or not this elevator is traveling
     * @return
     */
    public boolean isTraveling() {
        return traveling != NOT_TRAVELING;
    }

    /**
     * Return whether or not this elevator is occupied
     * @return
     */
    public boolean unoccupied() {
        // Unoccupied is defined as NOT traveling AND doors CLOSED
        return !isTraveling() && (getDoorsOpen() == DOORS_CLOSED);
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
