package net.seiko_comb.combS8214808.joiss2016.mnist;

import static net.seiko_comb.combS8214808.joiss2016.Vector.$;

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
	private boolean finished = false;
	private MnistReader reader;

	private void operate() {
		readData();
		reader = new OneByOneReader(trainList, testList);
		// reader.load();
		// reader.read();
		finished = true;
		System.out.println("Ready");
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

	private PGraphics resized;

	public void setup() {
		new Thread(this::operate).start();
		resized = createGraphics(28, 28);
	}

	private int index = 0;

	public void draw() {
		if (finished) {
			// image(testList.get(index).getImage(this), 0, 0, 140, 140);
			// fill(255, 0, 0);
			// textAlign(LEFT);
			// text(testList.get(index).getLabel(), 0, height);
			// fill(0, 0, 255);
			// textAlign(RIGHT);
			// text(reader.getResult().get(index), width, height);
		}
	}

	public void keyTyped() {
		if (key == 'j')
			index++;
		if (key == 'k')
			index--;
		index = (index + testList.size()) % testList.size();
		if (key == 'q')
			background(255);
		if ('0' <= key && key <= '9') {
			int label = key - '0';
			Vector vector = getVector();
			int result = reader.getResult(vector);
			reader.learnData(vector, label, 0.05, 0.2);
			System.out.format("learned %d->%d%n", result, label);
			reader.save();
			background(255);
		}
	}

	public void mousePressed() {
	}

	public void mouseDragged() {
		float v = dist(pmouseX, pmouseY, mouseX, mouseY);
		stroke(0, 255 - v);
		strokeWeight(5);
		line(pmouseX, pmouseY, mouseX, mouseY);
	}

	public void mouseReleased() {
		Vector vector = getVector();
		int ans = reader.getResult(vector);
//		System.out.println(ans);
	}

	private Vector getVector() {
		PImage cut = get();
		resized.beginDraw();
		resized.copy(cut, 0, 0, cut.width, cut.height, 0, 0, 28, 28);
		resized.loadPixels();
		double[] x = getArrayFromImage(resized);
		resized.endDraw();
		Vector vector = $(x).addOne();
		return vector;
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
