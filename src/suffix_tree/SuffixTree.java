package suffix_tree;

public class SuffixTree {

	private String text;
	private SuffixNode root;

	public SuffixTree(String text) {
		this.text = text;
		this.root = new SuffixNode(this);
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
		this.root.insert(index);
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

	public SuffixNode searchNode(String text) {
		return this.root.searchNode(text, 0);
	}

}
