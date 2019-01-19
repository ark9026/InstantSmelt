package me.ark9026.instantfurnace.versions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.server.v1_12_R1.TileEntityFurnace;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftFurnace;

public class InstantSmelt_1_12_1_R1 implements InstantSmelt {

	@Override
	public void setInstant(Furnace furnace) {
		CraftFurnace craft = (CraftFurnace) furnace;
		// ((CraftFurnace) furnace).getTileEntity().setProperty(3, 2);
		Method m = null;
		try {
			m = CraftBlockEntityState.class.getDeclaredMethod("getTileEntity");
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		m.setAccessible(true);
		TileEntityFurnace tef = null;
		try {
			tef = (TileEntityFurnace) m.invoke(craft);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		tef.setProperty(3, 2);
		m.setAccessible(false);
	}

}
