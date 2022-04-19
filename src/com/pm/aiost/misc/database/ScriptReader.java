package com.pm.aiost.misc.database;

import java.util.ArrayList;

import com.pm.aiost.misc.log.Logger;

public class ScriptReader {

	// DOESNT WORK!
	@SuppressWarnings("unused")
	private static ArrayList<String> scriptToBatchList(Iterable<String> script) {
		ArrayList<String> list = new ArrayList<String>();
		StringBuilder stringbuilder = new StringBuilder();
		String delimiter = ";";

		for (String line : script) {
			if (line.contains("DELIMITER")) {
				delimiter = line.split("DELIMITER")[1].trim();
				Logger.log("DELIMITER: " + delimiter);
				continue;
			}

			if (line.contains(delimiter)) {
				String[] delimiterlineSplit = line.split(delimiter);
				if (delimiterlineSplit.length > 0)
					stringbuilder.append(delimiterlineSplit[0]);
				list.add(stringbuilder.toString());
				Logger.log(stringbuilder.toString());
				stringbuilder = new StringBuilder();
			} else {
				stringbuilder.append(line);
			}
		}
		return list;
	}

	public static ArrayList<String> scriptToBatchList(String script) {
		ArrayList<String> list = new ArrayList<String>();

		if (script.contains("DELIMITER")) {
			String delimiter = ";";
			int index = script.indexOf("DELIMITER");
			readRegion(list, script.substring(0, index), delimiter);

			script = script.substring(index + 9, script.length());
			while (script.contains("DELIMITER")) {
				delimiter = script.split("\\r?\\n")[0].trim();
				index = script.indexOf("DELIMITER");
				String region = script.substring(0, index);
				readRegion(list, region, delimiter);
				if (index + 9 != script.length())
					script = script.substring(index + 9, script.length());
			}
			if (index + 9 != script.length())
				readRegion(list, script, ";");
		} else
			readRegion(list, script, ";");

		return list;
	}

	private static void readRegion(ArrayList<String> list, String region, String delimiter) {
		for (String split : region.split(delimiter)) {
			split = split.trim();
			if (!split.isEmpty()) {
				list.add(split);
			}
		}
	}
}
