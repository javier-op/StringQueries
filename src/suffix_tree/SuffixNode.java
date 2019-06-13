package suffix_tree;

import java.util.HashMap;

class SuffixNode {

	private HashMap<Character, Edge> edges;
	private SuffixTree tree;

	SuffixNode(SuffixTree tree) {
		this.edges = new HashMap<>();
		this.tree = tree;
	}

	int edgeNumber() {
		return this.edges.size();
	}

	private void addEdge(char character, Edge edge) {
		this.edges.put(character, edge);
	}

	void insert(int index) {
		int remaining_len_from_index = this.tree.getTextLenght() - index - 1;
		char first_char = this.tree.getChar(index);
		if (this.edges.containsKey(first_char)) {
			// Contains an edge that follows the input
			Edge current_edge = this.edges.get(first_char);
			int edge_from_index = current_edge.getFrom();
			int edge_length = current_edge.getLen();
			int chars_to_compare = edge_length;
			int division = edge_from_index;
			if (edge_length > remaining_len_from_index) {
				// Current edge is longer than the input
				// -> Input would be a new node dividing the current edge
				// As all input comes from the same text and it is terminated with an special char,
				// it will necessarily differ somewhere in the remaining chars
				chars_to_compare = remaining_len_from_index;
			}
			for (int i = 1; i < chars_to_compare; i++) {
				// Break when the division point is found
				if (this.tree.getChar(index + i) != this.tree.getChar(edge_from_index + i)) {
					division = i;
					break;
				}
			}
			if (division == edge_from_index) {
				// The substring of the edge and the input are equal
				// -> The input should be inserted following this edge
				current_edge.getNode().insert(index + edge_length);
			} else {
				// The substring of the edge differs from the input
				// -> The input would create a new node dividing the current edge, then descend from it
				int length_diff = division - edge_from_index;
				SuffixNode division_node = new SuffixNode(this.tree);
				SuffixNode input_node = new SuffixNode(this.tree);
				Edge division_edge = new Edge(edge_from_index, length_diff, division_node);
				Edge input_edge = new Edge(division, remaining_len_from_index - length_diff, input_node);
				// Replace current_edge with division_edge
				this.edges.replace(first_char, division_edge);
				// Add current_edge to division node
				current_edge.setFrom(division);
				division_node.addEdge(this.tree.getChar(division), current_edge);
				// Add input_node to division_node
				division_node.addEdge(this.tree.getChar(index + length_diff), input_edge);
			}
		} else {
			// Doesn't contain an edge that follows the input
			// -> The input fits just to this edge
			// -> Insert a new edge starting in this node
			SuffixNode newNode = new SuffixNode(this.tree);
			Edge newEdge = new Edge(index, remaining_len_from_index, newNode);
			this.edges.put(this.tree.getChar(index), newEdge);
		}
	}
}
