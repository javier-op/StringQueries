package suffix_tree;

class Edge {

	private int from;
	private int len;
	private SuffixNode node;

	public Edge(int from, int len, SuffixNode node) {
		this.from = from;
		this.len = len;
		this.node = node;
	}

	public int getFrom() {
		return this.from;
	}

	public void setFrom(int i) {
		int prev_from = this.from;
		this.from = i;
		this.len = prev_from + len - this.len;
	}

	public int getLen() {
		return this.len;
	}

	public SuffixNode getNode() {
		return this.node;
	}
}
