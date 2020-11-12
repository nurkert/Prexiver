package de.nurkert.Pexiver.Engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import de.nurkert.Pexiver.Engine.GInput.GMouseClick;
import de.nurkert.Pexiver.Engine.Back.GBackground;
import de.nurkert.Pexiver.Engine.Events.EventHandler;
import de.nurkert.Pexiver.Engine.Events.Essential.KeyPressedEvent;
import de.nurkert.Pexiver.Engine.Events.Essential.MouseClickEvent;
import de.nurkert.Pexiver.Game.World;

@SuppressWarnings("serial")
public class GPanel extends JPanel {

	private static final GraphicsConfiguration graphicsConf = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice().getDefaultConfiguration();
	private int fps = 60;

	private String title;
	private String[] infoText;
	private boolean showInfoText = true;

	private BufferedImage imageBuffer;
	private Graphics graphics;

	private GInput input;
	private World content;

	private GFrame frame;
	private static GPanel instance;

	public GPanel(String title) {
		this.title = title;
		infoText = new String[0];

		input = new GInput();
		content = new World();

		frame = null;

		imageBuffer = graphicsConf.createCompatibleImage(420, 69);
		graphics = imageBuffer.getGraphics();
		((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		instance = this;
		init();
	}

	public static GPanel getInstance() {
		return instance;
	}

	private void init() {
		setBackground(Color.BLACK);
	}

	public void setFrame(GFrame frame) {
		this.frame = frame;
	}

	public String[] getInfoText() {
		return infoText;
	}

	public void updateInfoText() {
		updateInfoText("FPS: " + getFPS(), "Window: x:" + getWidth() + ", y:" + getHeight());
	}

	public void updateInfoText(String... infoText) {
		this.infoText = infoText;
	}

	Thread panelThread;

	public void start() {
		panelThread = new Thread(title + "Thread") {

			public void run() {
				double last = System.currentTimeMillis();
				double diffAvg = 0.0166666;

//				freeze(800); // startup anderer threads

//				showStartInfos(diffAvg);
				last = System.currentTimeMillis();
				while (true) {

					long current = System.currentTimeMillis();
					double diff = (current - last) / 1000.0;
					diffAvg = 0.98 * diffAvg + 0.02 * diff;
					last = current;
					fps = (int) (1D / diffAvg);

//					if (showInfoText)
//						updateInfoText();

					handleInput(diff);

//					if (paused) {
////						System.out.println("paused");
//						continue;
//					}

					content.deleteDeadObjects();
					content.handle(diff);
//
					clear();

					content.paint(imageBuffer, frame.getPanel());

					if (showInfoText && infoText != null)
						drawInfoText();

					frame.getGraphics().drawImage(imageBuffer, 0, 0, frame);

					// Limit the fps to 60:
					freeze((long) (16 - diff * 1000));
				}
			}

		};
		panelThread.start();
	}

//	private void showStartInfos(double diffAvg) {
//		handleInput(diffAvg);
//		content.handle(diffAvg);
//		clear();
//		content.paint(imageBuffer, frame.getPanel());
//
//		BufferedImage pic_injection = new GTexture("/textures/injection.png").getBufImg();
//		String injection_descr = "Für 8 Sekunden unverwundbar";
//		BufferedImage pic_mask = new GTexture("/textures/mask.png").getBufImg();
//		String mask_descr = "schnelleres Schießen + schnellere Fortbewegung";
//
//		freeze(300);
//
//		Font fontBig = new Font("Arial", 0, getHeight() / 7);
//		Font fontSmall = new Font("Arial", 0, getHeight() / 20);
//		FontMetrics f_metr = graphics.getFontMetrics(fontSmall);
//
//		String description1 = "Zerstöre mit deiner Pistole alle Viren";
//		String description2 = "ACHTUNG: Lass dich nicht berühren";
//		int offset = (int) (f_metr.getHeight() * 1.5);
//
//		int pic_size = getHeight() / 10;
//
//		for (int i = 4; i > 0; i--) {
//			content.handle(diffAvg);
//			clear();
//			content.paint(imageBuffer, frame.getPanel());
//
//			graphics.setColor(new Color(0f, 0f, 0f, .4f));
//			graphics.fillRect(0, 0, getWidth(), getHeight());
//			graphics.drawRect(0, 0, getWidth(), getHeight());
//
//			graphics.setFont(fontBig);
//			graphics.setColor(Color.YELLOW);
//			f_metr = graphics.getFontMetrics(fontBig);
//
//			// Anzeige Counter
//			graphics.drawString(Integer.toString(i), getWidth() / 2 - f_metr.stringWidth(Integer.toString(i)) / 2,
//					getHeight() / 4 + f_metr.getHeight() / 2);
//			graphics.setFont(fontSmall);
//			f_metr = graphics.getFontMetrics(fontSmall);
//			graphics.drawString(description1, getWidth() / 2 - f_metr.stringWidth(description1) / 2,
//					getHeight() / 4 + f_metr.getHeight() / 2 + offset * 2);
//			graphics.drawString(description2, getWidth() / 2 - f_metr.stringWidth(description2) / 2,
//					getHeight() / 4 + f_metr.getHeight() / 2 + offset * 3);
//
//			// Erkl�rung Buffs
//			graphics.drawImage(pic_injection, getWidth() / 5, getHeight() / 4 + f_metr.getHeight() / 2 + offset * 4,
//					pic_size, pic_size, null);
//			graphics.drawString(injection_descr, getWidth() / 4 + 30,
//					(int) (getHeight() / 4 + f_metr.getHeight() * 1.5 + offset * 4));
//
//			graphics.drawImage(pic_mask, getWidth() / 5, getHeight() / 4 + f_metr.getHeight() + offset * 5, pic_size,
//					pic_size, null);
//			graphics.drawString(mask_descr, getWidth() / 4 + 30,
//					(int) (getHeight() / 4 + f_metr.getHeight() * 2 + offset * 5));
//
//			frame.getGraphics().drawImage(imageBuffer, 0, 0, frame);
//			freeze(1000);
//
//		}
//
//	}

	/*
	 * freeze Frame content
	 */
	public synchronized void freeze(long time) {
		if (time > 0)
			try {
				synchronized (panelThread) {
					wait(time);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	public GLocation getViewCenter() {
		return content.getViewCenter();
	}

	public void setBackground(GBackground gBackground) {
		content.place(gBackground);
	}

	private void handleInput(double diff) {
		try {
			if (input.getPressedKeys().keySet().toArray().length > 0)
				for (int i = 0; i < input.getPressedKeys().size(); i++) {
					if (input != null) {
						char key = (char) input.getPressedKeys().keySet().toArray()[i];
						int code = input.getPressedKeys().get(key);
						EventHandler.call(new KeyPressedEvent(diff, key, code, this.content));
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (input.getMouseClicks().size() > 0)
			for (int i = 0; i < input.getMouseClicks().size(); i++) {
				if (input != null) {
					GMouseClick click = input.getMouseClicks().get(i);
					EventHandler.call(new MouseClickEvent(diff, click.getMousePressedX(), click.getMousePressedY()));
				}
			}

		input.clearMouseClicks();
	}

	long start = System.currentTimeMillis();

	private void clear() {
		if (frame.getWidth() != imageBuffer.getWidth() || frame.getHeight() != imageBuffer.getHeight()
				|| System.currentTimeMillis() - start < 2000) {
			setSize(frame.getWidth(), frame.getHeight());
			imageBuffer = graphicsConf.createCompatibleImage(getWidth(), getHeight());
			graphics = imageBuffer.getGraphics();
		}

		// clear frame:
		graphics.setColor(Color.DARK_GRAY);
		graphics.fillRect(0, 0, getWidth(), getHeight());
	}

	private void drawInfoText() {
		Graphics graphics = imageBuffer.getGraphics();
		graphics.setFont(new Font("Arial", 0, getHeight() / 50));
		graphics.setColor(Color.WHITE);
		for (int i = 0; i < infoText.length; i++) {
			if (infoText[i] != null) {

				try {
					graphics.drawString(infoText[i], 10, 20 * i + 250);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

//	private void drawGameOverText() {
//		Graphics graphics = imageBuffer.getGraphics();
//		Font font = new Font("Arial", 0, getHeight() / 10);
//		FontMetrics f_metr = graphics.getFontMetrics(font);
//
//		String s_gameOver = "GAME OVER";
//		String s_points = "Punkte: " + content.getPunkte();
//
//		graphics.setFont(font);
//		graphics.setColor(Color.WHITE);
//		graphics.drawString(s_gameOver, (getWidth() - f_metr.stringWidth(s_gameOver)) / 2,
//				(getHeight() - f_metr.getHeight()) / 2);
//
//		font = new Font("Arial", 0, getHeight() / 15);
//		f_metr = graphics.getFontMetrics(font);
//
//		graphics.setFont(font);
//		graphics.drawString(s_points, (getWidth() - f_metr.stringWidth(s_points)) / 2,
//				(getHeight() - f_metr.getHeight()) / 2 + getHeight() / 8);
//
//	}

//	private void drawGameStats() {
//		Graphics graphics = imageBuffer.getGraphics();
//		graphics.setFont(new Font("Arial", 0, getHeight() / 30));
//		graphics.setColor(Color.LIGHT_GRAY);
//		int offset = getHeight() / 10;
//		String sGameState = "";
//		for (String key : gameStats.keySet()) {
//			try {
//				if (key.toLowerCase().equals("leben")) {
//					Image pic_ui = new GTexture("/ui/heart.png").getImage();
//					graphics.drawImage(pic_ui, getWidth() - getWidth() / 8, offset - (pic_ui.getHeight(null) / 2),
//							getHeight() / 30, getHeight() / 30, null);
//				} else if (key.toLowerCase().equals("punkte")) {
//					Image pic_ui = new GTexture("/ui/star.png").getImage();
//					graphics.drawImage(pic_ui, getWidth() - getWidth() / 8, offset - (pic_ui.getHeight(null) / 2),
//							getHeight() / 30, getHeight() / 30, null);
//				}
//				if (gameStats.get(key) != null) {
//					sGameState = gameStats.get(key);
//				}
//				graphics.drawString(sGameState, getWidth() - getWidth() / 11, offset + getHeight() / 70);
//
//				offset += getHeight() / 15;
//				System.out.println(sGameState);
//
//			} catch (NullPointerException e) {
//				e.printStackTrace();
//			}
//
//		}
//		int buffTimeWidth;
//		if (content.getActivePowerBuffTime() > 0) {
//			buffTimeWidth = (int) (content.getActivePowerBuffTime() / 50);
//			graphics.setColor(Color.BLUE);
//			graphics.fillRect(getWidth() - getWidth() / 15 - buffTimeWidth, offset, buffTimeWidth, 10);
//			graphics.drawRect(getWidth() - getWidth() / 15 - buffTimeWidth, offset, buffTimeWidth, 10);
//			offset += getHeight() / 15;
//		}
//
//		if (content.getActiveImmunBuffTime() > 0) {
//			buffTimeWidth = (int) (content.getActiveImmunBuffTime() / 50);
//			graphics.setColor(Color.PINK);
//			graphics.fillRect(getWidth() - getWidth() / 15 - buffTimeWidth, offset, buffTimeWidth, 10);
//			graphics.drawRect(getWidth() - getWidth() / 15 - buffTimeWidth, offset, buffTimeWidth, 10);
//		}
//
//	}

	public void pause() {
		try {
			panelThread.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getFPS() {
		return fps;
	}

	public GContent getContent() {
		return content;
	}

	public void setContent(World content) {
		this.content = content;
	}

	public double getSizeFactor() {
		return getHeight() / 100;
	}

	public static GraphicsConfiguration getGraphicsconf() {
		return graphicsConf;
	}

	public String getTitle() {
		return title;
	}

	public BufferedImage getImageBuffer() {
		return imageBuffer;
	}

	public Graphics getGraphics() {
		return graphics;
	}

	public GInput getInput() {
		return input;
	}

	public Thread getPanelThread() {
		return panelThread;
	}
}
