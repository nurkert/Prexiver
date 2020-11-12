package de.nurkert.Pexiver.Game.PlayerMagic;

import java.util.Random;

import de.nurkert.Pexiver.Engine.GLocation;
import de.nurkert.Pexiver.Engine.Back.Velocity;
import de.nurkert.Pexiver.Engine.Front.GParticle;

public class MagicForceField extends MagicTrick {

	public MagicForceField() {
		super(32);
	}

	@Override
	public void perform(GLocation location) {
		for (int i = 0; i < 5; i++) {
			GLocation loc = location.copy();
			loc.applyRandomAngle();
			
			double radius = random.nextDouble()  + 10;
			double addX = new Velocity(loc.getAngle(), radius).getX();
			double addY = new Velocity(loc.getAngle(), radius).getY();
			loc.addX(addX);
			loc.addY(addY);

			loc.addAnlge((float) (Math.PI / 1.5));
			new Velocity();
			location.getWorld().place(
					new GParticle(getMagicColor(), loc, new Random().nextInt(4) + 3, new Random().nextInt(1300) + 500));

		}

	}

}
