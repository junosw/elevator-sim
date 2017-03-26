package com.junosw.elevatorsim;

import java.security.InvalidKeyException;

/**
 * Elevator controller for tests
 */
public class TestController implements ElevatorController {

    @Override
    public void setCurrentFloor(Elevator elevator, int floor) throws InvalidKeyException {
        throw new UnsupportedOperationException();
    }

    /**
     * Event - elevator tells us when its doors are open
     * @param elevator
     */
    public void setDoorState(final Elevator elevator) {
        throw new UnsupportedOperationException();
    }
}
