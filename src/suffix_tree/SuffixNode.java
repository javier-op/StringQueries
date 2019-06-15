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
		int remaining_len_from_index = this.tree.getTextLenght() - index;
		char first_char = this.tree.getChar(index);
		if (this.edges.containsKey(first_char)) {
			// Contains an edge that follows the input
			Edge current_edge = this.edges.get(first_char);
			int current_from_index = current_edge.getFrom();
			int chars_to_compare = Math.min(current_edge.getLen(), remaining_len_from_index);
			int division = 0;
			for (int i = 1; i < chars_to_compare ; i++) {
				// Break when the division point is found
				char insert_char = this.tree.getChar(index + i);
				char edge_char = this.tree.getChar(current_from_index + i);
				if (this.tree.getChar(index + i) != this.tree.getChar(current_from_index + i)) {
					division = i;
					break;
				}
			}
			if (division == 0) {
				// The substring of the edge and the input are equal
				// -> The input should be inserted following this edge
				current_edge.getNode().insert(index + current_edge.getLen());
			} else {
				// The substring of the edge differs from the input
				// -> The input would create a new node dividing the current edge, then descend from it
				int new_from = Math.min(current_from_index, index);
				SuffixNode division_node = new SuffixNode(this.tree);
				SuffixNode input_node = new SuffixNode(this.tree);
				Edge division_edge = new Edge(new_from, division, division_node);
				Edge input_edge = new Edge(index + division, remaining_len_from_index - division, input_node);
				// Replace current_edge with division_edge
				this.edges.replace(first_char, division_edge);
				// Add current_edge to division node
				current_edge.setFrom(current_from_index + division);
				division_node.addEdge(this.tree.getChar(current_from_index + division), current_edge);
				// Add input_node to division_node
				division_node.addEdge(this.tree.getChar(index + division), input_edge);
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

	public SuffixNode searchNode(String text, int index) {
		int remaining_len = text.length() - index - 1;
		if (remaining_len <= 0) {
			// If the string has been fully consumed, this is the target node
			return this;
		}
		// We'll return an empty node if there's no match and only change this when we find a match
		SuffixNode result = new SuffixNode(this.tree);
		char first_char = text.charAt(index);
		if (this.edges.containsKey(first_char)) {
			Edge fitting_edge = this.edges.get(first_char);
			int edge_len = fitting_edge.getLen();
			int chars_to_compare = edge_len;
			if (edge_len > remaining_len) {
				// Fitting edge is longer than the input
				// -> If all the chars remaining fit, the node following the edge is the search result
				chars_to_compare = remaining_len;
			}
			int starting_char = fitting_edge.getFrom();
			String substr_to_compare = this.tree.getSubString(starting_char, starting_char + chars_to_compare - 1);
			if (substr_to_compare.equals(text.substring(index, index + chars_to_compare - 1))) {
				// If the edge fits, follow the edge in the search
				result = fitting_edge.getNode().searchNode(text, index + chars_to_compare - 1);
			}
		}
		return result;
	}
}
