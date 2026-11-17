package dev.celestiacraft.steam_letters.datagen.advancement;

import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import dev.celestiacraft.steam_letters.api.SlApis;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SlAdvancementBuilder {
	/**
	 * 原版自动生成的成就描述一律是 #DBA213 金色, 这里手动套用同一套样式.
	 */
	private static final int CREATE_DESCRIPTION_COLOR = 0xDBA213;

	private final ResourceLocation id;
	private final Map<String, CriterionTriggerInstance> criteria = new LinkedHashMap<>();
	private final Advancement.Builder builder = Advancement.Builder.advancement();

	@Nullable
	private ItemLike icon;
	@Nullable
	private ResourceLocation background;
	private Component title;
	private Component description;
	private FrameType type = FrameType.TASK;
	private boolean parentSet;
	private boolean toast = true;
	private boolean announce;
	private boolean hidden;

	private SlAdvancementBuilder(ResourceLocation id) {
		this.id = id;
		title = Component.translatable(SlAdvancement.titleKey(id.getPath()));
		description = Component.translatable(SlAdvancement.descKey(id.getPath()))
				.withStyle((style) -> {
					return style.withColor(CREATE_DESCRIPTION_COLOR);
				});
	}

	/**
	 * 以本模组的命名空间创建进度.
	 */
	public static SlAdvancementBuilder create(String id) {
		return new SlAdvancementBuilder(SlApis.loadResource(id));
	}

	public static SlAdvancementBuilder create(String namespace, String id) {
		return new SlAdvancementBuilder(ResourceLocation.fromNamespaceAndPath(namespace, id));
	}

	/**
	 * 父进度. 拿不到父模组成就对象时只能用 id 指定.
	 */
	public SlAdvancementBuilder parent(ResourceLocation parent) {
		builder.parent(parent);
		parentSet = true;
		return this;
	}

	public SlAdvancementBuilder parent(String parent) {
		return parent(SlApis.loadResource(parent));
	}

	public SlAdvancementBuilder parent(Advancement parent) {
		builder.parent(parent);
		parentSet = true;
		return this;
	}

	/**
	 * 图标. 只决定进度框里的图案, 不参与判定.
	 */
	public SlAdvancementBuilder icon(ItemLike icon) {
		this.icon = icon;
		return this;
	}

	public SlAdvancementBuilder title(Component title) {
		this.title = title;
		return this;
	}

	public SlAdvancementBuilder title(String translationKey) {
		return title(Component.translatable(translationKey));
	}

	public SlAdvancementBuilder description(Component description) {
		this.description = description;
		return this;
	}

	/**
	 * 按语言键设置描述, 并套用原版成就的金色样式.
	 */
	public SlAdvancementBuilder description(String translationKey) {
		return description(Component.translatable(translationKey)
				.withStyle((style) -> {
					return style.withColor(CREATE_DESCRIPTION_COLOR);
				}));
	}

	/**
	 * 背景图, 只有根进度用得到.
	 */
	public SlAdvancementBuilder background(ResourceLocation background) {
		this.background = background;
		return this;
	}

	public SlAdvancementBuilder frame(FrameType type) {
		this.type = type;
		return this;
	}

	public SlAdvancementBuilder toast(boolean toast) {
		this.toast = toast;
		return this;
	}

	public SlAdvancementBuilder announce(boolean announce) {
		this.announce = announce;
		return this;
	}

	/**
	 * 隐藏进度. 只能靠父进度解锁.
	 */
	public SlAdvancementBuilder hidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	/**
	 * 判定条件为持有指定标签的物品, 标签内容由数据包提供.
	 */
	public SlAdvancementBuilder requires(TagKey<Item> tag) {
		return criterion(InventoryChangeTrigger.TriggerInstance.hasItems(
				ItemPredicate.Builder.item()
						.of(tag)
						.build()
		));
	}

	/**
	 * 判定条件为持有指定物品, 只接受具体物品.
	 */
	public SlAdvancementBuilder requires(ItemLike... items) {
		return criterion(InventoryChangeTrigger.TriggerInstance.hasItems(items));
	}

	/**
	 * 自动按插入顺序编号并登记判定条件.
	 */
	public SlAdvancementBuilder criterion(CriterionTriggerInstance criterion) {
		return criterion(String.valueOf(criteria.size()), criterion);
	}

	public SlAdvancementBuilder criterion(String name, CriterionTriggerInstance criterion) {
		criteria.put(name, criterion);
		return this;
	}

	/**
	 * 生成进度 JSON 并交给 datagen 输出.
	 */
	public Advancement save(RegistrateAdvancementProvider provider) {
		if (!parentSet) {
			throw new IllegalStateException("Advancement " + id + " is missing a parent");
		}
		if (icon == null) {
			throw new IllegalStateException("Advancement " + id + " is missing an icon");
		}
		if (criteria.isEmpty()) {
			throw new IllegalStateException("Advancement " + id + " is missing a criterion");
		}

		builder.display(icon, title, description, background, type, toast, announce, hidden);
		criteria.forEach(builder::addCriterion);

		return builder.save(provider, id.toString());
	}
}