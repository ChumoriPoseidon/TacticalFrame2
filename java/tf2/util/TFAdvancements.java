package tf2.util;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;

public class TFAdvancements
{
	public static final TFAdvancementTrigger MISSION_01 = CriteriaTriggers.register(new TFAdvancementTrigger(new ResourceLocation(Reference.MOD_ID, "event_mission_01")));
	public static void init() {}
}