package de.nurkert.Pexiver.Engine.Back;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import de.nurkert.Pexiver.Engine.GContent;
import de.nurkert.Pexiver.Engine.GLocation;
import de.nurkert.Pexiver.Engine.Entitys.GObject;
import de.nurkert.Pexiver.Engine.Front.GTexture;


public class GBackground extends GObject {

	private double x, y;
	private GLocation viewCenter;

	public GBackground(GTexture texture, GLocation viewCenter) {
		super(new GLocation(0, 0, viewCenter.getWorld()), GLayor.BACKGROUND, false, true);
		this.texture = texture;
		this.viewCenter = viewCenter;
		x = texture.getBufImg().getWidth();
		y = texture.getBufImg().getHeight();
	}

	/**
	 * adjusts the position of the background, 
	 * matching to the "camera" position.
	 */
	@Override
	public void handle(double diff, GContent world) {
		double x = viewCenter.getX() - viewCenter.getX() % this.x;
		double y = viewCenter.getY() - viewCenter.getY() % this.y;
		getLocation().setX(x);
		getLocation().setY(y);
	}

	/**
	 * overwrites the draw method of the GObject 
	 * class, because the background texture has 
	 * to be placed image filling (several times)
	 */
	@Override
	public void draw(BufferedImage image, ImageObserver observer, GLocation viewCenter, double screenFactor) {
		GLocation relative = new GLocation((getLocation().getX() - viewCenter.getX()) * screenFactor,
				(getLocation().getY() - viewCenter.getY()) * screenFactor, viewCenter.getWorld());

		relative.addX(image.getWidth() / 2);
		relative.addY(image.getHeight() / 2);

		Image texture = getTexture();

		int width = (int) (texture.getWidth(observer) * screenFactor);
		int height = (int) (texture.getHeight(observer) * screenFactor);
		int x = (int) relative.getX() - width / 2;
		int y = (int) relative.getY() - height / 2;

		int aX = image.getWidth()/width;
		int aY = image.getHeight()/height;
		
		for (int x2 = -aX-1; x2 < aX+1; x2++)
			for (int y2 = -aY-1; y2 < aY+2; y2++)
				image.getGraphics().drawImage(texture, x + width * x2, y + height * y2, width, height, observer);
	}


}
