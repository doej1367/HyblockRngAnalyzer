package me.hyblockrnganalyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import me.hyblockrnganalyzer.command.TestCommand;
import me.hyblockrnganalyzer.eventhandler.NucleusLootEventHandler;
import me.hyblockrnganalyzer.eventhandler.TreasureChestEventHandler;
import net.minecraftforge.client.ClientCommandHandler;
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
	public static final String VERSION = "1.2";

	private File logFolder;
	public String[] logFileNames = { "database.txt", "databaseFileNucleusLoot.txt" };

	public static File databaseFile;
	public static File databaseFileNucleusLoot;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logFolder = event.getModConfigurationDirectory();
		for (String name : logFileNames)
			createFile(name);
		System.out.println("[OK] preInit Hyblock RNG Analyzer");
	}

	private void createFile(String name) {
		try {
			File dir = new File(logFolder, MODID);
			dir.mkdirs();
			File file = new File(dir, name);
			if (!file.exists())
				file.createNewFile();
			System.out.println("database file: " + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ClientCommandHandler.instance.registerCommand(new TestCommand());
		// converting Minecraft events into specific Hypixel SkyBlock events
		MinecraftForge.EVENT_BUS.register(new HypixelEventHandler(this));
		// handling Hypixel events
		MinecraftForge.EVENT_BUS.register(new TreasureChestEventHandler(this));
		// TODO dungeon chests ( open chest event )
		// TODO add Jerry loot boxes ( right click with item event -> remember box type,
		// extract loot from chat)
		MinecraftForge.EVENT_BUS.register(new NucleusLootEventHandler(this));

		// TODO add more events

		System.out.println("[OK] registered events");
		System.out.println("[OK] init Hyblock RNG Analyzer");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		System.out.println("[OK] postInit Hyblock RNG Analyzer");
	}

	public void addDataset(String dataset, int fileName) {
		File file = new File(new File(logFolder, MODID), logFileNames[fileName]);
		if (file.exists()) {
			try {
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8));
				writer.append(dataset);
				writer.close();
			} catch (IOException e) {
			}
		}
	}

}
