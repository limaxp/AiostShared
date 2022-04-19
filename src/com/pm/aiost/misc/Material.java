package com.pm.aiost.misc;

public enum Material {
	
	GOLD ("gold", 32),
	WOODEN ("wood", 59),
	STONE ("stone", 131),
	COPPER ("copper", 200),
	TIN ("tin", 200),
	IRON ("iron", 250),
	BRONZE ("bronze", 250),
	DIAMOND ("diamond", 1561),
	EMERALD ("emerald", 1732),
	RUBY ("ruby", 1934),
	BLACK_DIAMOND ("black diamond", 2372),    		// rare drop form diamond ore
	OBSIDIAN_CRYSTAL ("obsidian crystal", 3000),	// get by mining with black diamond
	LAVA_CRYSTAL ("lava crystal", 2700),   			// rare drop von nether lava block
	QUARTZ_CRYSTAL ("quartz crystal", 3500), 		// rare drop from quartz with lava pickaxe
	
	
	BOW ("wood", 384),
	BOW_IRON ("iron", 872),
	
	FISHING_ROD ("wood", 64),
	FLINT_AND_STEEL ("iron", 64),
	CARROT_ON_A_STICK ("wood", 25),
	SHEARS ("iron", 238),
	TRIDENT ("iron", 250),
	ELYTRA ("iron", 432),
	CROSSBOW ("iron", 326),
	SHIELD ("iron", 336);
	
	Material(String name, int durability) {
		this.name = name;
		this.durability = durability;
	}
		
	public String name;
	public int durability;
	
}
