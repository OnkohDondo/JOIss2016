package net.seiko_comb.combS8214808.joiss2016.mnist;

import static net.seiko_comb.combS8214808.joiss2016.Vector.*;
import java.io.BufferedInputStream;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.seiko_comb.combS8214808.joiss2016.Vector;
import processing.core.PApplet;

public class Mnist extends PApplet {
	public static void main(String[] args) {
		PApplet.main(Mnist.class.getName());
		// new Mnist().operate();
	}

	private void operate() {
		readData();

		Vector[] w = new Vector[10];
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
				w[i] = svm(w[i], x, y, eta, c);
			}
		}
		double count = 0;
		for (MnistData data : testList) {
			Vector x = $(data.getPixel()).addOne();
			double[] score = new double[10];
			for (int i = 0; i < 10; i++) {
				score[i] = w[i].product(x);
			}
			int max = 0;
			for (int i = 1; i < 10; i++) {
				if (score[max] < score[i])
					max = i;
			}
			if (max == data.getLabel()) {
				count++;
			}
			result.add(max);
		}
		System.out.println(count / testList.size());
	}

	public static Vector perceptron(Vector w, Vector x, double y) {
		if (y * w.product(x) <= 0) {
			w = w.plus(x.product(y));
		}
		return w;
	}

	public static Vector svm(Vector w, Vector x, double y, double eta, double c) {
		Vector ww = w;
		if (y * w.product(x) <= 0) {
			w = w.plus(x.product(y * eta));
		}
		w = w.minus(ww.product(2 * eta * c));
		return w;
	}

	private List<MnistData> trainList, testList;
	private List<Integer> result = new ArrayList<>();

	private void readData() {
		trainList = readData("train-images.idx3-ubyte", "train-labels.idx1-ubyte");
		testList = readData("t10k-images.idx3-ubyte", "t10k-labels.idx1-ubyte");
	}

	private List<MnistData> readData(String imageFileName, String labelFileName) {
		List<MnistData> ret = new ArrayList<>();
		try (DataInputStream imageIn = new DataInputStream(
				new BufferedInputStream(Files.newInputStream(Paths.get("input", "mnist", imageFileName))));
				DataInputStream labelIn = new DataInputStream(
						new BufferedInputStream(Files.newInputStream(Paths.get("input", "mnist", labelFileName))))) {
			if (imageIn.readInt() != 0x803)
				throw new RuntimeException();
			if (labelIn.readInt() != 0x801)
				throw new RuntimeException();
			int count = imageIn.readInt();
			if (count != labelIn.readInt())
				throw new RuntimeException();
			int w = imageIn.readInt(), h = imageIn.readInt();
			if (w != 28 || h != 28)
				throw new RuntimeException();
			for (int i = 0; i < count; i++) {
				int label = labelIn.readByte() & 0xff;
				int[] pixel = new int[w * h];
				for (int j = 0; j < w * h; j++) {
					pixel[j] = imageIn.readByte() & 0xff;
				}
				ret.add(new MnistData(label, pixel));
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return ret;
	}

	public void settings() {
		size(140, 140);
	}

	public void setup() {
		operate();
	}

	private int index = 0;

	public void draw() {
		image(testList.get(index).getImage(this), 0, 0, 140, 140);
		fill(255, 0, 0);
		textAlign(LEFT);
		text(testList.get(index).getLabel(), 0, height);
		fill(0, 0, 255);
		textAlign(RIGHT);
		text(result.get(index), width, height);
	}

	public void keyTyped() {
		if (key == 'j')
			index++;
		if (key == 'k')
			index--;
		index = (index + testList.size()) % testList.size();
	}
}
