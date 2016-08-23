package net.seiko_comb.combS8214808.joiss2016;

public class LearningData {
	public final Vector x;
	public final double y;

	public LearningData(LinearClassifier classifier, Vector x) {
		this.x = x.addOne();
		if (classifier.f(x) > 0)
			y = +1;
		else
			y = -1;
	}
	
	@Override
	public String toString() {
		return String.format("[%s -> %d]", x, (int)y);
	}
}
