package me.hyblockrnganalyzer.eventhandler;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.JerryBoxOpenedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class JerryBoxEventHandler {

	private Main main;

	public JerryBoxEventHandler(Main main) {
		this.main = main;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onJerryBox(JerryBoxOpenedEvent event) {
		main.getTxtDatabase().addDataset(
				"_boxType:" + event.getBoxTypeNumber() + "," + event.getName() + ":" + event.getCount() + "\n", 3);
	}
}
