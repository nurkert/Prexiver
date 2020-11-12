package de.nurkert.Pexiver.Engine.Front;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

public class GTexture {

	URL url;
	Image image;

	public GTexture(String path) {
		url = getClass().getResource(path);
		image = Toolkit.getDefaultToolkit().getImage(url);
		init();
	}
	
	public GTexture(Image image) {
		this.image = image;
	}

	BufferedImage img;

	private void init() {
		try {
			img = ImageIO.read(new File(url.toURI()));// .getPath()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void setImage(Image image) {
		this.image = image;
	}

	public Image getImage() {
		return image;
	}
	
	public BufferedImage getBufImg() {
		return img;
	}
}
