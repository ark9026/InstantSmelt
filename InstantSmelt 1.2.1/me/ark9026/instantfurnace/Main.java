package me.ark9026.instantfurnace;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.ark9026.instantfurnace.versions.InstantSmelt;
import me.ark9026.instantfurnace.versions.InstantSmeltBelow1_12_1;
import me.ark9026.instantfurnace.versions.InstantSmelt_1_12_1_R1;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	private InstantSmelt instantsmelt;

	@Override
	public void onEnable() {
		setupVersions();
		getServer().getPluginManager().registerEvents(this, this);
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			getLogger().info("Generating new config for InstantSmelt...");
			saveDefaultConfig();
			getConfig().options().copyDefaults(true);
		} else {
			getLogger().info("Loading config for InstantSmelt...");
		}
		getLogger()
				.info("InstantFurnace has successfully loaded! This plugin is coded by ark9026!");
	}

	@Override
	public void onDisable() {
		getLogger()
				.info("InstantFurnace has successfully closed! This plugin was coded by ark9026!");
	}

	private boolean setupVersions() {
		String version;
		try {
			version = Bukkit.getServer().getClass().getPackage().getName()
					.replace(".", ",").split(",")[3];

		} catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
			return false;
		}
		getLogger().info("Your server is running version " + version);
		if (version.equals("v1_12_R1")) {
			instantsmelt = new InstantSmelt_1_12_1_R1();

		} else {
			instantsmelt = new InstantSmeltBelow1_12_1();
		}
		return instantsmelt != null;
	}

	@EventHandler
	public void furnaceBurn(FurnaceBurnEvent event) {
		Furnace furnace = (Furnace) event.getBlock().getState();
		Location l = furnace.getLocation();
		for (Entity e : getNearbyEntities(l, 5)) {
			if (e instanceof Player) {
				Player p = (Player) e;
				if (p.getOpenInventory().getType()
						.equals(InventoryType.FURNACE)) {
					if (p.hasPermission("instantsmelt.allow")) {
						instantsmelt.setInstant(furnace);
					} else {
						if (getConfig().getBoolean("permissionMessageUsed")) {
							p.sendMessage(ChatColor
									.translateAlternateColorCodes(
											'&',
											getConfig().getString(
													"permissionMessage")));
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void furnaceSmeltEvent(FurnaceSmeltEvent event) {
		Furnace furnace = (Furnace) event.getBlock().getState();
		Location l = furnace.getLocation();
		for (Entity e : getNearbyEntities(l, 5)) {
			if (e instanceof Player) {
				Player p = (Player) e;
				if (p.getOpenInventory().getType()
						.equals(InventoryType.FURNACE)) {
					if (p.hasPermission("instantsmelt.allow")) {
						instantsmelt.setInstant(furnace);
					} else {
						if (getConfig().getBoolean("permissionMessageUsed")) {
							p.sendMessage(ChatColor
									.translateAlternateColorCodes(
											'&',
											getConfig().getString(
													"permissionMessage")));
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Block blocktype = event.getWhoClicked().getTargetBlock(
				(Set<Material>) null, 10);
		if (blocktype.getType() == Material.FURNACE
				|| blocktype.getType() == Material.BURNING_FURNACE) {
			// if ((event.getSlot() == 0 || event.getSlot() == 1)
			// && event.getCursor().getType() != Material.AIR) {
			Furnace furnace = (Furnace) blocktype.getState();
			if (player.hasPermission("instantsmelt.allow")) {
				instantsmelt.setInstant(furnace);
			} else {
				if (getConfig().getBoolean("permissionMessageUsed")) {
					player.sendMessage(ChatColor
							.translateAlternateColorCodes(
									'&',
									getConfig().getString(
											"permissionMessage")));
				}
			}
			// }
		}
	}

	private List<Entity> getNearbyEntities(Location l, int size) {
		List<Entity> entities = new ArrayList<Entity>();
		for (Entity e : l.getWorld().getEntities()) {
			if (l.distance(e.getLocation()) <= size)
				entities.add(e);
		}
		return entities;
	}
}
