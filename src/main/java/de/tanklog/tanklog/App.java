package de.tanklog.tanklog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.tanklog.model.TanklogModel;
import de.tanklog.parser.TanklogModelParser;
import de.tanklog.writer.latex.PdfGenerator;
import de.tanklog.writer.latex.TanklogWriterLatexDocument;

public class App {
	public static void main(String[] args) throws IOException {
		Environment environment = new Environment();
		boolean checkAndPrepare = environment.checkAndPrepare();
		if (checkAndPrepare)
			return;

		InputStream in = environment.openInputStream();
		TanklogModelParser modelParser = new TanklogModelParser();
		TanklogModel tanklogModel = modelParser.parse(in);
		TanklogWriterLatexDocument tanklogWriterLatexDocument = new TanklogWriterLatexDocument();
		String document = tanklogWriterLatexDocument.writeDocument(tanklogModel);
		Path destinationFolder = environment.getDestinationFolder();
		File destinationFile = Paths.get(destinationFolder.toString(), "tanklog.tex").toFile();
		destinationFile.createNewFile();
		Files.write(destinationFile.toPath(), document.getBytes());
		PdfGenerator.generatePdf(destinationFile);
	}
}
