package dev.celestiacraft.steam_letters.datagen.recipe.type;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.common.recipe.builder.FluidFuelRecipeGen;
import dev.celestiacraft.steam_letters.common.recipe.conditions.HasFluidCondition;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.FluidTags;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.fluids.TinkerFluids;

public class SlFluidFuelRecipeGen extends FluidFuelRecipeGen {
	public SlFluidFuelRecipeGen(PackOutput output) {
		super(output, CreateLettersSteam.MODID);

		// 熔岩
		recipe("lava", (builder) -> {
			return builder.fluidTag(FluidTags.LAVA)
					.timeSeconds(30);
		});

		// 烈焰血
		recipe("compat/blazing_blood", (builder) -> {
			return builder.fluid(TinkerFluids.blazingBlood.get())
					.timeSeconds(60)
					.superheated()
					.whenModLoaded(TConstruct.MOD_ID);
		});

		// 油
		recipe("compat/oil", (builder) -> {
			return builder.fluidTag("forge:oil")
					.timeSeconds(45)
					.withCondition(HasFluidCondition.ofTag("forge:oil"));
		});

		// 原油 我就不信有哪个Mod捣乱俩Tag都加:(
		recipe("compat/crude_oil", (builder) -> {
			return builder.fluidTag("forge:crude_oil")
					.timeSeconds(45)
					.withCondition(HasFluidCondition.ofTag("forge:crude_oil"));
		});

		// 杂酚油
		recipe("compat/creosote", (builder) -> {
			return builder.fluidTag("forge:creosote")
					.timeSeconds(40)
					.withCondition(HasFluidCondition.ofTag("forge:creosote"));
		});

		// 生物柴油
		recipe("compat/biodiesel", (builder) -> {
			return builder.fluidTag("forge:biodiesel")
					.time(20 * (60 + (40)))
					.superheated()
					.withCondition(HasFluidCondition.ofTag("forge:biodiesel"));
		});

		// 生物乙醇
		recipe("compat/bioethanol", (builder) -> {
			return builder.fluidTag("forge:bioethanol")
					.timeSeconds(60 * 2)
					.superheated()
					.withCondition(HasFluidCondition.ofTag("forge:bioethanol"));
		});

		// 柴油
		recipe("compat/diesel", (builder) -> {
			return builder.fluidTag("forge:diesel")
					.timeSeconds(20 + (60 * 3))
					.superheated()
					.withCondition(HasFluidCondition.ofTag("forge:diesel"));
		});

		// 汽油
		recipe("compat/gasoline", (builder) -> {
			return builder.fluidTag("forge:gasoline")
					.timeSeconds(20 + (60 * 3))
					.superheated()
					.withCondition(HasFluidCondition.ofTag("forge:gasoline"));
		});

		// 种子油
		recipe("compat/plantoil", (builder) -> {
			return builder.fluidTag("forge:plantoil")
					.timeSeconds(20 + 5)
					.withCondition(HasFluidCondition.ofTag("forge:plantoil"));
		});

		// 乙醇
		recipe("compat/ethanol", (builder) -> {
			return builder.fluidTag("forge:ethanol")
					.timeSeconds(20 * 10)
					.withCondition(HasFluidCondition.ofTag("forge:ethanol"));
		});
	}
}