package suffix_tree;

import java.util.HashMap;

class SuffixNode {

	private int from;
	private int len;
	private HashMap<Character, SuffixNode> children;
	private SuffixTree tree;

	SuffixNode(SuffixTree tree) {
		this.children = new HashMap<>();
		this.tree = tree;
	}

	private SuffixNode(SuffixTree tree, int from, int len) {
		this(tree);
		this.from = from;
		this.len = len;
	}

	void insert(int index, int start_of_suffix) {
		int remaining_len_from_index = this.tree.getTextLenght() - index - 1;
		char first_char = this.tree.getChar(index);
		if (this.children.containsKey(first_char)) {
			SuffixNode next_node = this.children.get(first_char);
			int chars_to_compare = Math.min(remaining_len_from_index, next_node.getLen());
			int division_index = -1;
			for (int i = 1; i < chars_to_compare; i++) {
				// Break when the division point is found
				if (this.tree.getChar(index + i) != this.tree.getChar(next_node.getFrom() + i)) {
					division_index = i;
					break;
				}
			}
			if (division_index < 0) {
				// The substring of the edge and the input are equal
				// -> The input should be inserted following this edge
				next_node.insert(index + next_node.getLen(), start_of_suffix);
			} else {
				// The substring of the edge differs from the input
				// -> The input would create a new node dividing the current edge, then descend from it
				int length_diff = division_index - next_node.getFrom();
				SuffixNode division_node = new SuffixNode(this.tree, next_node.getFrom(), length_diff);
				SuffixNode input_node = new SuffixNode(this.tree, division_index, remaining_len_from_index);
				// Replace current_edge with division_edge
				this.children.replace(first_char, division_node);
				// Add current_edge to division node
				next_node.setFrom(division_index);
				division_node.addChild(this.tree.getChar(division_index), next_node);
				// Add input_node to division_node
				division_node.addChild(this.tree.getChar(index + length_diff), input_node);
			}
		} else {
			// Doesn't contain an edge that follows the input
			// -> The input fits just to this edge
			// -> Insert a new edge starting in this node
			SuffixNode newNode = new SuffixNode(this.tree, index, remaining_len_from_index);
			this.children.put(this.tree.getChar(index), newNode);
		}
	}

	private void addChild(char c, SuffixNode node) {
		children.put(c, node);
	}

	private int getFrom() {
		return from;
	}

	private int getLen() {
		return len;
	}

	private void setFrom(int from) {
		this.from = from;
	}
}
