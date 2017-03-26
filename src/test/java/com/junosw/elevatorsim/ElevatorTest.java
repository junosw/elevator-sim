package com.junosw.elevatorsim;

import org.junit.Test;

import java.security.InvalidParameterException;

/**
 * Testing the Elevator object
 *
 * Typically there would be a lot more unit testing for all of the classes
 * but at this point the focus is on design
 */
public class ElevatorTest {

    @Test (expected = InvalidParameterException.class)
    public void testElevatorCreateNumFloorsConstraint() {
        final int badInput = 0;
        Elevator e = new Elevator(badInput, new TestController());
    }
}
