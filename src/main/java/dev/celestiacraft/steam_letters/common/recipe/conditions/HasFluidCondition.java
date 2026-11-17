package dev.celestiacraft.steam_letters.common.recipe.conditions;

import com.google.gson.JsonObject;
import dev.celestiacraft.steam_letters.api.SlApis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class HasFluidCondition implements ICondition {
	public static final ResourceLocation ID = SlApis.loadResource("has_fluid");

	private final @Nullable ResourceLocation fluid;
	private final @Nullable TagKey<Fluid> tag;

	private HasFluidCondition(@Nullable ResourceLocation fluid, @Nullable TagKey<Fluid> tag) {
		if ((fluid == null) == (tag == null)) {
			throw new IllegalArgumentException("`has_fluid` requires exactly one of `fluid` or `tag` to be specified.");
		}

		this.fluid = fluid;
		this.tag = tag;
	}

	public static HasFluidCondition ofFluid(Fluid fluid) {
		return new HasFluidCondition(ForgeRegistries.FLUIDS.getKey(fluid), null);
	}

	public static HasFluidCondition ofFluid(ResourceLocation fluid) {
		return new HasFluidCondition(fluid, null);
	}

	public static HasFluidCondition ofFluid(String fluid) {
		return new HasFluidCondition(parse(fluid), null);
	}

	public static HasFluidCondition ofTag(TagKey<Fluid> tag) {
		return new HasFluidCondition(null, tag);
	}

	public static HasFluidCondition ofTag(ResourceLocation tag) {
		return new HasFluidCondition(null, FluidTags.create(tag));
	}

	public static HasFluidCondition ofTag(String tag) {
		return ofTag(parse(tag));
	}

	public @Nullable ResourceLocation fluid() {
		return fluid;
	}

	public @Nullable TagKey<Fluid> tag() {
		return tag;
	}

	@Override
	public ResourceLocation getID() {
		return ID;
	}

	@Override
	public boolean test(IContext context) {
		if (fluid != null) {
			return ForgeRegistries.FLUIDS.containsKey(fluid);
		}

		return !context.getTag(tag).isEmpty();
	}

	@Override
	public String toString() {
		return String.format("has_fluid(\"%s\")", Objects.requireNonNullElseGet(fluid, () -> {
			return tag.location();
		}));
	}

	private static ResourceLocation parse(String location) {
		ResourceLocation parsed = ResourceLocation.tryParse(location);

		if (parsed == null) {
			throw new IllegalArgumentException("Invalid resource location: " + location);
		}

		return parsed;
	}

	public static class Serializer implements IConditionSerializer<HasFluidCondition> {
		@Override
		public void write(JsonObject json, HasFluidCondition value) {
			if (value.fluid != null) {
				json.addProperty("fluid", value.fluid.toString());
				return;
			}

			json.addProperty("tag", value.tag.location().toString());
		}

		@Override
		public HasFluidCondition read(JsonObject json) {
			if (json.has("fluid")) {
				return HasFluidCondition.ofFluid(json.get("fluid").getAsString());
			}

			return HasFluidCondition.ofTag(json.get("tag").getAsString());
		}

		@Override
		public ResourceLocation getID() {
			return ID;
		}
	}
}