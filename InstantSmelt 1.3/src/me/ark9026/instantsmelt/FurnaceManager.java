package me.ark9026.instantsmelt;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;

public class FurnaceManager {

	public static Map<Material, Material> recipes = new HashMap<Material, Material>();
	private static Map<Location, SmeltingFurnace> smeltingStatus = new HashMap<Location, SmeltingFurnace>();

	public static void startSmelting(Furnace furnace) {
		Location loc = furnace.getLocation();
		if (smeltingStatus.containsKey(loc)) {
			SmeltingFurnace sf = smeltingStatus.get(loc);
			sf.smelt();
		} else {
			SmeltingFurnace sf = new SmeltingFurnace(furnace);
			smeltingStatus.put(loc, sf);
			sf.smelt();
		}
	}

	public static void stopSmelting(Furnace furnace) {
		Location loc = furnace.getLocation();
		if (smeltingStatus.containsKey(loc)) {
			SmeltingFurnace sf = smeltingStatus.get(loc);
			sf.stopSmelt();
		}
	}

}
