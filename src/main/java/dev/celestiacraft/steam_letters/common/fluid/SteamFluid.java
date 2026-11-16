package dev.celestiacraft.steam_letters.common.fluid;

import com.tterrag.registrate.builders.FluidBuilder;
import dev.celestiacraft.steam_letters.tags.SlFluidTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.ModList;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class SteamFluid extends FluidType {
	private Vector3f fogColor;
	private Supplier<Float> fogDistance;
	private final ResourceLocation stillTexture;
	private final ResourceLocation flowingTexture;
	private final int tintColor;
	private Object clientExtensions;

	public SteamFluid(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture, int tintColor) {
		super(properties.density(-10)
				.viscosity(1)
				.canPushEntity(false)
				.canConvertToSource(false)
				.canDrown(true));
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
		this.tintColor = tintColor;
	}

	public static FluidBuilder.FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance, int tintColor) {
		return (properties, still, flowing) -> {
			SteamFluid fluidType = new SteamFluid(properties, still, flowing, tintColor);
			fluidType.fogColor = new Vector3f(
					((fogColor >> 16) & 0xFF) / 255.0f,
					((fogColor >> 8) & 0xFF) / 255.0f,
					(fogColor & 0xFF) / 255.0f
			);
			fluidType.fogDistance = fogDistance;
			return fluidType;
		};
	}

	@Override
	public int getTemperature(FluidStack stack) {
		Fluid fluid = stack.getFluid();

		if (!ModList.get().isLoaded("gtceu")) {
			return 114514;
		}

		if (fluid.is(SlFluidTags.STEAM)) {
			return 373;
		}

		if (fluid.is(SlFluidTags.SUPERHEATED_STEAM)) {
			return 573;
		}

		return super.getTemperature(stack);
	}

	@Override
	public Object getRenderPropertiesInternal() {
		if (clientExtensions == null && stillTexture != null) {
			clientExtensions = new SteamFluidClient(stillTexture, flowingTexture, tintColor, fogColor, fogDistance);
		}
		return clientExtensions;
	}
}