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
		checker.learn(100000, 100, (w, data) -> {
			if (data.y * w.product(data.x) <= 0) {
				w = w.plus(data.x.product(data.y));
			}
			return w;
		});
		checker.check(100000);
		checker.reset();

		for (double cc = 5e-10; cc <= 5e10; cc *= 10) {
			int count = 100000;
			double eta = 0.001;
			double c = cc / count;

			checker = new BinaryClassificationChecker(new LinearClassifier($(2, 2, 2, 1, 1, -4)),
					() -> $(Math.random(), Math.random(), Math.random(), Math.random(), Math.random()));
			checker.learn(count, 1, (w, data) -> {
				Vector x = data.x;
				double y = data.y;
				if (y * w.product(x) <= 1) {
					w = w.plus(x.product(eta * y));
				}
				double last = w.value[w.length - 1];
				w = w.plus(w.product(2 * eta * c * -1));
				w.value[w.length - 1] = last;
				return w;
			});
			System.out.format("c=%.2e ", c);
//			checker.check(100000);

			checker = new BinaryClassificationChecker(new LinearClassifier($(2, 2, 2, 1, 1, -4)),
					() -> $(Math.random(), Math.random(), Math.random(), Math.random(), Math.random()));
			checker.learn(count, 1, (w, data) -> {
				Vector x = data.x;
				double y = data.y;
				w = w.plus(x.product(-y).product(1 - sigmoid(y * w.product(x))).plus(w.product(2 * c)).product(-eta));
				return w;
			});
//			System.out.print("           ");
			checker.check(100000);
		}
		// attempt(new LinearClassifier($(200, 200, 200, 100, 100, -400)));
	}

	private double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
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
