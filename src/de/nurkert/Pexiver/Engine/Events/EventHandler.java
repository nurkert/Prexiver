package de.nurkert.Pexiver.Engine.Events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.nurkert.Pexiver.Engine.Events.EventHandler.GEventMethod.GEventPriority;

/**
 * @author nikoburkert
 */
public class EventHandler {

	/**
	 * a method id links to the corresponding method
	 * these are the methods that listening for events
	 */
	final private static HashMap<String, GMethod> methods = new HashMap<String, GMethod>();
	/**
	 * the class of an event linked to a list filled 
	 * with the methods ids of the corresponding 
	 * listening methods
	 */
	final private static HashMap<String, List<String>> events = new HashMap<String, List<String>>();

	/**
	 * @param event - GEvent to be called.
	 * @return The methods that listen for this event.
	 */
	private static List<GMethod> getMethods(Event event) {
		if (events.containsKey(event.getClass().toString())) {
			List<GMethod> methods = new ArrayList<GMethod>();
			for (int i = 0; i < events.get(event.getClass().toString()).size(); i++) {
				String id = events.get(event.getClass().toString()).get(i);
				if (EventHandler.methods.containsKey(id))
					methods.add(EventHandler.methods.get(id));
				else
					events.get(event.getClass().toString()).remove(i--);
			}
			try {
				Collections.sort(methods);
			}
			catch(Exception e) {e.printStackTrace();}
			
			return methods;
		}
		return null;
	}

	/**
	 * Puts the methods contained in the listener in the 
	 * corresponding lists and updates the links
	 * 
	 * @param listener	a subclass of GEventListener 
	 * 					containing methods that listen for events 
	 */
	public static void register(GEventListener listener) {
		List<String> listeningMethods = new ArrayList<String>();

		Method[] listenerMethods = listener.getClass().getMethods();
		for (int i = 0; i < listenerMethods.length; i++) {
			Method method = listenerMethods[i];

			if (method.isAnnotationPresent(GEventMethod.class)) {
				String methodID = UUID.randomUUID().toString().substring(0, 10);

				methods.put(methodID, new GMethod(method, listener));
				listeningMethods.add(methodID);

				Class<?>[] parameterTypes = method.getParameterTypes();
				if (parameterTypes.length == 1) {
					if (!events.containsKey(parameterTypes[0].toString()))
						events.put(parameterTypes[0].toString(), new ArrayList<String>());

					events.get(parameterTypes[0].toString()).add(methodID);
				}
			}
		}
	}

	/**
	 * Calls all methods waiting for the corresponding event
	 * 
	 * @param event		New Instance of an event subclass
	 */
	public static void call(Event event) {
		if (getMethods(event) != null)
			for (GMethod method : getMethods(event))
				if (!event.isCancelled || method.ignoreCancelled)
					method.invoke(event);

	}

	/**
	 * 
	 * Annotation for the waiting methods in order to be able 
	 * to load them more easily and assign a priority to their 
	 * execution (the higher the priority, the earlier it is executed)
	 *
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface GEventMethod {
		
		boolean ignoreCancelled() default false;

		GEventPriority priority() default GEventPriority.NORMAL;

		public enum GEventPriority {
			LOWER, LOW, NORMAL, HIGH, HIGHER, FOURTWENTY;
		}
	}

	/**
	 * Container class for the method and a matching listener instance
	 */
	private static class GMethod implements Comparable<GMethod> {

		Method method;
		GEventListener listener;
		GEventPriority priority;
		boolean ignoreCancelled;

		public GMethod(Method method, GEventListener listener) {
			this.method = method;
			this.listener = listener;
			this.priority = method.getAnnotation(GEventMethod.class).priority();
		}

		public void invoke(Event event) {
			try {
				method.invoke(listener, event);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

				e.printStackTrace();
			}
		}

		@Override
		public int compareTo(GMethod gMethod) {
			if (priority.ordinal() > gMethod.priority.ordinal()) {
				return 1;
			} else if (priority.ordinal() < gMethod.priority.ordinal()) {
				return -1;
			}
			return 0;
		}
	}

	public interface GEventListener {
	}
}
