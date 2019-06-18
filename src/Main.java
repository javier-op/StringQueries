import suffix_tree.SuffixTree;
import utils.Preprocessor;
import utils.SortablePair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class Main {
	public static void main(String[] args) {
		String s = "guaguaalagua\0";
		SuffixTree st = new SuffixTree(s);

		String target = "guaa";

		Integer[] numbers = st.locate(target);
		System.out.println("Done!");

		// Run testing of top_K_Q
		testTopKQ();

	}

	private static void testTopKQ() {

		// If it's failing check if this path is giving you the correct one
		Path path = Paths.get("data/").toAbsolutePath();

		Preprocessor preprocessor = new Preprocessor(path.toString());

		preprocessor.preprocessEnglish("english.50MB", "english.txt");
		String english102400 = "";

		try {
			english102400 = Files.readString(Paths.get("data/english.txt"));
			int len = english102400.length();
			english102400 = english102400.substring(len - 102400, len);
		} catch (IOException e) {
			System.exit(1);
		}

		SuffixTree test_top_k_q = new SuffixTree(english102400);

		ArrayList<String[]> results = new ArrayList<>();
		ArrayList<SortablePair> kq_pairs = new ArrayList<>();

		Random random = new Random();
		int random_length, random_amount;

		for (int i = 0; i < 20; i++) {
			do {
				random_length = random.nextInt(30);
			} while (random_length < 1);
			do {
				random_amount = random.nextInt(20);
			} while (random_amount < 1);
			results.add(test_top_k_q.top_K_Q(random_amount, random_length));
			kq_pairs.add(new SortablePair(random_amount, random_length));
		}

		// Print results
		for (int i = 0; i < results.size(); i++) {
			System.out.println("------");
			SortablePair kq = kq_pairs.get(i);
			System.out.println("Test " + (i + 1) + ": K = " + kq.getFirst() + " & Q = " + kq.getSecond());
			for (String str : results.get(i)) {
				System.out.println(str);
			}
			System.out.println("------");
		}
	}
}
