package de.nurkert.Pexiver.Game.PlayerMagic;

import java.util.Random;

import de.nurkert.Pexiver.Engine.GLocation;
import de.nurkert.Pexiver.Engine.Back.Velocity;
import de.nurkert.Pexiver.Engine.Entitys.GObject.GLayor;
import de.nurkert.Pexiver.Engine.Front.GParticle;

public class MagicSinusWave extends MagicTrick {

	public MagicSinusWave() {
		super(74);
	}

	@Override
	public void perform(GLocation location) {

		GLocation loc = location.copy();

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
				getMagicColor(), loc, new Random().nextInt(10) + 15, new Random().nextInt(250) + 500));

	}
}
