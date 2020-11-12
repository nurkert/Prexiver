package de.nurkert.Pexiver.Engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Collections;

import de.nurkert.Pexiver.Engine.Back.CollisionDetection;
import de.nurkert.Pexiver.Engine.Entitys.GEntity;
import de.nurkert.Pexiver.Engine.Entitys.GObject;
import de.nurkert.Pexiver.Engine.Entitys.GObject.GObjectOrganizer;
import de.nurkert.Pexiver.Engine.Events.EventHandler;
import de.nurkert.Pexiver.Engine.Events.Essential.HandleObjectsEvent;

//import de.thuGProgSS20.ImmuneTillDeath.Game.Entitys.Cell;


public abstract class GContent {

	ArrayList<GObject> objects;
	GLocation viewCenter;
	boolean shouldSort, freezed;
	GFreezeReason freezeReason;

	CollisionDetection collisions;

	public GContent() {
		objects = new ArrayList<GObject>();
		viewCenter = new GLocation(0, 0, null);
		shouldSort = true;
		collisions = new CollisionDetection();
		freezed = true;
		freezeReason = GFreezeReason.START;
	}

	public void handle(double diff) {
		for (int i = 0; i < objects.size(); i++) {
			GObject obj = objects.get(i);
			if (obj == null || obj.isUseless())
				objects.remove(i--);
		}

		EventHandler.call(new HandleObjectsEvent(diff / 2, objects));

		for (int i = 0; i < objects.size(); i++) {
			GObject object = objects.get(i);
			if (object != null && (!freezed || object.isFreezeImmune()))
				object.handle(diff / 2, this);
		}

		if (!freezed)
			collisions.detect(objects);
	}

	public boolean isFreezed() {
		return freezed;
	}

	public void freeze(GFreezeReason reason) {
		freezed = true;
		freezeReason = reason;
	}
	
	public void unfreeze() {
		freezed = false;
		freezeReason = null;
	}

	public GFreezeReason getFreezeReason() {
		return freezeReason;
	}
	
	public ArrayList<GObject> getObjects() {
		return objects;
	}

	public static enum GFreezeReason {
		START, PAUSED, GAMEOVER;
	}

	public void deleteDeadObjects() {
		// Entferne alle toten Objekte + t�te Objekte, die zu weit au�erhalb des
		// Sichtfeldes des Spielers ist

		int num = 0;
		int gameSize = this.objects.size();
		while (num < gameSize) {
//			if (this.objects.get(num) instanceof GEntity && ((GEntity) this.objects.get(num)).isDead()) {
			if (objects.get(num) != null && (objects.get(num).getLocation().distance(viewCenter) > 2048)) 
				
			
//					&& !(objects.get(num) instanceof Player) && !(objects.get(num) instanceof Cell))
					
				objects.get(num).setUseless(true);
				

			num++;
		}
	}

	/**
	 * 
	 * "registers" the object as present so that it is included into the process.
	 * Also, the list must then be re-sorted to ensure the correct order of the
	 * layors
	 * 
	 * @param object New instance of an GObject subclass
	 */
	public void place(GObject object) {
		objects.add(object);
		shouldSort = true;
	}

	/**
	 * 
	 * Here all registered objects are processed in a for loop. The method first
	 * checks if the object is an entity, if it is marked as dead, it checks if it
	 * is deleted from the processing. But if it is alive or not an entity, then it
	 * has to be checked if the object would be shown in the frame at all to save
	 * resources.
	 * 
	 * @param image    The image that fills the frame
	 * @param observer ImageObserver of the Frame (needed for correct scaling of the
	 *                 textures)
	 */
	public void paint(BufferedImage image, ImageObserver observer) {
		if (shouldSort) {
			try {
				try {
					Collections.sort(objects, new GObjectOrganizer());
				} catch (Exception e) {
					// TODO: handle exception
				}
			} catch (java.util.ConcurrentModificationException e) {
				e.printStackTrace();
			}

			shouldSort = false;
		}

		for (int i = 0; i < objects.size(); i++) {
			GObject object = objects.get(i);
			double screenFactor = image.getHeight() / 100D;
			if (object != null)
				if (object instanceof GEntity && ((GEntity) object).isDead()) {
					objects.remove(object);
					continue;
				} else if (object.getLocation().distance(viewCenter) < image.getWidth() / screenFactor / 1.25)
					object.draw(image, observer, viewCenter, screenFactor);

		}
	}

	/**
	 * A Default Content that projects a red rectangle
	 */
	public static class GDefaultContent extends GContent {

		@Override
		public void handle(double diff) {
		}

		@Override
		public void paint(BufferedImage image, ImageObserver observer) {
			Graphics graphics = image.getGraphics();
			graphics.setColor(Color.RED);
			graphics.fillRect(image.getWidth() / 4, image.getHeight() / 4, image.getWidth() / 2, image.getHeight() / 2);
		}
	}

	/**
	 * 
	 * @return The center of the Camera
	 */
	public GLocation getViewCenter() {
		return viewCenter;
	}

	public void setViewCenter(GLocation viewCenter) {
		this.viewCenter = viewCenter;
	}
}
