package net.seiko_comb.combS8214808.joiss2016.mnist;

import static net.seiko_comb.combS8214808.joiss2016.Vector.zero;

import java.util.List;

import net.seiko_comb.combS8214808.joiss2016.Vector;

public class OneByOneReader extends MnistReader {

	private Vector[] w;

	public OneByOneReader(List<MnistData> trainList, List<MnistData> testList) {
		super(trainList, testList);
		w = new Vector[9 * 10 / 2];
		for (int i = 0, k = 0; i < 10; i++) {
			for (int j = i + 1; j < 10; j++, k++) {
				w[k] = zero(28 * 28 + 1);
			}
		}
	}

	@Override
	public void learnData(Vector x, int label, double eta, double c) {
		for (int i = 0, k = 0; i < 10; i++) {
			for (int j = i + 1; j < 10; j++, k++) {
				double y;
				if (i == label)
					y = 1;
				else if (j == label)
					y = -1;
				else
					continue;
				w[k] = Mnist.svm(w[k], x, y, 0.0002, 0.05);
			}
		}
	}

	@Override
	public int getResult(Vector x) {
		int[] vote = new int[10];
		for (int i = 0, k = 0; i < 10; i++) {
			for (int j = i + 1; j < 10; j++, k++) {
				if (w[k].product(x) > 0) {
					vote[i]++;
				} else {
					vote[j]++;
				}
			}
		}
		int max = 0;
		for (int i = 1; i < 10; i++) {
			if (vote[max] < vote[i])
				max = i;
		}
		return max;
	}

}
