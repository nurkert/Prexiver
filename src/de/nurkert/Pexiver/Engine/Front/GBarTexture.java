package de.nurkert.Pexiver.Engine.Front;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class GBarTexture extends GTexture {

	public GBarTexture(String path) {
		super(path);

		init();
	}

	Image[] frames;

	private void init() {
		frames = new Image[5];
		for (int i = 0; i < 5; i++) {
			frames[i] = img.getSubimage(0, img.getWidth() / 2 * i, img.getWidth(), img.getWidth() / 2);
		}
	}

	public Image getImage(double relation) {

		BufferedImage bimage = new BufferedImage(img.getWidth() * 150 / 16, img.getWidth() / 2,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		if (relation < 0.1) {
			for (int i = 0; i < 10; i++) {
				bGr.drawImage(frames[4], (int) ((img.getWidth() - 2) * i), 0, null);
			}
		} else

			for (int i = 0; i < 10; i++) {
				bGr.drawImage(
						frames[relation <= i ? 4
								: relation > (i + 0.75) ? 0
										: relation > (i + 0.5) ? 1 : relation >= (i + 0.25) ? 2 : 3],
						(int) ((img.getWidth() - 2) * i), 0, null);
			}
		bGr.dispose();

		return bimage;

//		return frames[Math.abs((int)(System.currentTimeMillis()/100)%frames.length)];
	}

}
