package de.tanklog.writer.latex;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import de.tanklog.model.TanklogEntry;
import de.tanklog.model.TanklogEntryKey;
import de.tanklog.model.TanklogModel;
import de.tanklog.model.TanklogOilchangeEntry;
import de.tanklog.model.TanklogPurchaseDetail;
import de.tanklog.model.TanklogYearStatistics;
import de.tanklog.model.TanklogYearStatisticsEntry;

public class TanklogWriterLatexDocument {
	public String writeDocument(TanklogModel model) {
		StringBuilder sb = new StringBuilder();
		writeDocumentHeader(sb);
		writeTitle(sb, model);
		writeYears(sb, model);
		writeImportantData(sb, model);
		writeDocumentFooter(sb);

		return sb.toString();
	}

	private void writeYears(StringBuilder sb, TanklogModel model) {
		ArrayList<String> descendingYears = model.getUniqueDescendingYears();
		for (String year : descendingYears) {
			writeYear(sb, model, year);
		}
	}

	private void writeYear(StringBuilder sb, TanklogModel model, String year) {
		sb.append("\\subsection*{");
		sb.append(year);
		sb.append("}");
		sb.append("\n");
		sb.append("\n");

		ArrayList<String> descendingMonths = model.getUniqueDescendingMonthsFor(year);
		for (String month : descendingMonths) {
			writeMonth(sb, model, year, month);
		}

		writeConclusionForYear(sb, model, year);
	}

	private void writeMonth(StringBuilder sb, TanklogModel model, String year, String month) {
		sb.append("\\subsubsection*{");
		sb.append(getLocalizedNameOfMonth(month));
		sb.append(" \\hspace*{1mm} ");
		sb.append(year);
		sb.append("}");
		sb.append("\n");
		sb.append("\n");
		sb.append("\\begin{tabular}{|cccccc|}");
		sb.append("\n");
		sb.append("\\hline");
		sb.append("\n");
		sb.append("Tag & km-Stand & Liter & Euro & Öl-km & km \\\\");
		sb.append("\n");
		sb.append("\\hline");
		sb.append("\n");
		writeEntriesForMonth(sb, model, year, month);
		sb.append("\\hline");
		sb.append("\n");
		sb.append(" Gesamt: & & ");
		String cumulatedLiterPerMonth = model.getCumulatedLiterPerMonth(year, month);
		sb.append(cumulatedLiterPerMonth);
		sb.append(" & & & ");
		String cumulatedKilometerPerMonth = model.getCumulatedKilometerPerMonth(year, month);
		sb.append(cumulatedKilometerPerMonth);
		sb.append(" \\\\");
		sb.append("\n");
		sb.append(" & & & & ergibt: & ");
		String consumptionForMonth = model.getConsumptionForMonth(year, month);
		sb.append(consumptionForMonth);
		sb.append(" L./100 km \\\\");
		sb.append("\n");
		sb.append("\\hline");
		sb.append("\n");
		sb.append("\\end{tabular}");
		sb.append("\n");
		sb.append("\n");
	}

	private void writeEntriesForMonth(StringBuilder sb, TanklogModel model, String year, String month) {
		HashMap<TanklogEntryKey, TanklogEntry> entries = model.getEntriesForMonth(year, month);
		HashMap<Integer, String> garageEntries = model.getGarageEntriesForMonth(year, month);
		HashMap<Integer, TanklogOilchangeEntry> oilchangeEntries = model.getOilchangeEntriesForMonth(year, month);
		HashMap<Integer, String> wookEntries = model.getWookEntriesForMonth(year, month);
		SortedMap<BigDecimal, String> weightedEntries = new TreeMap<BigDecimal, String>();

		for (Entry<TanklogEntryKey, TanklogEntry> entry : entries.entrySet()) {
			BigDecimal weight = new BigDecimal("0.1");
			TanklogEntryKey tanklogEntryKey = entry.getKey();
			BigDecimal adjustedEntryWeight = new BigDecimal(tanklogEntryKey.getKilometer()).scaleByPowerOfTen(-15);
			Integer tanklogDayOfMonth = tanklogEntryKey.getLocalDate().getDayOfMonth();
			BigDecimal key = new BigDecimal(tanklogDayOfMonth ).add(weight)
					.add(adjustedEntryWeight);
			TanklogEntry tanklogEntry = entry.getValue();
			String value = tanklogDayOfMonth + ". & " + tanklogEntry.getFormattedKilometer() + " & "
					+ tanklogEntry.getFormattedLiter() + " & " + tanklogEntry.getFormattedPrice() + " & "
					+ tanklogEntry.getFormattedOilKilometer() + " & " + tanklogEntry.getFormattedDrivenKilometer()
					+ " \\\\";
			weightedEntries.put(key, value);
		}

		for (Entry<Integer, String> entry : garageEntries.entrySet()) {
			BigDecimal weight = new BigDecimal("0.2");
			BigDecimal key = new BigDecimal(entry.getKey()).add(weight);
			String value = entry.getKey() + ". & \\multicolumn{5}{c|}{\\textbf{" + entry.getValue() + "}} \\\\";
			weightedEntries.put(key, value);
		}

		for (Entry<Integer, TanklogOilchangeEntry> entry : oilchangeEntries.entrySet()) {
			BigDecimal weight = new BigDecimal("0.3");
			BigDecimal key = new BigDecimal(entry.getKey()).add(weight);
			String value = entry.getKey() + ". & \\multicolumn{5}{c|}{\\textbf{"
					+ entry.getValue().getFormattedOilchangeKilometer() + "}} \\\\";
			weightedEntries.put(key, value);
		}

		for (Entry<Integer, String> entry : wookEntries.entrySet()) {
			BigDecimal weight = new BigDecimal("0.4");
			BigDecimal key = new BigDecimal(entry.getKey()).add(weight);
			String value = entry.getKey() + ". & \\multicolumn{5}{c|}{\\textbf{" + entry.getValue() + "}} \\\\";
			weightedEntries.put(key, value);
		}

		for (Entry<BigDecimal, String> entry : weightedEntries.entrySet()) {
			sb.append(entry.getValue());
			sb.append("\n");
		}
	}

