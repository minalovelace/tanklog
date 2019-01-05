package de.tanklog.tanklog;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import de.tanklog.model.TanklogEntry;
import de.tanklog.model.TanklogEntryKey;
import de.tanklog.model.TanklogModel;
import de.tanklog.model.TanklogOilchangeEntry;
import de.tanklog.model.TanklogYearStatistics;
import de.tanklog.model.TanklogYearStatisticsEntry;
import de.tanklog.parser.TanklogModelParser;

public class IntegrationTest {
	@Rule
	public TestName testName = new TestName();

	@Test
	public void integrationSimpleTest() throws IOException {
		// arrange
		InputStream in = arrangeTest();
		TanklogModelParser modelParser = new TanklogModelParser();

		// act
		TanklogModel tanklogModel = modelParser.parse(in);

		// assert
		assertDetails(tanklogModel);
		assertNotes(tanklogModel);
		assertSimpleEntries(tanklogModel);
		assertGarageEntries(tanklogModel);
		assertWookEntries(tanklogModel);
		assertStatistics(tanklogModel);
	}

	@Test
	public void integrationOilchangeTest() throws IOException {
		// arrange
		InputStream in = arrangeTest();
		TanklogModelParser modelParser = new TanklogModelParser();

		// act
		TanklogModel tanklogModel = modelParser.parse(in);

		// assert
		assertEntriesWithOilchange(tanklogModel);
		assertOilchangeEntry(tanklogModel);
	}

	private void assertOilchangeEntry(TanklogModel tanklogModel) {
		HashMap<Integer, TanklogOilchangeEntry> oilchangeEntriesForMonth = tanklogModel
				.getOilchangeEntriesForMonth("1986", "11");
		assertEquals(1, oilchangeEntriesForMonth.size(), 0.1);
		String actualOilchangeEntry = oilchangeEntriesForMonth.get(1).getFormattedOilchangeKilometer();
		String expectedOilchangeEntry = "Ölwechsel bei 23.800 km";
		assertEquals(expectedOilchangeEntry, actualOilchangeEntry);
	}

	private void assertEntriesWithOilchange(TanklogModel tanklogModel) {
		assertSimpleEntries(tanklogModel);
		HashMap<TanklogEntryKey, TanklogEntry> entriesForMonth = tanklogModel.getEntriesForMonth("1986", "11");
		TanklogEntryKey expectedTanklogEntryKey = new TanklogEntryKey(LocalDate.of(1986, 11, 22), 23_985);
		TanklogEntry actualEntry = entriesForMonth.get(expectedTanklogEntryKey);
		TanklogEntry expectedEntry = new TanklogEntry("23.98", "27.99", 23_985);
		expectedEntry.setDrivenKilometer(250);
		expectedEntry.setOilKilometer(185);
		assertEntry(expectedEntry, actualEntry);
	}

	private InputStream arrangeTest() throws FileNotFoundException {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(testName.getMethodName() + ".txt");
		File file = new File(resource.getFile());
		InputStream in = new FileInputStream(file);
		return in;
	}

	private void assertGarageEntries(TanklogModel tanklogModel) {
		HashMap<Integer, String> garageEntriesForMonth = tanklogModel.getGarageEntriesForMonth("1986", "11");
		assertEquals(1, garageEntriesForMonth.size(), 0.1);
		assertEquals("Winter tires mounted", garageEntriesForMonth.get(1));
	}

	private void assertStatistics(TanklogModel tanklogModel) {
		String expectedCumulatedLiterPerMonth = "67,97";
		String actualCumulatedLiterPerMonth = tanklogModel.getCumulatedLiterPerMonth("1986", "10");
		assertEquals(expectedCumulatedLiterPerMonth, actualCumulatedLiterPerMonth);
		String expectedCumulatedKilometerPerMonth = "320";
		String actualCumulatedKilometerPerMonth = tanklogModel.getCumulatedKilometerPerMonth("1986", "10");
		assertEquals(expectedCumulatedKilometerPerMonth, actualCumulatedKilometerPerMonth);
		String expectedConsumptionForMonth = "21,24";
		String actualConsumptionForMonth = tanklogModel.getConsumptionForMonth("1986", "10");
		assertEquals(expectedConsumptionForMonth, actualConsumptionForMonth);
		TanklogYearStatistics statisticsForYear = tanklogModel.getStatisticsForYear("1986");
		TanklogYearStatisticsEntry cumulatedYearEntry = statisticsForYear.getCumulatedYearEntry();
		String actualLiterForYear = cumulatedYearEntry.getLiter();
		String expectedLiterForYear = "67,97";
		assertEquals(expectedLiterForYear, actualLiterForYear);
		String actualKilometerForYear = cumulatedYearEntry.getKilometer();
		String expectedKilometerForYear = "320";
		assertEquals(expectedKilometerForYear, actualKilometerForYear);
		String actualConsumptionForYear = cumulatedYearEntry.getLiterPerHundredKilometer();
		String expectedConsumptionForYear = "21,24";
		assertEquals(expectedConsumptionForYear, actualConsumptionForYear);
	}

