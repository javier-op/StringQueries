package utils;

public class Pair<L, R> {
	private final L first;
	private final R second;

	public Pair(L first, R second) {
		this.first = first;
		this.second = second;
	}

	public L getFirst() {
		return this.first;
	}

	public R getSecond() {
		return this.second;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Pair)) return false;
		Pair pair_object = (Pair) o;
		return this.first.equals(pair_object.getFirst()) &&
				this.second.equals(pair_object.getSecond());
	}

}
