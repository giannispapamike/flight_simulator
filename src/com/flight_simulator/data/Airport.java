package com.flight_simulator.data;

public class Airport {
    /*
     * Id: unique airport's id.
     * (x,y): airport's coordinates.
     * Name: Name of the airport.
     * Orientation: Airport's orientation. 1 -> North, 2 -> East, 3 -> South, 4 -> West.
     * Type: Aircrafts that are acceptable by this airport. 1 -> Single Engine Planes (SEP),
     * 2 -> Turboprops & Jets, 3 -> All types.
     * State: Airport's state. true -> Open, false -> Closed.
     */
    private int id, x, y, orientation, aircraftType;
    private String name;
    private boolean state;

    Airport(String[] elements) {
        try {
            this.id = Integer.parseInt(elements[0]);
            this.y = Integer.parseInt(elements[1]);
            this.x = Integer.parseInt(elements[2]);
            this.name = elements[3];
            this.orientation = Integer.parseInt(elements[4]);
            this.aircraftType = Integer.parseInt(elements[5]);
            this.state = (elements[6].equals("1") ) ? true : false;
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }

    public String toString() {
        return ( "Airport id: " + id + " , Airport coordinates: (" + y + "," + x + ") , Airport name: " + name + " , Airport orientation: " + orientation + " , Airport's aircraft type: " + aircraftType + " , Airport open status: " + state );
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOrientation() {
        return orientation;
    }

    public int getAircraftType() {
        return aircraftType;
    }

    public String getName() {
        return name;
    }

    public boolean isState() {
        return state;
    }

    public void printElements() {
        System.out.print(id + " ");
        System.out.print(x + " ");
        System.out.print(y + " ");
        System.out.print(name + " ");
        System.out.print(orientation + " ");
        System.out.print(aircraftType + " ");
        System.out.println(state);
    }
}
