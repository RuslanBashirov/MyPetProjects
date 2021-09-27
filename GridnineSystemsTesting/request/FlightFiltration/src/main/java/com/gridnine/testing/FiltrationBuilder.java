package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FiltrationBuilder {
	
	FiltrationBuilder() {
		new Filter0();
		new Filter1();
		new Filter2();
	}
	
	private static List<FilterSegmentLevel> filtersSegmentLevel = new ArrayList<FilterSegmentLevel>();
	private static List<FilterFlightLevel> filtersFlightLevel = new ArrayList<FilterFlightLevel>();

	public static void addFilterSegmentLevel(FilterSegmentLevel filter) {
		filtersSegmentLevel.add(filter);
	}

	public static void addFilterFlightLevel(FilterFlightLevel filter) {
		filtersFlightLevel.add(filter);
	}

	public static FilterSegmentLevel getFilterSegmentLevel(int i) {
		return filtersSegmentLevel.get(i);
	}

	public static FilterFlightLevel getFilterFlightLevel(int i) {
		return filtersFlightLevel.get(i);
	}

	//this method is not used for tests from problem's description
	public static List<Flight> getFilteredFlights(List<Flight> flights, FilterSegmentLevel filter) {
		List<Flight> filteredFlights = new ArrayList<Flight>();

		for (Flight flight : flights) {
			List<Segment> filteredSegments = new ArrayList<Segment>();
			for (Segment segment : flight.getSegments()) {
				if (!filter.ifFilter(segment)) {
					filteredSegments.add(segment);
				}
			}
			if (filteredSegments.size() > 0) {
				filteredFlights.add(new Flight(filteredSegments));
			}
		}

		return filteredFlights;
	}

	public static List<Flight> getFilteredFlights(List<Flight> flights, FilterFlightLevel filter) {
		List<Flight> filteredFlights = new ArrayList<Flight>();

		for (Flight flight : flights) {
			if (!filter.ifFilter(flight)) {
				filteredFlights.add(flight);
			}
		}

		return filteredFlights;
	}
}

class Filter0 extends FilterFlightLevel {

	public boolean ifFilter(Flight flight) {
		List<Segment> segments = flight.getSegments();
		LocalDateTime firstDepDt = segments.get(0).getDepartureDate();

		if ((segments.size() > 0) && (!firstDepDt.isBefore(LocalDateTime.now()))) {
			return false;
		}

		return true;
	}
}

class Filter1 extends FilterFlightLevel {
	
	public boolean ifFilter(Flight flight) {
		List<Segment> segments = flight.getSegments();

		for (Segment segment : segments) {
			LocalDateTime dep = segment.getDepartureDate();
			LocalDateTime arr = segment.getArrivalDate();
			if (arr.isBefore(dep)) {
				return true;
			}
		}

		return false;
	}
}

class Filter2 extends FilterFlightLevel {

	public boolean ifFilter(Flight flight) {
		List<Segment> segments = flight.getSegments(); 
		
		for (int i = 0; i < segments.size() - 1; i+=2) {
			final LocalDateTime arr0 = segments.get(i).getDepartureDate();
			final LocalDateTime dep1 = segments.get(i + 1).getArrivalDate();
			if (dep1.isAfter(arr0.plusHours(2))) {
				return true;
			}
		}
		
		return false;
	}
}
