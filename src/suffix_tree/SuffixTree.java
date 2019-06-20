package suffix_tree;

import utils.SortablePair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
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

	public int size() {
		return root.getTotalSize();
	}

	/**
	 * Counts the number of appearances of word in the text
	 *
	 * @param word String to count
	 * @return The number of appearances
	 */
	public int count(String word) {
		SuffixNode target = this.root.search(word);
		if (target == null) {
			return 0;
		}
		return target.getLeaves().size();
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
		// Create a priority queue to sort the array
		int queue_size = Math.min(k, strsDepthQ.length);
		PriorityQueue<SortablePair> sorting_queue = new PriorityQueue<>(queue_size + 1);
		// Insert the first k pairs (or less, if result amount is smaller) in the queue
		for (int i = 0; i < queue_size; i++){
			sorting_queue.add(strsDepthQ[i]);
		}
		// Add the remaining pairs one by one, maintaining size
		for (int i = queue_size + 1; i < strsDepthQ.length; i++){
			sorting_queue.offer(strsDepthQ[i]);
			sorting_queue.poll();
		}
		// Copies the strings in the queue to the result array
		int text_index;
		for (int i = 0; i < queue_size; i++) {
			text_index = sorting_queue.poll().getSecond();
			result[queue_size - i - 1] = this.getSubString(text_index, text_index + q);
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
