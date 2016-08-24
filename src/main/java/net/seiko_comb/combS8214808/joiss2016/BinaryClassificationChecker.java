package net.seiko_comb.combS8214808.joiss2016;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BinaryClassificationChecker {

	private LinearClassifier classifier;
	private Vector w;
	private Supplier<Vector> testSupplier;

	public BinaryClassificationChecker(LinearClassifier classifier, Supplier<Vector> testSupplier) {
		this.classifier = classifier;
		this.testSupplier = testSupplier;
		reset();
	}

	public void learn(int size, int count, BiFunction<Vector, LearningData, Vector> reloader) {
		List<LearningData> datas = IntStream.range(0, size).mapToObj(i -> generateData()).collect(Collectors.toList());
		for (int i = 0; i < count; i++) {
			for (LearningData data : datas) {
				w = reloader.apply(w, data);
			}
		}
		// System.out.println(w);
	}

	public void check(int size) {
		LinearClassifier learned = new LinearClassifier(w);
		double successCount = 0;
		for (int i = 0; i < size; i++) {
			Vector x = testSupplier.get();
			if (classifier.signf(x) * learned.signf(x) > 0)
				successCount++;
		}
		System.out.format("Result : %.5f%% (%d/%d)\n", successCount / size * 100, (int) successCount, size);
	}

	public void reset() {
		this.w = Vector.zero(classifier.w.length);
	}

	private LearningData generateData() {
		return new LearningData(classifier, testSupplier.get());
	}
}
