package com.flight_simulator.data;

import com.flight_simulator.simulation.Application;
import com.flight_simulator.simulation.Astar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SimulationData {

    private String input = "default";
    private Scanner scanner = null;

    private ArrayList<Airport> airports = new ArrayList<>();
    private ArrayList<Flight> flights = new ArrayList<>(), activeFlights = new ArrayList<>(), invalidFlights = new ArrayList<>();
    private int[][] map = new int[30][60];

    private BufferedImage airportImg;
    private BufferedImage[] bigAircraftImg, middleAircraftImg, smallAircraftImg;

    public boolean error = false;

    public SimulationData() {
        readInput();
    }

    public SimulationData(String input) {
        this.input = input;
        readInput();
    }

    private void readInput() {
        if ( readMap() || readAirports() || readFlights() || readSimulationImages() )
            error = true;

        // TODO if error inform timer and stop the simulation.
    }

    // Initialize all needed simulation Images from assets/SimulationIcons directory.
    private boolean readSimulationImages() {

        bigAircraftImg = new BufferedImage[4];
        middleAircraftImg = new BufferedImage[4];
        smallAircraftImg = new BufferedImage[4];

        try {
            // Read Airport Image.
            airportImg = ImageIO.read(new File("assets/SimulationIcons/airport.png"));

            // Big airplane images
            bigAircraftImg[0] = ImageIO.read(new File("assets/SimulationIcons/big_n.png"));
            bigAircraftImg[1] = ImageIO.read(new File("assets/SimulationIcons/big_e.png"));
            bigAircraftImg[2] = ImageIO.read(new File("assets/SimulationIcons/big_s.png"));
            bigAircraftImg[3] = ImageIO.read(new File("assets/SimulationIcons/big_w.png"));

            // Middle airplane images
            middleAircraftImg[0] = ImageIO.read(new File("assets/SimulationIcons/middle_n.png"));
            middleAircraftImg[1] = ImageIO.read(new File("assets/SimulationIcons/middle_e.png"));
            middleAircraftImg[2] = ImageIO.read(new File("assets/SimulationIcons/middle_s.png"));
            middleAircraftImg[3] = ImageIO.read(new File("assets/SimulationIcons/middle_w.png"));

            // Small airplane images
            smallAircraftImg[0] = ImageIO.read(new File("assets/SimulationIcons/small_n.png"));
            smallAircraftImg[1] = ImageIO.read(new File("assets/SimulationIcons/small_e.png"));
            smallAircraftImg[2] = ImageIO.read(new File("assets/SimulationIcons/small_s.png"));
            smallAircraftImg[3] = ImageIO.read(new File("assets/SimulationIcons/small_w.png"));
        } catch (IOException e) {
            System.err.println("Read Simulation Images caught Exception: " + e.getMessage());
            return true;
        }

        return false;

    }

    // Read map from specified world_ID file.
    private boolean readMap() {
        String inputWorld = "assets/examples/world_" + input + ".txt";
        int height = 30, width = 60;
        // Open World file.
        try {
            scanner = new Scanner(new File(inputWorld)).useDelimiter("\\s*\\D\\s*");
        }
        catch(FileNotFoundException e) {
            System.err.println("Read Map caught FileNotFoundException: " + e.getMessage());
            return true;
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (scanner.hasNext())
                    map[i][j] = scanner.nextInt();
                else return true;
            }
        }

        scanner.close();
        return false;
    }

    // Read airports from specified airports_ID file.
    private boolean readAirports() {

        String inputAirports = "assets/examples/airports_" + input + ".txt";
        // Open Airports file.
        try {
            scanner = new Scanner(new File(inputAirports)).useDelimiter("\\r\\n");
        }
        catch(FileNotFoundException e) {
            System.err.println("Caught FileNotFoundException: " + e.getMessage());
            return true;
        }

        // Read every airport and add it to airports array.
        while(scanner.hasNext()) {
            airports.add(new Airport( scanner.next().split(",") ));
        }

        scanner.close();
        return false;

    }

    // Read flights from specified flights_ID file.
    private boolean readFlights() {
        String inputFlights = "assets/examples/flights_" + input + ".txt";
        Validator validator = new Validator();
        // Open Flights file.
        try {
            scanner = new Scanner(new File(inputFlights)).useDelimiter("\\r\\n");
        }
        catch(FileNotFoundException e) {
            System.err.println("Read Flights caught FileNotFoundException: " + e.getMessage());
            return true;
        }

        while(scanner.hasNext()) {
            Flight flight = new Flight(scanner.next().split(","));
            int takeOffOrientation, landOrientation;

            // Check if flight is valid for simulation, precompute it's path and add it to Active Flights array.
            // Else add it to Invalid Flights Array.
            if ( validator.valid(flight, findAirport(flight.getTakeOffId()), findAirport(flight.getLandingId()) ) ) {

                takeOffOrientation = findAirport( flight.getTakeOffId() ).getOrientation();
                landOrientation = findAirport( flight.getLandingId() ).getOrientation();

                flight.setPath( new Astar( findAirport( flight.getTakeOffId() ).getX(),
                                           findAirport( flight.getTakeOffId() ).getY(),
                                           findAirport( flight.getLandingId() ).getX(),
                                           findAirport( flight.getLandingId() ).getY(),
                                           map,
                                           flight,
                                           takeOffOrientation,
                                           landOrientation
                                         ).getPath() );
                flights.add(flight);
            } else {
                invalidFlights.add(flight);
            }
        }
        return false;
    }

    // This method searches all airports and returns the airport with the specified id.
    public Airport findAirport(int id) {
        for ( Airport airport : airports ) {
            if (airport.getId() == id)
                return airport;
        }
        // This point should not be reached at any point.
        System.out.println("Find Airport: The specified airport does not exist. Returning null instead.");
        return null;
    }

    // Getters Section starts.

    public BufferedImage getAirportImg() { return airportImg; }

    public BufferedImage[] getBigAircraftImg() { return bigAircraftImg; }

    public BufferedImage[] getMiddleAircraftImg() {
        return middleAircraftImg;
    }

    public BufferedImage[] getSmallAircraftImg() {
        return smallAircraftImg;
    }

    public int[][] getMap() {
        return map;
    }

    public ArrayList<Airport> getAirports() {
        return airports;
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public ArrayList<Flight> getInvalidFlights() { return invalidFlights; }

    public ArrayList<Flight> getActiveFlights() {
        return activeFlights;
    }

    // Setters Section starts.

    public void setActiveFlights(ArrayList<Flight> activeFlights) {
        this.activeFlights = activeFlights;
    }


    // Debugging Section starts.

    public void printAirports() {
        for( Airport airport : airports ) {
            airport.printElements();
        }
    }

    public void printMap() {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 60; j++) {
                System.out.print( map[i][j] + " " );
            }
            System.out.println();
        }
    }

    public void printFlights() {
        for (Flight flight : flights) {
            flight.printFlight();
        }
    }

    public void printFlightsPaths() {
        for (Flight flight : flights) {
            flight.printPath();
        }
    }
}
