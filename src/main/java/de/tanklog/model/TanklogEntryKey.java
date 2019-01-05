package de.tanklog.model;

import java.time.LocalDate;

public class TanklogEntryKey implements Comparable<TanklogEntryKey> {
	private final LocalDate _localDate;
	private final Integer _kilometer;

	public TanklogEntryKey(LocalDate localDate, Integer kilometer) {
		_localDate = localDate;
		_kilometer = kilometer;
	}

	public LocalDate getLocalDate() {
		return _localDate;
	}

	@Override
	public int compareTo(TanklogEntryKey other) {
		int localDateComparison = this._localDate.compareTo(other._localDate);
		if (localDateComparison == 0)
			return this._kilometer.compareTo(other._kilometer);

		return localDateComparison;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof TanklogEntryKey) {
			return this.compareTo((TanklogEntryKey) obj) == 0;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + _localDate.hashCode();
		hash = hash * 31 + _kilometer.hashCode();
		return hash;
	}

	public Integer getKilometer() {
		return _kilometer;
	}
}
