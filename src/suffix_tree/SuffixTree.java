package suffix_tree;

import java.util.HashSet;
import java.util.Stack;

public class SuffixTree {

	private String text;
	private SuffixNode root;

	public SuffixTree(String text) {
		this.text = text;
		this.root = new SuffixNode(this);

		for (int i = 0; i < text.length(); i++) {
			this.insert(i);
		}
	}

	public int count(String text) {
		return this.root.search(text).getLeaves().size();
	}

	public Integer[] locate(String text) {
		HashSet<SuffixNode> leaves = this.root.search(text).getLeaves();
		Integer[] output = new Integer[leaves.size()];
		int i = 0;
		for (SuffixNode node : leaves) {
			output[i++] = node.getValue();
		}
		return output;
	}

	char getChar(int index) {
		return this.text.charAt(index);
	}

	int getTextLenght() {
		return this.text.length();
	}

	String getSubString(int from, int to) {
		return this.text.substring(from, to);
	}

	private void insert(int index) {
		this.root.insert(index, index, new Stack<>());
	}

}
