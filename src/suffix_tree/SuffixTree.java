package suffix_tree;

public class SuffixTree {

	private String text;
	private SuffixNode root;

	public SuffixTree(String text) {
		this.text = text;
		this.root = new SuffixNode(this);
	}

	char getChar(int index) {
		return this.text.charAt(index);
	}

	public void insert(int index) {
		this.root.insert(index, index);
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

	public SuffixNode searchNode() {
		// TODO: Implement?
		return this.root;
	}

}
