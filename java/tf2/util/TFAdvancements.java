package tf2.util;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;
import tf2.Advancements.AdvancementTF;

public class TFAdvancements
{
	public static final AdvancementTF MISSION_01 = CriteriaTriggers.register(new AdvancementTF(new ResourceLocation(Reference.MOD_ID, "event_mission_01")));
	public static void init() {}

}