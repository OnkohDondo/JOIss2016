package net.seiko_comb.combS8214808.joiss2016.multiclass;

import static net.seiko_comb.combS8214808.joiss2016.Vector.$;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Iris {
	public static void main(String[] args) {
		new Iris();
	}

	private DataFactory iris;

	public Iris() {
		iris = new DataFactory(3);
		iris.setCaption(new String[] { "Iris-setosa", "Iris-versicolor", "Iris-virginica" });
		readData();
		test(null);
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

	private void test(Determiner determiner) {
	}
}
