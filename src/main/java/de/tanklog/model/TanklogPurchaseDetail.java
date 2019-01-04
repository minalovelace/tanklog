package de.tanklog.model;

import java.time.LocalDate;

public class TanklogPurchaseDetail {
	private final LocalDate _purchaseDate;
	private final String _kilometer;

	public TanklogPurchaseDetail(LocalDate purchaseDate, String kilometer) {
		_purchaseDate = purchaseDate;
		_kilometer = kilometer;
	}

	public LocalDate getPurchaseDate() {
		return _purchaseDate;
	}

	public String getKilometer() {
		return _kilometer;
	}
}
