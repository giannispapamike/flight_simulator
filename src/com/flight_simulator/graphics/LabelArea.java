package com.flight_simulator.graphics;

import javax.swing.*;
import java.awt.*;

public class LabelArea extends JPanel {

    public JLabel time, aircrafts, collisions, landings;

    LabelArea() {

        time = new JLabel("0 seconds");
        aircrafts = new JLabel("0");
        collisions = new JLabel("0");
        landings = new JLabel("0");

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        addComponentsToPanel();
    }

    private void addComponentsToPanel() {

        this.add(Box.createRigidArea(new Dimension(2, 0)));

        this.add(new JLabel("Simulation Time: "));

        this.add(Box.createRigidArea(new Dimension(5, 0)));

        this.add(time);

        this.add(Box.createRigidArea(new Dimension(175, 0)));

        this.add(new JLabel("Total Aircrafts: "));

        this.add(Box.createRigidArea(new Dimension(5, 0)));

        this.add(aircrafts);

        this.add(Box.createRigidArea(new Dimension(175, 0)));

        this.add(new JLabel("Collisions: "));

        this.add(Box.createRigidArea(new Dimension(5, 0)));

        this.add(collisions);

        this.add(Box.createRigidArea(new Dimension(175, 0)));

        this.add(new JLabel("Landings: "));

        this.add(Box.createRigidArea(new Dimension(5, 0)));

        this.add(landings);

    }
}