	private void writeConclusionForYear(StringBuilder sb, TanklogModel model, String year) {
		sb.append("\\subsubsection*{Verbrauch und Reparaturen im Jahr ");
		sb.append(year);
		sb.append("}");
		sb.append("\n");
		sb.append("\n");
		sb.append("\\begin{tabular}{|l|c|c|c|l|}");
		sb.append("\n");
		sb.append("\\hline");
		sb.append("\n");
		sb.append("Monat & Liter & Kilometer & L./100 km & Reparaturen \\\\");
		sb.append("\n");
		sb.append("\\hline");
		sb.append("\n");
		TanklogYearStatistics statisticsForYear = model.getStatisticsForYear(year);
		ArrayList<String> descendingMonthsForYear = model.getUniqueDescendingMonthsFor(year);
		for (int i = descendingMonthsForYear.size() - 1; i > -1; i--) {
			String month = descendingMonthsForYear.get(i);
			Integer intMonth = Integer.parseInt(month);
			TanklogYearStatisticsEntry statisticsForMonth = statisticsForYear.getStatisticsForMonth(intMonth);
			writeConclusionForMonth(sb, month, statisticsForMonth);
		}

		sb.append("\\hline");
		sb.append("\n");
		sb.append("Gesamt & ");
		TanklogYearStatisticsEntry cumulatedYearEntry = statisticsForYear.getCumulatedYearEntry();
		String liter = cumulatedYearEntry.getLiter();
		sb.append(liter);
		sb.append(" & ");
		String kilometer = cumulatedYearEntry.getKilometer();
		sb.append(kilometer);
		sb.append(" & ");
		String consumption = cumulatedYearEntry.getLiterPerHundredKilometer();
		sb.append(consumption);
		sb.append(" & \\\\");
		sb.append("\n");
		sb.append("\\hline");
		sb.append("\n");
		sb.append("\\end{tabular}");
		sb.append("\n");
		sb.append("\n");
	}

	private void writeConclusionForMonth(StringBuilder sb, String month,
			TanklogYearStatisticsEntry statisticsForMonth) {
		sb.append(getLocalizedNameOfMonth(month));
		sb.append(" & ");
		sb.append(statisticsForMonth.getLiter());
		sb.append(" & ");
		sb.append(statisticsForMonth.getKilometer());
		sb.append(" & ");
		sb.append(statisticsForMonth.getLiterPerHundredKilometer());
		sb.append(" & ");
		ArrayList<String> repairs = statisticsForMonth.getRepairs();
		if (!repairs.isEmpty()) {
			sb.append(repairs.get(0));
			repairs.remove(0);
		}

		sb.append("\\\\");
		sb.append("\n");

		for (String repairEntry : repairs) {
			sb.append(" & & & & ");
			sb.append(repairEntry);
			sb.append(" \\\\");
			sb.append("\n");
		}
	}

