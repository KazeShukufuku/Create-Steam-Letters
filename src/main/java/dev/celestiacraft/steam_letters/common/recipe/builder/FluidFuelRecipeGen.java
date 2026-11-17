package dev.celestiacraft.steam_letters.common.recipe.builder;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import dev.celestiacraft.steam_letters.common.register.SlRecipeTypes;
import dev.celestiacraft.steam_letters.common.recipe.type.FluidFuelRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public abstract class FluidFuelRecipeGen extends ProcessingRecipeGen {
	protected FluidFuelRecipeGen(PackOutput output, String defaultNamespace) {
		super(output, defaultNamespace);
	}

	@Override
	protected @NotNull IRecipeTypeInfo getRecipeType() {
		return SlRecipeTypes.FLUID_FUEL;
	}

	protected GeneratedRecipe recipe(String name, @NotNull UnaryOperator<FluidFuelBuilder> transform) {
		return create(name, (ProcessingRecipeBuilder<FluidFuelRecipe> builder) -> {
			return transform.apply(new FluidFuelBuilder(builder)).unwrap();
		});
	}

	public static class FluidFuelBuilder {
		private final ProcessingRecipeBuilder<FluidFuelRecipe> builder;
		private boolean superheated;

		private FluidFuelBuilder(@NotNull ProcessingRecipeBuilder<FluidFuelRecipe> builder) {
			this.builder = builder;
		}

		public FluidFuelBuilder fluid(Fluid fluid) {
			return ingredient(FluidIngredient.fromFluid(fluid, FluidFuelRecipe.FUEL_AMOUNT));
		}

		public FluidFuelBuilder fluid(ResourceLocation fluid) {
			return fluid(BuiltInRegistries.FLUID.get(fluid));
		}

		public FluidFuelBuilder fluid(String fluid) {
			return fluid(parse(fluid));
		}

		public FluidFuelBuilder fluidTag(TagKey<Fluid> tag) {
			return ingredient(FluidIngredient.fromTag(tag, FluidFuelRecipe.FUEL_AMOUNT));
		}

		public FluidFuelBuilder fluidTag(ResourceLocation tag) {
			return fluidTag(FluidTags.create(tag));
		}

		public FluidFuelBuilder fluidTag(String tag) {
			return fluidTag(parse(tag));
		}

		private FluidFuelBuilder ingredient(@NotNull FluidIngredient ingredient) {
			builder.withFluidIngredients(ingredient);
			return this;
		}

		public FluidFuelBuilder time(int ticks) {
			builder.duration(ticks);
			return this;
		}

		public FluidFuelBuilder timeSeconds(int seconds) {
			return time(20 * seconds);
		}

		public FluidFuelBuilder superheated(boolean superheated) {
			this.superheated = superheated;
			return this;
		}

		public FluidFuelBuilder superheated() {
			superheated = true;
			return this;
		}

		public FluidFuelBuilder whenModLoaded(@NotNull String modid) {
			builder.whenModLoaded(modid);
			return this;
		}

		public FluidFuelBuilder whenModMissing(@NotNull String modid) {
			builder.whenModMissing(modid);
			return this;
		}

		public FluidFuelBuilder withCondition(@NotNull ICondition condition) {
			builder.withCondition(condition);
			return this;
		}

		private ProcessingRecipeBuilder<FluidFuelRecipe> unwrap() {
			FluidFuelRecipe.markNextSuperheated(superheated);
			return builder;
		}

		private static ResourceLocation parse(String location) {
			ResourceLocation parsed = ResourceLocation.tryParse(location);

			if (parsed == null) {
				throw new IllegalArgumentException("Invalid resource location: " + location);
			}

			return parsed;
		}
	}
}