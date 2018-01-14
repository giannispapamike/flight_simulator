package com.flight_simulator.data;

import com.flight_simulator.simulation.Application;
import com.flight_simulator.simulation.Astar;

import java.util.ArrayList;

public class Flight {

    private int flightId, flightStart, takeOffId ,landingId, aircraftType, flightSpeed, flightHeight, flightFuelCapacity;
//    private Astar.Node leftIndex = new Astar.Node(), rightIndex = new Astar.Node();
    private int graphicX, graphicY, leftIndex, rightIndex;
    private float currentX, currentY, currentVelocity, currentHeight, currentFuelCapacity;
    private String flightName, currentState = "In queue for simulation";
    private ArrayList<Astar.Node> path;
    private Aircraft aircraft;
    private boolean printState;
    private String currentAircraftImg;

    Flight(String[] elements) {
        this.flightId = Integer.parseInt(elements[0]);
        this.flightStart = Integer.parseInt(elements[1]);
        this.takeOffId = Integer.parseInt(elements[2]);
        this.landingId = Integer.parseInt(elements[3]);
        this.flightName = elements[4];
        this.aircraftType = Integer.parseInt(elements[5]);
        this.aircraft = new Aircraft(this.aircraftType);
        this.flightSpeed = Integer.parseInt(elements[6]);
        this.flightHeight = Integer.parseInt(elements[7]);
        this.flightFuelCapacity = Integer.parseInt(elements[8]);
    }

    public String aircraftToString() {
        return ( "Take off Airport: " + Application.getData().findAirport(takeOffId).getName() +
        " , Landing Airport: " + Application.getData().findAirport(landingId).getName() +
        " , Flight speed: " + flightSpeed +
        " , Flight Height: " + flightHeight +
        " , Fuel: " + flightFuelCapacity );
    }

    public String toString() {
        return ( "Take off Airport: " + Application.getData().findAirport(takeOffId).getName() +
                " , Landing Airport: " + Application.getData().findAirport(landingId).getName() +
                " , Aircraft type: " + aircraftType +
                " , Flight state: " + currentState );
    }

    public void setCurrentAircraftImg(String img) {
        this.currentAircraftImg = img;
    }

    public String getCurrentAircraftImg() {
        return currentAircraftImg;
    }

    public void setPaintState(boolean state) {
        this.printState = state;
    }

    public boolean isPaintState() {
        return this.printState;
    }

    public void setLeftIndex(int leftIndex) {
        this.leftIndex = leftIndex;
    }

    public int getLeftIndex() {
        return this.leftIndex;
    }

    public void setRightIndex(int rightIndex) {
        this.rightIndex = rightIndex;
    }

    public int getRightIndex() {
        return this.rightIndex;
    }

    public void setCurrentX(float currentX) {
        this.currentX = currentX;
    }

    public void setCurrentY(float currentY) {
        this.currentY = currentY;
    }

    public void setGraphicX(int graphicX) {
        this.graphicX = graphicX;
    }

    public int getGraphicX() {
        return this.graphicX;
    }

    public int getGraphicY() {
        return this.graphicY;
    }

    public void setGraphicY(int graphicY) {
        this.graphicY = graphicY;
    }

    public void setCurrentVelocity(float currentVelocity) {
        this.currentVelocity = currentVelocity;
    }

    public float getCurrentVelocity() { return this.currentVelocity; }

    public void setCurrentHeight(float currentHeight) {
        this.currentHeight = currentHeight;
    }

    public float getCurrentHeight() { return currentHeight; }

    public float getCurrentX() { return this.currentX; }

    public float getCurrentY() { return this.currentY; }

    public void setCurrentFuelCapacity(float currentFuelCapacity) { this.currentFuelCapacity = currentFuelCapacity; }

    public float getCurrentFuelCapacity() { return currentFuelCapacity; }

    public void setCurrentState(String state) { currentState = state; }

    public String getCurrentState() { return currentState; }

    public void setPath(ArrayList<Astar.Node> path) {
        this.path = path;
    }

