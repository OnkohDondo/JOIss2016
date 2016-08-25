package net.seiko_comb.combS8214808.joiss2016.mnist;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class MnistData {
	private int label;
	private int[] pixel;

	public MnistData(int label, int[] pixel) {
		this.label = label;
		this.pixel = pixel;
	}

	public int[] getPixel() {
		return pixel;
	}

	public int getLabel() {
		return label;
	}

	private PGraphics image;

	public PImage getImage(PApplet p) {
		if (image == null) {
			image = p.createGraphics(28, 28);
			image.beginDraw();
			image.background(255, 0, 0);
			image.noStroke();
			for (int i = 0, k = 0; i < 28; i++) {
				for (int j = 0; j < 28; j++, k++) {
					image.fill(255 - pixel[k]);
					image.rect(j, i, 1, 1);
				}
			}
			image.endDraw();
		}
		return image;
	}

	private static List<MnistData> trainList, testList;

	public static List<MnistData> getTrainList() {
		if (trainList == null) {
			trainList = readData("train-images.idx3-ubyte", "train-labels.idx1-ubyte");
		}
		return trainList;
	}

	public static List<MnistData> getTestList() {
		if (testList == null) {
			testList = readData("t10k-images.idx3-ubyte", "t10k-labels.idx1-ubyte");
		}
		return testList;
	}

	private static List<MnistData> readData(String imageFileName, String labelFileName) {
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
}
