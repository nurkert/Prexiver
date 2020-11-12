package de.nurkert.Pexiver.Engine.Back;

import java.util.ArrayList;
import java.util.HashMap;

import de.nurkert.Pexiver.Engine.Entitys.GObject;
import de.nurkert.Pexiver.Engine.Entitys.GObject.Collidable;
import de.nurkert.Pexiver.Engine.Entitys.GPlayer;

/**
 * @author nikoburkert
 */
public class CollisionDetection {

	public CollisionDetection() {
	}

	public void detect(ArrayList<GObject> objects) {
		new CollisionField(objects);
	}

	/**
	 * collision detection through recursive spational partitioning
	 */
	public static class CollisionField {

		double minX, maxX, minY, maxY;
		HashMap<GObject, Integer> objects;
		CollisionSubFieldWithObjects subField;
		ArrayList<GObject> content;

		public CollisionField(ArrayList<GObject> content) {
			this.content = content;

			minX = Double.MAX_VALUE;
			maxX = Double.MIN_VALUE;
			minY = Double.MAX_VALUE;
			maxY = Double.MIN_VALUE;
			for (int i = 0; i < content.size(); i++) {
				GObject object = content.get(i);
				if (object instanceof Collidable) {
					if (minX > object.getLocation().getX())
						minX = object.getLocation().getX();
					if (maxX < object.getLocation().getX())
						maxX = object.getLocation().getX();
					if (minY > object.getLocation().getY())
						minY = object.getLocation().getY();
					if (maxY < object.getLocation().getY())
						maxY = object.getLocation().getY();
				}
			}

			subField = new CollisionSubFieldWithObjects(null, minX, maxX, minY, maxY, "");

			init();
		}

		private void init() {
			for (int i = 0; i < content.size(); i++) {
				if (content.get(i) instanceof Collidable)
					subField.place(content.get(i));
			}

			for (int i = 0; i < content.size(); i++) {
				GObject object = content.get(i);
				if (object != null)
					if ((object.isMoveing() || object instanceof GPlayer) && object instanceof Collidable) {

						CollisionSubField field = subField.getSubField(object.getFieldID());
						if (field != null) {

							for (CollisionSubField sub : field.getNeighborFields()) {

								GObject other = sub.get().get(0);
								if (other != object && other instanceof Collidable
										&& (((Collidable) object).wouldCollide(other)
												|| ((Collidable) other).wouldCollide(object))) {
									((Collidable) object).collide(other);
									((Collidable) other).collide(object);
								}
							}
						}
					} //else
//						content.remove(i--);
			}
		}
	}

	public enum NeighborType {
		LEFT(-1), DOWN_RIGHT(3), DOWN(2), LEFT_DOWN(1), RIGHT(1), LEFT_UP(-3), UP(-2), UP_RIGHT(-1);

		int add;

		NeighborType(int add) {
			this.add = add;
		}

		public NeighborType invert() {
			int i = (ordinal() + 4) % values().length;
			return values()[i];
		}

		public int directNeighborIndex(int index) {
			int i = (index + add) % values().length;
			return i;
		}
	}

	public static abstract class CollisionSubField {

		String id;
		int directID;
		CollisionSubField parent;
		double minX, maxX, minY, maxY, centerX, centerY;
		CollisionSubField[] fields;

		public CollisionSubField(CollisionSubField parent, double minX, double maxX, double minY, double maxY,
				String id) {
			directID = id.toCharArray().length > 0 ? Integer.valueOf(id.toCharArray()[id.toCharArray().length - 1] + "")
					: -1;
			this.parent = parent;
			this.minX = minX;
			this.maxX = maxX;
			this.minY = minY;
			this.maxY = maxY;
			centerX = mid(minX, maxX);
			centerY = mid(minY, maxY);
			fields = new CollisionSubField[4];
			this.id = id;
		}

		public abstract void place(GObject object);

		public abstract ArrayList<GObject> get(String id);

		public abstract ArrayList<GObject> get();

		public abstract CollisionSubField getSubField(String id);

		public CollisionSubField[] getFields() {
			return fields;
		}

		public ArrayList<CollisionSubField> getNeighborFields() {
			ArrayList<CollisionSubField> fields = new ArrayList<CollisionSubField>();
			for (NeighborType type : NeighborType.values()) {
				getNeighborField(type, fields, true);
			}
			return fields;
		}

