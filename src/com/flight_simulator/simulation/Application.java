package com.flight_simulator.simulation;

import com.flight_simulator.data.Flight;
import com.flight_simulator.data.SimulationData;
import com.flight_simulator.graphics.MainContainer;
import com.flight_simulator.graphics.Menu;
import javax.swing.*;
import java.awt.*;

/*
* Main class:
* Is responsible for handling all program aspects,
* such as creating the GUI, the simulation timer and providing
* to the event handler the methods for manipulating simulation.
*/

public class Application {

    // Create main frame's menu.
    private static Menu menu = new Menu();
    // Create the default data.
    private static SimulationData data = new SimulationData();
    // Default input = default
    private static String input = "default";
    // Create Main Container.
    private static MainContainer container = new MainContainer();
    // Create the timer.
    private static SimulationTimer timer;

    public static void main(String[] args) {

        // Create the graphics.
        SwingUtilities.invokeLater(Application::createAndShowGui);

    }

    private static void createAndShowGui() {

        // Check if GUI created on event dispatch thread. | Debugging purposes.
        System.out.print("Application Class: ");
        System.out.println("Created GUI on EDT? " + SwingUtilities.isEventDispatchThread());

        // Create main Frame.
        JFrame appFrame = new JFrame("MediaLab Flight Simulator");

        // Set Close Operation.
        appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Set minimum size of window.
        appFrame.setMinimumSize(new Dimension(1230, 565));

        // Set Look and Feel.
        setLookAndFeel();

        // Set resize to false
        appFrame.setResizable(false);

        // Initialize menu and add it to the frame.

        appFrame.setJMenuBar(menu.getMenu());

        // Set Layout.
        appFrame.setLayout(new GridBagLayout());

        // Add Main Container to the frame.

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        appFrame.add(container, constraints);

        // Pack frame.
        appFrame.pack();

        //Set location of window to the center of the screen.
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        appFrame.setLocation((dim.width/2)-appFrame.getSize().width/2, (dim.height/2)-appFrame.getSize().height/2);

        // Make frame visible.
        appFrame.setVisible(true);

        container.addMessage("Application initialized.");

    }

    private static void setLookAndFeel() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // handle exception
            e.printStackTrace();
        }
    }

    public static SimulationData getData() { return data; }

    public static MainContainer getContainer() { return container; }

    public static void startSimulation() {
        int delay = 100;
        if(data.error) {
            container.addMessage("There is an error in specified files.");
            return;
        }
        timer = new SimulationTimer(delay, data);
        timer.startTimer();
        container.addMessage("Starting simulation with MapID: " + input);

        for (Flight flight : data.getInvalidFlights()) {
            container.addMessage("Flight with id: " + flight.getFlightId() + " not valid for simulation.");
        }
    }

    public static void stopSimulation() {
        container.updateSimulationTime(0);
        container.updateTotalAircrafts(0);
        container.updateCollisions(0);
        container.updateLandings(0);
        data = new SimulationData(input);
        // Update data repaints.
        container.updateData();

        //stop timer
        timer.stopTimer();

        container.addMessage("Simulation with MapID: " + input + " stopped.");
    }

    public static void endSimulation() {
        timer.stopTimer();
        container.addMessage("Simulation with MapID: " + input + " ended.");
    }

    public static void newSimulation(String newInput) {
        input = newInput;
        data = new SimulationData(input);
        container.updateData();
        container.addMessage("Creating new simulation with MapID: " + input);
    }


    public static void closeApplication() {
        System.out.println("User closed the application.");
        System.exit(0);
    }

    public static void repaint() {
        container.getPaintAccess().repaint();
    }
}
