package tf2.util;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;
import tf2.Advancements.AdvancementTF;

public class TFAdvancements
{
	//public static final TriggerAnvil TRIGGER_ANVIL = CriteriaTriggers.register(new TriggerAnvil());
	public static final AdvancementTF SUMMON_FRIENDMECHA = CriteriaTriggers.register(new AdvancementTF(new ResourceLocation(Reference.MOD_ID, "summon_friend_mecha")));
	public static final AdvancementTF SUMMON_GYNOID = CriteriaTriggers.register(new AdvancementTF(new ResourceLocation(Reference.MOD_ID, "summon_gynoid")));
	public static final AdvancementTF SUMMON_RIDEMECHA = CriteriaTriggers.register(new AdvancementTF(new ResourceLocation(Reference.MOD_ID, "summon_ride_mecha")));

	public static void init() {}

}