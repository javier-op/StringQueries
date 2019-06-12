package utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashSet;

public class Preprocessor {

	private String active_dir;

	public Preprocessor(String base_dir) {
		this.active_dir = base_dir;
	}

	public String getDirectory() {
		return this.active_dir;
	}

	public void showDirectory() {
		System.out.println("Current pre-processor directory: " + this.active_dir);
	}

	public void changeDirectory(String new_dir) {
		this.active_dir = new_dir;
	}

	private String preprocessFile(String input_file, String charset_name) {
		Path full_path = Paths.get(this.active_dir, input_file);
		String content = "";
		try {
			byte[] encoded_file = Files.readAllBytes(full_path);
			content = new String(encoded_file, charset_name);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The encoding " + charset_name + " is not supported.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("The input file could not be read, received the following exception:");
			System.err.println(e.getClass().getSimpleName());
			System.exit(1);
		}
		// Outputs for clarity
		System.out.println("Pre-process started.");
		// Remove line jumps, linebreaks and other whitespaces
		content = content.replaceAll("[\\h\\s\\v]", " ");
		// Remove multiple spaces
		content = content.replaceAll("[\\h]+", " ");
		System.out.println("- All whitespace turned into spaces.");
		// Normalize all accents
		content = Normalizer.normalize(content, Normalizer.Form.NFD);
		System.out.println("- All accents normalized.");
		// Remove punctuation
		content = content.replaceAll("[\\p{Punct}]", "");
		System.out.println("- All punctuation removed.");
		// Make all lowercase
		content = content.toLowerCase(Locale.ROOT);
		System.out.println("- All letters turned into lowercase.");
		// Output message for clarity
		System.out.println("Standard pre-process done.");
		return content;
	}

	private String getRegexMatchList(String regex, String text) {
		Pattern compiled = Pattern.compile(regex);
		Matcher matcher = compiled.matcher(text);
		HashSet<String> char_set = new HashSet<>();
		while (matcher.find()) {
			char_set.add(matcher.group());
		}
		Iterator<String> iter = char_set.iterator();
		StringBuilder char_list = new StringBuilder();
		while (iter.hasNext()) {
			char_list.append(iter.next());
			char_list.append(" ");
		}
		return char_list.toString();
	}

	private void saveToFile(String text, String output_name, String charset_name) {
		// Always append special character at the end of the string
		text += '\0';
		Charset charset = Charset.forName(charset_name);
		Path full_path = Paths.get(this.active_dir, output_name);
		try {
		Files.writeString(full_path, text, charset);} catch (IOException e) {
			System.err.println("The output file could not be written, received the following exception::");
			System.err.println(e.getClass().getSimpleName());
			System.exit(1);
		}
	}

	private void fullPreprocess(String input_file, String output_name, String charset_name, String exclusion_regex) {
		String std_preprocessed = this.preprocessFile(input_file, charset_name);
		// Search characters that should be excluded by regex
		String excluded_chars_list = this.getRegexMatchList(exclusion_regex, std_preprocessed);
		// Remove excluded characters from text
		System.out.println("Extra pre-processing started with the following regex: " + exclusion_regex + ".");
		std_preprocessed = std_preprocessed.replaceAll(exclusion_regex, "");
		// Output removed characters for clarity
		System.out.println("The following extra characters have been deleted from the text:");
		System.out.println(excluded_chars_list);
		// Save preprocessed text to file
		this.saveToFile(std_preprocessed, output_name, charset_name);
		System.out.println("Text pre-processing done.");
	}

	public void preprocessDNA(String input_file, String output_name) {
		this.preprocessDNA(input_file, output_name, "UTF-8");
	}

	public void preprocessDNA(String input_file, String output_name, String charset_name) {
		// Search characters that aren't dna bases
		String non_dna_regex = "([^agtc&&[\\H]])";
		this.fullPreprocess(input_file, output_name, charset_name, non_dna_regex);
	}

	public void preprocessEnglish(String input_file, String output_name) {
		// ISO-8859-1 Charset is the one used in the "english" texts.
		this.preprocessEnglish(input_file, output_name, "ISO-8859-1");
	}

	public void preprocessEnglish(String input_file, String output_name, String charset_name) {
		// Search characters that aren't standard text
		String non_std_eng_regex = "([\\W&&[\\H]])";
		this.fullPreprocess(input_file, output_name, charset_name, non_std_eng_regex);
	}

}
