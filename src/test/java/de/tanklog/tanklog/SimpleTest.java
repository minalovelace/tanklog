package de.tanklog.tanklog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
import de.tanklog.model.TanklogPurchaseDetail;
import de.tanklog.parser.TanklogModelParser;

public class SimpleTest {
	@Rule
	public TestName testName = new TestName();

	@Test
	public void simpleAirpressureTest() throws IOException {
		// arrange
		InputStream in = arrangeTest();
		TanklogModelParser modelParser = new TanklogModelParser();

		// act
		TanklogModel tanklogModel = modelParser.parse(in);

		// assert
		String halfladenAirpressure = tanklogModel.getHalfladenAirpressure();
		assertEquals("2,4", halfladenAirpressure);
		String fullyladenAirpressure = tanklogModel.getFullyladenAirpressure();
		assertEquals("2,8", fullyladenAirpressure);
		String sparewheelAirpressure = tanklogModel.getSparewheelAirpressure();
		assertEquals("4,2", sparewheelAirpressure);
	}

	@Test
	public void simpleCarnameTest() throws IOException {
		// arrange
		InputStream in = arrangeTest();
		TanklogModelParser modelParser = new TanklogModelParser();

		// act
		TanklogModel tanklogModel = modelParser.parse(in);

		// assert
		String carname = tanklogModel.getCarname();
		assertEquals("Red hot rod", carname);
	}

	@Test
	public void simpleDoubleEntriesTest() throws IOException {
		// arrange
		InputStream in = arrangeTest();
		TanklogModelParser modelParser = new TanklogModelParser();

		// act
		TanklogModel tanklogModel = modelParser.parse(in);

		// assert
		HashMap<TanklogEntryKey, TanklogEntry> entries = tanklogModel.getEntriesForMonth("2019", "1");
		assertEquals(2, entries.size());

		int expectedKilometer13_1 = 23_420;
		TanklogEntryKey expectedTanklogEntryKey13_1 = new TanklogEntryKey(LocalDate.of(2019, 1, 13),
				expectedKilometer13_1);
		TanklogEntry actualTanklogEntry13_1 = entries.get(expectedTanklogEntryKey13_1);
		assertEquals(new BigDecimal("45.94"), actualTanklogEntry13_1.getPrice());
		assertEquals(new BigDecimal("35.99"), actualTanklogEntry13_1.getLiter());
		assertEquals(expectedKilometer13_1, actualTanklogEntry13_1.getKilometer(), 0.1);

		int expectedKilometer13_2 = 23_500;
		TanklogEntryKey expectedTanklogEntryKey13_2 = new TanklogEntryKey(LocalDate.of(2019, 1, 13),
				expectedKilometer13_2);
		TanklogEntry actualTanklogEntry13_2 = entries.get(expectedTanklogEntryKey13_2);
		assertEquals(new BigDecimal("5.98"), actualTanklogEntry13_2.getPrice());
		assertEquals(new BigDecimal("3.99"), actualTanklogEntry13_2.getLiter());
		assertEquals(expectedKilometer13_2, actualTanklogEntry13_2.getKilometer(), 0.1);
	}

