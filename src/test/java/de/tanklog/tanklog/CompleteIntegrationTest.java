package de.tanklog.tanklog;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import de.tanklog.model.TanklogModel;
import de.tanklog.parser.TanklogModelParser;
import de.tanklog.writer.latex.TanklogWriterLatexDocument;

public class CompleteIntegrationTest {
	@Rule
	public TestName testName = new TestName();

	@Test
	public void completeIntegrationTest() throws IOException {
		// arrange
		InputStream in = arrangeTest();
		TanklogModelParser modelParser = new TanklogModelParser();

		// act
		TanklogModel tanklogModel = modelParser.parse(in);
		TanklogWriterLatexDocument writer = new TanklogWriterLatexDocument();
		String actualDocument = writer.writeDocument(tanklogModel);

		// assert
		String expectedFilename = "expectedCompleteIntegrationTest.tex";
		File expectedFile = getFileBy(expectedFilename);
		byte[] expectedFileContent = Files.readAllBytes(expectedFile.toPath());
		String expectedDocument = new String(expectedFileContent, "UTF-8");
		assertEquals(expectedDocument, actualDocument);
	}

	private InputStream arrangeTest() throws FileNotFoundException {
		File file = getFileBy(testName.getMethodName() + ".txt");
		InputStream in = new FileInputStream(file);
		return in;
	}

	private File getFileBy(String filename) {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(filename);
		File file = new File(resource.getFile());
		return file;
	}
}
