package suffix_tree;

import java.util.HashMap;

class SuffixNode {
	private HashMap<Character, Edge> edges;
	private SuffixTree tree;


	SuffixNode(SuffixTree tree) {
		edges = new HashMap<>();
		this.tree = tree;
	}

	public void insert(int index) {
		char firstChar = tree.getChar(index);
		if (edges.containsKey(firstChar)) {
			Edge currentEdge = edges.get(firstChar);
			int edge_from = currentEdge.getFrom();
			int division = edge_from;
			for (int i = 1; i < currentEdge.getLen(); i++) {
				if (tree.getChar(index + i) != tree.getChar(edge_from + i)) {
					division = i;
					break;
				}
			}
			if (division == edge_from) {
				currentEdge.getNode().insert(index + currentEdge.getLen());
			} else {
				//TODO: Crear nuevo nodo division
				currentEdge.setFrom(division);
				//TODO: Agregar currentEdge al nodo division nuevo y quitarlo del padre
				//TODO: Crear nodo y edge para el index y agregarlos al division
			}

		} else {
			SuffixNode newNode = new SuffixNode(tree);
			Edge newEdge = new Edge(index, tree.getLastIndex(), newNode);
		}
	}
}
