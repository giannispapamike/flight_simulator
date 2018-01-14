package com.flight_simulator.graphics;

import com.flight_simulator.data.SimulationData;
import com.flight_simulator.simulation.Application;
import com.flight_simulator.simulation.EventHandler;
import com.flight_simulator.simulation.SimulationTimer;

import javax.swing.*;

public class Menu {

    private JMenuBar menu = new JMenuBar();

    public Menu() {
        addComponentsToMenu(menu);
    }

    private void addComponentsToMenu(JMenuBar menu) {

        // Create Menus.
        JMenu game = new JMenu("Game");
        JMenu simulation = new JMenu("Simulation");
        JMenu help = new JMenu("Help");

        // Create Game Items.
        JMenuItem start = new JMenuItem("Start");
        JMenuItem stop = new JMenuItem("Stop");
        JMenuItem load = new JMenuItem("Load...");
        JMenuItem exit = new JMenuItem("Exit");

        // Create simulation items
        JMenuItem airports = new JMenuItem("Airports");
        JMenuItem airplanes = new JMenuItem("Aircrafts");
        JMenuItem flights = new JMenuItem("Flights");

        // Create help items
        JMenuItem show_log = new JMenuItem("Show Log in Explorer");
        JMenuItem about = new JMenuItem("About");

        // Add game items
        game.add(start);
        game.add(stop);
        game.add(load);
        game.add(exit);

        // Add simulation items
        simulation.add(airports);
        simulation.add(airplanes);
        simulation.add(flights);

        // Add help items
//        help.add(show_log);
//        help.addSeparator();
        help.add(about);

        // Add menu items
        menu.add(game);
        menu.add(simulation);
        menu.add(help);

        // Add Action listeners to menu items.
        addListeners(start, stop, exit, load, airports, airplanes, flights, about);

    }

    private void addListeners(JMenuItem start, JMenuItem stop, JMenuItem exit, JMenuItem load, JMenuItem airports, JMenuItem airplanes, JMenuItem flights, JMenuItem about) {
        start.addActionListener( new EventHandler("start") );
        stop.addActionListener( new EventHandler("stop") );
        exit.addActionListener( new EventHandler("exit") );
        load.addActionListener( new EventHandler("load") );
        airports.addActionListener( new EventHandler("show_airports_info") );
        airplanes.addActionListener( new EventHandler("show_airplanes_info") );
        flights.addActionListener( new EventHandler("show_flights_info") );
        about.addActionListener( new EventHandler("about") );
    }

    public JMenuBar getMenu() {
        return this.menu;
    }

    public void setPaintAccess(MainContainer container) {
//        timer.setPaintAccess(container);
    }
}
