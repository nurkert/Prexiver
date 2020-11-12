package de.nurkert.Pexiver.Engine.Events.Essential;

import de.nurkert.Pexiver.Engine.Events.Event;

public class MouseClickEvent extends Event {

	int mousePressedX, mousePressedY;

	public MouseClickEvent(double diff, int mousePressedX, int mousePressedY) {
		super(diff);
		this.mousePressedX = mousePressedX;
		this.mousePressedY = mousePressedY;
	}

	public int getMousePressedX() {
		return mousePressedX;
	}

	public int getMousePressedY() {
		return mousePressedY;
	}

	
}
