package dev.celestiacraft.steam_letters.common.recipe.conditions;

import com.google.gson.JsonObject;
import dev.celestiacraft.steam_letters.api.SlApis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class HasItemCondition implements ICondition {
	public static final ResourceLocation ID = SlApis.loadResource("has_item");

	private final @Nullable ResourceLocation item;
	private final @Nullable TagKey<Item> tag;

	private HasItemCondition(@Nullable ResourceLocation item, @Nullable TagKey<Item> tag) {
		if ((item == null) == (tag == null)) {
			throw new IllegalArgumentException("`has_item` requires exactly one of `item` or `tag` to be specified.");
		}

		this.item = item;
		this.tag = tag;
	}

	public static HasItemCondition ofItem(Item item) {
		return new HasItemCondition(ForgeRegistries.ITEMS.getKey(item), null);
	}

	public static HasItemCondition ofItem(ResourceLocation item) {
		return new HasItemCondition(item, null);
	}

	public static HasItemCondition ofItem(String item) {
		return new HasItemCondition(parse(item), null);
	}

	public static HasItemCondition ofTag(TagKey<Item> tag) {
		return new HasItemCondition(null, tag);
	}

	public static HasItemCondition ofTag(ResourceLocation tag) {
		return new HasItemCondition(null, ItemTags.create(tag));
	}

	public static HasItemCondition ofTag(String tag) {
		return ofTag(parse(tag));
	}

	public @Nullable ResourceLocation item() {
		return item;
	}

	public @Nullable TagKey<Item> tag() {
		return tag;
	}

	@Override
	public ResourceLocation getID() {
		return ID;
	}

	@Override
	public boolean test(IContext context) {
		if (item != null) {
			return ForgeRegistries.ITEMS.containsKey(item);
		}

		return !context.getTag(tag).isEmpty();
	}

	@Override
	public String toString() {
		return String.format("has_item(\"%s\")", Objects.requireNonNullElseGet(item, () -> {
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

	public static class Serializer implements IConditionSerializer<HasItemCondition> {
		@Override
		public void write(JsonObject json, HasItemCondition value) {
			if (value.item != null) {
				json.addProperty("item", value.item.toString());
				return;
			}

			json.addProperty("tag", value.tag.location().toString());
		}

		@Override
		public HasItemCondition read(JsonObject json) {
			if (json.has("item")) {
				return HasItemCondition.ofItem(json.get("item").getAsString());
			}

			return HasItemCondition.ofTag(json.get("tag").getAsString());
		}

		@Override
		public ResourceLocation getID() {
			return ID;
		}
	}
}