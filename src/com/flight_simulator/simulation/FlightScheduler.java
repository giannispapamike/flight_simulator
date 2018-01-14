package com.flight_simulator.simulation;

import com.flight_simulator.data.Flight;
import com.flight_simulator.data.SimulationData;
import com.flight_simulator.graphics.MainContainer;

import java.util.ArrayList;

public class FlightScheduler {

    private ArrayList<Flight> activeFlights = new ArrayList<>();
    private ArrayList<Astar.Node> path = new ArrayList<>();
    private SimulationData data;
    private MainContainer container;
    private int collisions, aircrafts, landings;

    FlightScheduler(SimulationData data) {

        this.data = data;
        collisions = 0;
        aircrafts = 0;
        landings = 0;

        container = Application.getContainer();

        System.out.println("Scheduler created.");
    }

    public void addFlight(Flight flight) {

        activeFlights.add(flight);

        aircrafts++;
        container.updateTotalAircrafts(aircrafts);

        container.addMessage("Flight with id: " + flight.getFlightId() + " added to simulation.");

        path = flight.getPath();

        flight.setLeftIndex(0);
        flight.setRightIndex(1);
        flight.setCurrentX( (float) ( ( path.get( flight.getLeftIndex() ).x * 16 + 8 ) * 1.25 ) );
        flight.setCurrentY( (float) ( ( path.get( flight.getLeftIndex() ).y * 16 + 8 ) * 1.25 ) );
        flight.setGraphicX( path.get( flight.getLeftIndex() ).x * 16 );
        flight.setGraphicY( path.get( flight.getLeftIndex() ).y * 16 );
        flight.setCurrentHeight( (float) ( path.get( flight.getLeftIndex() ).height  * 3.28 )  );
        flight.setCurrentVelocity( (float) ( flight.getAircraftLandingSpeed() ) * 12 / 3600 );
        flight.setCurrentFuelCapacity( flight.getFlightFuelCapacity() );
        flight.setCurrentState("Active");
        flight.setPaintState(true);

        updateFlightImg(flight);

        // Debugging.
        debugging(flight);

        data.setActiveFlights(activeFlights);
        repaint();
    }

    public void update(int time) {

        container.updateSimulationTime(time);

        // Check if simulation has ended.
        simulationEnded();

        checkCollisions();

        if ( activeFlights.isEmpty() ) {
            return;
        }

        int iteration = 0;

        for ( Flight flight : activeFlights ) {

            // Update Height.
            updateHeight(flight);

            // Update Fuel Capacity.
            updateFuelCapacity(flight);

            // Update Speed
            updateSpeed(flight);

            // Update Position
            updatePosition(flight);

            // Update Flight Image.
            updateFlightImg(flight);

            if (checkEndStatus(flight)) {
                activeFlights.remove(iteration);

                aircrafts--;
                landings++;

                container.updateTotalAircrafts(aircrafts);
                container.updateLandings(landings);

                System.out.println("Flight with id: " + flight.getFlightId() + " ended and removed from scheduler.");

                container.addMessage("Flight with id: " + flight.getFlightId() + " landed.");

                if ( activeFlights.isEmpty() ) {
                    break;
                }
            }

            debugging(flight);

            iteration++;
        }

        data.setActiveFlights(activeFlights);
        repaint();

    }

    private void simulationEnded() {
        int counter = 0;
        for (Flight flight : data.getFlights()) {
            if ( (flight.getCurrentState() == "Crashed") || (flight.getCurrentState() == "Landed") )
                counter++;
        }

        if (counter == data.getFlights().size())
            Application.endSimulation();

    }

    private void updateHeight(Flight flight) {
        if ( flight.getCurrentHeight() < flight.getFlightHeight() ) {
            if ( flight.getCurrentHeight() + ( flight.getAircraftHeightRatio() * 12 / 60 ) < flight.getFlightHeight() ) {
                flight.setCurrentHeight( flight.getCurrentHeight() + ( (float) flight.getAircraftHeightRatio() * 12 / 60 ) );
            } else {
                flight.setCurrentHeight( flight.getFlightHeight() );
            }
        }
    }

