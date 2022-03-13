package me.hyblockrnganalyzer.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import me.hyblockrnganalyzer.Main;
import net.minecraft.client.Minecraft;

public class ServerAPI {
	private Main main;
	private String userID;
	private String boundary;
	private SSLContext context;

	private static final String LINE_FEED = "\r\n";
	private static final String CHARSET = "UTF-8";

	public ServerAPI(Main main) {
		this.main = main;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public boolean sendFile(File file) {
		// mostly from https://gist.github.com/shtratos/8e9570a4a5591b2bcecd55ca60b3f24f
		try {
			URL url = new URL("https://www.wgaayocaxsjzyusrerag.tk/upload");
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			final String boundary = "---------------" + Long.toHexString(System.currentTimeMillis());
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			con.setRequestProperty("user-agent", "Mozilla/5.0 ");
			OutputStream directOutput = con.getOutputStream();
			PrintWriter body = new PrintWriter(new OutputStreamWriter(directOutput, CHARSET), true);
			body.append(LINE_FEED);
			addSimpleFormData("username", Minecraft.getMinecraft().getSession().getUsername(), body, boundary);
			addSimpleFormData("uuid", Minecraft.getMinecraft().getSession().getPlayerID(), body, boundary);
			addSimpleFormData("userID", userID, body, boundary);
			addSimpleFormData("modversion", Main.VERSION, body, boundary);
			addFileData("file", file.getName(), Files.readAllBytes(file.toPath()), body, directOutput, boundary);
			addCloseDelimiter(body, boundary);
			int code = con.getResponseCode();
			return code >= 200 && code < 300;
		} catch (IOException ignored) {
			return false;
		}
	}

	private static void addSimpleFormData(String paramName, String wert, PrintWriter body, final String boundary) {
		body.append("--").append(boundary).append(LINE_FEED);
		body.append("Content-Disposition: form-data; name=\"" + paramName + "\"").append(LINE_FEED);
		body.append("Content-Type: text/plain; charset=" + CHARSET).append(LINE_FEED);
		body.append(LINE_FEED);
		body.append(wert).append(LINE_FEED);
		body.flush();
	}

	private static void addFileData(String paramName, String filename, byte[] byteStream, PrintWriter body,
			OutputStream directOutput, final String boundary) throws IOException {
		body.append("--").append(boundary).append(LINE_FEED);
		body.append("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\"" + filename + "\"")
				.append(LINE_FEED);
		body.append("Content-Type: application/octed-stream").append(LINE_FEED);
		body.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
		body.append(LINE_FEED);
		body.flush();
		directOutput.write(byteStream);
		directOutput.flush();
		body.append(LINE_FEED);
		body.flush();
	}

	private static void addCloseDelimiter(PrintWriter body, final String boundary) {
		body.append("--").append(boundary).append("--").append(LINE_FEED);
		body.flush();
	}

}
