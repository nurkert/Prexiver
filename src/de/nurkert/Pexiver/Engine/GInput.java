package de.nurkert.Pexiver.Engine;

import java.util.ArrayList;
import java.util.HashMap;

public class GInput {

	private HashMap<Character, Integer> pressedKeys;
	private ArrayList<GMouseClick> mouseClicks;
	private boolean cursorInFrame;

	/**
	 * Container class for the user input
	 */
	public GInput() {
		pressedKeys = new HashMap<Character, Integer>();
		mouseClicks = new ArrayList<GMouseClick>();
	}

	public void add(Character key, int keycode) {
		if (!pressedKeys.containsKey(key)) 
			pressedKeys.put(key, keycode);
	}

	public void remove(Character key) {
		if (pressedKeys.containsKey(key)) {
			pressedKeys.remove(key);
		}
	}

	public void add(GMouseClick click) {
		mouseClicks.add(click);
	}

	public HashMap<Character, Integer> getPressedKeys() {
		return pressedKeys;
	}

	public ArrayList<GMouseClick> getMouseClicks() {
		return mouseClicks;
	}

	public void clearPressedKeys() {
		pressedKeys.clear();
	}

	public void clearMouseClicks() {
		mouseClicks.clear();
	}

	public boolean isCursorInFrame() {
		return cursorInFrame;
	}

	public void setCursorInFrame(boolean cursorInFrame) {
		this.cursorInFrame = cursorInFrame;
	}

	public static class GMouseClick {

		int mousePressedX;
		int mousePressedY;

		public GMouseClick(int mousePressedX, int mousePressedY) {
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
}
