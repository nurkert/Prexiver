package de.nurkert.Pexiver.Engine.Entitys;

import de.nurkert.Pexiver.Engine.GContent;
import de.nurkert.Pexiver.Engine.GLocation;
import de.nurkert.Pexiver.Engine.Entitys.GEntity;
import de.nurkert.Pexiver.Engine.Entitys.GObject.Collidable;
import de.nurkert.Pexiver.Engine.Events.EventHandler.GEventListener;
import de.nurkert.Pexiver.Game.Entitys.EntityType;

public abstract class GPlayer extends GEntity implements Collidable, GEventListener {

	GLocation viewCenter;

	public GPlayer(GLocation location, GLocation viewCenter, double speed, double hitRadius) {
		super(location, speed, hitRadius, EntityType.PLAYER);
		this.viewCenter = viewCenter;
	}

	/**
	 * 
	 * Viewcenter/("camera" center) follows player entity smooth
	 */
	@Override
	public void handle(double diff, GContent world) {
		super.handle(diff, world);

		double dX = ((getLocation().getX() - viewCenter.getX()) / 4) * diff * 10;
		double dY = ((getLocation().getY() - viewCenter.getY()) / 4) * diff * 10;
		viewCenter.add(dX, dY);
	}

	
}
