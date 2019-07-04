package tf2.entity.mob.frend;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import tf2.TF2Core;
import tf2.common.MessageKeyPressed;
import tf2.common.PacketHandler;

public abstract class EntityVehicle extends EntityFriendMecha
{
	public boolean serverLeftclick;
	public boolean serverRightclick;
	public boolean serverBoost;
	public boolean serverGetoff;
	public boolean serverShift;

	public EntityVehicle(World worldIn, byte maxSlot, byte maxLevel, double defaultDamage, double upDamage,
			double defaultArmor, double upArmor, double defaultArmorToughness, double upArmorToughness,
			double defaultMaxHealth, double upMaxHealth, boolean canRide)
	{
		super(worldIn, (byte)0, (byte)0, defaultDamage, upDamage, defaultArmor, upArmor, defaultArmorToughness,
				upArmorToughness, defaultMaxHealth, upMaxHealth, true);
	}

	@Override
	protected boolean canDespawn()
	{
		return false;
	}

	@Override
	public void setInWeb()
	{}

	@Override
	public boolean shouldDismountInWater(Entity rider)
	{
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (source.damageType == "starve" || source.damageType == "inWall" || source.damageType == "flyIntoWall" || source.damageType == "cactus" || source.damageType == "inFire" || source.damageType == "onFire" || source.damageType == "hotFloor"
				|| source.damageType == "fall")
		{
			return false;
		}

		Entity entity = source.getTrueSource();
		if (entity instanceof EntityPlayer)
		{
			return this.isBeingRidden() ? false : super.attackEntityFrom(source, amount);
		}
		//		if (entity instanceof EntityMobFriend)
		//		{
		//			return false;
		//		}
		if (entity instanceof EntityVehicle)
		{
			return false;
		}
		return this.isBeingRidden() && entity != null && this.isRidingOrBeingRiddenBy(entity) ? false : super.attackEntityFrom(source, amount);
	}

	@Override
	public boolean isPotionApplicable(PotionEffect potioneffectIn)
	{
		return false;
	}


	@Override
	public boolean canBeSteered()
	{
		Entity entity = this.getControllingPassenger();
		return entity instanceof EntityLivingBase;
	}

	@Override
	@Nullable
	public Entity getControllingPassenger()
	{
		return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
	}

	@Override
	public boolean canPassengerSteer()
	{
		Entity entity = this.getControllingPassenger();
		return entity instanceof EntityPlayer ? ((EntityPlayer) entity).isUser() : !this.world.isRemote;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		//this.maxBoostStack = 40;

		if (this.canBeSteered() && this.getControllingPassenger() != null && this.getHealth() > 0.0F)
		{
			EntityPlayer entitylivingbase = (EntityPlayer) this.getControllingPassenger();

			boolean jump = TF2Core.proxy.jumped();
			boolean left = TF2Core.proxy.leftmove();
			boolean right = TF2Core.proxy.rightmove();
			boolean shift = TF2Core.proxy.shift();
			boolean getoff = TF2Core.proxy.getoff();

			if (getoff)
			{
				PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(10, this.getEntityId()));
				this.serverGetoff = true;
			}
			if (left)
			{
				PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(11, this.getEntityId()));
				this.serverLeftclick = true;
			}
			if (right)
			{
				PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(12, this.getEntityId()));
				this.serverRightclick = true;
			}
			if (jump)
			{
				PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(13, this.getEntityId()));
				this.serverBoost = true;
			}
			if (shift)
			{
				PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(14, this.getEntityId()));
				this.serverShift = true;
			}
		}
	}

	public void onLeftMove(World world, EntityPlayer player)
	{}

	public void onRightMove(World world, EntityPlayer player)
	{}

	public void onJumped(World world, EntityPlayer player)
	{}

	public void onShift(World world, EntityPlayer player)
	{}

	@Override
	public void onLivingUpdate()
	{
		if (!this.getPassengers().isEmpty())
		{
			if (this.getPassengers().get(0).isSneaking())
			{
				this.getPassengers().get(0).setSneaking(false);
			}
		}
		super.onLivingUpdate();
	}
}