    private void updateFuelCapacity(Flight flight) {
        flight.setCurrentFuelCapacity( flight.getCurrentFuelCapacity() - ( (float) flight.getAircraftFuelConsumption() * flight.getCurrentVelocity() ) );
    }

    private void updateSpeed(Flight flight) {
        if (
            (
                ( Math.abs( flight.getCurrentX() - ( (float) ( flight.getPath().get(0).x * 16 + 8 ) * 1.25 ) ) > 30 ) ||
                ( Math.abs( flight.getCurrentY() - ( (float) ( flight.getPath().get(0).y * 16 + 8 ) * 1.25 ) ) > 30 )
            ) &&
            (
                ( Math.abs( flight.getCurrentX() - ( (float) ( flight.getPath().get( flight.getPath().size() - 1 ).x * 16 + 8 ) * 1.25 ) ) > 30 ) ||
                ( Math.abs( flight.getCurrentY() - ( (float) ( flight.getPath().get( flight.getPath().size() - 1 ).y * 16 + 8 ) * 1.25 ) ) > 30 )
            )
        ) {
            flight.setCurrentVelocity((float) (flight.getFlightSpeed()) * 12 / 3600);
        }
        else {
            flight.setCurrentVelocity((float) (flight.getAircraftLandingSpeed()) * 12 / 3600);
        }
    }

    private void updatePosition(Flight flight) {

        if ( flight.getPath().get( flight.getLeftIndex() ).x  >  flight.getPath().get( flight.getRightIndex() ).x ) {
            flight.setCurrentX(flight.getCurrentX() - flight.getCurrentVelocity());

            System.out.println("left");

            if ( flight.getCurrentX() <=  (float) ( flight.getPath().get( flight.getRightIndex() ).x * 16 + 8 ) * 1.25 ) {
                flight.setLeftIndex( flight.getRightIndex() );
                flight.setRightIndex( flight.getRightIndex() + 1 );

                updatePaint(flight, true);
            } else flight.setPaintState(true);

        } else if ( flight.getPath().get( flight.getLeftIndex() ).x  <  flight.getPath().get( flight.getRightIndex() ).x ) {
            flight.setCurrentX(flight.getCurrentX() + flight.getCurrentVelocity());

            System.out.println("right");

            if ( flight.getCurrentX() >=  (float) ( flight.getPath().get( flight.getRightIndex() ).x * 16 + 8 ) * 1.25 ) {
                flight.setLeftIndex( flight.getRightIndex() );
                flight.setRightIndex( flight.getRightIndex() + 1 );

                updatePaint(flight, true);
            } else flight.setPaintState(true);

        } else if ( flight.getPath().get( flight.getLeftIndex() ).y  >  flight.getPath().get( flight.getRightIndex() ).y ) {
            flight.setCurrentY(flight.getCurrentY() - flight.getCurrentVelocity());

            System.out.println("up");

            if ( flight.getCurrentY() <=  (float) ( flight.getPath().get( flight.getRightIndex() ).y * 16 + 8 ) * 1.25 ) {
                flight.setLeftIndex( flight.getRightIndex() );
                flight.setRightIndex( flight.getRightIndex() + 1 );

                updatePaint(flight, true);
            } else flight.setPaintState(true);

        } else if ( flight.getPath().get( flight.getLeftIndex() ).y  <  flight.getPath().get( flight.getRightIndex() ).y ) {
            flight.setCurrentY(flight.getCurrentY() + flight.getCurrentVelocity());

            System.out.println("down");

            if ( flight.getCurrentY() >=  (float) ( flight.getPath().get( flight.getRightIndex() ).y * 16 + 8 ) * 1.25 ) {
                flight.setLeftIndex( flight.getRightIndex() );
                flight.setRightIndex( flight.getRightIndex() + 1 );

                updatePaint(flight, true);
            } else flight.setPaintState(true);

        }

    }

    private boolean checkEndStatus(Flight flight) {
        if ( flight.getRightIndex() == flight.getPath().size() ) {
            flight.setCurrentState("Landed");
            return true;
        }
        else return false;
    }

