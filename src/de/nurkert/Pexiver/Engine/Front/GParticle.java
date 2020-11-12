package de.nurkert.Pexiver.Engine.Front;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import de.nurkert.Pexiver.Engine.GContent;
import de.nurkert.Pexiver.Engine.GLocation;
import de.nurkert.Pexiver.Engine.Back.Velocity;
import de.nurkert.Pexiver.Engine.Entitys.GObject;

public class GParticle extends GObject  {


	private Velocity velocity;
	private Color color;
	private long deadline;
	
	public GParticle(Color color, GLocation location, double power, long lifetime) {
		super(location, GLayor.MAIN1, true, true);
		this.color = color;
		deadline = System.currentTimeMillis() + lifetime;
		setVelocity(new Velocity(location.getAngle(), power));
	}
	
	public GParticle(GLayor layor, Color color, GLocation location, double power, long lifetime) {
		super(location, layor, true, true);
		this.color = color;
		deadline = System.currentTimeMillis() + lifetime;
		setVelocity(new Velocity(location.getAngle(), power));
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void draw(BufferedImage image, ImageObserver observer, GLocation viewCenter, double screenFactor) {
		GLocation relative = new GLocation((getLocation().getX() - viewCenter.getX()) * screenFactor,
				(getLocation().getY() - viewCenter.getY()) * screenFactor, viewCenter.getWorld());

		relative.addX(image.getWidth() / 2);
		relative.addY(image.getHeight() / 2);

		int width = (int) screenFactor;
		int height = (int) screenFactor;
		int x = (int) relative.getX() - width / 2;
		int y = (int) relative.getY() - height / 2;

		Graphics graphics = image.getGraphics();
		
		graphics.setColor(getColor());
		graphics.fillRect(x, y, width, height);
	}

	
	public Velocity getVelocity() {
		return velocity;
	}

	public void setVelocity(Velocity velocity) {
		this.velocity = velocity;
	}

	@Override
	public void handle(double diff, GContent world) {
	
		if(deadline < System.currentTimeMillis()) {
			setUseless(true);
			return;
		}
		
		double addX = velocity.getX() * diff * 10;
		double addY = velocity.getY() * diff * 10;
		getLocation().addX(addX);
		getLocation().addY(addY);
		
		getVelocity().add(getLocation().getAngle(), getVelocity().getPower() * diff);
		
	}
}
