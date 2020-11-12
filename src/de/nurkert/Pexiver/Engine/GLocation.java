package de.nurkert.Pexiver.Engine;

import java.util.Random;

import de.nurkert.Pexiver.Game.World;

public class GLocation {

	double x, y;
	float angle;
	World world;

	public GLocation(double x, double y, float angle, World world) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.world = world;
	}

	public GLocation(double x, double y, World world) {
		this.x = x;
		this.y = y;
		this.angle = 0F;
		applyRandomAngle();
		this.world = world;
	}

	public double getX() {
		if (x == 0)
			return 0.0001;
		return x;
	}

	public GLocation setX(double x) {
		this.x = x;
		return this;
	}

	public GLocation addX(double x) {
		this.x += x;
		return this;
	}

	public double getY() {
		if (y == 0)
			return 0.0001;
		return y;
	}

	public GLocation setY(double y) {
		this.y = y;
		return this;
	}

	public GLocation addY(double y) {
		this.y += y;
		return this;
	}

	public GLocation add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public void applyRandomAngle() {
		angle = (float) (new Random().nextFloat() * 2 * Math.PI);
	}
	
	public void addAnlge(float angle) {
		this.angle += angle;
		fixAngle();
	}

	public float getAngle() {
		return angle;
	}

	public float getInvertAngle() {
		float invert = (float) (angle - Math.PI);
		if (invert < 0)
			invert += Math.PI * 2;
		else
			invert -= Math.PI * 2;
		return invert;
	}

	public GLocation setAngle(float angle) {
		this.angle = angle;
		fixAngle();
		return this;
	}

	private float fixAngle() {
		angle = (float) Math.atan2(Math.sin(angle), Math.cos(angle));
		return angle;
	}
	
	public World getWorld() {
		return world;
	}

	public double distance(GLocation loc) {
		double dX = Math.abs(getX() - loc.getX());
		double dY = Math.abs(getY() - loc.getY());
		return Math.sqrt(dX * dX + dY * dY);
	}

	public GLocation copy() {
		GLocation copy = new GLocation(x, y, world);
		copy.setAngle(angle);
		return copy;
	}
}
