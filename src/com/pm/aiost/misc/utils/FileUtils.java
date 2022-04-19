package com.pm.aiost.misc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.bukkit.craftbukkit.libs.org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class FileUtils {

	public static void copy(File source, File target) throws IOException {
		if (source.isDirectory())
			copyDirectory(source, target);
		else
			Files.copy(source, target);
	}

	public static void copyDirectory(File source, File target) throws IOException {
		if (!target.exists())
			target.mkdir();

		for (String f : source.list())
			copy(new File(source, f), new File(target, f));
	}

	public static void delete(final File file) throws IOException {
		delete(file.toPath());
	}

	public static void delete(final Path path) throws IOException {
		java.nio.file.Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
				java.nio.file.Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(final Path file, final IOException e) {
				return handleException(e);
			}

			private FileVisitResult handleException(final IOException e) {
				e.printStackTrace(); // replace with more robust error handling
				return FileVisitResult.TERMINATE;
			}

			@Override
			public FileVisitResult postVisitDirectory(final Path dir, final IOException e) throws IOException {
				if (e != null)
					return handleException(e);
				java.nio.file.Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	};

	public static byte[] readFile(Path path) throws IOException {
		return java.nio.file.Files.readAllBytes(path);
	}

	public static byte[] readFile(String path) throws IOException {
		return readFile(Paths.get(path));
	}

	public static List<String> readAllLines(String path, Charset encoding) throws IOException {
		return java.nio.file.Files.readAllLines(Paths.get(path), encoding);
	}

	public static List<String> readAllLines(String path) throws IOException {
		return readAllLines(path, Charset.defaultCharset());
	}

	public static void writeFile(String path, String data) throws FileNotFoundException {
		try (PrintStream out = new PrintStream(new FileOutputStream(path))) {
			out.print(data);
		}
	}

	public static void writeFile(String path, byte[] data) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(path)) {
			fos.write(data);
		}
	}

	public static void writeFile(String path, List<String> data) throws IOException {
		try (FileWriter writer = new FileWriter(path)) {
			for (String str : data)
				writer.write(str);
		}
	}

	public static void writeFile(String path, InputStream stream) throws IOException {
		writeFile(Paths.get(path), stream);
	}

	public static void writeFile(Path path, InputStream stream) throws IOException {
		java.nio.file.Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
	}

	public static String streamToString(InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is);
		java.util.Scanner s2 = s.useDelimiter("\\A");
		String string = s2.hasNext() ? s2.next() : "";
		s.close();
		s2.close();
		return string;
	}

	public static byte[] streamToByteArray(InputStream is) throws IOException {
		byte[] targetArray = new byte[is.available()];
		is.read(targetArray);
		return targetArray;
	}

	public static byte[] streamToByteArray_(InputStream is) throws IOException {
		return IOUtils.toByteArray(is);
	}

	public static List<String> streamToList(InputStream is) throws IOException {
		List<String> stringList = new ArrayList<String>();
		String line;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			while ((line = reader.readLine()) != null)
				stringList.add(line);
		}
		return stringList;
	}

	public static void zip(File file, File target) throws IOException {
		if (file.isDirectory())
			zipDir(file, target, File.separator, true);
		else
			zipFile(file, target);
	}

	public static void zipFile(File file, File target) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(target); ZipOutputStream zos = new ZipOutputStream(fos)) {
			zos.putNextEntry(new ZipEntry(file.getName()));
			byte[] bytes = readFile(file.getAbsolutePath());
			zos.write(bytes, 0, bytes.length);
			zos.closeEntry();
			zos.finish();
		}
	}

	public static void zipDir(File dir, File target, String fileSeperator, boolean withSourceFolder)
			throws IOException {
		String sourceName = "";
		if (withSourceFolder)
			sourceName = dir.getName() + fileSeperator;
		String sourcepath = dir.getAbsolutePath();
		int sourceLength = dir.getAbsolutePath().length();
		try (FileOutputStream fos = new FileOutputStream(target); ZipOutputStream zos = new ZipOutputStream(fos)) {
			for (String file : generateFileList(dir)) {
				file = file.substring(sourceLength + 1, file.length()).replace("\\", fileSeperator);
				zos.putNextEntry(new ZipEntry(sourceName + file));
				byte[] bytes = readFile(sourcepath + File.separator + file);
				zos.write(bytes, 0, bytes.length);
				zos.closeEntry();
			}
			zos.finish();
		}
	}

	public static List<String> generateFileList(File file) {
		List<String> fileList = new ArrayList<String>();
		generateFileList(fileList, file);
		return fileList;
	}

	private static void generateFileList(List<String> fileList, File file) {
		if (file.isFile())
			fileList.add(file.getAbsolutePath());
		else if (file.isDirectory()) {
			for (String filename : file.list())
				generateFileList(fileList, new File(file, filename));
		}
	}

	public static void createNotExisting(File file) throws IOException {
		if (!file.exists())
			file.createNewFile();
	}

	public static void createNotExisting_(File file) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createNotExistingFolder(File file) {
		if (!file.exists())
			file.mkdir();
	}

	public static void writeResourceFile(Class<?> clazz, String name, String path) throws IOException {
		try (InputStream in = getResourceStream(clazz, name)) {
			writeFile(path, in);
		}
	}

	public static void writeResourceFile(ClassLoader classLoader, String name, String path) throws IOException {
		try (InputStream in = getResourceStream(classLoader, name)) {
			writeFile(path, in);
		}
	}

	public static void writeResourceFile(String name, String path) throws IOException {
		try (InputStream in = getResourceStream(name)) {
			writeFile(path, in);
		}
	}

	public static InputStream getResourceStream(Class<?> clazz, String name) {
		return clazz.getResourceAsStream(name);
	}

	public static InputStream getResourceStream(ClassLoader classLoader, String name) {
		return classLoader.getResourceAsStream(name);
	}

	public static InputStream getResourceStream(String name) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
	}

	public static File getLastModified(String directoryFilePath) {
		return getLastModified(new File(directoryFilePath));
	}

	public static File getLastModified(File directory) {
		File[] files = directory.listFiles(File::isFile);
		long lastModifiedTime = Long.MIN_VALUE;
		File chosenFile = null;

		if (files != null) {
			for (File file : files) {
				if (file.lastModified() > lastModifiedTime) {
					chosenFile = file;
					lastModifiedTime = file.lastModified();
				}
			}
		}
		return chosenFile;
	}

	public static Object readJson(File file) throws FileNotFoundException, IOException, ParseException {
		try (FileReader reader = new FileReader(file)) {
			JSONParser parser = new JSONParser();
			return parser.parse(reader);
		}
	}

	public static void writeJson(File file, JSONObject json) throws FileNotFoundException, IOException {
		writeJson(file, new JsonParser().parse(json.toJSONString()));
	}

	public static void writeJson(File file, JSONArray json) throws FileNotFoundException, IOException {
		writeJson(file, new JsonParser().parse(json.toJSONString()));
	}

	public static void writeJson(File file, JsonElement json) throws FileNotFoundException, IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(gson.toJson(json));
			writer.flush();
		}
	}

	public static void writeJson(File file, String json) throws FileNotFoundException, IOException {
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(json);
			writer.flush();
		}
	}
}