    private void updatePaint(Flight flight, boolean paint) {

        if (paint) {
            flight.setGraphicX(flight.getPath().get(flight.getLeftIndex()).x * 16);
            flight.setGraphicY(flight.getPath().get(flight.getLeftIndex()).y * 16);
        } else {
            flight.setGraphicX(-20);
            flight.setGraphicY(-20);
            repaint();
        }

        System.out.print("left: " + flight.getLeftIndex() + " ");
        System.out.println("right: " + flight.getRightIndex() );
    }

    private void checkCollisions() {

        int iteration = 0;

        for ( Flight flight : activeFlights ) {

            // Check if flight's height is equal or smaller from map's current position height and update collisions.
            // Don't check starting point cause aircraft's height starts at airport's height.
            if ( flight.getLeftIndex() != 0 )
                if ( flight.getCurrentHeight() <= ( flight.getPath().get( flight.getLeftIndex() ).height * 3.28 ) ) {

                    flight.setCurrentState("Crashed");

                    activeFlights.remove(iteration);
                    collisions++;
                    aircrafts--;

                    container.updateCollisions(collisions);
                    container.updateTotalAircrafts(aircrafts);

                    updatePaint(flight, false);

                    System.out.println("Collisions: " + collisions);
                    System.out.println("Flight with id: " + flight.getFlightId() + " crashed and removed from scheduler.");

                    container.addMessage("Flight with id: " + flight.getFlightId() + " crashed.");

                    if (activeFlights.isEmpty()) break;
                }

            // Check if flight's fuel ended and update collisions.
            if ( flight.getCurrentFuelCapacity() < 0 ) {

                flight.setCurrentState("Crashed");

                activeFlights.remove(iteration);
                collisions++;
                aircrafts--;

                container.updateCollisions(collisions);
                container.updateTotalAircrafts(aircrafts);

                updatePaint(flight, false);

                System.out.println("Collisions: " + collisions);
                System.out.println("Flight with id: " + flight.getFlightId() + " crashed and removed from scheduler.");

                container.addMessage("Flight with id: " + flight.getFlightId() + " crashed.");

                if (activeFlights.isEmpty()) break;
            }

            // Check if two or more aircrafts are collided ( same position or height at the same time).
            int innerIteration = 0;
            for ( Flight innerFlight : activeFlights ) {

                if (flight == innerFlight) continue;

                if (
                        ( Math.abs( flight.getCurrentX() - innerFlight.getCurrentX() ) < 2 ) &&
                        ( Math.abs( flight.getCurrentY() - innerFlight.getCurrentY() ) < 2 ) &&
                        ( Math.abs( flight.getCurrentHeight() - innerFlight.getCurrentHeight() ) < 500 )
                    ) {

                    flight.setCurrentState("Crashed");
                    innerFlight.setCurrentState("Crashed");

                    activeFlights.remove(iteration);
                    activeFlights.remove(innerIteration);
                    collisions += 2;
                    aircrafts -= 2;

                    container.updateCollisions(collisions);
                    container.updateTotalAircrafts(aircrafts);

                    container.addMessage("Flights with ids " + flight.getFlightId() + " and " + innerFlight.getFlightId() + " collided." );

                    updatePaint(flight, false);
                    updatePaint(innerFlight, false);

                    if (activeFlights.isEmpty()) break;

                    System.out.println("Collisions: " + collisions);
                    System.out.println("Flight with id: " + flight.getFlightId() + " crashed and removed from scheduler.");
                    System.out.println("Flight with id: " + innerFlight.getFlightId() + " crashed and removed from scheduler.");
                }

                innerIteration++;
            }

            iteration++;
        }

    }

