package net.seiko_comb.combS8214808.joiss2016;

public class LinearClassifier {
	private Vector w;

	public LinearClassifier(Vector w) {
		this.w = w;
	}

	public double f(Vector x) {
		return w.product(x.addOne());
	}

	@Override
	public String toString() {
		return String.format("LinearClassifier %s", w);
	}
}
