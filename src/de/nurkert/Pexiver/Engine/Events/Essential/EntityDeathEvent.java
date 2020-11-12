package de.nurkert.Pexiver.Engine.Events.Essential;

import de.nurkert.Pexiver.Engine.Entitys.GEntity;
import de.nurkert.Pexiver.Engine.Events.Event;

public class EntityDeathEvent extends Event {
	
	GEntity entity;

	public EntityDeathEvent(GEntity entity) {
		super(-1);
		this.entity = entity;
	}

	public GEntity getEntity() {
		return entity;
	}
}
