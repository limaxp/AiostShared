package com.pm.aiost.misc.resourcePack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import org.bukkit.configuration.ConfigurationSection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pm.aiost.misc.Material;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.FileUtils;

import net.md_5.bungee.config.Configuration;

public class ResourcePackWriter {

	private final Gson gson;
	private final String itemFolderPath;
	private final Map<String, JsonObject> changedJsons;
	private String baseMaterialName;
	private String materialName;
	private Collection<String> predicateNames;
	private Function<String, Object> valueSupplier;

	public ResourcePackWriter(String resourcePackPath) {
		gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
		String modelFolderPath = resourcePackPath + File.separator + "assets" + File.separator + "minecraft"
				+ File.separator + "models";
		itemFolderPath = modelFolderPath + File.separator + "item";
		changedJsons = new HashMap<String, JsonObject>();
	}

	public void writeItemJasons(ConfigurationSection itemsConfig) {
		Logger.log("ResourcePackWriter: Start writing item jasons...");

		for (String itemName : itemsConfig.getKeys(false)) {
			ConfigurationSection itemSection = itemsConfig.getConfigurationSection(itemName);
			writeItemJason(itemSection);
			if (itemSection.contains("parts")) {
				baseMaterialName = materialName;
				ConfigurationSection partsSection = itemSection.getConfigurationSection("parts");
				for (String partName : partsSection.getKeys(false))
					writeItemJason(partsSection.getConfigurationSection(partName));
			}
		}
		writeJsons();

		Logger.log("ResourcePackWriter: Finished writing item jasons!");
	}

	public void writeItemJasons_(Configuration itemsConfig) {
		Logger.log("ResourcePackWriter: Start writing item jasons...");

		for (String itemName : itemsConfig.getKeys()) {
			Configuration itemSection = itemsConfig.getSection(itemName);
			writeItemJason_(itemSection);
			if (itemSection.contains("parts")) {
				baseMaterialName = materialName;
				Configuration partsSection = itemSection.getSection("parts");
				for (String partName : partsSection.getKeys())
					writeItemJason_(partsSection.getSection(partName));
			}
		}
		writeJsons();

		Logger.log("ResourcePackWriter: Finished writing item jasons!");
	}

	private void writeItemJason(ConfigurationSection section) {
		String modelpath = section.getString("path");
		if (modelpath == null || modelpath.isEmpty())
			return;
		ConfigurationSection predicatesSection = section.getConfigurationSection("predicate");
		if (predicatesSection == null)
			return;

		materialName = section.contains("material") ? section.getString("material") : baseMaterialName;
		predicateNames = predicatesSection.getKeys(false);
		valueSupplier = predicatesSection::get;
		JsonArray jarr = getOverrides(getJson(materialName));
		writeItemJason(jarr, modelpath);
	}

	private void writeItemJason_(Configuration section) {
		String modelpath = section.getString("path");
		if (modelpath == null || modelpath.isEmpty())
			return;
		Configuration predicatesSection = section.getSection("predicate");
		if (predicatesSection == null)
			return;

		materialName = section.contains("material") ? section.getString("material") : baseMaterialName;
		predicateNames = predicatesSection.getKeys();
		valueSupplier = predicatesSection::get;
		JsonArray jarr = getOverrides(getJson(materialName));
		writeItemJason(jarr, modelpath);
	}

	private void writeItemJason(JsonArray jarr, String modelpath) {
		boolean foundPredicate = false;
		for (JsonElement jarrElement : jarr) {
			JsonObject jPredicate = jarrElement.getAsJsonObject();
			if (comparePredicates(jPredicate)) {
				String model = jPredicate.get("model").getAsString();
				if (!model.equals(modelpath)) {
					jPredicate.remove("model");
					jPredicate.addProperty("model", modelpath);
				}
				foundPredicate = true;
			}
		}
		if (!foundPredicate || jarr.size() < 1) {
			JsonObject jnewArrayElem = new JsonObject();
			JsonObject jnewPredicate = new JsonObject();
			addPredicates(jnewPredicate);
			jnewArrayElem.add("predicate", jnewPredicate);
			jnewArrayElem.addProperty("model", modelpath);
			jarr.add(jnewArrayElem);
		}
	}

	private boolean comparePredicates(JsonObject jPredicate) {
		Set<Entry<String, JsonElement>> existentPredicates = jPredicate.get("predicate").getAsJsonObject().entrySet();
		if (predicateNames.size() != existentPredicates.size())
			return false;
		for (String predicateName : predicateNames) {
			boolean found = false;
			Object predicateValue = valueSupplier.apply(predicateName);
			for (Entry<String, JsonElement> existentEntry : existentPredicates) {
				if (existentEntry.getKey().equals(predicateName)) {
					if (comparePredicate(predicateName, predicateValue, existentEntry.getValue()))
						found = true;
					break;
				}
			}
			if (!found)
				return false;
		}
		return true;
	}

