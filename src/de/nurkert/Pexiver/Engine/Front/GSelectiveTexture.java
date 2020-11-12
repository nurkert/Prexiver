package de.nurkert.Pexiver.Engine.Front;

import java.awt.Image;

public class GSelectiveTexture extends GAnimatedTexture {

	public GSelectiveTexture(String path) {
		super(path);
	}

	public Image getImage(int index) {
		return frames[index];
	}

}
