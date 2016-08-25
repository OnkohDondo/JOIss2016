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
	private MnistReader reader;

	private void operate() {
		readData();
		reader = new MnistReader(trainList, testList);
		reader.read();
		finished = true;
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
		trainList = MnistData.getTrainList();
		testList = MnistData.getTestList();
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
		double[] x = getArrayFromImage(resized);
		resized.endDraw();
		int ans = reader.getResult($(x).addOne());
		System.out.println(ans);
	}

	public static double[] getArrayFromImage(PGraphics resized) {
		int w = resized.width, h = resized.height;
		double[] x = new double[w * h];
		for (int i = 0; i < w * h; i++) {
			x[i] = 255 - resized.pixels[i] & 0xff;
		}
		return x;
	}
}
