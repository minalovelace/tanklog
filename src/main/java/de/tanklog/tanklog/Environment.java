package de.tanklog.tanklog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Optional;

public class Environment {
	private final String _textFileEnding = ".txt";
	private final String _inputFolderName = "input";
	private final String _outputFolderName = "output";
	private final String _readmeFilename = "README.md";

	/**
	 * Check if input and output folder exist and create them if they don't exist.
	 * Create a readme file in the same folder as the jar.
	 * 
	 * @return true if the input folder has been created; otherwise false
	 * 
	 * @throws IOException
	 */
	public boolean checkAndPrepare() throws IOException {
		Path inputFolderPath = Paths.get(_inputFolderName);
		boolean result = inputFolderPath.toFile().mkdir();
		Path outputFolderPath = Paths.get(_outputFolderName);
		outputFolderPath.toFile().mkdir();
		copyReadme();
		return result;
	}

	private void copyReadme() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream in = classLoader.getResourceAsStream(_readmeFilename);
		Path targetReadmePath = Paths.get(_readmeFilename);
		Files.copy(in, targetReadmePath, StandardCopyOption.REPLACE_EXISTING);
	}

	public InputStream openInputStream() throws IOException {
		Path dir = Paths.get(_inputFolderName);

		Optional<Path> lastFilePath = Files.list(dir).filter(f -> !Files.isDirectory(f))
				.filter(f -> f.toString().endsWith(_textFileEnding))
				.max(Comparator.comparingLong(f -> f.toFile().lastModified()));

		if (lastFilePath.isPresent()) {
			Path path = lastFilePath.get();
			File file = path.toFile();
			InputStream in = new FileInputStream(file);
			return in;
		}

		throw new UnsupportedOperationException("No input file found.");
	}

	public Path getDestinationFolder() {
		return Paths.get(_outputFolderName);
	}
}
