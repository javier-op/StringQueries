import suffix_tree.SuffixTree;

public class Main {
	public static void main(String[] args) {
		String s = "guaguaalagua\0";
		SuffixTree st = new SuffixTree(s);

		String target = "guaa";

		Integer[] numbers = st.locate(target);
		System.out.println("Done!");
	}
}
