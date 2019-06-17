package suffix_tree;

import utils.SortablePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Stack;

public class SuffixTree {

	private String text;
	private SuffixNode root;

	public SuffixTree(String text) {
		this.text = text;
		this.root = new SuffixNode(this);

		for (int i = 0; i < text.length(); i++) {
			this.insert(i);
		}
	}

	public int count(String text) {
		return this.root.search(text).getLeaves().size();
	}

	public Integer[] locate(String text) {
		HashSet<SuffixNode> leaves = this.root.search(text).getLeaves();
		Integer[] output = new Integer[leaves.size()];
		int i = 0;
		for (SuffixNode node : leaves) {
			output[i++] = node.getValue();
		}
		return output;
	}

	public String[] top_K_Q(int k, int q) {
		String[] result = new String[k];
		// Get all the strings of length q with their number of appearances
		ArrayList<SortablePair> stringsDepthQ = this.root.getStrTimesByLenght(q, "");
		// Turn to array, to be able to sort in-place and in parallel, if possible
		// https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Arrays.html#parallelSort(T[])
		SortablePair[] strsDepthQ = new SortablePair[stringsDepthQ.size()];
		stringsDepthQ.toArray(strsDepthQ);
		// Uses parallelSort to sort the array for the number of appearances ascending
		Arrays.parallelSort(strsDepthQ);
		// Copies the strings in the last elements of the array
		for (int i = 0; i < k; i++) {
			result[i] = strsDepthQ[strsDepthQ.length - i - 1].getSecond();
		}
		return result;
	}

	char getChar(int index) {
		return this.text.charAt(index);
	}

	int getTextLenght() {
		return this.text.length();
	}

	String getSubString(int from, int to) {
		return this.text.substring(from, to);
	}

	private void insert(int index) {
		this.root.insert(index, index, new Stack<>());
	}

}
