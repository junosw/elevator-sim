package com.junosw.elevatorsim;

import org.junit.Test;

import java.security.InvalidParameterException;

/**
 * Testing the Elevator object
 */
public class ElevatorTest {

    @Test (expected = InvalidParameterException.class)
    public void testElevatorCreateNumFloorsConstraint() {
        final int badInput = 0;
        Elevator e = new Elevator(badInput);
    }
}