		/**
		 * 
		 * 
		 * @param type   The neighbour type to search for
		 * @param fields The list with the fields to affect
		 * @param invert If invert, then the next fields are zoomed in, otherwise zoomed
		 *               out
		 */
		public void getNeighborField(NeighborType type, ArrayList<CollisionSubField> fields, boolean invert) {
			if (invert && parent != null) {
				if (hasDirectNeighbor(type)) {
					switch (type) {
					case UP:
						if (parent.getFields()[0] != null)
							parent.getFields()[0].getNeighborField(type.invert(), fields, false);

						if (parent.getFields()[1] != null)
							parent.getFields()[1].getNeighborField(type.invert(), fields, false);
						break;
					case LEFT:
						if (parent.getFields()[0] != null)
							parent.getFields()[0].getNeighborField(type.invert(), fields, false);

						if (parent.getFields()[2] != null)
							parent.getFields()[2].getNeighborField(type.invert(), fields, false);
						break;
					case RIGHT:
						if (parent.getFields()[3] != null)
							parent.getFields()[3].getNeighborField(type.invert(), fields, false);

						if (parent.getFields()[1] != null)
							parent.getFields()[1].getNeighborField(type.invert(), fields, false);
						break;
					case DOWN:
						if (parent.getFields()[2] != null)
							parent.getFields()[2].getNeighborField(type.invert(), fields, false);

						if (parent.getFields()[3] != null)
							parent.getFields()[3].getNeighborField(type.invert(), fields, false);
						break;
					default:
						int i = type.directNeighborIndex(directID);
						if (parent.getFields()[i] != null)
							parent.getFields()[i].getNeighborField(type.invert(), fields, false);
						break;
					}
				} else
					parent.getNeighborField(type, fields, true);
			} else if (!invert) {
				if (this instanceof CollisionSubFieldWithObject) {
					fields.add(this);
					return;
				}

				switch (type) {
				case UP:
					if (getFields()[0] != null)
						getFields()[0].getNeighborField(type, fields, false);

					if (getFields()[1] != null)
						getFields()[1].getNeighborField(type, fields, false);
					break;
				case LEFT:
					if (getFields()[0] != null)
						getFields()[0].getNeighborField(type, fields, false);

					if (getFields()[2] != null)
						getFields()[2].getNeighborField(type, fields, false);
					break;
				case RIGHT:
					if (getFields()[3] != null)
						getFields()[3].getNeighborField(type, fields, false);

					if (getFields()[1] != null)
						getFields()[1].getNeighborField(type, fields, false);
					break;
				case DOWN:
					if (getFields()[2] != null)
						getFields()[2].getNeighborField(type.invert(), fields, false);

					if (getFields()[3] != null)
						getFields()[3].getNeighborField(type.invert(), fields, false);
					break;
				default:
					int i = type.directNeighborIndex(directID);
					if (getFields()[i] != null)
						getFields()[i].getNeighborField(type.invert(), fields, false);
					break;
				}
			}
		}

		/**
		 * The method looks if a field has a direct neighbor. A direct neighbor in the
		 * corresponding direction has a field if it does not have to leave the parent
		 * field to get to it
		 * 
		 * @param type The direction in which the neighbor should be checked
		 */
		public boolean hasDirectNeighbor(NeighborType type) {
			switch (type) {
			case LEFT_UP:
				return directID == 3;
			case UP:
				return directID == 2 || directID == 3;
			case UP_RIGHT:
				return directID == 2;
			case LEFT:
				return directID == 1 || directID == 3;
			case RIGHT:
				return directID == 0 || directID == 2;
			case LEFT_DOWN:
				return directID == 1;
			case DOWN:
				return directID == 0 || directID == 1;
			case DOWN_RIGHT:
				return directID == 0;
			default:
				break;
			}
			return false;
		}

		public double getCenterX() {
			return centerX;
		}

		public double getMinX() {
			return minX;
		}

		public double getMaxX() {
			return maxX;
		}

		public double getCenterY() {
			return centerY;
		}

		public double getMinY() {
			return minY;
		}

		public double getMaxY() {
			return maxY;
		}

		public String getId() {
			return id;
		}

