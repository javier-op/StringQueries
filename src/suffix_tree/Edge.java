package suffix_tree;

class Edge {

	private int from;
	private int len;
	private SuffixNode node;

	Edge(int from, int len, SuffixNode node) {
		this.from = from;
		this.len = len;
		this.node = node;
	}

	int getFrom() {
		return this.from;
	}

	void setFrom(int i) {
		int prev_from = this.from;
		this.from = i;
		this.len = prev_from + this.len - this.from;
	}

	int getLen() {
		return this.len;
	}

	SuffixNode getNode() {
		return this.node;
	}
}