    public ArrayList<Astar.Node> getPath() {
        return this.path;
    }

    public void printPath() {
        for ( Astar.Node node : this.path ) {
            System.out.println(node.x + " " + node.y + " " + node.height);
        }
    }

    public int getFlightId() {
        return flightId;
    }

    public int getFlightStart() {
        return flightStart;
    }

    public int getTakeOffId() {
        return takeOffId;
    }

    public int getLandingId() {
        return landingId;
    }

    public int getAircraftType() {
        return aircraftType;
    }

    public int getFlightSpeed() {
        return flightSpeed;
    }

    public int getFlightHeight() {
        return flightHeight;
    }

    public int getFlightFuelCapacity() {
        return flightFuelCapacity;
    }

    public int getAircraftMaxSpeed() { return aircraft.getMaxSpeed(); }

    public int getAircraftFuelCapacity() { return aircraft.getFuelCapacity(); }

    public int getAircraftMaxHeight() { return aircraft.getMaxHeight(); }

    public int getAircraftHeightRatio() { return aircraft.getHeightRatio(); }

    public int getAircraftLandingSpeed() { return aircraft.getLandingSpeed(); }

    public int getAircraftFuelConsumption() { return aircraft.getFuelConsumption(); }

    public void printFlight() {
        System.out.print(this.flightId + " ");
        System.out.print(this.flightStart + " ");
        System.out.print(this.takeOffId + " ");
        System.out.print(this.landingId + " ");
        System.out.print(this.flightName + " ");
        System.out.print(this.aircraftType + " ");
        System.out.print(this.flightSpeed + " ");
        System.out.print(this.flightHeight + " ");
        System.out.println(this.flightFuelCapacity);
        aircraft.printAircraft();
    }

    private class Aircraft {
        /*
         * Landing speed represents aircraft's speed while landing or taking off -> knots.
         * Max speed represents maximum speed an aircraft can achieve in flight -> knots.
         * Fuel capacity represents maximum aircraft's fuel capacity -> kilograms.
         * Max height represents the maximum height an aircraft can reach -> feet.
         * Height ratio represents the aircraft's speed for changing height -> feet/minute.
         * Fuel consumption represents how much kg fuel per nm aircraft need -> kilograms/nautical mile.
         */

        /*
         * Type 1: Single Engine Planes.
         * Type 2: Turboprops.
         * Type 3: Jets.
         */

        private int landingSpeed, maxSpeed, fuelCapacity, maxHeight, heightRatio, fuelConsumption;

        Aircraft(int type) {

            switch (type) {
                case 1: landingSpeed = 60;
                        maxSpeed = 110;
                        fuelCapacity = 280;
                        maxHeight = 8000;
                        heightRatio = 700;
                        fuelConsumption = 3;
                        break;
                case 2: landingSpeed = 100;
                        maxSpeed = 220;
                        fuelCapacity = 4200;
                        maxHeight = 16000;
                        heightRatio = 1200;
                        fuelConsumption = 9;
                        break;
                case 3: landingSpeed = 140;
                        maxSpeed = 280;
                        fuelCapacity = 16000;
                        maxHeight = 28000;
                        heightRatio = 2300;
                        fuelConsumption = 15;
                        break;
            }

        }

        public int getLandingSpeed() {
            return landingSpeed;
        }

        public int getMaxSpeed() {
            return maxSpeed;
        }

        public int getFuelCapacity() {
            return fuelCapacity;
        }

        public int getMaxHeight() {
            return maxHeight;
        }

        public int getHeightRatio() {
            return heightRatio;
        }

        public int getFuelConsumption() {
            return fuelConsumption;
        }

        public void printAircraft() {
            System.out.print(landingSpeed + " ");
            System.out.print(maxSpeed + " ");
            System.out.print(fuelCapacity + " ");
            System.out.print(maxHeight + " ");
            System.out.print(heightRatio + " ");
            System.out.println(fuelConsumption + " ");
        }
    }

}