	private void writeImportantData(StringBuilder sb, TanklogModel model) {
		sb.append("\\newpage");
		sb.append('\n');
		sb.append('\n');
		sb.append("\\subsection*{wichtige Daten}");
		sb.append('\n');
		sb.append('\n');
		sb.append("\\begin{tabular}{lcc}");
		sb.append("\\textbf{Reifendruck} & \\textbf{vorne} & \\textbf{hinten} \\\\");
		sb.append('\n');
		sb.append("\\textbf{volle Beladung} & ");
		String fullyladenAirpressure = model.getFullyladenAirpressure();
		sb.append(fullyladenAirpressure);
		sb.append(" & ");
		sb.append(fullyladenAirpressure);
		sb.append(" \\\\");
		sb.append('\n');
		sb.append("\\textbf{halbe Beladung} & ");
		String halfladenAirpressure = model.getHalfladenAirpressure();
		sb.append(halfladenAirpressure);
		sb.append(" & ");
		sb.append(halfladenAirpressure);
		sb.append(" \\\\");
		sb.append('\n');
		sb.append("\\textbf{Notrad} & \\multicolumn{2}{c}{");
		String sparewheelAirpressure = model.getSparewheelAirpressure();
		sb.append(sparewheelAirpressure);
		sb.append("} \\\\");
		sb.append('\n');
		sb.append("\\textbf{Ölwechsel} & \\multicolumn{2}{c}{");
		String oilchange = model.getOilchangeWithoutFilter();
		sb.append(oilchange);
		sb.append("} \\\\");
		sb.append('\n');
		sb.append("\\textbf{Ölwechsel mit Filter} & \\multicolumn{2}{c}{");
		String oilchangeWithFilter = model.getOilchangeWithFilter();
		sb.append(oilchangeWithFilter);
		sb.append("} \\\\");
		sb.append('\n');
		TanklogPurchaseDetail purchaseDetail = model.getPurchaseDetail();
		LocalDate purchaseDate = purchaseDetail.getPurchaseDate();
		if (purchaseDate != null) {
			sb.append("\\textbf{Kaufdatum} & \\multicolumn{2}{c}{");
			sb.append(purchaseDate);
			sb.append("} \\\\");
			sb.append('\n');
		}

		String kilometer = purchaseDetail.getKilometer();
		if (kilometer != null) {
			sb.append("\\textbf{Kilometerstand bei Übernahme} & \\multicolumn{2}{c}{");
			sb.append(kilometer);
			sb.append("} \\\\");
			sb.append('\n');
		}

		sb.append("\\end{tabular}");
		sb.append('\n');
		sb.append('\n');

		ArrayList<String> notes = model.getNotes();
		if (notes.isEmpty())
			return;

		sb.append("\\begin{tabular}{l}");
		sb.append('\n');
		for (String note : notes) {
			sb.append("\\textbf{");
			sb.append(note);
			sb.append("} \\\\");
			sb.append('\n');
		}

		sb.append("\\end{tabular}");
		sb.append('\n');
		sb.append('\n');
	}

	private void writeTitle(StringBuilder sb, TanklogModel model) {
		sb.append("\\section*{Tankbuch vom ");
		String carname = model.getCarname();
		sb.append(carname);
		sb.append("\\hspace*{4mm} \\texttt{");
		String numberplate = model.getNumberplate();
		sb.append(numberplate);
		sb.append("}}").append('\n');
		sb.append('\n');
	}

	private void writeDocumentHeader(StringBuilder sb) {
		sb.append("\\documentclass[11pt, a4paper]{article}").append('\n');
		sb.append("").append('\n');
		sb.append("\\usepackage{german, amssymb}").append('\n');
		sb.append("\\usepackage[utf8]{inputenc}").append('\n');
		sb.append("\\usepackage{graphicx}").append('\n');
		sb.append("").append('\n');
		sb.append("\\textwidth = 6.5 in").append('\n');
		sb.append("\\textheight = 8.5 in").append('\n');
		sb.append("\\oddsidemargin = 0.0 in").append('\n');
		sb.append("\\evensidemargin = 0.0 in").append('\n');
		sb.append("\\topmargin = 0.0 in").append('\n');
		sb.append("\\headheight = 0.0 in").append('\n');
		sb.append("\\headsep = 0.0 in").append('\n');
		sb.append("\\parskip = 0.2 in").append('\n');
		sb.append("\\parindent = 0.0 in").append('\n');
		sb.append("").append('\n');
		sb.append("\\begin{document}").append('\n');
		sb.append("").append('\n');
	}

	private void writeDocumentFooter(StringBuilder sb) {
		sb.append("\\ifpdf").append('\n');
		sb.append("\\DeclareGraphicsExtensions{.pdf, .jpg, .tif}").append('\n');
		sb.append("\\else").append('\n');
		sb.append("\\DeclareGraphicsExtensions{.eps, .jpg}").append('\n');
		sb.append("\\fi").append('\n');
		sb.append("\\end{document}").append('\n');
		sb.append("\\end").append('\n');
		sb.append("").append('\n');
	}

	private String getLocalizedNameOfMonth(String month) {
		int parsedInt = Integer.parseInt(month);
		switch (parsedInt) {
		case 1:
			return "Januar";
		case 2:
			return "Februar";
		case 3:
			return "März";
		case 4:
			return "April";
		case 5:
			return "Mai";
		case 6:
			return "Juni";
		case 7:
			return "Juli";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "Oktober";
		case 11:
			return "November";
		case 12:
			return "Dezember";
		default:
			throw new IllegalArgumentException("Could not get localized name of month for '" + month + "'.");
		}
	}
}
