package tf2.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tf2.TFItems;
import tf2.TFSoundEvents;
import tf2.entity.EntityItemSpawnFriendMecha;
import tf2.entity.mob.frend.EntityFriendMecha;
import tf2.potion.TFPotionPlus;

public class TFMechaSkillEvent
{
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent event)
	{
		if(event.getWorld().isRemote || event.isCanceled() || event.getEntity().isDead)
		{
			return;
		}

		if(event.getEntity().getClass() == EntityItem.class)
		{
			EntityItem item = (EntityItem)event.getEntity();
			if(!item.getItem().isEmpty() && item.getItem().getItem() == TFItems.SPAWNFM)
			{
				item.setDead();

				EntityItemSpawnFriendMecha core = new EntityItemSpawnFriendMecha(item.world, item.posX, item.posY, item.posZ, item.getItem());
				core.motionX = item.motionX;
				core.motionY = item.motionY;
				core.motionZ = item.motionZ;
				core.setPickupDelay(30);
				core.setNoDespawn();
				item.world.spawnEntity(core);
			}
		}
	}

	@SubscribeEvent
	public void mechaHurtEvent(LivingHurtEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		DamageSource damage = event.getSource();
		float amount = event.getAmount();

		if (entity instanceof EntityFriendMecha)
		{
			EntityFriendMecha mecha = (EntityFriendMecha) entity;

			if (mecha != null)
			{
				if (mecha.getInventoryMechaEquipment().getHasSkill(TFItems.SKILL_ADDITIONALARMOR_1))
				{
					amount *= 0.9F;
					event.setAmount(amount);
				}
				if (mecha.getInventoryMechaEquipment().getHasSkill(TFItems.SKILL_ADDITIONALARMOR_2))
				{
					amount *= 0.8F;
					event.setAmount(amount);
				}
				if (mecha.getInventoryMechaEquipment().getHasSkill(TFItems.SKILL_ADDITIONALARMOR_3))
				{
					amount *= 0.7F;
					event.setAmount(amount);
				}
			}
		}
	}

	@SubscribeEvent
	public void mechaLivingEvent(LivingUpdateEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		if (entity != null && entity instanceof EntityFriendMecha)
		{
			EntityFriendMecha mecha = (EntityFriendMecha) entity;

			if (mecha != null)
			{
				if (mecha.getInventoryMechaEquipment().getHasSkill(TFItems.SKILL_AUTOREPAIR))
				{
					if(mecha.ticksExisted % 300 == 0 && !mecha.world.isRemote)
					{
						mecha.heal(2F);
					}
				}

				if (mecha.getInventoryMechaEquipment().getHasSkill(TFItems.SKILL_HARDSTRIKE))
				{
					if(mecha.ticksExisted % 300 == 0 && !mecha.world.isRemote)
					{
						mecha.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 300, 0, true, false));

						EntityPlayer player = (EntityPlayer)mecha.getControllingPassenger();
						if(player != null && !player.world.isRemote)
						{
							player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 300, 0, true, false));
						}
					}
				}
				if (mecha.getInventoryMechaEquipment().getHasSkill(TFItems.SKILL_FIREFILLING))
				{
					if(mecha.ticksExisted % 300 == 0 && !mecha.world.isRemote)
					{
						mecha.addPotionEffect(new PotionEffect(TFPotionPlus.SHOOTING, 300, 0, true, false));

						EntityPlayer player = (EntityPlayer)mecha.getControllingPassenger();
						if(player != null && !player.world.isRemote)
						{
							player.addPotionEffect(new PotionEffect(TFPotionPlus.SHOOTING, 300, 0, true, false));
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void mechaHealEvent(LivingHealEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		float amount = event.getAmount();

		if (entity instanceof EntityFriendMecha)
		{
			EntityFriendMecha mecha = (EntityFriendMecha) entity;

			if (mecha != null)
			{
				if (mecha.getInventoryMechaEquipment().getHasSkill(TFItems.SKILL_REPAIRDOUBLING))
				{
					amount *= 2.0F;
					event.setAmount(amount);
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityFriendMecha)
		{
			EntityFriendMecha mecha = (EntityFriendMecha) entity;
			if (mecha != null)
			{
				if (mecha.getInventoryMechaEquipment().getHasSkill(TFItems.SKILL_RESURRECTION))
				{
	    			if(mecha.getInventoryMechaEquipment().getSkillAItem().getItem() == TFItems.SKILL_RESURRECTION)
	    			{
	    				mecha.getInventoryMechaEquipment().setSkillAItem(ItemStack.EMPTY);
	    			}
	    			else if(mecha.getInventoryMechaEquipment().getSkillBItem().getItem() == TFItems.SKILL_RESURRECTION)
	    			{
	    				mecha.getInventoryMechaEquipment().setSkillBItem(ItemStack.EMPTY);
	    			}
	    			else if(mecha.getInventoryMechaEquipment().getSkillCItem().getItem() == TFItems.SKILL_RESURRECTION)
	    			{
	    				mecha.getInventoryMechaEquipment().setSkillCItem(ItemStack.EMPTY);
	    			}
	    			mecha.playSound(TFSoundEvents.DECISION, 0.7F, 1.0F / (mecha.getRNG().nextFloat() * 0.4F + 0.8F));
	    			mecha.setHealth(mecha.getMaxHealth());
					event.setCanceled(true);
				}
			}
		}
	}
}
