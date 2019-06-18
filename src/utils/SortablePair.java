package utils;

public class SortablePair implements Comparable<SortablePair> {
	private final Integer first;
	private final Integer second;

	public SortablePair(Integer first, Integer second) {
		this.first = first;
		this.second = second;
	}

	public Integer getFirst() {
		return this.first;
	}

	public Integer getSecond() {
		return this.second;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SortablePair)) return false;
		SortablePair pair_object = (SortablePair) o;
		return this.first.equals(pair_object.getFirst()) &&
				this.second.equals(pair_object.getSecond());
	}

	@Override
	public int compareTo(SortablePair o) {
		return this.first - o.getFirst();
	}
}
