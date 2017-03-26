package com.junosw.elevatorsim;

/**
 * Entry point for the application.
 */
public final class ElevatorSim {

    private static final int DEFAULT_NUMBER_OF_ELEVATORS = 3;
    private static final int DEFAULT_NUMBER_OF_FLOORS = 12;

    private ElevatorSim() { }

    /**
     * Main entry point
     * @param args
     */
    public static void main(final String[] args) {
        // can take these args via the command line per application requirements
        final int desiredNumElevators = DEFAULT_NUMBER_OF_ELEVATORS;
        final int desiredNumFloors = DEFAULT_NUMBER_OF_FLOORS;

        runSim(desiredNumElevators, desiredNumFloors);
    }

    /**
     * Run the simulation
     */
    private static void runSim(final int numElevators, final int numFloors) {

        final SimElevatorController controller = new SimElevatorController(numElevators, numFloors);

        // call an elevator
        // hardcoded value for dev - call one elevator to floor 5
        controller.callElevator(5);
    }
}
