package suffix_tree;

import utils.SortablePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

class SuffixNode {

	private HashMap<Character, Edge> edges;
	private HashSet<SuffixNode> leaves; // Direct link to all leaves that hang from this node.
	private Integer value; // Pointer to text used in leaves.
	private SuffixTree tree;
	private int total_size;

	SuffixNode(SuffixTree tree) {
		this.edges = new HashMap<>();
		this.leaves = new HashSet<>();
		this.value = null;
		this.tree = tree;
		this.total_size = 1;
	}

	Integer getValue() {
		return this.value;
	}

	private void setValue(int value) {
		this.value = value;
	}

	HashSet<SuffixNode> getLeaves() {
		return this.leaves;
	}

	public ArrayList<SortablePair> getStrTimesByLenght(int depth) {
		ArrayList<SortablePair> result = new ArrayList<>();
		if (depth <= 0) {
			// If the depth has been reached
			// Add the amount of leaves and the text position of one of the leaves to the result
			Iterator<SuffixNode> leaves = this.leaves.iterator();
			result.add(new SortablePair(this.leaves.size(), leaves.next().value));
		} else {
			// Recursively get nodes with more depth through all the edges from this node
			Edge edge;
			int edge_len;
			for (char c : this.edges.keySet()) {
				// Get an edge, diminish depth with its length and get nodes by depth in it
				edge = this.edges.get(c);
				edge_len = edge.length();
				result.addAll(edge.getNode().getStrTimesByLenght(depth - edge_len));
			}
		}
		return result;
	}

    /**
     * Inserts in the node the substring of text in the tree, from index to end
     * @param index Starting position from which to insert in this node
     * @param original_index Starting position of the original substring inserted
     * @param parents Stack of parents of this node
     */
	void insert(int index, int original_index, Stack<SuffixNode> parents) {
		int remaining_len_from_index = this.tree.getTextLenght() - index;
		char first_char = this.tree.getChar(index);
		if (this.edges.containsKey(first_char)) {
			// Contains an edge that follows the input
			Edge current_edge = this.edges.get(first_char);
			int current_from_index = current_edge.getFrom();
			int chars_to_compare = Math.min(current_edge.length(), remaining_len_from_index);
			int division = 0; // Marks at which index the substrings differ.
			for (int i = 1; i < chars_to_compare; i++) {
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
				division_node.setTotalSize(current_edge.getNode().getTotalSize() + 2);
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
				while (!parents.isEmpty()) {
					SuffixNode node = parents.pop();
					node.addLeaf(input_node);
					node.incrementTotalSize(2);
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
			this.incrementTotalSize(1);

			// Add a reference to the new leaf to this node and its parents.
			this.addLeaf(newNode);
			while (!parents.isEmpty()) {
				SuffixNode node = parents.pop();
				node.addLeaf(newNode);
				node.incrementTotalSize(1);
			}
		}
	}

    /**
     * Searches the node in this subtree that matches with word
     * @param word String to match
     * @return A node that matches, or null if none exists.
     */
	SuffixNode search(String word) {
		char first_char = word.charAt(0);
		SuffixNode output = null;
		if (this.edges.containsKey(first_char)) {
			Edge fitting_edge = this.edges.get(first_char);
			int text_len = word.length();
			int edge_len = fitting_edge.length();
			int edge_from = fitting_edge.getFrom();

			if (word.length() <= edge_len) {
				if (word.equals(this.tree.getSubString(edge_from, edge_from + text_len))) {
					output = fitting_edge.getNode();
				}
			} else {
				if (word.substring(0, edge_len).equals(this.tree.getSubString(edge_from, edge_from + edge_len))) {
					output = fitting_edge.getNode().search(word.substring(edge_len));
				}
			}
		}
		return output;
	}

	private void addEdge(char character, Edge edge) {
		this.edges.put(character, edge);
	}

	private void addLeaf(SuffixNode node) {
		this.leaves.add(node);
	}

	private void addLeaves(HashSet<SuffixNode> leaves) {
		this.leaves.addAll(leaves);
	}

	private void setTotalSize(int n) {
		this.total_size = n;
	}

	private void incrementTotalSize(int n) {
		this.total_size += n;
	}

	public int getTotalSize() {
		return this.total_size;
	}
}
