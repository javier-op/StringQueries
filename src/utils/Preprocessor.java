package utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Locale;

public class Preprocessor {

	private String active_dir;

	public Preprocessor(String base_dir) {
		this.active_dir = base_dir;
	}

	public void changeDirectory(String new_dir) {
		this.active_dir = new_dir;
	}

	private String preprocessFile(String input_file, String charset_name) throws IOException {
		Path full_path = Paths.get(this.active_dir, input_file);
		byte[] encoded_file = Files.readAllBytes(full_path);
		String content = new String(encoded_file, charset_name);
		// Remove line jumps, linebreaks and other whitespaces
		content = content.replaceAll("[\\h\\s\\v]", " ");
		// Remove multiple spaces
		content = content.replaceAll("[\\h]+", " ");
		// Remove punctuation
		content = content.replaceAll("[\\p{Punct}]", "");
		// Make all lowercase
		content = content.toLowerCase(Locale.ROOT);
		return content;
	}

	public void preprocessDNA(String input_file, String output_name) throws IOException {
		preprocessDNA(input_file, output_name, "UTF-8");
	}

	public void preprocessDNA(String input_file, String output_name, String charset_name) throws IOException {
		String std_preprocessed = preprocessFile(input_file, charset_name);
		// TODO: Do preprocess

	}

	public void preprocessEnglish(String input_file, String output_name) throws IOException {
		// ISO-8859-1 Charset is the one used in the "english" texts.
		preprocessEnglish(input_file, output_name, "ISO-8859-1");
	}

	public void preprocessEnglish(String input_file, String output_name, String charset_name) throws IOException {
		String std_preprocessed = preprocessFile(input_file, charset_name);
		// TODO: Do preprocess
	}

}
