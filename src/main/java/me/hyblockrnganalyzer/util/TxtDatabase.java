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
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class TxtDatabase {
	private Main main;
	private File logFolder;
	private TreeMap<String, DiscordWebhook> logFileNames = new TreeMap<String, DiscordWebhook>();

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

	public void addFileNameWithWebhook(String name, DiscordWebhook hook) {
		logFileNames.put(name, hook);
	}

	public long getTotalFileSize() {
		long totalFileSize = 0;
		for (String name : logFileNames.keySet())
			totalFileSize += getFileSize(name);
		return totalFileSize;
	}

	private long getFileSize(String name) {
		File file = new File(logFolder, name);
		if (file.exists())
			return file.length();
		return 0;
	}

	public void submitFilesToDiscord() {
		File archive = new File(logFolder, "archive-" + System.currentTimeMillis());
		archive.mkdirs();
		for (String name : logFileNames.keySet()) {
			if (sendFileToDiscord(name)) {
				moveFile(name, archive);
				String filePath = new File(logFolder, name).getAbsolutePath();
				System.out.println("sent and archived file: " + filePath);
			}
		}
	}

	private boolean sendFileToDiscord(String name) {
		File file = new File(logFolder, name);
		if (file.exists())
			return logFileNames.get(name).sendFile(Minecraft.getMinecraft().getSession().getUsername(), file);
		return false;
	}

	private void moveFile(String name, File archive) {
		File file = new File(logFolder, name);
		if (file.exists())
			file.renameTo(new File(archive, name));
	}

	public boolean lookupDataset(String data, String fileName) {
		File file = new File(logFolder, fileName);
		if (!file.exists())
			return false;
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
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException ignored) {
			}
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
		for (String name : logFileNames.keySet()) {
			File txtFile = new File(logFolder, name);
			if (!txtFile.exists())
				continue;
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
