package de.nurkert.Pexiver.Game.Entitys;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Random;

import de.nurkert.Pexiver.Engine.GContent;
import de.nurkert.Pexiver.Engine.GContent.GFreezeReason;
import de.nurkert.Pexiver.Engine.GLocation;
import de.nurkert.Pexiver.Engine.Back.Velocity;
import de.nurkert.Pexiver.Engine.Entitys.GObject;
import de.nurkert.Pexiver.Engine.Entitys.GPlayer;
import de.nurkert.Pexiver.Engine.Events.EventHandler.GEventMethod;
import de.nurkert.Pexiver.Engine.Events.EventHandler.GEventMethod.GEventPriority;
import de.nurkert.Pexiver.Engine.Events.Essential.KeyPressedEvent;
import de.nurkert.Pexiver.Engine.Events.Essential.MouseClickEvent;
import de.nurkert.Pexiver.Engine.Front.GEntityTexture;
import de.nurkert.Pexiver.Engine.Front.GParticle;
import de.nurkert.Pexiver.Engine.Front.GTexture;

public class Player extends GPlayer {

	public Player(GLocation location, GLocation viewCenter) {
		super(location, viewCenter, 4, 9);
		texture = new GEntityTexture("/textures/nurkert.png", location);
		setHealth(getMaxHealth());

//		paused = Main.frame.getPanel().togglePaused();
	}

	boolean upwards = true;
	double floating = 0;

	@Override
	public void draw(BufferedImage image, ImageObserver observer, GLocation viewCenter, double screenFactor) {
//		super.draw(image, observer, viewCenter, screenFactor);

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

		if (!getLocation().getWorld().isFreezed())
			if (upwards && floating < 15) {
				floating += 0.15;
			} else if (!upwards && floating > 0) {
				floating -= 0.15;
			} else {
				upwards = !upwards;
			}

		image.getGraphics().drawImage(_texture, x, (int) (y + floating), width, height, null);
	}

	@GEventMethod(ignoreCancelled = true, priority = GEventPriority.HIGHER)
	public void on(KeyPressedEvent event) {

		if (event.getKeycode() == 27) {
			if (getLocation().getWorld().isFreezed())
				getLocation().getWorld().unfreeze();
			else
				getLocation().getWorld().freeze(GFreezeReason.PAUSED);
		} else if (!getLocation().getWorld().isFreezed())

			switch (event.getKey()) {
			case 'w':
				getVelocity().add(getLocation().getAngle(), speed * 3);// event.getDiff()
																		// *
//						System.out.println("test1");												// 1000);
				break;
			case 's':
				getVelocity().add(getLocation().getInvertAngle(), speed * 1.5);// event.getDiff()
																				// *
																				// 1000);
				break;
			case 'a':
				getLocation().addAnlge(-1 * (float) (3 * event.getDiff()));
				break;
			case 'd':
				getLocation().addAnlge((float) (3 * event.getDiff()));
				break;
			case ' ':
				for (int i = 0; i < Math.pow(getHitRadius(), 2); i++) {
					GLocation loc = getLocation().copy();
					double radius = new Random().nextDouble() * getHitRadius();
					double addX = new Velocity(loc.getAngle(), radius).getX();
					double addY = new Velocity(loc.getAngle(), radius).getY();
					loc.addX(addX);
					loc.addY(addY);
					loc.setAngle((float) (new Random().nextFloat() * 2 * Math.PI));
					getLocation().getWorld().place(new GParticle(getColorOfTexture(), loc, new Random().nextInt(5) + 2,
							new Random().nextInt(400) + 100));

				}
				break;
			default:
				break;
			}
	}

	@GEventMethod(ignoreCancelled = true, priority = GEventPriority.FOURTWENTY)
	public void on(MouseClickEvent event) {
//		paused = Main.frame.getPanel().togglePaused();
		if (getLocation().getWorld().isFreezed()) {
//			if(getLocation().getWorld().getFreezeReason() == GFreezeReason.GAMEOVER)
//				resetScore();
			getLocation().getWorld().unfreeze();

		} else
			getLocation().getWorld().freeze(GFreezeReason.PAUSED);
	}

	@Override
	public void handle(double diff, GContent world) {

		super.handle(diff, world);

//		for (int i = 0; i < 2; i++) {
		if (new Random().nextInt(3) == 0) {
			GLocation loc = getLocation().copy();
			loc.setAngle((float) ((new Random().nextFloat() * Math.PI / 3) + Math.PI / 3));
			double radius = new Random().nextDouble() * getHitRadius() + 2;
			double addX = new Velocity(loc.getAngle(), radius).getX();
			double addY = new Velocity(loc.getAngle(), radius).getY();
			loc.addX(addX);
			loc.addY(addY);
//			loc.setAngle((float) ((new Random().nextFloat() * Math.PI / 3) + Math.PI / 3));
			getLocation().getWorld().place(new GParticle(getColorOfTexture(), loc, new Random().nextInt(2) + 1,
					new Random().nextInt(100) + 500));
		}

//		}
	}

	@Override
	public void collide(GObject object) {
		super.collide(object);
		GLocation loc = getLocation().copy();

		double addX = new Velocity(loc.getAngle(), 8).getX();
		double addY = new Velocity(loc.getAngle(), 8).getY();
		loc.addX(addX);
		loc.addY(addY);

		super.collide(object);
	}

	@Override
	public void kill() {
		setHealth(getMaxHealth());

//		for (int i = 0; i < Math.pow(getHitRadius(), 2); i++) {
//			Location loc = getLocation().copy();
//			double radius = new Random().nextDouble() * getHitRadius();
//			double addX = new Velocity(loc.getAngle(), radius).getX();
//			double addY = new Velocity(loc.getAngle(), radius).getY();
//			loc.addX(addX);
//			loc.addY(addY);
//			loc.setAngle((float) (new Random().nextFloat() * 2 * Math.PI));
//			getLocation().getWorld().place(new GParticle(getColorOfTexture(), loc, new Random().nextInt(5) + 2,
//					new Random().nextInt(400) + 100));
//
//		}
//
//		Location loc = getLocation();
//		while (getLocation().distance(loc) < 128) {
//			double x = getLocation().getX() + new Random().nextInt(512) - 256;
//			double y = getLocation().getY() + new Random().nextInt(512) - 256;
//			loc = new Location(x, y, loc.getWorld());
//		}
//		teleport(loc);
//
//		System.out.println("Score: " + getScore());
//		
//
//		
//		getLocation().getWorld().freeze(GFreezeReason.GAMEOVER);
	}

	int i = 0;

//	@Override
//	public GLocation getLocation() {
//		GLocation loc = super.getLocation().copy();
////		i = i++ % 10;
////		loc.add(0, i);
//		return loc;
//	}

}
