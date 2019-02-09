package de.tanklog.writer.latex;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class PdfGenerator {
	private static final long WAIT_FOR_PDFLATEX_SEC = 60;
	private static final String PDFLATEX_ARG = "-halt-on-error";
	private static final String PDFLATEX_LOG_FILENAME = "pdflatex.log";

	public static void generatePdf(File file, String pdfLaTeXLocation) throws IOException {
		ProcessBuilder pb = new ProcessBuilder(Paths.get(pdfLaTeXLocation).toAbsolutePath().toString(), PDFLATEX_ARG,
				file.getAbsolutePath());
		pb.directory(file.getParentFile());
		pb.redirectErrorStream(true);
		pb.redirectOutput(new File(
				Paths.get(file.getParentFile().getAbsolutePath(), PDFLATEX_LOG_FILENAME).toAbsolutePath().toString()));
		Process p = pb.start();

		try {
			p.waitFor(WAIT_FOR_PDFLATEX_SEC, TimeUnit.SECONDS);
			assert p.getInputStream().read() == -1;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
