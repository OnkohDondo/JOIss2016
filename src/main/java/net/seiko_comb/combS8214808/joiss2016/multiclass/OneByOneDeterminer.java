package net.seiko_comb.combS8214808.joiss2016.multiclass;

import java.util.Arrays;
import java.util.stream.IntStream;

import net.seiko_comb.combS8214808.joiss2016.Vector;

public class OneByOneDeterminer implements Determiner {
	private DataFactory dataFactory;
	private Vector[] w;

	public OneByOneDeterminer(DataFactory dataFactory) {
		this.dataFactory = dataFactory;
		int count = dataFactory.getClassCount();
		w = new Vector[count * (count - 1) / 2];
		IntStream.range(0, w.length).forEach(i -> w[i] = Vector.zero(dataFactory.getInputDimension() + 1));
	}

	public void learn(Data data) {
		if (data.getDataSet() != dataFactory)
			throw new RuntimeException("Incorrect data : wrong data set");

		double eta = 0.001;
		double c = 0.00001;
		int count = dataFactory.getClassCount();

		Vector x = data.getX().addOne();
		for (int k = 0, i = 0; i < count; i++) {
			for (int j = i + 1; j < count; j++, k++) {
				double y;
				if (data.getClassIndex() == i) {
					y = 1;
				} else if (data.getClassIndex() == j) {
					y = -1;
				} else
					continue;
				System.out.println(data.getX());
				// if (y * w[k].product(x) <= 0) {
				// w[k] = w[k].plus(x.product(y));
				// }
				Vector ww = w[k];
				if (y * w[k].product(x) <= 1) {
					w[k] = w[k].plus(x.product(eta * y));
				}
//				double last = w[k].value[w[k].length - 1];
				w[k] = w[k].plus(ww.product(2 * eta * c * -1));
//				w[k].value[w[k].length - 1] = last;
			}
		}
	}

	@Override
	public boolean check(Data data) {
		int count = dataFactory.getClassCount();
		int[] vote = new int[count];
		Vector x = data.getX().addOne();
		for (int k = 0, i = 0; i < count; i++) {
			for (int j = i + 1; j < count; j++, k++) {
//				System.out.format("%.3f%n", w[k].product(x));
				if (w[k].product(x) > 0)
					vote[i]++;
				else
					vote[j]++;
			}
		}
//		System.out.println(Arrays.toString(vote));
		int mx = 0;
		for (int i = 1; i < count; i++)
			if (vote[mx] < vote[i])
				mx = i;
		boolean accepted = data.getClassIndex() == mx;
//		System.out.format("Answer:%d, Result:%d, Verdict:%s%n", data.getClassIndex(), mx,
//				accepted ? "Accepted" : "Denied");
		return accepted;
	}
}
