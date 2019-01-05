package de.tanklog.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class TanklogEntry {
	private final NumberFormat _numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
	private final DecimalFormat _decimalFormat = new DecimalFormat("####0.00");
	private final BigDecimal _price;
	private final BigDecimal _liter;
	private final Integer _kilometer;

	private Integer _drivenKilometer;
	private Integer _oilKilometer;

	public TanklogEntry(String price, String liter, Integer kilometer) {
		_price = new BigDecimal(price.replaceAll(",", "."));
		_liter = new BigDecimal(liter.replaceAll(",", "."));
		_kilometer = kilometer;
	}

	public BigDecimal getPrice() {
		return _price;
	}

	public String getFormattedPrice() {
		return _decimalFormat.format(_price);
	}

	public BigDecimal getLiter() {
		return _liter;
	}

	public String getFormattedLiter() {
		return _decimalFormat.format(_liter);
	}

	public Integer getKilometer() {
		return _kilometer;
	}

	public String getFormattedKilometer() {
		return _numberFormat.format(_kilometer);
	}

	public Integer getDrivenKilometer() {
		return _drivenKilometer;
	}

	public String getFormattedDrivenKilometer() {
		return _numberFormat.format(_drivenKilometer);
	}

	public void setDrivenKilometer(Integer _drivenKilometer) {
		this._drivenKilometer = _drivenKilometer;
	}

	public Integer getOilKilometer() {
		return _oilKilometer;
	}

	public String getFormattedOilKilometer() {
		return _numberFormat.format(_oilKilometer);
	}

	public void setOilKilometer(Integer _oilKilometer) {
		this._oilKilometer = _oilKilometer;
	}
}
