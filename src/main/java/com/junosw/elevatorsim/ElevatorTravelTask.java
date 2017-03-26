package com.junosw.elevatorsim;

import java.util.TimerTask;

/**
 * Extends timer task to handle the event of an elevator arriving at a new floor
 */
public class ElevatorTravelTask extends TimerTask {

    // the elevator associated with this task
    final Elevator elevator;

    /**
     * Create a new travel task for an elevator
     * @param elevator
     */
    public ElevatorTravelTask(final Elevator elevator) {
        this.elevator = elevator;
    }

    @Override
    public void run() {
        elevator.arrivedAtNewFloorEvent();
    }
}
