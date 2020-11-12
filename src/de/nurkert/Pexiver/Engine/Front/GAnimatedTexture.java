package de.nurkert.Pexiver.Engine.Front;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class GAnimatedTexture extends GTexture {



	public GAnimatedTexture(String path) {
		super(path);
		init();
	}

	protected Image[] frames;

	private void init() {
		int max = img.getHeight() / img.getWidth();
		frames = new Image[max];
		for (int i = 0; i < max; i++) {
			frames[i] = img.getSubimage(0, img.getWidth() * i, img.getWidth(), img.getWidth());
		}
	}

	int frame = 0;
	long last = System.currentTimeMillis();

	String test = "";
	
	@Override
	public Image getImage() {

		frame = Math.abs((int) (System.currentTimeMillis() / 100) % frames.length);
		
		return frames[frame];
	}

	@Override
	public BufferedImage getBufImg() {
		BufferedImage bimage = new BufferedImage(img.getWidth(), img.getWidth(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(getImage(), 0, 0, null);
		bGr.dispose();

		return bimage;
	}
}
