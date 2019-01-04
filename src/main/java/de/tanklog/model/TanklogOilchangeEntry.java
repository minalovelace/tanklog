package de.tanklog.model;

import java.text.NumberFormat;
import java.util.Locale;

public class TanklogOilchangeEntry {
	private final NumberFormat _numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
	private final String _oilchangeAt = "Ölwechsel bei ";
	private final String _km = " km";
	private Integer _kilometer;

	public TanklogOilchangeEntry(String kilometer) {
		String sanitizedKilometer = kilometer.replaceAll("[.,]", "");
		_kilometer = Integer.parseInt(sanitizedKilometer);
	}

	public Integer getOilchangeKilometer() {
		return _kilometer;
	}

	public String getFormattedOilchangeKilometer() {
		return _oilchangeAt + _numberFormat.format(_kilometer) + _km;
	}
}
