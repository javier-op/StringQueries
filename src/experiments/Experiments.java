package experiments;

import utils.Preprocessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.exit;

public class Experiments {

	private String readFile(String directory, String filename, String charset) {
		Path path = Paths.get(directory + filename);
		String content = "";
		try {
			byte[] encoded_file = Files.readAllBytes(path);
			content = new String(encoded_file, charset);
		} catch (IOException e) {
			System.err.println("Could not read file.");
			exit(1);
		}
		return content;
	}

	public String getCleanDNA(Preprocessor prep, String filename) {
		String output_name = "dna_out.txt";
		prep.preprocessEnglish(filename, output_name);
		return readFile(prep.getDirectory(), output_name, "UTF-8");
	}

	public String getCleanEnglish(Preprocessor prep, String filename) {
		String output_name = "english_out.txt";
		prep.preprocessEnglish(filename, output_name);
		return readFile(prep.getDirectory(), output_name, "ISO-8859-1");
	}


	public static void main(String[] args) {

	}

}
