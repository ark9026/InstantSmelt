package me.ark9026.instantsmelt.versions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_13_R2.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_13_R2.block.CraftFurnace;

import net.minecraft.server.v1_13_R2.TileEntityFurnace;

public class InstantSmelt_1_13_R2 implements NMSBurnTime {

	@Override
	public int get(Furnace furnace) {
		CraftFurnace craft = (CraftFurnace) furnace;
		Method m = null;
		try {
			m = CraftBlockEntityState.class.getDeclaredMethod("getTileEntity");
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return 0;
		}
		m.setAccessible(true);
		TileEntityFurnace tef = null;
		try {
			tef = (TileEntityFurnace) m.invoke(craft);
			return tef.getProperty(0);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return 0;
		} finally {
			m.setAccessible(false);
		}
	}
}
