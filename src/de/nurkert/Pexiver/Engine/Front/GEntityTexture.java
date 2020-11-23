package de.nurkert.Pexiver.Engine.Front;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import de.nurkert.Pexiver.Engine.GLocation;

public class GEntityTexture extends GTexture {

	GLocation location;
	
	public GEntityTexture(String path, GLocation location) {
		super(path);
		this.location = location;		
		init();
	}

	protected Image[] frames;

	private void init() {
		int max =  img.getWidth() / img.getHeight();
		frames = new Image[max];
		for (int i = 0; i < max; i++) {
			frames[i] = img.getSubimage(img.getHeight() * i , 0, img.getHeight(), img.getHeight());
		}
	}

	int frame = 0;
	long last = System.currentTimeMillis();

	String test = "";
	
	@Override
	public Image getImage() {
		int frame = (int) (Math.round((Math.toDegrees(location.getAngle()) + 180) / 45) + 6)  % 8;
		
		return frames[frame];
	}

	@Override
	public BufferedImage getBufImg() {
		BufferedImage bimage = new BufferedImage(img.getHeight(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(getImage(), 0, 0, null);
		bGr.dispose();

		return bimage;
	}
}
