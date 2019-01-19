package me.ark9026.instantfurnace.versions;

import org.bukkit.block.Furnace;

public class InstantSmeltBelow1_12_1 implements InstantSmelt {

	@Override
	public void setInstant(Furnace furnace) {
		furnace.setCookTime((short) 199);
	}

}
