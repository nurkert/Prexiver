package de.nurkert.Pexiver;

import de.nurkert.Pexiver.Engine.GFrame;
import de.nurkert.Pexiver.Engine.GLocation;
import de.nurkert.Pexiver.Engine.Events.EventHandler;
import de.nurkert.Pexiver.Engine.Events.EventHandler.GEventListener;
import de.nurkert.Pexiver.Engine.Events.EventHandler.GEventMethod;
import de.nurkert.Pexiver.Engine.Events.Essential.HandleObjectsEvent;
import de.nurkert.Pexiver.Engine.Front.GTexture;
import de.nurkert.Pexiver.Game.World;
import de.nurkert.Pexiver.Game.Entitys.Player;

public class Main implements GEventListener  {

	static World world;
	static Player player;
	public static GFrame frame;

	public static void main(String[] args) {
		EventHandler.register(new Main());

		Thread mainThread = new Thread("mainThread") {
			public void run() {

				frame = new GFrame("ImmuneTillDeath");
				world = new World();
				EventHandler.register(world);
				frame.setContent(world);
				frame.setBackground(new GTexture("/textures/background.png"));	

				player = new Player(new GLocation(0, 0, world), frame.getPanel().getContent().getViewCenter());
				world.setPlayer(player);
				world.place(player);

//				world.place(new PlayerUI(player));

				frame.update(frame.getGraphics());

//				GSound.playSound("/sounds/melody.wav");
			}
		};
		mainThread.start();
		
	}

	@GEventMethod
	public void on(HandleObjectsEvent event) {

		// System.out.println("pressed key:" + event.getKey());
	}
}
