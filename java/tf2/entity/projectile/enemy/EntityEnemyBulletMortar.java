package tf2.entity.projectile.enemy;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import tf2.TF2Core;
import tf2.TFDamageSource;
import tf2.potion.TFPotionPlus;
import tf2.util.RegistryHandler;

public class EntityEnemyBulletMortar extends EntityEnemyProjectile
{
	protected int amplifier;
	protected float velocity;
	protected double range;

	public EntityEnemyBulletMortar(World worldIn)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
	}

	public EntityEnemyBulletMortar(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityEnemyBulletMortar(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}

	public static void registerEntity(Class<EntityEnemyBulletMortar> clazz, ResourceLocation registryName, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(registryName, clazz, registryName.getResourceDomain() + "." + registryName.getResourcePath(), RegistryHandler.entityId++, TF2Core.INSTANCE, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy)
	{
		super.shoot(x, y, z, velocity, inaccuracy);
		this.velocity = velocity;
	}

	@Override
	public float inWaterSpeed()
	{
		return 0.95F;
	}

	@Override
	public void bulletHit(EntityLivingBase living)
	{
		if (living instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) living;
			if (!player.isActiveItemStackBlocking())
			{
				if (!this.world.isRemote)
				{
					living.addPotionEffect(new PotionEffect(TFPotionPlus.HEAT, 200, this.amplifier));
				}
			}
		}
		else
		{
			if (!this.world.isRemote)
			{
				living.addPotionEffect(new PotionEffect(TFPotionPlus.HEAT, 200, this.amplifier));
			}
		}
	}

	@Override
	public DamageSource damageSource()
	{
		if (this.thrower == null)
		{
			return TFDamageSource.causeGrenadeDamage(this);
		}
		else
		{
			return TFDamageSource.causeGrenadeDamage(this.thrower);
		}
	}

	@Override
	public void setEntityDead()
	{
		super.setDead();

		this.world.createExplosion((Entity) null, this.posX, this.posY, this.posZ, 0.0F, false);
		List var7 = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(3.0D));
		int var3;
		for (var3 = 0; var3 < var7.size(); ++var3)
		{
			EntityLivingBase var8 = (EntityLivingBase) var7.get(var3);

			DamageSource var201 = this.damageSource();
			var8.attackEntityFrom(var201, (float) this.damage * 0.5F);
			var8.hurtResistantTime = 0;
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("Amplifier", this.amplifier);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		if (compound.hasKey("Amplifier", 99))
		{
			this.amplifier = compound.getInteger("Amplifier");
		}

	}

	public void setAmplifier(int amplifierIn)
	{
		this.amplifier = amplifierIn;
	}

	public int getAmplifier()
	{
		return this.amplifier;
	}

	public void setRange(double rangeIn)
	{
		this.range = rangeIn;
	}

	public double getRange()
	{
		return this.range;
	}


	@Override
	public void onUpdate()
	{
		super.onUpdate();

//		this.motionY -=0.1F;
	}

	@Override
	protected void isGravity()
	{
		if(this.velocity != 0)
		{
			double speed = velocity * 0.1D;
			double gravity = 0.48D + this.getRange() * 0.01D;

			double v_x = speed * (MathHelper.cos((float) Math.toRadians(this.ticksInAir)));
			double v_y = speed * (MathHelper.cos((float)Math.toRadians(this.ticksInAir)));

			this.motionY -= (2 * v_x * v_y) / gravity;
		}
	}
}