	@Test
	public void simpleEntriesTest() throws IOException {
		// arrange
		InputStream in = arrangeTest();
		TanklogModelParser modelParser = new TanklogModelParser();

		// act
		TanklogModel tanklogModel = modelParser.parse(in);

		// assert
		HashMap<TanklogEntryKey, TanklogEntry> entries = tanklogModel.getEntriesForMonth("2019", "1");
		assertEquals(16, entries.size());
		TanklogEntry expectedTanklogEntry = new TanklogEntry("45.98", "35.99", 23_420);
		for (int i = 2; i < 12; ++i) {
			TanklogEntryKey expectedTanklogEntryKey = new TanklogEntryKey(LocalDate.of(2019, 1, i), 23_420);
			TanklogEntry actualTanklogEntry = entries.get(expectedTanklogEntryKey);
			assertEquals(expectedTanklogEntry.getPrice(), actualTanklogEntry.getPrice());
			assertEquals(expectedTanklogEntry.getLiter(), actualTanklogEntry.getLiter());
			assertEquals(expectedTanklogEntry.getKilometer(), actualTanklogEntry.getKilometer(), 0.1);
		}

		TanklogEntryKey expectedTanklogEntryKey12 = new TanklogEntryKey(LocalDate.of(2019, 1, 12), 23_420);
		TanklogEntry actualTanklogEntry12 = entries.get(expectedTanklogEntryKey12);
		assertEquals(new BigDecimal("45"), actualTanklogEntry12.getPrice());
		assertEquals(expectedTanklogEntry.getLiter(), actualTanklogEntry12.getLiter());
		assertEquals(expectedTanklogEntry.getKilometer(), actualTanklogEntry12.getKilometer(), 0.1);

		TanklogEntryKey expectedTanklogEntryKey13 = new TanklogEntryKey(LocalDate.of(2019, 1, 13), 23_420);
		TanklogEntry actualTanklogEntry13 = entries.get(expectedTanklogEntryKey13);
		assertEquals(new BigDecimal("45.9"), actualTanklogEntry13.getPrice());
		assertEquals(expectedTanklogEntry.getLiter(), actualTanklogEntry13.getLiter());
		assertEquals(expectedTanklogEntry.getKilometer(), actualTanklogEntry13.getKilometer(), 0.1);

		TanklogEntryKey expectedTanklogEntryKey14 = new TanklogEntryKey(LocalDate.of(2019, 1, 14), 23_420);
		TanklogEntry actualTanklogEntry14 = entries.get(expectedTanklogEntryKey14);
		assertEquals(new BigDecimal("5.98"), actualTanklogEntry14.getPrice());
		assertEquals(expectedTanklogEntry.getLiter(), actualTanklogEntry14.getLiter());
		assertEquals(expectedTanklogEntry.getKilometer(), actualTanklogEntry14.getKilometer(), 0.1);

		TanklogEntryKey expectedTanklogEntryKey15 = new TanklogEntryKey(LocalDate.of(2019, 1, 15), 23_420);
		TanklogEntry actualTanklogEntry15 = entries.get(expectedTanklogEntryKey15);
		assertEquals(new BigDecimal("45.98898"), actualTanklogEntry15.getPrice());
		assertEquals(expectedTanklogEntry.getLiter(), actualTanklogEntry15.getLiter());
		assertEquals(expectedTanklogEntry.getKilometer(), actualTanklogEntry15.getKilometer(), 0.1);

		TanklogEntryKey expectedTanklogEntryKey16 = new TanklogEntryKey(LocalDate.of(2019, 1, 16), 23_420);
		TanklogEntry actualTanklogEntry16 = entries.get(expectedTanklogEntryKey16);
		assertEquals(new BigDecimal("898945.98"), actualTanklogEntry16.getPrice());
		assertEquals(expectedTanklogEntry.getLiter(), actualTanklogEntry16.getLiter());
		assertEquals(expectedTanklogEntry.getKilometer(), actualTanklogEntry16.getKilometer(), 0.1);

		TanklogEntryKey expectedTanklogEntryKey17 = new TanklogEntryKey(LocalDate.of(2019, 1, 17), 23_420);
		TanklogEntry actualTanklogEntry17 = entries.get(expectedTanklogEntryKey17);
		assertEquals(new BigDecimal("458989"), actualTanklogEntry17.getPrice());
		assertEquals(expectedTanklogEntry.getLiter(), actualTanklogEntry17.getLiter());
		assertEquals(expectedTanklogEntry.getKilometer(), actualTanklogEntry17.getKilometer(), 0.1);
	}

	@Test
	public void simpleGarageTest() throws IOException {
		// arrange
		InputStream in = arrangeTest();
		TanklogModelParser modelParser = new TanklogModelParser();

		// act
		TanklogModel tanklogModel = modelParser.parse(in);

		// assert
		HashMap<Integer, String> entriesApril = tanklogModel.getGarageEntriesForMonth("2019", "4");
		assertEquals(1.0, entriesApril.size(), 0.001);
		String actualTanklogEntryApril = entriesApril.get(23);
		assertEquals("Ölwechsel", actualTanklogEntryApril);
		HashMap<Integer, String> entriesOctober = tanklogModel.getGarageEntriesForMonth("2019", "10");
		assertEquals(1.0, entriesOctober.size(), 0.001);
		String actualTanklogEntryOctober = entriesOctober.get(7);
		assertEquals("Winterreifen aufgezogen", actualTanklogEntryOctober);
	}

