package net.seiko_comb.combS8214808.joiss2016;

import static net.seiko_comb.combS8214808.joiss2016.Vector.$;
import static net.seiko_comb.combS8214808.joiss2016.Vector.zero;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class EasyTester {

	public static void main(String[] args) {
		new EasyTester();
	}

	private EasyTester() {
		BinaryClassificationChecker checker = new BinaryClassificationChecker(
				new LinearClassifier($(200, 200, 200, 100, 100, -400)), this::randomVector);
		checker.learn(1000000, 100, (w, data) -> {
			if (data.y * w.product(data.x) <= 0) {
				w = w.plus(data.x.product(data.y));
			}
			return w;
		});
		checker.check(100000);
		// attempt(new LinearClassifier($(200, 200, 200, 100, 100, -400)));
	}

	private void attempt(LinearClassifier classifier) {
		List<LearningData> datas = generateData(classifier, 1000).collect(Collectors.toList());
		Vector w = zero(6);
		for (int i = 0; i < 100; i++) {
			for (LearningData data : datas) {
				if (data.y * w.product(data.x) <= 0) {
					w = w.plus(data.x.product(data.y));
				}
			}
		}
		System.out.println("Expect : " + classifier);
		System.out.println("Actual : " + w);

		int testCount = 1000000;
		double successCount = 0;
		LinearClassifier learned = new LinearClassifier(w);
		for (int i = 0; i < testCount; i++) {
			Vector x = randomVector();
			if (classifier.signf(x) * learned.signf(x) > 0)
				successCount++;
		}
		System.out.format("%f\n", successCount / testCount);
	}

	private Stream<LearningData> generateData(LinearClassifier classifier, int n) {
		return IntStream.range(0, n).mapToObj(i -> generateData(classifier));
	}

	private LearningData generateData(LinearClassifier classifier) {
		return new LearningData(classifier, randomVector());
	}

	private Vector randomVector() {
		return $(Math.random(), Math.random(), Math.random(), Math.random(), Math.random());
	}

}
