package net.seiko_comb.combS8214808.joiss2016.mnist;

import static net.seiko_comb.combS8214808.joiss2016.Vector.$;
import static net.seiko_comb.combS8214808.joiss2016.Vector.zero;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.seiko_comb.combS8214808.joiss2016.Vector;

public class MnistReader {
	private Vector[] w = new Vector[10];

	protected List<MnistData> trainList, testList;
	private List<Integer> result = new ArrayList<>();

	public MnistReader(List<MnistData> trainList, List<MnistData> testList) {
		this.trainList = trainList;
		this.testList = testList;
		for (int i = 0; i < 10; i++) {
			w[i] = zero(28 * 28 + 1);
		}
	}

	public void read() {
		Collections.shuffle(trainList);
		double eta = 0.01;
		double c = 5.0 / trainList.size();
		for (MnistData data : trainList) {
			Vector x = $(data.getPixel()).addOne();
			int label = data.getLabel();
			learnData(x, label, eta, c);
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

	public void learnData(Vector x, int label, double eta, double c) {
		for (int i = 0; i < 10; i++) {
			double y = label == i ? 1 : -1;
			// w[i] = perceptron(w[i], x, y);
			w[i] = Mnist.svm(w[i], x, y, 0.0002, 0.05);
			// w[i] = logistic(w[i], x, y, eta, c);
		}
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

	public void save() {
		try (DataOutputStream out = new DataOutputStream(new FileOutputStream(new File("tmp/w")))) {
			for(Vector v : w){
				double[] ww = v.value;
				for (int j = 0; j < 28 * 28 + 1; j++) {
					out.writeDouble(ww[j]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() {
		try (DataInputStream in = new DataInputStream(
				new BufferedInputStream(new FileInputStream(new File("tmp/w"))))) {
			for (int i = 0; i < 10; i++) {
				double[] ww = w[i].value;
				for (int j = 0; j < 28 * 28 + 1; j++) {
					ww[j] = in.readDouble();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(w[0]);
	}

	public List<Integer> getResult() {
		return result;
	}
}