    private void updateFlightImg(Flight flight) {

        int orientation;

        if ( flight.getPath().get( flight.getLeftIndex() ).x > flight.getPath().get( flight.getRightIndex() ).x )
            orientation = 4;
        else if ( flight.getPath().get( flight.getLeftIndex() ).x < flight.getPath().get( flight.getRightIndex() ).x )
            orientation = 2;
        else if ( flight.getPath().get( flight.getLeftIndex() ).y > flight.getPath().get( flight.getRightIndex() ).y )
            orientation = 1;
        else if ( flight.getPath().get( flight.getLeftIndex() ).y < flight.getPath().get( flight.getRightIndex() ).y )
            orientation = 3;
        else orientation = 0;

        switch( orientation ) {
            case 1:
                if ( flight.getAircraftType() == 1 )
                    flight.setCurrentAircraftImg( "small_n" );
                else if ( flight.getAircraftType() == 2 )
                    flight.setCurrentAircraftImg( "middle_n" );
                else
                    flight.setCurrentAircraftImg( "big_n" );
                break;
            case 2:
                if ( flight.getAircraftType() == 1 )
                    flight.setCurrentAircraftImg( "small_e" );
                else if ( flight.getAircraftType() == 2 )
                    flight.setCurrentAircraftImg( "middle_e" );
                else
                    flight.setCurrentAircraftImg( "big_e" );
                break;
            case 3:
                if ( flight.getAircraftType() == 1 )
                    flight.setCurrentAircraftImg( "small_s" );
                else if ( flight.getAircraftType() == 2 )
                    flight.setCurrentAircraftImg( "middle_s" );
                else
                    flight.setCurrentAircraftImg( "big_s" );
                break;
            case 4:
                if ( flight.getAircraftType() == 1 )
                    flight.setCurrentAircraftImg( "small_w" );
                else if ( flight.getAircraftType() == 2 )
                    flight.setCurrentAircraftImg( "middle_w" );
                else
                    flight.setCurrentAircraftImg( "big_w" );
                break;
            default:
                flight.setCurrentAircraftImg( null );
                break;
        }
    }

    public void setPaintAccess(MainContainer container) {
        this.container = container;
    }

    private void repaint() {
        container.getPaintAccess().repaint();
    }

    public void stop() {

        for ( Flight flight : activeFlights ) {
            updatePaint(flight, false);
        }

        container.updateTotalAircrafts(0);
        container.updateCollisions(0);
        container.updateSimulationTime(0);
        container.updateLandings(0);
    }

    private void debugging(Flight flight) {
        // Debugging.
        System.out.println("-------------");
        System.out.println(
                "Left index:  " + flight.getLeftIndex() +
                        " -> Coordinates: (" + flight.getPath().get( flight.getLeftIndex() ).x +
                        "," + flight.getPath().get( flight.getLeftIndex() ).y +
                        ") -> Pixels: (" + ( flight.getPath().get( flight.getLeftIndex() ).x * 16 + 8 ) +
                        "," + ( flight.getPath().get( flight.getLeftIndex() ).y * 16 + 8 ) +
                        ") -> nautical miles: (" + ( flight.getPath().get( flight.getLeftIndex() ).x * 16 + 8 ) * 1.25 +
                        "," + ( flight.getPath().get( flight.getLeftIndex() ).y * 16 + 8 ) * 1.25 +
                        ")"
        );
        System.out.println(
                "Right index: " + flight.getRightIndex() +
                        " -> Coordinates: (" + flight.getPath().get( flight.getRightIndex() ).x +
                        "," + flight.getPath().get( flight.getRightIndex() ).y +
                        ") -> Pixels: (" + ( flight.getPath().get( flight.getRightIndex() ).x * 16 + 8 ) +
                        "," + ( flight.getPath().get( flight.getRightIndex() ).y * 16 + 8 ) +
                        ") -> nautical miles: (" + ( flight.getPath().get( flight.getRightIndex() ).x * 16 + 8 ) * 1.25 +
                        "," + ( flight.getPath().get( flight.getRightIndex() ).y * 16 + 8 ) * 1.25 +
                        ")"
        );
        System.out.println(
                "Flight Position: (" + flight.getCurrentX() +
                        "," + flight.getCurrentY() +
                        ") , Velocity: " + flight.getCurrentVelocity() +
                        " , Height: " + flight.getCurrentHeight() +
                        " , Fuel Capacity: " + flight.getCurrentFuelCapacity()
        );
        System.out.println("-------------");
    }
}
