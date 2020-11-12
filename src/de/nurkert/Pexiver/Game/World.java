package de.nurkert.Pexiver.Game;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.nurkert.Pexiver.Engine.GContent;
import de.nurkert.Pexiver.Engine.Entitys.GEntity;
import de.nurkert.Pexiver.Engine.Entitys.GObject;
import de.nurkert.Pexiver.Engine.Events.EventHandler.GEventListener;
import de.nurkert.Pexiver.Engine.Events.EventHandler.GEventMethod;
import de.nurkert.Pexiver.Engine.Events.Essential.HandleObjectsEvent;
import de.nurkert.Pexiver.Game.Entitys.EntityType;
import de.nurkert.Pexiver.Game.Entitys.Player;

public class World extends GContent implements GEventListener {

	Random random;
	Player player;
	
	HashMap<EntityType, Integer> amount;

	public World() {
		random = new Random();

		amount = new HashMap<EntityType, Integer>();
		init();
	}

	private void init() {

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {


			}
		}, 0, 10000);
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@GEventMethod()
	public void on(HandleObjectsEvent event) {
		long currentTime = System.currentTimeMillis();
		if (player != null)
			for (int i= 0; i < getObjects().size(); i++) {
				GObject object = getObjects().get(i);
				if (object.getLocation().distance(player.getLocation()) > 750)
					object.setUseless(true);
			}
	}

	public void takeAmount(EntityType type) {

		if (amount.containsKey(type))
			amount.put(type, amount.get(type) > 0 ? amount.get(type) - 1 : 0);

	}

	public int getAmount(EntityType type) {
		if (amount.containsKey(type))
			return amount.get(type);
		return 0;
	}

	@Override
	public void place(GObject object) {
		if (object instanceof GEntity) {
			GEntity entity = (GEntity) object;
			if (!amount.containsKey(entity.getType()))
				amount.put(entity.getType(), 1);
			else
				amount.put(entity.getType(), amount.get(entity.getType()) + 1);
		}
		super.place(object);
	}

}
