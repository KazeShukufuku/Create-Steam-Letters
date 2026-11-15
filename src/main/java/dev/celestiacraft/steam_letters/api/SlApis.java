package dev.celestiacraft.steam_letters.api;

import com.jesz.createdieselgenerators.CreateDieselGenerators;
import com.simibubi.create.Create;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.client.lang.ChineseLangProvider;
import dev.celestiacraft.steam_letters.api.client.lang.EnglishLangProvider;
import dev.celestiacraft.steam_letters.api.register.SlRegistrate;
import dev.celestiacraft.steam_letters.common.recipe.type.FluidFuelRecipe;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Objects;

public class SlApis {
	public static final SlRegistrate REGISTRATE = SlRegistrate.create(CreateLettersSteam.MODID)
			.setTooltipModifierFactory((item) -> {
				return new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
						.andThen(TooltipModifier.mapNull(KineticStats.create(item)));
			});

	static {
		// Registrate 在构造 RegistrateDataProvider 时快照 ProviderType 静态表,
		// 晚于此刻注册的自定义类型不会被实例化, 故提前触发类初始化。
		Objects.requireNonNull(ChineseLangProvider.TYPE);
		Objects.requireNonNull(EnglishLangProvider.TYPE);
	}

	@Info("1000mB")
	public static int getFuelAmount() {
		return FluidFuelRecipe.FUEL_AMOUNT;
	}

	public static ResourceLocation loadResource(String path) {
		return ResourceLocation.fromNamespaceAndPath(CreateLettersSteam.MODID, path);
	}

	@HideFromJS
	public static ResourceLocation loadCreate(String path) {
		return Create.asResource(path);
	}

	@HideFromJS
	public static ResourceLocation loadCDG(String path) {
		return CreateDieselGenerators.rl(path);
	}

	@HideFromJS
	public static ResourceLocation loadForge(String path) {
		return ResourceLocation.fromNamespaceAndPath("forge", path);
	}

	@HideFromJS
	public static void require(ProcessingRecipeBuilder<?> builder, TagKey<Item> tag, int count) {
		for (int i = 0; i < count; i++) {
			builder.require(tag);
		}
	}
}