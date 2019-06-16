package suffix_tree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

class SuffixNode {

	private HashMap<Character, Edge> edges;
	private HashSet<SuffixNode> leaves; // Direct link to all leaves that hang from this node.
	private Integer value; // Pointer to text used in leaves.
	private SuffixTree tree;

	SuffixNode(SuffixTree tree) {
		this.edges = new HashMap<>();
		this.leaves = new HashSet<>();
		this.value = null;
		this.tree = tree;
	}

	Integer getValue() {
		return this.value;
	}

	int edgeNumber() {
		return this.edges.size();
	}

	private void addEdge(char character, Edge edge) {
		this.edges.put(character, edge);
	}

	private void setValue(int value) {
		this.value = value;
	}

	 HashSet<SuffixNode> getLeaves() {
		return this.leaves;
	}

	private void addLeaf(SuffixNode node) {
		this.leaves.add(node);
	}

	private void addLeaves(HashSet<SuffixNode> leaves) {
		this.leaves.addAll(leaves);
	}

	void insert(int index, int original_index, Stack<SuffixNode> parents) {
		int remaining_len_from_index = this.tree.getTextLenght() - index;
		char first_char = this.tree.getChar(index);
		if (this.edges.containsKey(first_char)) {
			// Contains an edge that follows the input
			Edge current_edge = this.edges.get(first_char);
			int current_from_index = current_edge.getFrom();
			int chars_to_compare = Math.min(current_edge.length(), remaining_len_from_index);
			int division = 0; // Marks at which index the substrings differ.
			for (int i = 1; i < chars_to_compare ; i++) {
				// Break when the division point is found
				if (this.tree.getChar(index + i) != this.tree.getChar(current_from_index + i)) {
					division = i;
					break;
				}
			}
			if (division == 0) {
				// The substring of the edge and the input are equal
				// -> The input should be inserted following this edge
				parents.push(this);
				current_edge.getNode().insert(index + current_edge.length(), original_index, parents);
			} else {
				// The substring of the edge differs from the input
				// -> The input would create a new node dividing the current edge, then descend from it

				// New intermediate node
				SuffixNode division_node = new SuffixNode(this.tree);
				Edge division_edge = new Edge(Math.min(current_from_index, index), division, division_node);

				// New leaf
				SuffixNode input_node = new SuffixNode(this.tree);
				input_node.setValue(original_index);
				input_node.addLeaf(input_node);
				Edge input_edge = new Edge(index + division, remaining_len_from_index - division, input_node);

				// Add current_edge to division node
				current_edge.setFrom(current_from_index + division);
				division_node.addEdge(this.tree.getChar(current_from_index + division), current_edge);
				division_node.addLeaves(current_edge.getNode().getLeaves());

				// Add input_node to division_node
				division_node.addEdge(this.tree.getChar(index + division), input_edge);
				division_node.addLeaf(input_node);

				// Replace current_edge with division_edge
				this.edges.replace(first_char, division_edge);

				// Add a reference to the new leaf to this node and its parents.
				this.addLeaf(input_node);
				while(!parents.isEmpty()) {
					SuffixNode node = parents.pop();
					node.addLeaf(input_node);
				}
			}
		} else {
			// Doesn't contain an edge that follows the input
			// -> The input fits just to this edge
			// -> Insert a new edge starting in this node
			SuffixNode newNode = new SuffixNode(this.tree);
			newNode.setValue(original_index);
			newNode.addLeaf(newNode);
			Edge newEdge = new Edge(index, remaining_len_from_index, newNode);
			this.edges.put(this.tree.getChar(index), newEdge);

			// Add a reference to the new leaf to this node and its parents.
			this.addLeaf(newNode);
			while(!parents.isEmpty()) {
				SuffixNode node = parents.pop();
				node.addLeaf(newNode);
			}
		}
	}

	public SuffixNode searchNode(String text, int index) {
		int remaining_len = text.length() - index;
		if (remaining_len <= 0) {
			// If the string has been fully consumed, this is the target node
			return this;
		}
		// We'll return an empty node if there's no match and only change this when we find a match
		SuffixNode result = new SuffixNode(this.tree);
		char first_char = text.charAt(index);
		if (this.edges.containsKey(first_char)) {
			Edge fitting_edge = this.edges.get(first_char);
			int chars_to_compare = Math.min(fitting_edge.length(), remaining_len);
			int starting_char = fitting_edge.getFrom();
			String substr_to_compare = this.tree.getSubString(starting_char, starting_char + chars_to_compare - 1);
			if (substr_to_compare.equals(text.substring(index, index + chars_to_compare - 1))) {
				// If the edge fits, follow the edge in the search
				result = fitting_edge.getNode().searchNode(text, index + chars_to_compare - 1);
			}
		}
		return result;
	}

	public SuffixNode search(String text) {
		char first_char = text.charAt(0);
		SuffixNode output = null;
		if(this.edges.containsKey(first_char)) {
			Edge fitting_edge = this.edges.get(first_char);
			int text_len = text.length();
			int edge_len = fitting_edge.length();
			int edge_from = fitting_edge.getFrom();

			if(text.length() <= edge_len) {
				if(text.equals(this.tree.getSubString(edge_from, edge_from + text_len))) {
					output = fitting_edge.getNode();
				}
			} else {
				if(text.substring(0, edge_len).equals(this.tree.getSubString(edge_from, edge_from + edge_len))) {
					output = fitting_edge.getNode().search(text.substring(edge_len));
				}
			}
		}
		return output;
	}
}
