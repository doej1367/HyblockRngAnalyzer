package me.hyblockrnganalyzer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class DiscordWebhook {
	private String channel_id;
	private String token;
	private String boundary;

	private static final String LINE_FEED = "\r\n";
	private static final String CHARSET = "UTF-8";

	public DiscordWebhook(String channel_id) {
		this.channel_id = channel_id;
	}

	public void setToken(byte[] token) {
		this.token = new String(token);
	}

	public boolean sendFile(String username, File file) {
		return sendFile(username, null, file);
	}

	public boolean sendFile(String username, String message, File file) {
		// mostly from https://stackoverflow.com/a/34409142/6307611
		try {
			boundary = "===" + System.currentTimeMillis() + "===";
			URL url = new URL("https://discord.com/api/webhooks/" + channel_id + "/" + token);
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(5000);
			urlConnection.setUseCaches(false);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setRequestProperty("user-agent", "Mozilla/5.0 ");
			urlConnection.setRequestProperty("content-type", "multipart/form-data; boundary=" + boundary);

			OutputStream os = urlConnection.getOutputStream();
			PrintWriter w = new PrintWriter(new OutputStreamWriter(os, CHARSET), true);
			if (message != null)
				addFormField(w, "payload_json",
						"{\"username\": \"" + username + "\", \"content\": \"" + message + "\"}");
			else
				addFormField(w, "payload_json", "{\"username\": \"" + username + "\"}");
			addFilePart(os, w, "file", file);
			w.append(LINE_FEED).flush();
			w.append("--" + boundary + "--").append(LINE_FEED);
			w.close();
			os.close();
			int code = urlConnection.getResponseCode();

			// error handling
			System.out.println(code + ": " + urlConnection.getResponseMessage());
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(code >= 100 && code < 400) ? urlConnection.getInputStream() : urlConnection.getErrorStream()));
			StringBuilder sb = new StringBuilder();
			String buffer;
			while ((buffer = br.readLine()) != null)
				sb.append(buffer);
			System.out.println(sb.toString());

			urlConnection.disconnect();
			return code >= 200 && code < 300;
		} catch (MalformedURLException ignored) {
			return false;
		} catch (IOException ignored) {
			return false;
		}
	}

	public void addFormField(PrintWriter w, String name, String value) {
		// mostly from https://stackoverflow.com/a/34409142/6307611
		w.append("--" + boundary).append(LINE_FEED);
		w.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
		w.append("Content-Type: text/plain; charset=" + CHARSET).append(LINE_FEED).append(LINE_FEED);
		w.append(value).append(LINE_FEED);
		w.flush();
	}

	public void addFilePart(OutputStream os, PrintWriter w, String fieldName, File uploadFile) throws IOException {
		// mostly from https://stackoverflow.com/a/34409142/6307611
		String fileName = uploadFile.getName();
		w.append("--" + boundary).append(LINE_FEED);
		w.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"")
				.append(LINE_FEED);
		w.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
		w.append("Content-Transfer-Encoding: binary").append(LINE_FEED).append(LINE_FEED);
		w.flush();

		FileInputStream inputStream = new FileInputStream(uploadFile);
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1)
			os.write(buffer, 0, bytesRead);
		os.flush();
		inputStream.close();
		w.append(LINE_FEED);
		w.flush();
	}
}
