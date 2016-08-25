package net.seiko_comb.combS8214808.joiss2016.mnist;

import static net.seiko_comb.combS8214808.joiss2016.Vector.$;
import static net.seiko_comb.combS8214808.joiss2016.Vector.zero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.seiko_comb.combS8214808.joiss2016.Vector;

public class MnistReader {
	private Vector[] w = new Vector[10];

	private List<MnistData> trainList, testList;
	private List<Integer> result = new ArrayList<>();

	public MnistReader(List<MnistData> trainList, List<MnistData> testList) {
		this.trainList = trainList;
		this.testList = testList;
	}

	public void read() {
		for (int i = 0; i < 10; i++) {
			w[i] = zero(28 * 28 + 1);
		}
		Collections.shuffle(trainList);
		double eta = 0.01;
		double c = 5.0 / trainList.size();
		for (MnistData data : trainList) {
			Vector x = $(data.getPixel()).addOne();
			for (int i = 0; i < 10; i++) {
				double y = data.getLabel() == i ? 1 : -1;
				// w[i] = perceptron(w[i], x, y);
				// w[i] = Mnist.svm(w[i], x, y, eta, c);
				// w[i] = logistic(w[i], x, y, eta, c);
			}
		}
		double count = 0;
		for (MnistData data : testList) {
			Vector x = $(data.getPixel()).addOne();
			int max = getResult(x);
			if (max == data.getLabel()) {
				count++;
			}
			result.add(max);
		}
		System.out.println(count / testList.size());
	}

	public int getResult(Vector x) {
		double[] score = new double[10];
		for (int i = 0; i < 10; i++) {
			score[i] = w[i].product(x);
		}
		int max = 0;
		for (int i = 1; i < 10; i++) {
			if (score[max] < score[i])
				max = i;
		}
		return max;
	}

}
