package utils;

public class SortablePair<R> implements Comparable<SortablePair<R>> {
	private final Integer first;
	private final R second;

	public SortablePair(Integer first, R second) {
		this.first = first;
		this.second = second;
	}

	public Integer getFirst() {
		return this.first;
	}

	public R getSecond() {
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
	public int compareTo(SortablePair<R> o) {
		return this.first - o.getFirst();
	}
}