	private void assertWookEntries(TanklogModel tanklogModel) {
		HashMap<Integer, String> wookEntriesForMonth = tanklogModel.getWookEntriesForMonth("1986", "9");
		assertEquals(1, wookEntriesForMonth.size(), 0.1);
		assertEquals("Wasser, Öl etc. in Ordnung", wookEntriesForMonth.get(8));
	}

	private void assertDetails(TanklogModel tanklogModel) {
		assertEquals("Red hot rod", tanklogModel.getCarname());
		assertEquals("AB - C 1234", tanklogModel.getNumberplate());
		assertEquals("2.6", tanklogModel.getFullyladenAirpressure());
		assertEquals("2.4", tanklogModel.getHalfladenAirpressure());
		assertEquals("4.2", tanklogModel.getSparewheelAirpressure());
		assertEquals("3.5", tanklogModel.getOilchangeWithoutFilter());
		assertEquals("4.5", tanklogModel.getOilchangeWithFilter());
		assertEquals(LocalDate.parse("1986-10-02"), tanklogModel.getPurchaseDetail().getPurchaseDate());
		assertEquals("23.415", tanklogModel.getPurchaseDetail().getKilometer());
	}

	private void assertSimpleEntries(TanklogModel tanklogModel) {
		HashMap<TanklogEntryKey, TanklogEntry> actualEntries = tanklogModel.getEntriesForMonth("1986", "10");
		assertEquals(3, actualEntries.size());

		TanklogEntry expectedEntry13 = new TanklogEntry("45.98", "35.99", 23_420);
		expectedEntry13.setDrivenKilometer(5);
		expectedEntry13.setOilKilometer(5);
		TanklogEntryKey expectedEntryKey13 = new TanklogEntryKey(LocalDate.of(1986, 10, 13), 23_420);
		TanklogEntry actualEntry13 = actualEntries.get(expectedEntryKey13);
		assertEntry(expectedEntry13, actualEntry13);
		TanklogEntry expectedEntry15 = new TanklogEntry("5.98", "5.99", 23_520);
		expectedEntry15.setDrivenKilometer(100);
		expectedEntry15.setOilKilometer(105);
		TanklogEntryKey expectedEntryKey15 = new TanklogEntryKey(LocalDate.of(1986, 10, 15), 23_520);
		TanklogEntry actualEntry15 = actualEntries.get(expectedEntryKey15);
		assertEntry(expectedEntry15, actualEntry15);
		TanklogEntry expectedEntry25 = new TanklogEntry("25.98", "25.99", 23_735);
		expectedEntry25.setDrivenKilometer(215);
		expectedEntry25.setOilKilometer(320);
		TanklogEntryKey expectedEntryKey25 = new TanklogEntryKey(LocalDate.of(1986, 10, 25), 23_735);
		TanklogEntry actualEntry25 = actualEntries.get(expectedEntryKey25);
		assertEntry(expectedEntry25, actualEntry25);
	}

	private void assertEntry(TanklogEntry expectedEntry, TanklogEntry actualEntry) {
		assertEquals(expectedEntry.getPrice(), actualEntry.getPrice());
		assertEquals(expectedEntry.getLiter(), actualEntry.getLiter());
		assertEquals(expectedEntry.getKilometer(), actualEntry.getKilometer(), 0.0001);
		assertEquals(expectedEntry.getDrivenKilometer(), actualEntry.getDrivenKilometer(), 0.0001);
		assertEquals(expectedEntry.getOilKilometer(), actualEntry.getOilKilometer(), 0.0001);
	}

	private void assertNotes(TanklogModel tanklogModel) {
		ArrayList<String> actualNotes = tanklogModel.getNotes();
		assertEquals(3, actualNotes.size());
		ArrayList<String> expectedNotes = new ArrayList<String>();
		expectedNotes.add("Some noteworthy words");
		expectedNotes.add("More noteworthy words");
		expectedNotes.add("Some more noteworthy words");
		assertEquals(expectedNotes, actualNotes);
	}
}
