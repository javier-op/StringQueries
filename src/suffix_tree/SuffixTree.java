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

	/**
	 * Counts the number of appearances of word in the text
	 *
	 * @param word String to count
	 * @return The number of appearances
	 */
	public int count(String word) {
		return this.root.search(word).getLeaves().size();
	}

	/**
	 * Locates the appearances of word in the text
	 * @param word String to locate
	 * @return Array of integers, the positions of word in the text
	 */
	public Integer[] locate(String word) {
		SuffixNode target = this.root.search(word);
		if (target == null) {
			return new Integer[0];
		}
		HashSet<SuffixNode> leaves = target.getLeaves();
		Integer[] output = new Integer[leaves.size()];
		int i = 0;
		for (SuffixNode node : leaves) {
			output[i++] = node.getValue();
		}
		return output;
	}


	/**
	 * Returns the k strings of length q with more appearances in the text
	 *
	 * @param k Amount of strings to get
	 * @param q Length of the strings to search
	 * @return Array of the k strings of length q with more appearances in the text
	 */
	public String[] top_K_Q(int k, int q) {
		String[] result = new String[k];
		// Get all the strings of length q with their number of appearances
		ArrayList<SortablePair> stringsDepthQ = this.root.getStrTimesByLenght(q);
		// Turn to array, to be able to sort in-place and in parallel, if possible
		// https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Arrays.html#parallelSort(T[])
		SortablePair[] strsDepthQ = new SortablePair[stringsDepthQ.size()];
		stringsDepthQ.toArray(strsDepthQ);
		// Uses parallelSort to sort the array for the number of appearances ascending
		Arrays.parallelSort(strsDepthQ);
		// Copies the strings in the last elements of the array
		int strs_depth_q_len = strsDepthQ.length;
		int text_index;
		for (int i = 0; i < k; i++) {
			if (strs_depth_q_len - i > 0) {
				text_index = strsDepthQ[strs_depth_q_len - i - 1].getSecond();
				result[i] = this.getSubString(text_index, text_index + q);
			} else {
				result[i] = null;
			}
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

	/**
	 * Inserts in the tree the substring of the text, from index to end
	 * @param index Starting position from which to insert
	 */
	private void insert(int index) {
		this.root.insert(index, index, new Stack<>());
	}

}
