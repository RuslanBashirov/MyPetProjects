package com.gridnine.testing;

import java.util.List;

public class Main{
	public static void main(String[] args) {
		List<Flight> initialFlights = FlightBuilder.createFlights();
		
		FiltrationBuilder fb = new FiltrationBuilder();
		FilterFlightLevel filter0 = FiltrationBuilder.getFilterFlightLevel(0);
		FilterFlightLevel filter1 = FiltrationBuilder.getFilterFlightLevel(1);
		FilterFlightLevel filter2 = FiltrationBuilder.getFilterFlightLevel(2);
		
		List<Flight> filteredFlights0 = FiltrationBuilder.getFilteredFlights(initialFlights, filter0);
		System.out.println("1: " + filteredFlights0); 
		List<Flight> filteredFlights1 = FiltrationBuilder.getFilteredFlights(initialFlights, filter1);
		System.out.println("2: " + filteredFlights1); 
		List<Flight> filteredFlights2 = FiltrationBuilder.getFilteredFlights(initialFlights, filter2);
		System.out.println("3: " + filteredFlights1); 
	}
}