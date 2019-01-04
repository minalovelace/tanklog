package de.tanklog.model;

import java.util.ArrayList;

public class TanklogYearStatisticsEntry {
	private final String _liter;
	private final String _kilometer;
	private final String _literPerHundredKilometer;
	private final ArrayList<String> _repairs;

	public TanklogYearStatisticsEntry(String liter, String kilometer, String literPerHundredKilometer,
			ArrayList<String> repairs) {
		_liter = liter;
		_kilometer = kilometer;
		_literPerHundredKilometer = literPerHundredKilometer;
		_repairs = repairs;
	}

	public TanklogYearStatisticsEntry(String liter, String kilometer, String literPerHundredKilometer) {
		this(liter, kilometer, literPerHundredKilometer, new ArrayList<String>());
	}

	public String getLiter() {
		return _liter;
	}

	public String getKilometer() {
		return _kilometer;
	}

	public String getLiterPerHundredKilometer() {
		return _literPerHundredKilometer;
	}

	public ArrayList<String> getRepairs() {
		return new ArrayList<String>(_repairs);
	}
}
