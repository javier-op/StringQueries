package utils;

import java.util.Arrays;

public class Alphabet {

	private String alphabet;

	Alphabet(String text) {
		char[] aux = text.replaceAll("(.)(?=.*?\\1)", "").toCharArray();
		Arrays.parallelSort(aux);
		this.alphabet = new String(aux);
	}

	public int size(){
		return this.alphabet.length();
	}

	public String asString() {
		return this.alphabet;
	}

}
