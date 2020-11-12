package de.nurkert.Pexiver.Engine.Events.Essential;

import java.util.ArrayList;

import de.nurkert.Pexiver.Engine.Entitys.GObject;
import de.nurkert.Pexiver.Engine.Events.Event;

public class HandleObjectsEvent extends Event {

	ArrayList<GObject> objects;
	
	public HandleObjectsEvent(double diff, ArrayList<GObject> objects) {
		super(diff);
		this.objects = objects;
	}

	public ArrayList<GObject> getObjects() {
		return objects;
	}
}
