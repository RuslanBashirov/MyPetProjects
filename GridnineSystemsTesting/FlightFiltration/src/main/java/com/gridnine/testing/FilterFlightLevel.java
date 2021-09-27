package com.gridnine.testing;

public abstract class FilterFlightLevel {
	FilterFlightLevel() {
		FiltrationBuilder.addFilterFlightLevel(this);
	}
	
	public abstract boolean ifFilter(Flight flight);
}
