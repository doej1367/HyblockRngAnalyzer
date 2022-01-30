package me.hyblockrnganalyzer.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.TreeSet;

import me.hyblockrnganalyzer.Main;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class TxtDatabase {
	private File logFolder;
	public String[] logFileNames = { "databaseTreasureChest.txt", "databaseLootChest.txt", "databaseNucleusLoot.txt",
			"databaseJerryBoxes.txt", "databaseDungeons.txt", "databaseGifts.txt" };

	public void createFiles(FMLPreInitializationEvent event) {
		logFolder = event.getModConfigurationDirectory();
		for (String name : logFileNames)
			createFile(name);
	}

	private void createFile(String name) {
		try {
			File dir = new File(logFolder, Main.MODID);
			dir.mkdirs();
			File file = new File(dir, name);
			if (!file.exists())
				file.createNewFile();
			System.out.println("database file: " + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addDataset(String dataset, int fileNumber) {
		File file = new File(new File(logFolder, Main.MODID), logFileNames[fileNumber]);
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

	public void allToCsv() {
		for (String name : logFileNames) {
			File txtFile = new File(new File(logFolder, Main.MODID), name);
			File csvFile = new File(new File(logFolder, Main.MODID), name.split("\\.")[0] + ".csv");
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
					Collections.sort(itemList, Collator.getInstance(Locale.ENGLISH));
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
}
