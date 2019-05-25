package tf2.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tf2.items.guns.ItemTFGuns;
import tf2.items.guns.ItemTFGunsHG;
import tf2.items.guns.ItemTFGunsLMG;
import tf2.items.guns.ItemTFGunsSMG;
import tf2.items.guns.ItemTFGunsSR;
import tf2.potion.TFPotionPlus;

public class TFLivingUpdateEvent
{
	@SubscribeEvent
	public void onLivingMob(LivingUpdateEvent event)
	{
		EntityLivingBase target = event.getEntityLiving();
		if (target != null && target instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) target;
			ItemStack itemstack = ((EntityPlayer) (player)).getHeldItemMainhand();

			if (itemstack != null && itemstack.getItem() instanceof ItemTFGuns)
			{
				double x = player.motionX;
				double z = player.motionZ;
				double d0 = MathHelper.sqrt((float) (x * x + z * z));

				if (player.isPotionActive(TFPotionPlus.MOVE_SHOOTING))
				{
					PotionEffect potion = player.getActivePotionEffect(TFPotionPlus.MOVE_SHOOTING);
					int i = potion.getAmplifier();

					if (player.isHandActive() && !player.isSneaking())
					{
						if (itemstack.getItem() instanceof ItemTFGunsSMG ||
								itemstack.getItem() instanceof ItemTFGunsHG )
						{
							if (d0 <= 0.14F + (i * 0.01F))
							{
								target.motionX *= 1.65D + (i * 0.01F);
								target.motionZ *= 1.65D + (i * 0.01F);
							}
						}
						else if (itemstack.getItem() instanceof ItemTFGunsLMG ||
								itemstack.getItem() instanceof ItemTFGunsSR)
						{
							if (d0 <= 0.13F + (i * 0.01F))
							{
								target.motionX *= 1.58D + (i * 0.01F);
								target.motionZ *= 1.58D + (i * 0.01F);
							}
						}
						else
						{
							if (d0 <= 0.13F + (i * 0.01F))
							{
								target.motionX *= 1.61D + (i * 0.01F);
								target.motionZ *= 1.61D + (i * 0.01F);
							}
						}

					}
				}
			}
		}
	}
}
