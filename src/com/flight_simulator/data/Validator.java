package com.flight_simulator.data;

public class Validator {

    private Flight flight;
    private Airport takeOffAirport, landAirport;

    public boolean valid( Flight flight, Airport takeOff, Airport land) {
        this.flight = flight;
        this.takeOffAirport = takeOff;
        this.landAirport = land;

        if (
                checkAirportStatus(this.takeOffAirport) &&
                checkAirportStatus(this.landAirport) &&
                checkAirMatch(this.takeOffAirport) &&
                checkAirMatch(this.landAirport) &&
                checkSpeed() &&
                checkFuelCapacity() &&
                checkHeight()
                ) return true;
        else return false;
    }

    private boolean checkAirportStatus(Airport airport) {
        if (airport.isState()) return true;
        else return false;
    }

    private boolean checkAirMatch(Airport airport) {

        if (airport.getAircraftType() == 1) {
            if (flight.getAircraftType() == 1) return true;
            else return false;
        } else if (airport.getAircraftType() == 2) {
            if ( (flight.getAircraftType() == 2) || (flight.getAircraftType() == 3) )
                return true;
            else return false;
        } else if (airport.getAircraftType() == 3) return true;
        else return false;

    }

    private boolean checkSpeed() {
        if ( flight.getFlightSpeed() <= flight.getAircraftMaxSpeed() )
            return true;
        else return false;
    }

    private boolean checkFuelCapacity() {
        if ( flight.getFlightFuelCapacity() <= flight.getAircraftFuelCapacity() )
            return true;
        else return false;
    }

    private boolean checkHeight() {
        if ( flight.getFlightHeight() <= flight.getAircraftMaxHeight() )
            return true;
        else return false;
    }
}