		/**
		 * Determine the middle of two numbers and also works with negative values
		 */
		private double mid(double min, double max) {
			if (min < 0 && max > 0) {
				return min + (Math.abs(min) + max) / 2;
			} else if (min < 0 && max < 0) {
				return -(Math.abs(min) + Math.abs(max)) / 2;
			}
			return min + (max - min) / 2;
		}
	}

	public static class CollisionSubFieldWithObject extends CollisionSubField {

		GObject object;

		public CollisionSubFieldWithObject(CollisionSubField parent, double minX, double maxX, double minY, double maxY,
				String id) {
			super(parent, minX, maxX, minY, maxY, id);
			object = null;
		}

		@Override
		public void place(GObject object) {
			this.object = object;
			object.setFieldID(id);
		}

		public GObject getGObject() {
			return object;
		}

		@Override
		public ArrayList<GObject> get(String id) {
			return get();
		}

		@Override
		public ArrayList<GObject> get() {
			ArrayList<GObject> objects = new ArrayList<GObject>();
			objects.add(object);
			return objects;
		}

		@Override
		public CollisionSubField getSubField(String id) {
			return this;
		}
	}

	public static class CollisionSubFieldWithObjects extends CollisionSubField {

		public CollisionSubFieldWithObjects(CollisionSubField parent, double minX, double maxX, double minY,
				double maxY, String id) {
			super(parent, minX, maxX, minY, maxY, id);
		}

		@Override
		public void place(GObject object) {
			int subFieldIndex = getSubFieldID(object.getLocation().getX(), object.getLocation().getY());

			CollisionSubField field = getSubField(subFieldIndex);
			if (field instanceof CollisionSubFieldWithObject) {
				if (((CollisionSubFieldWithObject) field).getGObject() == null) {
					field.place(object);
				} else if (id.length() < 25) { //
					GObject other = ((CollisionSubFieldWithObject) field).getGObject();
					fields[subFieldIndex] = new CollisionSubFieldWithObjects(this, field.getMinX(), field.getMaxX(),
							field.getMinY(), field.getMaxY(), field.getId());
					fields[subFieldIndex].place(other);
					fields[subFieldIndex].place(object);
				}
			} else {
				fields[subFieldIndex].place(object);
			}
		}

//		Fields relative to center:
//			|
//		 0	|  1
//		--- C.---
//		 2	|  3	
//			|
		private int getSubFieldID(double x, double y) {
			if (y < getCenterY()) {
				if (x < getCenterX())
					return 0;
				else
					return 1;
			} else {
				if (x < getCenterX())
					return 2;
				else
					return 3;
			}
		}

		private CollisionSubField getSubField(int index) {
			if (fields[index] == null)
				switch (index) {
				case 0:
					fields[0] = new CollisionSubFieldWithObject(this, minX, getCenterX(), minY, getCenterY(), id + "0");
					break;
				case 1:
					fields[1] = new CollisionSubFieldWithObject(this, getCenterX(), maxX, minY, getCenterY(), id + "1");
					break;
				case 2:
					fields[2] = new CollisionSubFieldWithObject(this, minX, getCenterX(), getCenterY(), maxY, id + "2");
					break;
				case 3:
					fields[3] = new CollisionSubFieldWithObject(this, getCenterX(), maxX, getCenterY(), maxY, id + "3");
					break;
				default:
					break;
				}
			return fields[index];
		}

		@Override
		public ArrayList<GObject> get(String id) {
			if (id.length() > 0) {
				int index = Integer.valueOf(id.toCharArray()[0] + "");
				if (fields[index] != null)
					return fields[index].get(id.substring(1));
			}
			return get();
		}

		@Override
		public ArrayList<GObject> get() {
			ArrayList<GObject> objects = new ArrayList<GObject>();
			for (int i = 0; i < 3; i++)
				if (fields[i] != null)
					objects.addAll(fields[i].get());
			return objects;
		}

		@Override
		public CollisionSubField getSubField(String id) {
			int index = -1;
			if (id.toCharArray().length > 0)
				index = Integer.valueOf(id.toCharArray()[0] + "");
			if (fields.length > 0 && index >= 0 && fields[index] != null)
				return fields[index].getSubField(id.substring(1));
			return null;
		}
	}
}
