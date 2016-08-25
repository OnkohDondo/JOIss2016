package net.seiko_comb.combS8214808.joiss2016.mnist;

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
	
	public int[] getPixel(){
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
}