	@Test
	public void simpleNoteTest() throws IOException {
		// arrange
		InputStream in = arrangeTest();
		TanklogModelParser modelParser = new TanklogModelParser();

		// act
		TanklogModel tanklogModel = modelParser.parse(in);

		// assert
		ArrayList<String> notes = tanklogModel.getNotes();

		assertEquals(1.0, notes.size(), 0.001);
		String expectedNotes = "Some noteworthy words";
		assertEquals(expectedNotes, notes.get(0));
	}

	@Test
	public void simpleNumberplateTest() throws IOException {
		// arrange
		InputStream in = arrangeTest();
		TanklogModelParser modelParser = new TanklogModelParser();

		// act
		TanklogModel tanklogModel = modelParser.parse(in);

		// assert
		String numberplate = tanklogModel.getNumberplate();
		assertEquals("AB - C 1234", numberplate);
	}

	@Test
	public void simpleOilchangeGarageTest() throws IOException {
		// arrange
		InputStream in = arrangeTest();
		TanklogModelParser modelParser = new TanklogModelParser();

		// act
		TanklogModel tanklogModel = modelParser.parse(in);

		// assert
		HashMap<LocalDate, TanklogOilchangeEntry> oilchangeEntries = tanklogModel.getOilchangeEntries();
		assertEquals(1.0, oilchangeEntries.size(), 0.001);

		HashMap<Integer, TanklogOilchangeEntry> entryForMonth = tanklogModel.getOilchangeEntriesForMonth("2019", "3");
		Integer kilometer = entryForMonth.get(4).getOilchangeKilometer();
		assertEquals(12_432, kilometer, 0.1);
	}

	@Test
	public void simpleOilchangeTest() throws IOException {
		// arrange
		InputStream in = arrangeTest();
		TanklogModelParser modelParser = new TanklogModelParser();

		// act
		TanklogModel tanklogModel = modelParser.parse(in);

		// assert
		String oilchange = tanklogModel.getOilchangeWithoutFilter();
		assertEquals("4,5", oilchange);
		String oilchangeWithFilter = tanklogModel.getOilchangeWithFilter();
		assertEquals("5,5", oilchangeWithFilter);
	}

	@Test
	public void simplePurchaseDetailTest() throws IOException {
		// arrange
		InputStream in = arrangeTest();
		TanklogModelParser modelParser = new TanklogModelParser();

		// act
		TanklogModel tanklogModel = modelParser.parse(in);

		// assert
		TanklogPurchaseDetail purchaseDetail = tanklogModel.getPurchaseDetail();
		LocalDate expectedPurchaseDate = LocalDate.of(2016, 2, 15);
		LocalDate actualPurchaseDate = purchaseDetail.getPurchaseDate();
		assertTrue(expectedPurchaseDate.isEqual(actualPurchaseDate));
		assertEquals("20.115", purchaseDetail.getKilometer());
	}

	@Test
	public void simpleWookTest() throws IOException {
		// arrange
		InputStream in = arrangeTest();
		TanklogModelParser modelParser = new TanklogModelParser();

		// act
		TanklogModel tanklogModel = modelParser.parse(in);

		// assert
		HashMap<Integer, String> entries = tanklogModel.getWookEntriesForMonth("2019", "1");
		assertEquals(1.0, entries.size(), 0.001);
		String actualTanklogEntry = entries.get(2);
		assertEquals("Wasser, Öl etc. in Ordnung", actualTanklogEntry);
	}

	private InputStream arrangeTest() throws FileNotFoundException {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(testName.getMethodName() + ".txt");
		File file = new File(resource.getFile());
		InputStream in = new FileInputStream(file);
		return in;
	}
}
