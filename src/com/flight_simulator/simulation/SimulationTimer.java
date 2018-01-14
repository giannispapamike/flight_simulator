package com.flight_simulator.simulation;


import com.flight_simulator.data.Flight;
import com.flight_simulator.data.SimulationData;
import com.flight_simulator.graphics.MainContainer;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SimulationTimer {

    private Timer timer;
    private int time;
    private SimulationData data;
    private FlightScheduler scheduler;
    private MainContainer container = Application.getContainer();

    public SimulationTimer(int delay, SimulationData data) {
        this.data = data;
        scheduler = new FlightScheduler(data);
        timer = new Timer(delay, this::actionPerformed);
        System.out.println("Timer started...");
        System.out.println("EDT? " + SwingUtilities.isEventDispatchThread());

    }

    private void actionPerformed(ActionEvent event) {

        if (time % 1 == 0)
            System.out.println(time / 1);

        // Add flights that should start on scheduler.
        startFlightSimulation();
        // Update the graphics if needed.
        scheduler.update(time);
        // Increment time
        time++;

    }

    private void startFlightSimulation() {
        for ( Flight flight : data.getFlights() ) {
            if ( flightShouldStart(flight) )
                scheduler.addFlight(flight);
        }
    }

    private boolean flightShouldStart(Flight flight) {
        if ( flight.getFlightStart() == this.time )
            return true;
        else return false;
    }

    public void startTimer() {
        time = 0;
        timer.start();
    }

    public void restartTimer() {
        time = 0;
        timer.restart();
    }

    public void stopTimer() {
        time = 0;
        scheduler.stop();
        this.timer.stop();
    }

    public int getTime() {
        return this.time;
    }


    public void setPaintAccess(MainContainer container) {
        this.container = container;

        scheduler.setPaintAccess(container);
    }
}
