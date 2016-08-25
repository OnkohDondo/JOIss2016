package net.seiko_comb.combS8214808.joiss2016.mnist;

import static net.seiko_comb.combS8214808.joiss2016.Vector.$;
import static net.seiko_comb.combS8214808.joiss2016.Vector.zero;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.seiko_comb.combS8214808.joiss2016.Vector;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class Mnist extends PApplet {
	public static void main(String[] args) {
		PApplet.main(Mnist.class.getName());
		// new Mnist().operate();
	}

	private List<MnistData> trainList, testList;
	private List<Integer> result = new ArrayList<>();
	private boolean finished = false;
	private Vector[] w = new Vector[10];

	private void operate() {
		readData();

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
				w[i] = perceptron(w[i], x, y);
				// w[i] = svm(w[i], x, y, eta, c);
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
		finished = true;
	}

	private int getResult(Vector x) {
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

	public static Vector logistic(Vector w, Vector x, double y, double eta, double c) {
		w = w.plus(x.product(-y).product(1 - sigmoid(y * w.product(x))).plus(w.product(2 * c)).product(-eta));
		return w;
	}

	public static double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}

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
		new Thread(this::operate).start();
	}

	private int index = 0;

	public void draw() {
	}

	public void keyTyped() {
		background(255);
	}

	public void mousePressed() {
	}

	public void mouseDragged() {
		float v = dist(pmouseX, pmouseY, mouseX, mouseY);
		stroke(0);
		strokeWeight(15);
		line(pmouseX, pmouseY, mouseX, mouseY);
	}

	public void mouseReleased() {
		PImage cut = get();
		PGraphics resized = createGraphics(28, 28);
		resized.beginDraw();
		resized.copy(cut, 0, 0, cut.width, cut.height, 0, 0, 28, 28);
		resized.loadPixels();
		double[] x = new double[28 * 28];
		for (int i = 0; i < 28 * 28; i++) {
			x[i] = 255 - resized.pixels[i] & 0xff;
		}
		resized.endDraw();
		int ans = getResult($(x).addOne());
		System.out.println(ans);
	}
}
