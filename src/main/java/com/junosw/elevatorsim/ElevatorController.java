package com.junosw.elevatorsim;

import java.security.InvalidKeyException;

/**
 * Interface for Elevators to interact with
 */
public interface ElevatorController {

    void setCurrentFloor(Elevator elevator, int floor) throws InvalidKeyException;
}
