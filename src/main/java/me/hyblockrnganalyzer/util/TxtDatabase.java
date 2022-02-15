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
	private Main main;
	private File logFolder;
	private ArrayList<String> logFileNames = new ArrayList<String>();

	public TxtDatabase(Main main) {
		this.main = main;
	}

	public File getFolder() {
		return logFolder;
	}

	public void setFolder(FMLPreInitializationEvent event) {
		logFolder = new File(event.getModConfigurationDirectory(), Main.MODID);
		logFolder.mkdirs();
	}

	public void addFileName(String name) {
		logFileNames.add(name);
	}

	public void createFiles() {
		for (String name : logFileNames)
			createFile(name);
	}

	private void createFile(String name) {
		try {
			File file = new File(logFolder, name);
			if (!file.exists())
				file.createNewFile();
			System.out.println("database file: " + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean lookupDataset(String data, String fileName) {
		File file = new File(logFolder, fileName);
		if (file.exists()) {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.isEmpty())
						continue;
					if (line.contains(data)) {
						reader.close();
						return true;
					}
				}
				reader.close();
			} catch (IOException ignored) {
			}
		}
		return false;
	}

	public void addDataset(String dataset, String fileName) {
		File file = new File(logFolder, fileName);
		if (file.exists()) {
			try {
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8));
				writer.append("_timestamp:" + System.currentTimeMillis() + ",_lobby:"
						+ main.getLobbyStatus().getServer() + ",");
				writer.append(dataset);
				writer.close();
			} catch (IOException e) {
			}
		}
	}

	public void allToCsv() {
		for (String name : logFileNames) {
			File txtFile = new File(logFolder, name);
			File csvFile = new File(logFolder, name.split("\\.")[0] + ".csv");
			if (!csvFile.exists())
				try {
					csvFile.createNewFile();
				} catch (IOException ignored) {
				}
			if (txtFile.exists() && csvFile.exists()) {
				try {
					ArrayList<TreeMap<String, String>> data = new ArrayList<TreeMap<String, String>>();
					TreeSet<String> items = new TreeSet<String>();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(new FileInputStream(txtFile), StandardCharsets.UTF_8));
					String line;
					while ((line = reader.readLine()) != null) {
						if (line.isEmpty())
							continue;
						TreeMap<String, String> tm = new TreeMap<String, String>();
						for (String s : line.split(",")) {
							if (s.isEmpty() || !s.contains(":") || s.split(":")[1].length() <= 0)
								continue;
							tm.put(s.split(":")[0], s.split(":")[1]);
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
					for (TreeMap<String, String> tm : data) {
						first = true;
						for (String item : itemList)
							writer.append((first ? ((first = false) ? "" : "") : ",") + tm.getOrDefault(item, "0"));
						writer.append("\n");
					}
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
