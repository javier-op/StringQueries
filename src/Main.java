import suffix_tree.SuffixTree;

public class Main {

	public static void main(String[] args) {
		String s = "aaa\0";
		SuffixTree st = new SuffixTree(s);
		
		for(int i = s.length() -1; i >= 0; i--) {
			st.insert(i);
		}
		
		System.out.println("Done!");

	}

}
