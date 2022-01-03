package me.hyblockrnganalyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import me.hyblockrnganalyzer.eventhandler.HypixelEventHandler;
import me.hyblockrnganalyzer.eventhandler.TreasureChestEventHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main {
	public static final String MODID = "hyblockrnganalyzer";
	public static final String VERSION = "1.0";

	private File databaseFile;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		try {
			File dir = new File(event.getModConfigurationDirectory(), MODID);
			dir.mkdirs();
			databaseFile = new File(dir, "database.txt");
			if (!databaseFile.exists())
				databaseFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("database file: " + databaseFile.getAbsolutePath());
		System.out.println("[OK] preInit Hyblock RNG Analyzer");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// converting Minecraft events into specific Hypixel SkyBlock events
		MinecraftForge.EVENT_BUS.register(new HypixelEventHandler(this));
		// handling Hypixel events
		MinecraftForge.EVENT_BUS.register(new TreasureChestEventHandler(this));

		// TODO add more events

		System.out.println("[OK] registered events");
		System.out.println("[OK] init Hyblock RNG Analyzer");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		System.out.println("[OK] postInit Hyblock RNG Analyzer");
	}

	public void addDataset(String dataset) {
		if (databaseFile.exists()) {
			try {
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(databaseFile, true), StandardCharsets.UTF_8));
				writer.append(dataset);
				writer.close();
			} catch (IOException e) {
			}
		}
	}

}
