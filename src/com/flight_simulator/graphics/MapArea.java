package com.flight_simulator.graphics;

import com.flight_simulator.data.Airport;
import com.flight_simulator.data.Flight;
import com.flight_simulator.data.SimulationData;
import com.flight_simulator.simulation.Application;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MapArea extends JPanel {

    private SimulationData data;
    private int[][] map;
    private ArrayList<Airport> airports;

    public MapArea() {
        data = Application.getData();
        initializeData();
    }

    public void updateData() {
        data = Application.getData();
        initializeData();
        repaint();
    }

    private void initializeData() {
        map = data.getMap();
        airports = data.getAirports();
    }


    public Dimension getPreferredSize() {
        return new Dimension(960,480);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Paint only the first time paint is called.

            paintMap(g);
            paintAirports(g);

            if (data.getActiveFlights() != null) {
                paintFlights(g);  // Update flights from scheduler.
            }
    }

    private void paintMap(Graphics g) {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 60; j++) {
                if (map[i][j] == 0)
                    g.setColor(new Color(0, 0, 255));
                else if (map[i][j] <= 200)
                    g.setColor(new Color(60, 179, 113));
                else if (map[i][j] <= 400)
                    g.setColor(new Color(46, 139, 87));
                else if (map[i][j] <= 700)
                    g.setColor(new Color(34, 139, 34));
                else if (map[i][j] <= 1500)
                    g.setColor(new Color(222, 184, 135));
                else if (map[i][j] <= 3500)
                    g.setColor(new Color(205, 133, 63));
                else
                    g.setColor(new Color(145, 80, 20));

                g.fillRect(j * 16, i * 16, 16, 16);
            }
        }
    }

    private void paintAirports(Graphics g) {
        for (Airport airport : airports)
            if (airport.isState())
                g.drawImage(data.getAirportImg(), airport.getX() * 16 + 4, airport.getY() * 16 + 4, null);
    }

    private void paintFlights(Graphics g) {
        for (Flight flight : data.getActiveFlights()) {
            if ( flight.isPaintState() ) {
                if ( flight.getCurrentAircraftImg() == "small_n" )
                    g.drawImage(data.getSmallAircraftImg()[0], flight.getGraphicX(), flight.getGraphicY(), null);
                else if ( flight.getCurrentAircraftImg() == "small_e" )
                    g.drawImage(data.getSmallAircraftImg()[1], flight.getGraphicX(), flight.getGraphicY(), null);
                else if ( flight.getCurrentAircraftImg() == "small_s" )
                    g.drawImage(data.getSmallAircraftImg()[2], flight.getGraphicX(), flight.getGraphicY(), null);
                else if ( flight.getCurrentAircraftImg() == "small_w" )
                    g.drawImage(data.getSmallAircraftImg()[3], flight.getGraphicX(), flight.getGraphicY(), null);
                else if ( flight.getCurrentAircraftImg() == "middle_n" )
                    g.drawImage(data.getMiddleAircraftImg()[0], flight.getGraphicX(), flight.getGraphicY(), null);
                else if ( flight.getCurrentAircraftImg() == "middle_e" )
                    g.drawImage(data.getMiddleAircraftImg()[1], flight.getGraphicX(), flight.getGraphicY(), null);
                else if ( flight.getCurrentAircraftImg() == "middle_s" )
                    g.drawImage(data.getMiddleAircraftImg()[2], flight.getGraphicX(), flight.getGraphicY(), null);
                else if ( flight.getCurrentAircraftImg() == "middle_w" )
                    g.drawImage(data.getMiddleAircraftImg()[3], flight.getGraphicX(), flight.getGraphicY(), null);
                else if ( flight.getCurrentAircraftImg() == "big_n" )
                    g.drawImage(data.getBigAircraftImg()[0], flight.getGraphicX(), flight.getGraphicY(), null);
                else if ( flight.getCurrentAircraftImg() == "big_e" )
                    g.drawImage(data.getBigAircraftImg()[1], flight.getGraphicX(), flight.getGraphicY(), null);
                else if ( flight.getCurrentAircraftImg() == "big_s" )
                    g.drawImage(data.getBigAircraftImg()[2], flight.getGraphicX(), flight.getGraphicY(), null);
                else if ( flight.getCurrentAircraftImg() == "big_w" )
                    g.drawImage(data.getBigAircraftImg()[3], flight.getGraphicX(), flight.getGraphicY(), null);
            }
        }
    }
}
