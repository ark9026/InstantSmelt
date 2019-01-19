package me.ark9026.instantsmelt;

import java.io.File;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import me.ark9026.instantsmelt.versions.InstantSmelt_1_12_R1;
import me.ark9026.instantsmelt.versions.InstantSmelt_1_13_R1;
import me.ark9026.instantsmelt.versions.InstantSmelt_1_13_R2;
import me.ark9026.instantsmelt.versions.NMSBurnTime;

public class InstantSmelt extends JavaPlugin implements Listener {

	private NMSBurnTime burnTime;
	private static InstantSmelt instance;

	@Override
	public void onEnable() {
		instance = this;
		getServer().getPluginManager().registerEvents(this, this);
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			getLogger().info("Generating new config for InstantSmelt...");
			saveDefaultConfig();
			getConfig().options().copyDefaults(true);
		} else {
			getLogger().info("Loading config for InstantSmelt...");
		}
		Iterator<Recipe> iter = Bukkit.recipeIterator();
		while (iter.hasNext()) {
			Recipe rep = iter.next();
			if (!(rep instanceof FurnaceRecipe))
				continue;
			FurnaceRecipe frep = (FurnaceRecipe) rep;
			// getLogger().info("Input: " + frep.getInput().getType() + "Output: " +
			// frep.getResult().getType());
			FurnaceManager.recipes.put(frep.getInput().getType(), frep.getResult().getType());
		}
		if (!setupVersions()) {
			getLogger().info("There was a problem determining your server version!"
					+ "We'll default to 1.13.2; if this is incorrect please" + "contact ark9026 on SpigotMC");
		}
	}

	public static InstantSmelt getInstance() {
		return instance;
	}

	public NMSBurnTime getBurnTime() {
		return burnTime;
	}

	private boolean setupVersions() {
		String version;
		String[] bukkitVersionData;
		int subVersion;
		try {
			version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
			bukkitVersionData = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
			subVersion = Integer.parseInt(bukkitVersionData.length > 2 ? bukkitVersionData[2] : "0");
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException whatVersionAreYouUsingException) {
			burnTime = new InstantSmelt_1_13_R2();
			return false;
		}
		if (version.contains("v1_13")) {
			switch (subVersion) {
			case 0:
				burnTime = new InstantSmelt_1_13_R1();
				break;
			default:
				burnTime = new InstantSmelt_1_13_R2();
			}
		} else if (version.contains("v1_12")) {
			burnTime = new InstantSmelt_1_12_R1();
		} else {
			getLogger().info(
					"You're using a server version below 1.12 or above 1.13! If you're using a server version"
							+ "between 1.8 and 1.11, please visit "
							+ "https://www.spigotmc.org/resources/instantsmelt.15851/history"
							+ "and download InstantSmelt 1.2.1. If you are using a version above 1.13, please "
							+ "reach out to ark9026 on SpigotMC or check if a new version has been released!");
			getServer().getPluginManager().disablePlugin(this);
			return true;
		}
		getLogger().info("InstantSmelt has successfully loaded!");
		return burnTime != null;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player p = (Player) event.getWhoClicked();
			if (p.getOpenInventory() != null) {
				if (p.getOpenInventory().getTopInventory() != null) {
					if (event.getClickedInventory() != null) {
						if (event.getCursor() != null) {
							if (event.getCurrentItem() != null) {
								if (p.getOpenInventory().getTopInventory().getType() == InventoryType.FURNACE) {
									FurnaceInventory fi = (FurnaceInventory) p.getOpenInventory().getTopInventory();
									Furnace furnace = fi.getHolder();
									if (((event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
											&& (event.getClickedInventory().getType() != InventoryType.FURNACE))
											|| ((event.getSlotType() == SlotType.CRAFTING)
													&& ((event.getAction() == InventoryAction.PLACE_ALL)
															|| (event.getAction() == InventoryAction.PLACE_ONE)
															|| (event.getAction() == InventoryAction.PLACE_SOME)))) {
										ItemStack cursor = event.getCursor();
										ItemStack currentItem = event.getCurrentItem();
										if ((FurnaceManager.recipes.containsKey(cursor.getType()))
												|| (FurnaceManager.recipes.containsKey(currentItem.getType()))) {
											if (p.hasPermission("instantsmelt.allow")) {
												FurnaceManager.startSmelting(furnace);
											} else {
												FurnaceManager.stopSmelting(furnace);
												if (getConfig().getBoolean("permissionMessageUsed")) {
													p.sendMessage(ChatColor.translateAlternateColorCodes('&',
															getConfig().getString("permissionMessage")));
												}
											}
										}
									}
									if (((event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
											&& (event.getClickedInventory().getType() != InventoryType.FURNACE))
											|| ((event.getSlotType() == SlotType.FUEL)
													&& ((event.getAction() == InventoryAction.PLACE_ALL)
															|| (event.getAction() == InventoryAction.PLACE_ONE)
															|| (event.getAction() == InventoryAction.PLACE_SOME)))) {
										if ((event.getCursor().getType().isFuel())
												|| (event.getCurrentItem().getType().isFuel())) {
											if (p.hasPermission("instantsmelt.allow")) {
												FurnaceManager.startSmelting(furnace);
											} else {
												FurnaceManager.stopSmelting(furnace);
												if (getConfig().getBoolean("permissionMessageUsed")) {
													p.sendMessage(ChatColor.translateAlternateColorCodes('&',
															getConfig().getString("permissionMessage")));
												}
											}
										}
									} else if (((event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
											&& (event.getClickedInventory().getType() == InventoryType.FURNACE))
											|| ((event.getSlotType() == SlotType.RESULT)
													&& ((event.getAction() == InventoryAction.PICKUP_ALL)
															|| (event.getAction() == InventoryAction.PICKUP_HALF)
															|| (event.getAction() == InventoryAction.PICKUP_ONE)
															|| (event.getAction() == InventoryAction.PICKUP_SOME)))) {
										if (p.hasPermission("instantsmelt.allow")) {
											FurnaceManager.startSmelting(furnace);
										} else {
											FurnaceManager.stopSmelting(furnace);
											if (getConfig().getBoolean("permissionMessageUsed")) {
												p.sendMessage(ChatColor.translateAlternateColorCodes('&',
														getConfig().getString("permissionMessage")));
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
