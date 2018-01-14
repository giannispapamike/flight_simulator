package com.flight_simulator.simulation;

import com.flight_simulator.data.Airport;
import com.flight_simulator.data.Flight;
import com.flight_simulator.data.SimulationData;
import com.flight_simulator.graphics.MainContainer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class EventHandler implements ActionListener {

    private String event;

    public EventHandler(String event) {
        this.event = event;
    }

    public void actionPerformed(ActionEvent e) {

        if (event == "start")
            Application.startSimulation();
        else if (event == "stop")
            Application.stopSimulation();
        else if (event == "load")
            onLoad();
        else if (event == "exit")
            Application.closeApplication();
        else if (event == "show_airports_info")
            showAirportsInfo();
        else if (event == "show_airplanes_info")
            showAirplanesInfo();
        else if (event == "show_flights_info")
            showFlightsInfo();
        else if (event == "about")
            showAboutInfo();
    }

    private void showAboutInfo() {
        JOptionPane.showMessageDialog(null,
                "MediaLab Flight Simulator version beta 1.0.0\n" +
                "Created by Giannis Papamichail\n" +
                "github profile: https://github.com/giannispapamike",
                "About Info",
                JOptionPane.PLAIN_MESSAGE);
    }

    private void showFlightsInfo() {
        //custom title, no icon

        String[] s = new String[Application.getData().getFlights().size()];
        int i = 0;

        for ( Flight flight : Application.getData().getFlights() ) {
            s[i] = flight.toString();
            i++;
        }

        JOptionPane.showMessageDialog(null,
                (s.length == 0 ? "No valid flights." : s),
                "Valid Flights info",
                JOptionPane.PLAIN_MESSAGE);
    }

    private void showAirportsInfo() {
        //custom title, no icon

        String[] s = new String[Application.getData().getAirports().size()];
        int i = 0;

        for ( Airport airport : Application.getData().getAirports() ) {
            s[i] = airport.toString();
            i++;
        }

        JOptionPane.showMessageDialog(null,
                (s.length == 0 ? "No airports in the specified file." : s),
                "Airports info",
                JOptionPane.PLAIN_MESSAGE);
    }

    private void showAirplanesInfo() {
        //custom title, no icon

        String[] s = new String[Application.getData().getActiveFlights().size()];
        int i = 0;

        for ( Flight flight : Application.getData().getActiveFlights() ) {
            s[i] = flight.aircraftToString();
            i++;
        }

        JOptionPane.showMessageDialog(null,
                (s.length == 0 ? "No active flights at the moment." : s),
                "Active Aircrafts info",
                JOptionPane.PLAIN_MESSAGE);
    }

    private void onLoad() {
        Object[] possibilities = null;
        String input = (String)JOptionPane.showInputDialog(
                null,
                "Select MapID to create simulation...\n",
                "Set Map ID",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                "");

        //If a string was returned.
        if ((input != null) && (input.length() > 0)) {
            if (!checkInput(input)) {
                JOptionPane.showMessageDialog(null,
                        "Required files with specified id don't exist!\n");
            } else {
                Application.newSimulation(input);
            }
            return;
        }

        //If you're here, the return value was null/empty.
        JOptionPane.showMessageDialog(null,
                "You should enter some id buddy!\n" +
                "Better luck next time...");
    }

    private boolean checkInput(String input) {

        File f1 = new File("assets/examples/world_" + input + ".txt");
        File f2 = new File("assets/examples/airports_" + input + ".txt");
        File f3 = new File("assets/examples/flights_" + input + ".txt");
        if(f1.exists() && !f1.isDirectory())
            if(f2.exists() && !f2.isDirectory())
                if(f3.exists() && !f3.isDirectory()) return true;
                else return false;
            else return false;
        else return false;
    }
}
