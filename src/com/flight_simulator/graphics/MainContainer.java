package com.flight_simulator.graphics;

import com.flight_simulator.data.SimulationData;
import javax.swing.*;
import java.awt.*;
import java.rmi.activation.ActivationGroupDesc;

public class MainContainer extends JPanel {
    /*
     * This class is responsible for creating and holding all the graphics,
     * that will appear in the application.
     */

    private MapArea mapArea = new MapArea();
    private LabelArea labelArea = new LabelArea();
    private MessagesArea messagesArea = new MessagesArea();
    private GridBagConstraints constraints = new GridBagConstraints();

    public MainContainer() {
        this.setLayout(new GridBagLayout());
        addComponentsToPanel();
    }

    private void addComponentsToPanel() {

        // Set constraints for Label Area panel.
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 10;
        // Add Label Area
        this.add(labelArea, constraints);

        // Set constraints for Map Area panel.
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.ipady = 0;
        // Add Map Area.
        this.add(mapArea, constraints);

        // Set constraints for Messages Area panel.
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.ipady = 0;
        // Add Messages Area.
        this.add(messagesArea, constraints);
    }

    public MapArea getPaintAccess() {
        return mapArea;
    }

    public void updateData() { mapArea.updateData(); }

    public void updateCollisions(int collisions) {
        labelArea.collisions.setText(Integer.toString(collisions));
    }

    public void updateSimulationTime(int time) {
        labelArea.time.setText(Integer.toString(time) + " seconds");
    }

    public void updateTotalAircrafts(int totalAircrafts) {
        labelArea.aircrafts.setText(Integer.toString(totalAircrafts));
    }

    public void updateLandings(int landings) {
        labelArea.landings.setText(Integer.toString(landings));
    }

    public void addMessage(String message) {
        messagesArea.statusMessages.append(message + "\n");
    }

}
