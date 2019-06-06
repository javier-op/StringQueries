package utils;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
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

	private void saveToFile(String text, String output_name, String charset_name) throws IOException {
		// Always append special character at the end of the string
		text += '\0';
		Charset charset = Charset.forName(charset_name);
		Path full_path = Paths.get(this.active_dir, output_name);
		Files.writeString(full_path, text, charset);
	}

	private void fullPreprocess(String input_file, String output_name, String charset_name, String exclusion_regex) throws IOException {
		String std_preprocessed = this.preprocessFile(input_file, charset_name);
		// Search characters that should be excluded by regex
		String excluded_chars_list = this.getRegexMatchList(exclusion_regex, std_preprocessed);
		// Remove excluded characters from text
		std_preprocessed = std_preprocessed.replaceAll(exclusion_regex, "");
		// Save preprocessed text to file
		this.saveToFile(std_preprocessed, output_name, charset_name);
		// Output removed characters for clarity
		System.out.println("The following extra characters have been deleted from the text:");
		System.out.println(excluded_chars_list);
	}

	public void preprocessDNA(String input_file, String output_name) throws IOException {
		this.preprocessDNA(input_file, output_name, "UTF-8");
	}

	public void preprocessDNA(String input_file, String output_name, String charset_name) throws IOException {
		// Search characters that aren't dna bases
		String non_dna_regex = "([^agtc])";
		this.fullPreprocess(input_file, output_name, charset_name, non_dna_regex);
	}

	public void preprocessEnglish(String input_file, String output_name) throws IOException {
		// ISO-8859-1 Charset is the one used in the "english" texts.
		this.preprocessEnglish(input_file, output_name, "ISO-8859-1");
	}

	public void preprocessEnglish(String input_file, String output_name, String charset_name) throws IOException {
		// Search characters that aren't standard english text
		String non_std_eng_regex = "([\\W])";
		this.fullPreprocess(input_file, output_name, charset_name, non_std_eng_regex);
	}

}
