package me.hyblockrnganalyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import me.hyblockrnganalyzer.command.CsvFileCreationCommand;
import me.hyblockrnganalyzer.command.TestCommand;
import me.hyblockrnganalyzer.eventhandler.DungeonChestEventHandler;
import me.hyblockrnganalyzer.eventhandler.JerryBoxEventHandler;
import me.hyblockrnganalyzer.eventhandler.NucleusLootEventHandler;
import me.hyblockrnganalyzer.eventhandler.TreasureChestEventHandler;
import me.hyblockrnganalyzer.util.DungeonChestStatus;
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
	public static final String VERSION = "1.5";

	private File logFolder;
	public String[] logFileNames = { "databaseTreasureChest.txt", "databaseLootChest.txt", "databaseNucleusLoot.txt",
			"databaseGreenJerryBox.txt", "databaseBlueJerryBox.txt", "databasePurpleJerryBox.txt",
			"databaseGoldJerryBox.txt", "databaseDungeons.txt" };
	private DungeonChestStatus dungeonChestStatus = new DungeonChestStatus();

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
		ClientCommandHandler.instance.registerCommand(new CsvFileCreationCommand(this));
		// converting Minecraft events into specific Hypixel SkyBlock events
		MinecraftForge.EVENT_BUS.register(new HypixelEventHandler(this));
		// handling Hypixel events
		MinecraftForge.EVENT_BUS.register(new TreasureChestEventHandler(this));
		MinecraftForge.EVENT_BUS.register(new DungeonChestEventHandler(this));
		MinecraftForge.EVENT_BUS.register(new JerryBoxEventHandler(this));
		MinecraftForge.EVENT_BUS.register(new NucleusLootEventHandler(this));
		// TODO (placeholder for more event handlers)

		System.out.println("[OK] registered events");
		System.out.println("[OK] init Hyblock RNG Analyzer");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		System.out.println("[OK] postInit Hyblock RNG Analyzer");
	}

	public void addDataset(String dataset, int fileNumber) {
		File file = new File(new File(logFolder, MODID), logFileNames[fileNumber]);
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

	public void databasesToCsv() {
		for (String name : logFileNames) {
			File txtFile = new File(new File(logFolder, MODID), name);
			File csvFile = new File(new File(logFolder, MODID), name.split("\\.")[0] + ".csv");
			if (!csvFile.exists())
				try {
					csvFile.createNewFile();
				} catch (IOException ignored) {
				}
			if (txtFile.exists() && csvFile.exists()) {
				try {
					ArrayList<TreeMap<String, Integer>> data = new ArrayList<TreeMap<String, Integer>>();
					TreeSet<String> items = new TreeSet<String>();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(new FileInputStream(txtFile), StandardCharsets.UTF_8));
					String line;
					while ((line = reader.readLine()) != null) {
						if (line.isEmpty())
							continue;
						TreeMap<String, Integer> tm = new TreeMap<String, Integer>();
						for (String s : line.split(",")) {
							if (s.isEmpty() || !s.contains(":") || !s.split(":")[1].matches("[0-9]+"))
								continue;
							tm.put(s.split(":")[0], Integer.parseInt(s.split(":")[1]));
							items.add(s.split(":")[0]);
						}
						data.add(tm);
					}
					reader.close();
					List<String> itemList = new ArrayList<String>(items);
					Collections.sort(itemList);
					BufferedWriter writer = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(csvFile, false), StandardCharsets.UTF_8));
					boolean first = true;
					for (String item : itemList)
						writer.append((first ? ((first = false) ? "" : "") : ",") + item);
					writer.append("\n");
					for (TreeMap<String, Integer> tm : data) {
						first = true;
						for (String item : itemList)
							writer.append((first ? ((first = false) ? "" : "") : ",") + tm.getOrDefault(item, 0));
						writer.append("\n");
					}
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public DungeonChestStatus getDungeonChestStatus() {
		return dungeonChestStatus;
	}
}
