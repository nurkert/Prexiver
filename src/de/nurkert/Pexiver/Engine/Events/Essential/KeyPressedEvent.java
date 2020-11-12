package de.nurkert.Pexiver.Engine.Events.Essential;

import de.nurkert.Pexiver.Engine.GContent;
import de.nurkert.Pexiver.Engine.Events.Event;

public class KeyPressedEvent extends Event {

	char key;
	int keycode;
	GContent world;
	
	public KeyPressedEvent(double diff, char key, int keycode, GContent world) {
		super(diff);
		this.key = key;
		this.keycode = keycode;
		this.world = world;
	}
	
	public char getKey() {
		return key;
	}
	
	public GContent getWorld() {
		return this.world;
	}
	
	public int getKeycode() {
		return keycode;
	}
}
