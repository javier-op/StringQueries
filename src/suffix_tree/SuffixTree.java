package suffix_tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class SuffixTree {

	private String text;
	private SuffixNode root;

	public SuffixTree(String text) {
		this.text = text;
		this.root = new SuffixNode(this);

		for(int i = 0; i < text.length(); i++) {
			this.insert(i);
		}
	}

	public boolean createNodes() {
		try {
			if (this.root.edgeNumber() > 0) {
				// If it's already created, reset it
				this.root = new SuffixNode(this);
			}
			for (int i = text.length() - 1; i >= 0; i--) {
				// Insert every single index of the text
				this.insert(i);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	char getChar(int index) {
		return this.text.charAt(index);
	}

	public void insert(int index) {
		this.root.insert(index, index, new Stack<SuffixNode>());
	}

	int getTextLenght() {
		return this.text.length();
	}

	public String getSubString(int from) {
		return this.text.substring(from);
	}

	public String getSubString(int from, int to) {
		return this.text.substring(from, to);
	}

	public int count(String text) {
		return this.root.search(text).getLeaves().size();
	}

	public Integer[] locate(String text) {
		HashSet<SuffixNode> leaves = this.root.search(text).getLeaves();
		Integer[] output = new Integer[leaves.size()];
		int i = 0;
		for(SuffixNode node : leaves) {
			output[i++] = node.getValue();
		}
		return output;
	}

	private SuffixNode searchNode(String text) {
		return this.root.search(text);
	}

}