	private boolean comparePredicate(String predicateName, Object predicateValue, JsonElement existentEntry) {
		if (predicateValue instanceof Double) {
			if ((Double) predicateValue == existentEntry.getAsDouble())
				return true;
			return false;

		} else if (predicateValue instanceof Float) {
			if ((Float) predicateValue == existentEntry.getAsFloat())
				return true;
			return false;

		} else if (predicateValue instanceof Integer) {
			if (predicateName.equals("damage")) {
				if (calculateDurabilityPercentage((int) predicateValue) == existentEntry.getAsDouble())
					return true;
			} else if ((Integer) predicateValue == existentEntry.getAsInt())
				return true;
			return false;

		} else if (predicateValue instanceof Short) {
			if ((Short) predicateValue == existentEntry.getAsShort())
				return true;
			return false;

		} else if (predicateValue instanceof Byte) {
			if ((Byte) predicateValue == existentEntry.getAsByte())
				return true;
			return false;

		} else if (predicateValue instanceof Character) {
			if (((Character) predicateValue).equals(existentEntry.getAsCharacter()))
				return true;
			return false;

		} else if (predicateValue instanceof String) {
			if (((String) predicateValue).equals(existentEntry.getAsString()))
				return true;
			return false;
		}
		return false;
	}

	private void addPredicates(JsonObject jPredicate) {
		if (predicateNames.size() > 0) {
			for (String predicateName : predicateNames)
				addPredicate(predicateName, valueSupplier.apply(predicateName), jPredicate);
		}
	}

	private void addPredicate(String predicateName, Object predicateValue, JsonObject jPredicate) {
		if (predicateValue instanceof Double)
			jPredicate.addProperty(predicateName, (Double) predicateValue);
		else if (predicateValue instanceof Float)
			jPredicate.addProperty(predicateName, (Float) predicateValue);
		else if (predicateValue instanceof Integer) {
			if (predicateName.equals("damage"))
				jPredicate.addProperty(predicateName, calculateDurabilityPercentage((int) predicateValue));
			else
				jPredicate.addProperty(predicateName, (Integer) predicateValue);
		} else if (predicateValue instanceof Short)
			jPredicate.addProperty(predicateName, (Short) predicateValue);
		else if (predicateValue instanceof Byte)
			jPredicate.addProperty(predicateName, (Byte) predicateValue);
		else if (predicateValue instanceof Character)
			jPredicate.addProperty(predicateName, (Character) predicateValue);
		else if (predicateValue instanceof String)
			jPredicate.addProperty(predicateName, (String) predicateValue);
	}

	private float calculateDurabilityPercentage(int durability) {
		int maxDurability = Material.valueOf(materialName).durability;
		float percentage = ((float) maxDurability - (float) durability) / (float) maxDurability;
		return (float) (Math.ceil(percentage * 10000.0) / 10000.0) + 0.00001f;
	}

	private String getItemPath(String materialName) {
		return itemFolderPath + File.separator + materialName + ".json";
	}

	private JsonArray getOverrides(JsonObject jo) {
		JsonArray jarr = jo.getAsJsonArray("overrides");
		if (jarr == null) {
			jarr = new JsonArray();
			jo.add("overrides", jarr);
		}
		return jarr;
	}

	private JsonObject getJson(String materialName) {
		JsonObject jo = changedJsons.get(materialName);
		if (jo == null) {
			jo = readJson(getItemPath(materialName));
			changedJsons.put(materialName, jo);
		}
		return jo;
	}

	private JsonObject readJson(String itemPath) {
		String jsonString = null;
		try {
			jsonString = new String(FileUtils.readFile(itemPath));
			return gson.fromJson(jsonString, JsonElement.class).getAsJsonObject();
		} catch (IOException e) {
			Logger.err("ResourcepackWriter: Error! Could not load item.json for path '" + itemPath + "'", e);
			return null;
		}
	}

	private void writeJsons() {
		for (Entry<String, JsonObject> entry : changedJsons.entrySet())
			writeJson(entry.getValue(), getItemPath(entry.getKey()));
	}

	private void writeJson(JsonObject jo, String itemPath) {
		try (Writer writer = new FileWriter(itemPath)) {
			gson.toJson(jo, writer);
		} catch (IOException e) {
			Logger.err("ResourcePackWriter: Error! Could not write item.json for path '" + itemPath + "'", e);
		}
	}
}