package de.tanklog.model;

import java.util.TreeMap;

public class TanklogYearStatistics {
	private final TreeMap<Integer, TanklogYearStatisticsEntry> _collectedStatisticsForMonth = new TreeMap<>();
	private final TanklogYearStatisticsEntry _cumulatedYear;

	public TanklogYearStatistics(String liter, String kilometer, String literPerHundredKilometer) {
		_cumulatedYear = new TanklogYearStatisticsEntry(liter, kilometer, literPerHundredKilometer);
	}

	public void setStatisticsForMonths(TreeMap<Integer, TanklogYearStatisticsEntry> entries) {
		_collectedStatisticsForMonth.putAll(entries);
	}

	public TanklogYearStatisticsEntry getStatisticsForMonth(Integer month) {
		return _collectedStatisticsForMonth.get(month);
	}

	public TanklogYearStatisticsEntry getCumulatedYearEntry() {
		return _cumulatedYear;
	}
}
