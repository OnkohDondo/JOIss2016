package net.seiko_comb.combS8214808.joiss2016.mnist;

import static net.seiko_comb.combS8214808.joiss2016.Vector.$;
import static net.seiko_comb.combS8214808.joiss2016.Vector.zero;

import java.util.List;

import net.seiko_comb.combS8214808.joiss2016.Vector;
import processing.core.PApplet;

public class DetectLetters extends PApplet {

	public static void main(String... args) {
		PApplet.main(DetectLetters.class.getName());
	}

	private List<MnistData> trainList, testList;
	private MnistReader reader;
	private int w = 16, h = 9;
	private int pic = 28;

	private int[] genarr = new int[w * pic * h * pic];
	private int[] vxarr = new int[pic * pic + 1];
	private boolean[][] used = new boolean[w][h];
	private Vector vw;

	public void operate() {
		readData();
		vw = zero(28 * 28 + 1);
		for (int i = 0; i < 10000; i++) {
			generateImage(genarr, used);
			for (int j = 0; j < 50; j++) {
				int vy = generateSubImage(genarr, vxarr, used);
				Vector vx = $(vxarr);
				vw = Mnist.logistic(vw, vx, vy, 0.01, 5.0 / 500000);
			}
			for (int j = 0; j < w; j++)
				for (int k = 0; k < h; k++)
					used[j][k] = false;
		}
		double count = 0;
		for (int i = 0; i < 1000; i++) {
			generateImage(genarr, used);
			for (int j = 0; j < 10; j++) {
				int vy = generateSubImage(genarr, vxarr, used);
				Vector vx = $(vxarr);
				double product = vx.product(vw);
				if (product * vy > 0) {
					count++;
				}
			}
			for (int j = 0; j < w; j++)
				for (int k = 0; k < h; k++)
					used[j][k] = false;
		}
		System.out.println(count / 10000);
		reader = new OneByOneReader(trainList, testList);
		// reader.read()j
		reader.load();
	}

	private void readData() {
		trainList = MnistData.getTrainList();
		testList = MnistData.getTestList();
	}

	public void settings() {
		size(w * pic * 2, h * pic * 2);
	}

	public void setup() {
		new Thread(this::operate).start();
		background(255);
	}

	public void draw() {
	}

	public void keyTyped() {
		if (key == 'q')
			background(255);
		if (key == 'd')
			detect();
		if (key == 'r')
			for (int i = 0; i < w * pic; i++) {
				for (int j = 0; j < h * pic; j++) {
					set(i, j, color(255 - genarr[j * w * pic + i]));
				}
			}
	}

	public void mouseDragged() {
		float v = dist(pmouseX, pmouseY, mouseX, mouseY);
		stroke(0);
		strokeWeight(1);
		line(pmouseX / 2, pmouseY / 2, mouseX / 2, mouseY / 2);
	}

	private void detect() {
		for (int i = 0; i < 100000; i++) {
			int x = (int) random((w - 1) * pic), y = (int) random((h - 1) * pic);
			for (int j = 0, l = 0; j < pic; j++) {
				for (int k = 0; k < pic; k++, l++) {
					vxarr[l] = 255 - (0xff & get(x + j, y + k));
				}
			}
			vxarr[pic * pic] = 1;
			Vector vx = $(vxarr);
			if (vx.product(vw) > 0) {
				noFill();
				strokeWeight(1);
				stroke(0, 0, 255);
				rect(x, y, pic, pic);
				for (int j = x; j < x + pic; j++) {
					for (int k = y; k < y + pic; k++) {
						if ((get(j, k) & 0xff) < 255)
							set(j, k, color(0, 0, 255));
					}
				}
				fill(0, 0, 255);
				int result = reader.getResult(vx);
				text(result, x, y);
			}
		}
	}

	private void generateImage(int[] genarr, boolean[][] used) {
		for (int i = 0; i < genarr.length; i++) {
			genarr[i] = 0;
		}
		for (int i = 0; i < w * h / 6; i++) {
			int x, y;
			do {
				x = (int) random(1, w - 1);
				y = (int) random(1, h - 1);
			} while (used[x][y]);
			used[x][y] = true;
			int index = (int) random(60000);
			int[] src;
			if (index < 50000)
				src = trainList.get(index).getPixel();
			else
				src = testList.get(index - 50000).getPixel();
			for (int k = 0; k < pic; k++) {
				int destPos = (y * pic + k) * w * pic + x * pic;
				System.arraycopy(src, k * pic, genarr, destPos, pic);
			}
		}
	}

	private int generateSubImage(int[] genarr, int[] vxarr, boolean[][] used) {
		double x, y;
		int vy;
		if (random(6) < 4) {
			x = (int) random(w - 1);
			y = (int) random(h - 1);
			double bef = x + y;
			if (random(2) < 1)
				x += random(0.1f, 0.9f);
			if (random(2) < 1)
				y += random(0.1f, 0.9f);
			if (x + y - bef > 0)
				vy = -1;
			else
				vy = 1;
		} else {
			int xx, yy;
			do {
				xx = (int) random(w - 1);
				yy = (int) random(h - 1);
			} while (!used[xx][yy]);
			x = xx;
			y = yy;
			vy = 1;
		}
		// System.out.println(currentImage.pixels.length + "\t" +
		// vxarr.length);
		for (int k = 0; k < pic; k++) {
			int srcPos = (int) ((y * pic + k) * w * pic + x * pic);
			int destPos = k * pic;
			// System.out.println(srcPos + "\t" + destPos);
			System.arraycopy(genarr, srcPos, vxarr, destPos, pic);
		}
		vxarr[pic * pic] = 1;
		return vy;
	}

}
