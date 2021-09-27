package com.gridnine.testing;

public abstract class FilterSegmentLevel {
	FilterSegmentLevel() {
		FiltrationBuilder.addFilterSegmentLevel(this);
	}
	
	public abstract boolean ifFilter(Segment segment);
}
