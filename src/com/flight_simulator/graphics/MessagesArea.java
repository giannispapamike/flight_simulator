package com.flight_simulator.graphics;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MessagesArea extends JPanel {

    public JTextArea statusMessages = new JTextArea();;
    private JScrollPane scrollPane = new JScrollPane(statusMessages);

    MessagesArea() {

        statusMessages.setEditable(false);
        statusMessages.setLineWrap(true);

        this.setLayout(new BorderLayout());

        addComponentsToPanel();
    }

    private void addComponentsToPanel() {

        this.add(scrollPane, BorderLayout.CENTER);
    }

}
