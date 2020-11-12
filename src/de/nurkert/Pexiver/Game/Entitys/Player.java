package de.nurkert.Pexiver.Game.Entitys;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Random;

import de.nurkert.Pexiver.Engine.GContent;
import de.nurkert.Pexiver.Engine.GLocation;
import de.nurkert.Pexiver.Engine.Back.Velocity;
import de.nurkert.Pexiver.Engine.Entitys.GObject;
import de.nurkert.Pexiver.Engine.Entitys.GPlayer;
import de.nurkert.Pexiver.Engine.Entitys.GObject.GLayor;
import de.nurkert.Pexiver.Engine.Events.EventHandler.GEventMethod;
import de.nurkert.Pexiver.Engine.Events.EventHandler.GEventMethod.GEventPriority;
import de.nurkert.Pexiver.Engine.Events.Essential.KeyPressedEvent;
import de.nurkert.Pexiver.Engine.Events.Essential.MouseClickEvent;
import de.nurkert.Pexiver.Engine.Front.GEntityTexture;
import de.nurkert.Pexiver.Engine.Front.GParticle;
import de.nurkert.Pexiver.Game.PlayerMagic.MagicTrick;

public class Player extends GPlayer {

	HashMap<Integer, MagicTrick> repertoire;
	
	public Player(GLocation location, GLocation viewCenter) {
		super(location, viewCenter, 4, 9);
		texture = new GEntityTexture("/textures/nurkert.png", location);
		setHealth(getMaxHealth());
		repertoire = new HashMap<Integer, MagicTrick>();
	}

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

		double floating = Math.sin(((double) System.currentTimeMillis() / 250) % (Math.PI * 2)) * 7;

		image.getGraphics().drawImage(_texture, x, (int) (y + floating), width, height, null);
	}
	
	@GEventMethod(ignoreCancelled = true, priority = GEventPriority.HIGHER)
	public void on(KeyPressedEvent event) {
		
		switch (event.getKey()) {
		case 'w':
			getVelocity().add(getLocation().getAngle(), speed * 3);
			break;
		case 's':
			getVelocity().add(getLocation().getInvertAngle(), speed * 1.5);
			break;
		case 'a':
			getLocation().addAnlge(-1 * (float) (5 * event.getDiff()));
			break;
		case 'd':
			getLocation().addAnlge((float) (5 * event.getDiff()));
			break;
		default:
			performMagicTrick(event.getKeycode());
			break;
		}
	}
	
	private void performMagicTrick(int keyCode) {
		if(repertoire.containsKey(keyCode)) {
			MagicTrick trick = repertoire.get(keyCode);
			trick.perform(getLocation().copy());
		} else {
			System.out.println("pressed key code is not assigned to any magic trick (" + keyCode + ")");
		}
			
	}

	public void learn(MagicTrick... tricks) {
		for(MagicTrick trick : tricks) {
			repertoire.put(trick.getBindedKeyCode(), trick);
		}
	}
	
	@GEventMethod(ignoreCancelled = true, priority = GEventPriority.FOURTWENTY)
	public void on(MouseClickEvent event) {
//		if (getLocation().getWorld().isFreezed()) 
//			getLocation().getWorld().unfreeze();
//		else
//			getLocation().getWorld().freeze(GFreezeReason.PAUSED);
		System.out.println("happend");
		
		GLocation loc = getLocation().copy(), location= getLocation().copy();
		loc.add(event.getMousePressedX(), event.getMousePressedY());
		
		double radius = 3;
		double addX = new Velocity(loc.getAngle(), radius).getX();
		double addY = new Velocity(loc.getAngle(), radius).getY();
		loc.addX(addX);
		loc.addY(addY);

		loc.addAnlge((float) (Math.PI / 2));

		double floating = Math.cos(((double) System.currentTimeMillis() / 150) % (Math.PI * 2)) * 2;

		addX = new Velocity(loc.getAngle(), floating).getX();
		addY = new Velocity(loc.getAngle(), floating).getY();
		loc.addX(addX);
		loc.addY(addY);

		loc.addAnlge((float) (-Math.PI / 2));

		location.getWorld().place(new GParticle(location.getAngle() < 0 ? GLayor.MAIN1 : GLayor.OVERLAY1,
				getColorOfTexture(), loc, new Random().nextInt(10) + 15, new Random().nextInt(250) + 500));
	}

	@Override
	public void handle(double diff, GContent world) {

		super.handle(diff, world);

		//Spray particle under the player
		if (new Random().nextInt(3) == 0) {
			GLocation loc = getLocation().copy();
			loc.setAngle((float) ((new Random().nextFloat() * Math.PI / 3) + Math.PI / 3));
			double radius = 6;
			double addX = new Velocity(loc.getAngle(), radius).getX();
			double addY = new Velocity(loc.getAngle(), radius).getY();
			loc.addX(addX);
			loc.addY(addY);

			getLocation().getWorld().place(new GParticle(getColorOfTexture(), loc, 1,
					new Random().nextInt(100) + 250));
		}
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
	}

}
