package de.nurkert.Pexiver.Engine.Entitys;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Comparator;
import java.util.UUID;

import de.nurkert.Pexiver.Engine.GContent;
import de.nurkert.Pexiver.Engine.GLocation;
import de.nurkert.Pexiver.Engine.Front.GTexture;

public abstract class GObject {

	private String objectID, fieldID;
	private GLayor layor;
	private GLocation location;
	protected GTexture texture;
	protected double sizeFaktor;
	private boolean moving, useless, freezeImmune;// , collidAble;// , isLiving = true;

	/**
	 * @param location The position of the object
	 * @param layor    On which layer the object is to be drawed
	 * @param moveable Whether the object can move by itself
	 */
	public GObject(GLocation location, GLayor layor, boolean moving, boolean freezeImmune) {
		this.location = location;
		this.layor = layor;
		this.moving = moving;

		this.freezeImmune = freezeImmune;
		this.useless = false;
//		this.collidAble = collidAble;

		objectID = UUID.randomUUID().toString().substring(0, 5);
		fieldID = "";

		texture = new GTexture("/textures/404.png");

	}

	public void drawFreezseScreen(BufferedImage image, ImageObserver observer, GLocation viewCenter,
			double screenFactor) {

	}

	
	
	public boolean isFreezeImmune() {
		return freezeImmune;
	}

	public boolean isUseless() {
		return useless;
	}

	public void setUseless(boolean useless) {
		this.useless = useless;
	}

	/**
	 * @param image      The entire image of the frame
	 * @param observer   The image observer of frame or panel
	 * @param viewCenter Middle of the "camera"
	 */
	public void draw(BufferedImage image, ImageObserver observer, GLocation viewCenter, double screenFactor) {

		GLocation relative = new GLocation((getLocation().getX() - viewCenter.getX()) * screenFactor,
				(getLocation().getY() - viewCenter.getY()) * screenFactor, viewCenter.getWorld());

		relative.addX(image.getWidth() / 2);
		relative.addY(image.getHeight() / 2);

		Image _texture = getTexture();

		if (_texture == null)
			return;

		int width = (int) (_texture.getWidth(observer) * screenFactor / 1.5);
		int height = (int) (_texture.getHeight(observer) * screenFactor / 1.5);
		int x = (int) relative.getX() - width / 2;
		int y = (int) relative.getY() - height / 2;

		image.getGraphics().drawImage(_texture, x, y, width, height, null);
	}

//
//	public static BufferedImage toBufferedImage(Image img) {
//		if (img == null)
//			return null;
//		else if (img instanceof BufferedImage) {
//			return (BufferedImage) img;
//		}
//
//		if(img.getWidth(null) < 0 || img.getHeight(null) < 0)
//			return null;
//		
//		// Create a buffered image with transparency
//		
//		
//		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
//
//		// Draw the image on to the buffered image
//		Graphics2D bGr = bimage.createGraphics();
//		bGr.drawImage(img, 0, 0, null);
//		bGr.dispose();
//
//		// Return the buffered image
//		return bimage;
//	}

	public interface Collidable {

		public void collide(GObject object);

		public boolean wouldCollide(GObject object);

		public double getHitRadius();
	}

	public String getFieldID() {
		return fieldID;
	}

	public void setFieldID(String fieldID) {
		this.fieldID = fieldID;
	}

//	public boolean isCollidAble() {
//		return collidAble;
//	}

	public boolean isMoveing() {
		return moving;
	}

	public String getObjectID() {
		return objectID;
	}

	public GLocation getLocation() {
		return location;
	}

	public void setLocation(GLocation location) {
		this.location = location;
	}

//	just an alias xD
	public void teleport(GLocation location) {
		setLocation(location);
	}

	public void setLayor(GLayor layor) {
		this.layor = layor;
	}

	public Image getTexture() {
		return texture.getImage();
	}

	public void setTexture(GTexture texture) {
		this.texture = texture;
	}

	public abstract void handle(double diff, GContent world);

	public enum GLayor {
		BACKGROUND, MAIN1, MAIN2, MAIN3, MAIN4, OVERLAY1, OVERLAY2;
	}

	/**
	 * sorts the objects according to the planes to ensure that, for example, the
	 * background textures are in the background
	 */
	public static class GObjectOrganizer implements Comparator<GObject> {
		@Override
		public int compare(GObject o1, GObject o2) {
			if (o1.getLayor().ordinal() > o2.getLayor().ordinal())
				return 1;
			else if (o1.getLayor().ordinal() < o2.getLayor().ordinal())
				return -1;
			else
				return 0;
		}

	}

	public GLayor getLayor() {
		return layor;
	}

//	public boolean isLiving() {
//		
//		return this.isLiving;
//	}
}
