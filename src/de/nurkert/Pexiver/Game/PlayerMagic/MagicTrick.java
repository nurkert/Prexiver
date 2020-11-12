package de.nurkert.Pexiver.Game.PlayerMagic;

import java.awt.Color;
import java.util.Random;

import de.nurkert.Pexiver.Engine.GLocation;

public abstract class MagicTrick {

	private int bindedKeyCode;
	protected Random random;
	
	public MagicTrick(int bindedKeyCode) {
		this.bindedKeyCode = bindedKeyCode;
		random = new Random();
	}
	
	public int getBindedKeyCode() {
		return bindedKeyCode;
	}
	
	public abstract void perform(GLocation location);
	
	protected Color getMagicColor() {
		return new Color(205, 10 + random.nextInt(56), 150 + random.nextInt(40));
	}
}
