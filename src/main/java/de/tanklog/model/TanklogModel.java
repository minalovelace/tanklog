package de.tanklog.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class TanklogModel {
	private final String _noEntry = "kein Eintrag";
	private final String _wook = "Wasser, Öl etc. in Ordnung";
	private final ArrayList<String> _notes = new ArrayList<>();
	private final Map<TanklogEntryKey, TanklogEntry> _entries = new HashMap<>();
	private final Map<LocalDate, String> _garageEntries = new HashMap<>();
	private final Map<LocalDate, TanklogOilchangeEntry> _oilchangeEntries = new HashMap<>();
	private final Map<LocalDate, String> _wookEntries = new HashMap<>();
	private final Map<Integer, TanklogYearStatistics> _tanklogYearStatistics = new HashMap<>();
	private final NumberFormat _numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
	private final DecimalFormat _decimalFormat = new DecimalFormat("####0.00");

	private String _carname = _noEntry;
	private String _numberplate = _noEntry;
	private String _fullyladenAirpressure = _noEntry;
	private String _halfladenAirpressure = _noEntry;
	private String _sparewheelAirpressure = _noEntry;
	private String _oilchange = _noEntry;
	private String _oilchangeWithFilter = _noEntry;
	private TanklogPurchaseDetail _purchaseDetail = new TanklogPurchaseDetail(null, null);

	public String getCarname() {
		return _carname;
	}

	public String getNumberplate() {
		return _numberplate;
	}

	public String getFullyladenAirpressure() {
		return _fullyladenAirpressure;
	}

	public String getHalfladenAirpressure() {
		return _halfladenAirpressure;
	}

	public String getSparewheelAirpressure() {
		return _sparewheelAirpressure;
	}

	public HashMap<LocalDate, TanklogOilchangeEntry> getOilchangeEntries() {
		return new HashMap<LocalDate, TanklogOilchangeEntry>(_oilchangeEntries);
	}

	public String getCumulatedKilometerPerMonth(String year, String month) {
		TanklogYearStatisticsEntry statisticsforMonth = getStatisticsForMonth(year, month);
		return statisticsforMonth.getKilometer();
	}

	public String getCumulatedLiterPerMonth(String year, String month) {
		TanklogYearStatisticsEntry statisticsforMonth = getStatisticsForMonth(year, month);
		return statisticsforMonth.getLiter();
	}

	public String getConsumptionForMonth(String year, String month) {
		TanklogYearStatisticsEntry statisticsforMonth = getStatisticsForMonth(year, month);
		return statisticsforMonth.getLiterPerHundredKilometer();
	}

	public ArrayList<String> getRepairsForMonth(String year, String month) {
		TanklogYearStatisticsEntry statisticsforMonth = getStatisticsForMonth(year, month);
		return statisticsforMonth.getRepairs();
	}

	public TanklogYearStatistics getStatisticsForYear(String year) {
		Integer intYear = Integer.parseInt(year);
		TanklogYearStatistics result = _tanklogYearStatistics.get(intYear);
		return result;
	}

	public String getOilchangeWithoutFilter() {
		return _oilchange;
	}

	public String getOilchangeWithFilter() {
		return _oilchangeWithFilter;
	}

	public TanklogPurchaseDetail getPurchaseDetail() {
		return _purchaseDetail;
	}

	public ArrayList<String> getNotes() {
		return new ArrayList<String>(_notes);
	}

	public ArrayList<String> getUniqueDescendingYears() {
		ArrayList<Integer> resultAsInt = new ArrayList<>();

		List<Integer> entries = _entries.keySet().stream().map(k -> k.getLocalDate().getYear())
				.collect(Collectors.toList());
		List<Integer> garageEntries = _garageEntries.keySet().stream().map(k -> k.getYear())
				.collect(Collectors.toList());
		List<Integer> oilchangeEntries = _oilchangeEntries.keySet().stream().map(k -> k.getYear())
				.collect(Collectors.toList());

		resultAsInt.addAll(entries);
		resultAsInt.addAll(garageEntries);
		resultAsInt.addAll(oilchangeEntries);

		ArrayList<String> result = resultAsInt.stream().distinct().sorted(Comparator.reverseOrder())
				.map(String::valueOf).collect(Collectors.toCollection(ArrayList::new));

		return result;
	}

	public ArrayList<String> getUniqueDescendingMonthsFor(String year) {
		ArrayList<Integer> resultAsInt = new ArrayList<>();

		int yearAsInt = Integer.parseInt(year);
		List<Integer> entries = _entries.keySet().stream().filter(k -> k.getLocalDate().getYear() == yearAsInt)
				.map(k -> k.getLocalDate().getMonthValue()).collect(Collectors.toList());
		List<Integer> garageEntries = _garageEntries.keySet().stream().filter(k -> k.getYear() == yearAsInt)
				.map(k -> k.getMonthValue()).collect(Collectors.toList());
		List<Integer> oilchangeEntries = _oilchangeEntries.keySet().stream().filter(k -> k.getYear() == yearAsInt)
				.map(k -> k.getMonthValue()).collect(Collectors.toList());

		resultAsInt.addAll(entries);
		resultAsInt.addAll(garageEntries);
		resultAsInt.addAll(oilchangeEntries);

		ArrayList<String> result = resultAsInt.stream().distinct().sorted(Comparator.reverseOrder())
				.map(String::valueOf).collect(Collectors.toCollection(ArrayList::new));

		return result;
	}

	public HashMap<TanklogEntryKey, TanklogEntry> getEntriesForMonth(String year, String month) {
		int intYear = Integer.parseInt(year);
		int intMonth = Integer.parseInt(month);
		HashMap<TanklogEntryKey, TanklogEntry> result = new HashMap<>();
		for (Entry<TanklogEntryKey, TanklogEntry> entry : _entries.entrySet()) {
			LocalDate actualLocalDate = entry.getKey().getLocalDate();
			boolean isYear = actualLocalDate.getYear() == intYear;
			boolean isMonth = actualLocalDate.getMonthValue() == intMonth;
			if (isYear & isMonth) {
				result.put(entry.getKey(), entry.getValue());
			}
		}

		return result;
	}

	public HashMap<Integer, String> getGarageEntriesForMonth(String year, String month) {
		return getEntriesForMonthImpl(year, month, _garageEntries.entrySet());
	}

	public HashMap<Integer, TanklogOilchangeEntry> getOilchangeEntriesForMonth(String year, String month) {
		return getEntriesForMonthImpl(year, month, _oilchangeEntries.entrySet());
	}

	public HashMap<Integer, String> getWookEntriesForMonth(String year, String month) {
		return getEntriesForMonthImpl(year, month, _wookEntries.entrySet());
	}

	private <T> HashMap<Integer, T> getEntriesForMonthImpl(String year, String month,
			Set<Entry<LocalDate, T>> entrySet) {
		int intYear = Integer.parseInt(year);
		int intMonth = Integer.parseInt(month);
		HashMap<Integer, T> result = new HashMap<>();
		for (Entry<LocalDate, T> entry : entrySet) {
			LocalDate actualLocalDate = entry.getKey();
			boolean isYear = actualLocalDate.getYear() == intYear;
			boolean isMonth = actualLocalDate.getMonthValue() == intMonth;
			if (isYear & isMonth) {
				result.put(actualLocalDate.getDayOfMonth(), entry.getValue());
			}
		}

		return result;
	}

	private TanklogYearStatisticsEntry getStatisticsForMonth(String year, String month) {
		Integer intYear = Integer.parseInt(year);
		TanklogYearStatistics yearStatistics = _tanklogYearStatistics.get(intYear);
		Integer intMonth = Integer.parseInt(month);
		TanklogYearStatisticsEntry statisticsforMonth = yearStatistics.getStatisticsForMonth(intMonth);
		return statisticsforMonth;
	}

	public class TanklogModelFactory {
		private TanklogModel _tanklogModel = new TanklogModel();

		public TanklogModel build() {
			calculateOilKilometer();
			calculateCumulatedValues();
			return _tanklogModel;
		}

		private void calculateCumulatedValues() {
			ArrayList<String> years = _tanklogModel.getUniqueDescendingYears();
			for (String year : years) {
				Integer intYear = Integer.parseInt(year);
				calculateStatisticsForYear(intYear);
			}
		}

		private void calculateStatisticsForYear(Integer intYear) {
			String year = String.valueOf(intYear);
			ArrayList<String> months = _tanklogModel.getUniqueDescendingMonthsFor(year);
			Integer kilometerForYear = 0;
			BigDecimal literForYear = BigDecimal.ZERO;
			TreeMap<Integer, TanklogYearStatisticsEntry> entries = new TreeMap<>();
			for (String month : months) {
				Integer intMonth = Integer.valueOf(month);
				Integer kilometerForMonth = calculateCumulatedKilometerForMonth(year, month);
				BigDecimal literForMonth = calculateCumulatedLiterForMonth(year, month);
				BigDecimal consumptionForMonth = calculateCumulatedConsumption(literForMonth, kilometerForMonth);
				ArrayList<String> repairs = getFormattedRepairsForMonth(year, month);

				String consumptionForMonthFormatted = formatCumulatedConsumption(consumptionForMonth);

				TanklogYearStatisticsEntry entry = new TanklogYearStatisticsEntry(_decimalFormat.format(literForMonth),
						_numberFormat.format(kilometerForMonth), consumptionForMonthFormatted, repairs);

				entries.put(intMonth, entry);
				kilometerForYear += kilometerForMonth;
				literForYear = literForYear.add(literForMonth);
			}

			BigDecimal consumptionForYear = calculateCumulatedConsumption(literForYear, kilometerForYear);
			TanklogYearStatistics statistics = new TanklogYearStatistics(_decimalFormat.format(literForYear),
					_numberFormat.format(kilometerForYear), formatCumulatedConsumption(consumptionForYear));

			statistics.setStatisticsForMonths(entries);
			_tanklogModel._tanklogYearStatistics.put(intYear, statistics);
		}

		private String formatCumulatedConsumption(BigDecimal consumptionForMonth) {
			if (consumptionForMonth != null)
				return _decimalFormat.format(consumptionForMonth);

			return _decimalFormat.format(BigDecimal.ZERO);
		}

		private BigDecimal calculateCumulatedLiterForMonth(String year, String month) {
			HashMap<TanklogEntryKey, TanklogEntry> entriesForMonth = _tanklogModel.getEntriesForMonth(year, month);
			BigDecimal cumulatedLiter = BigDecimal.ZERO;
			for (TanklogEntry entry : entriesForMonth.values()) {
				cumulatedLiter = cumulatedLiter.add(entry.getLiter());
			}

			return cumulatedLiter;
		}

		private Integer calculateCumulatedKilometerForMonth(String year, String month) {
			HashMap<TanklogEntryKey, TanklogEntry> entriesForMonth = _tanklogModel.getEntriesForMonth(year, month);
			Integer cumulatedKilometer = entriesForMonth.values().stream().filter(e -> e.getDrivenKilometer() != null)
					.mapToInt(e -> e.getDrivenKilometer()).sum();
			return cumulatedKilometer;
		}

		private BigDecimal calculateCumulatedConsumption(BigDecimal cumulatedLiter, Integer cumulatedKilometer) {
			if (cumulatedKilometer == 0) {
				return null;
			}

			BigDecimal result = cumulatedLiter.multiply(new BigDecimal(100)).divide(new BigDecimal(cumulatedKilometer),
					2, RoundingMode.HALF_EVEN);
			return result;
		}

		private ArrayList<String> getFormattedRepairsForMonth(String year, String month) {
			HashMap<Integer, String> garageEntries = _tanklogModel.getGarageEntriesForMonth(year, month);
			SortedMap<Integer, String> sortedGarageEntries = new TreeMap<>(garageEntries);
			HashMap<Integer, TanklogOilchangeEntry> oilchangeEntries = _tanklogModel.getOilchangeEntriesForMonth(year,
					month);
			SortedMap<Integer, TanklogOilchangeEntry> sortedOilchangeEntries = new TreeMap<>(oilchangeEntries);
			ArrayList<String> result = new ArrayList<>();
			for (Entry<Integer, String> garageEntry : sortedGarageEntries.entrySet()) {
				if (sortedOilchangeEntries.isEmpty()) {
					result.add(getFormattedRepairEntry(garageEntry.getKey(), garageEntry.getValue()));
					continue;
				}

				Integer firstOilchangeKey = sortedOilchangeEntries.firstKey();
				if (garageEntry.getKey() < firstOilchangeKey) {
					result.add(getFormattedRepairEntry(garageEntry.getKey(), garageEntry.getValue()));
				}

				if (garageEntry.getKey() == firstOilchangeKey) {
					result.add(getFormattedRepairEntry(garageEntry.getKey(), garageEntry.getValue()));
					result.add(getFormattedRepairEntry(firstOilchangeKey,
							sortedOilchangeEntries.remove(firstOilchangeKey).getFormattedOilchangeKilometer()));
				}
			}

			for (Entry<Integer, TanklogOilchangeEntry> oilchangeEntry : sortedOilchangeEntries.entrySet()) {
				result.add(getFormattedRepairEntry(oilchangeEntry.getKey(),
						oilchangeEntry.getValue().getFormattedOilchangeKilometer()));
			}

			return result;
		}

		private String getFormattedRepairEntry(Integer day, String description) {
			String result = String.valueOf(day) + ". " + description;
			return result;
		}

		private void calculateOilKilometer() {
			String kilometerAtPurchase = _tanklogModel._purchaseDetail.getKilometer();
			LocalDate purchaseDate = _tanklogModel._purchaseDetail.getPurchaseDate();

			if (kilometerAtPurchase == null || purchaseDate == null) {
				return;
			}

			SortedMap<TanklogEntryKey, Integer> sortedKilometer = new TreeMap<>();
			for (Entry<TanklogEntryKey, TanklogEntry> entry : _tanklogModel._entries.entrySet()) {
				sortedKilometer.put(entry.getKey(), entry.getValue().getKilometer());
			}

			SortedMap<LocalDate, Integer> sortedOilchangeEntries = new TreeMap<>();
			for (Entry<LocalDate, TanklogOilchangeEntry> entry : _tanklogModel._oilchangeEntries.entrySet()) {
				sortedOilchangeEntries.put(entry.getKey(), entry.getValue().getOilchangeKilometer());
			}

			Integer delta = 0;
			Integer actualOilKilometer = 0;
			LocalDate previousDate = purchaseDate;
			Integer previousKilometer = Integer.valueOf(kilometerAtPurchase.replaceAll("[.,]", ""));
			LocalDate nextDate;
			Integer nextKilometer;
			for (Entry<TanklogEntryKey, Integer> entry : sortedKilometer.entrySet()) {
				nextDate = entry.getKey().getLocalDate();
				nextKilometer = entry.getValue();
				TanklogEntry tanklogEntry = _tanklogModel._entries.get(entry.getKey());
				tanklogEntry.setDrivenKilometer(nextKilometer - previousKilometer);

				for (LocalDate oilchangeEntryDate : sortedOilchangeEntries.keySet()) {
					Boolean containsOilchangeEntryDate = !oilchangeEntryDate.isBefore(previousDate)
							&& oilchangeEntryDate.isBefore(nextDate);

					if (containsOilchangeEntryDate) {
						previousKilometer = sortedOilchangeEntries.get(oilchangeEntryDate);
						actualOilKilometer = 0;
					}
				}

				delta = nextKilometer - previousKilometer;
				actualOilKilometer += delta;
				tanklogEntry.setOilKilometer(actualOilKilometer);
				previousDate = nextDate;
				previousKilometer = nextKilometer;
			}
		}

		public TanklogModelFactory setCarname(String carname) {
			_tanklogModel._carname = carname;
			return this;
		}

		public TanklogModelFactory setFullyladenAirpressure(String airpressure) {
			_tanklogModel._fullyladenAirpressure = airpressure;
			return this;
		}

		public TanklogModelFactory setHalfladenAirpressure(String airpressure) {
			_tanklogModel._halfladenAirpressure = airpressure;
			return this;
		}

		public TanklogModelFactory setSparewheelAirpressure(String airpressure) {
			_tanklogModel._sparewheelAirpressure = airpressure;
			return this;
		}

		public TanklogModelFactory setOilchange(String liter) {
			_tanklogModel._oilchange = liter;
			return this;
		}

		public TanklogModelFactory setOilchangeWithFilter(String liter) {
			_tanklogModel._oilchangeWithFilter = liter;
			return this;
		}

		public TanklogModelFactory setNumberplate(String numberplate) {
			_tanklogModel._numberplate = numberplate;
			return this;
		}

		public TanklogModelFactory setEntry(String date, String price, String liter, String kilometer) {
			LocalDate parsedDate = parseDate(date);
			String sanitizedKilometer = kilometer.replaceAll("[.,]", "");
			Integer intKilometer = Integer.parseInt(sanitizedKilometer);
			TanklogEntryKey tanklogEntryKey = new TanklogEntryKey(parsedDate, intKilometer);
			TanklogEntry tanklogEntry = new TanklogEntry(price, liter, intKilometer);
			_tanklogModel._entries.put(tanklogEntryKey, tanklogEntry);
			return this;
		}

		public TanklogModelFactory setGarageEntry(String date, String content) {
			LocalDate parsedDate = parseDate(date);
			_tanklogModel._garageEntries.put(parsedDate, content);
			return this;
		}

		public TanklogModelFactory setOilchangeEntry(String date, String kilometer) {
			LocalDate parsedDate = parseDate(date);
			TanklogOilchangeEntry tanklogOilchangeEntry = new TanklogOilchangeEntry(kilometer);
			_tanklogModel._oilchangeEntries.put(parsedDate, tanklogOilchangeEntry);
			return this;
		}

		public TanklogModelFactory setPurchaseDetail(String date, String kilometer) {
			LocalDate parsedDate = parseDate(date);
			_tanklogModel._purchaseDetail = new TanklogPurchaseDetail(parsedDate, kilometer);
			return this;
		}

		public TanklogModelFactory setNote(String note) {
			_tanklogModel._notes.add(note);
			return this;
		}

		public TanklogModelFactory setWook(String date) {
			LocalDate parsedDate = parseDate(date);
			_tanklogModel._wookEntries.put(parsedDate, _wook);
			return this;
		}

		private LocalDate parseDate(String date) {
			if (date.contains("/")) {
				String[] split = date.split("/");
				if (split.length == 3) {
					int year = Integer.parseInt(split[2]);
					int month = Integer.parseInt(split[0]);
					int dayOfMonth = Integer.parseInt(split[1]);
					return LocalDate.of(year, month, dayOfMonth);
				}
			}

			if (date.contains(".")) {
				String[] split = date.split(".");
				if (split.length == 3) {
					int year = Integer.parseInt(split[2]);
					int month = Integer.parseInt(split[1]);
					int dayOfMonth = Integer.parseInt(split[0]);
					return LocalDate.of(year, month, dayOfMonth);
				}
			}

			if (date.contains("-")) {
				String[] split = date.split("-");
				if (split.length == 3) {
					int year = Integer.parseInt(split[0]);
					int month = Integer.parseInt(split[1]);
					int dayOfMonth = Integer.parseInt(split[2]);
					return LocalDate.of(year, month, dayOfMonth);
				}
			}

			throw new IllegalArgumentException("The given date is not valid: '" + date + "'.");
		}
	}
}
