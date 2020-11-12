package de.nurkert.Pexiver.Engine.Events;


public abstract class Event {

	protected boolean isCancelled;
	double diff;

	/**
	 * @param diff - time since last frame
	 */
	public Event(double diff) {
		isCancelled = false;
		this.diff = diff;
	}
	
	/**
	 * check if event is cancled (if its cancled listening methods get skipped)
	 */
	public boolean isCancelled() {
		return isCancelled;
	}

	/**
	 * clancle to skip following listening methods
	 */
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	/**
	 * get time since last frame
	 */
	public double getDiff() {
		return diff;
	}

	public void setDiff(double diff) {
		this.diff = diff;
	}
}
