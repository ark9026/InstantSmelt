package me.ark9026.instantsmelt;

import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class SmeltingFurnace {

	private Furnace _furnace;
	private boolean _smelting;

	public SmeltingFurnace(Furnace furnace) {
		this._furnace = furnace;
	}

	public void smelt() {
		if (_smelting == true)
			return;
		_smelting = true;
		new BukkitRunnable() {
			public void run() {
				while (_smelting) {
					FurnaceInventory fi = _furnace.getInventory();
					if (fi.getSmelting() == null) {
						_smelting = false;
						break;
					}
					if (fi.getSmelting().getType() == Material.AIR) {
						_smelting = false;
						break;
					}
					if ((fi.getFuel() == null) && (InstantSmelt.getInstance().getBurnTime().get(_furnace) == 0)) {
						_smelting = false;
						break;
					}
					ItemStack smelting = fi.getSmelting();
					Material desiredResult = FurnaceManager.recipes.get(smelting.getType());
					if (desiredResult == null) {
						_smelting = false;
						break;
					}
					if (fi.getResult() != null) {
						ItemStack result = fi.getResult();
						if (desiredResult != result.getType()) {
							_smelting = false;
							break;
						}
						if (result.getAmount() == 64) {
							_smelting = false;
							break;
						}
						result.setAmount((result.getAmount() + 1));
					} else {
						fi.setResult(new ItemStack(desiredResult, 1));
					}
					if (smelting.getAmount() == 1) {
						fi.setSmelting(new ItemStack(Material.AIR));
						_smelting = false;
						break;
					} else {
						smelting.setAmount((smelting.getAmount() - 1));
					}
				}
			}
		}.runTaskLater(InstantSmelt.getInstance(), 21L);

	}

	public void stopSmelt() {
		_smelting = false;
	}
}
