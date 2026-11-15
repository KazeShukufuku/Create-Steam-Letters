package dev.celestiacraft.steam_letters.api.client.lang;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.LogicalSide;

import java.util.function.Supplier;

public class ChineseLangProvider extends LanguageProvider implements RegistrateProvider {
	public static final ProviderType<ChineseLangProvider> TYPE;

	private final AbstractRegistrate<?> owner;

	public ChineseLangProvider(AbstractRegistrate<?> owner, PackOutput output) {
		super(output, owner.getModid(), "zh_cn");
		this.owner = owner;
	}

	static {
		TYPE = ProviderType.register("lang_zh_cn", (parent, event) -> {
			return new ChineseLangProvider(parent, event.getGenerator().getPackOutput());
		});
	}

	@Override
	public LogicalSide getSide() {
		return LogicalSide.CLIENT;
	}

	@Override
	protected void addTranslations() {
		owner.genData(TYPE, this);
	}

	public void addFluid(Fluid fluid, String name) {
		add(fluid.getFluidType().getDescriptionId(), name);
	}

	public void addFluid(Supplier<Fluid> fluid, String name) {
		addFluid(fluid.get(), name);
	}
}