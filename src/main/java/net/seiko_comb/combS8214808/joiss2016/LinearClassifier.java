package net.seiko_comb.combS8214808.joiss2016;

public class LinearClassifier {
	private Vector w;

	public LinearClassifier(Vector w) {
		this.w = w;
	}

	public double f(Vector x) {
		return w.product(x.addOne());
	}

	public double signf(Vector x) {
		if (f(x) > 0)
			return 1;
		else
			return -1;
	}

	@Override
	public String toString() {
		return String.format("LinearClassifier %s", w);
	}
}
