package dev.celestiacraft.steam_letters.common.register;

import dev.celestiacraft.steam_letters.common.recipe.conditions.HasFluidCondition;
import dev.celestiacraft.steam_letters.common.recipe.conditions.HasItemCondition;
import net.minecraftforge.common.crafting.CraftingHelper;

public class SlConditionTypes {
	public static void init() {
		CraftingHelper.register(new HasItemCondition.Serializer());
		CraftingHelper.register(new HasFluidCondition.Serializer());
	}
}