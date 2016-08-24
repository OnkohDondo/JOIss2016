package net.seiko_comb.combS8214808.joiss2016.multiclass;

import static net.seiko_comb.combS8214808.joiss2016.Vector.$;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import net.seiko_comb.combS8214808.joiss2016.BinaryClassificationChecker;
import net.seiko_comb.combS8214808.joiss2016.LinearClassifier;
import net.seiko_comb.combS8214808.joiss2016.Vector;

public class Iris {
	public static void main(String[] args) {
		new Iris();
	}

	private DataFactory iris;

	public Iris() {
		iris = new DataFactory(4, 3);
		iris.setCaption(new String[] { "Iris-setosa", "Iris-versicolor", "Iris-virginica" });
		readData();
		// test(iris, new OneByOneDeterminer(iris));

		DataFactory iris2 = new DataFactory(4, 2);
		iris2.setCaption(new String[] { "Iris-setosa", "Iris-versicolor" });
		try (BufferedReader in = Files.newBufferedReader(Paths.get("input", "iris", "iris.data.txt"))) {
			in.lines().filter(str -> str.length() > 0).limit(100).map(str -> {
				String[] split = str.split(",");
				double[] w = Stream.of(split).limit(4).mapToDouble(Double::parseDouble).toArray();
				int classIndex = iris2.getIndexFromCaption(split[4]);
				Data data = new Data(iris2, $(w), classIndex);
				return data;
			}).forEach(iris2::addData);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		test(iris2, new OneByOneDeterminer(iris2));
	}

	private void readData() {
		try (BufferedReader in = Files.newBufferedReader(Paths.get("input", "iris", "iris.data.txt"))) {
			in.lines().filter(str -> str.length() > 0).map(str -> {
				String[] split = str.split(",");
				double[] w = Stream.of(split).limit(4).mapToDouble(Double::parseDouble).toArray();
				int classIndex = iris.getIndexFromCaption(split[4]);
				Data data = new Data(iris, $(w), classIndex);
				return data;
			}).forEach(iris::addData);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void test(DataFactory factory, Determiner determiner) {
		List<Data> list = new ArrayList<>(factory.getDatas());
		Collections.shuffle(list);
		int count = list.size() * 3 / 5;
		list.stream().limit(count).forEach(determiner::learn);
		double result = list.stream().skip(count).filter(determiner::check).count();
		System.out.format("%.3f%% (%d/%d)%n", result / (list.size() - count) * 100, (int) result,
				(list.size() - count));

		Iterator<Data> iterator = list.iterator();
		BinaryClassificationChecker checker = new BinaryClassificationChecker(new LinearClassifier($(0, 0, 0, 0, 0)),
				() -> iterator.next().getX());
		Iterator<Data> it = list.iterator();
		double eta = 0.001;
		double c = 0.00001;
		checker.learn(60, 1, (w, d) -> {
			Data data = it.next();
			Vector x = data.getX().addOne();
			double y = data.getClassIndex() == 0 ? 1 : -1;
			Vector ww = w;
			if (y * w.product(x) <= 1) {
				w = w.plus(x.product(eta * y));
			}
			// double last = w.value[w.length - 1];
			w = w.plus(ww.product(2 * eta * c * -1));
			return w;
		});
	}
}
