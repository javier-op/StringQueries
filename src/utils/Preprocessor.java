package utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class Preprocessor {

	private String active_dir;

	public Preprocessor(String base_dir){
		this.active_dir = base_dir;
	}

	public void changeDirectory(String new_dir){
		this.active_dir = new_dir;
	}

	private String preprocessFile(String input_file, String charset_name) throws IOException {
		return "";
	}

	public void preprocessDNA(String input_file, String output_name) throws IOException {
		preprocessDNA(input_file, output_name, "UTF-8");
	}

	public void preprocessDNA(String input_file, String output_name, String charset_name) throws IOException {

	}

	public void preprocessEnglish(String input_file, String output_name) throws IOException {
		// ISO-8859-1 Charset is the one used in the "english" texts.
		preprocessEnglish(input_file, output_name, "ISO-8859-1");
	}

	public void preprocessEnglish(String input_file, String output_name, String charset_name) throws IOException {

	}

}
