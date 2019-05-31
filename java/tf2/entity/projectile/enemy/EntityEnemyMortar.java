package tf2.entity.projectile.enemy;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import tf2.TF2Core;
import tf2.TFDamageSource;
import tf2.util.RegistryHandler;

public class EntityEnemyMortar extends EntityEnemyProjectile
{
	protected float velocity;
	protected double range;
	protected double spread;

	public EntityEnemyMortar(World worldIn)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
	}

	public EntityEnemyMortar(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityEnemyMortar(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}

	public static void registerEntity(Class<EntityEnemyMortar> clazz, ResourceLocation registryName, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
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
	public int plusTickAir()
	{
		return 150;
	}
	@Override
	public void setEntityDead()
	{
		super.setDead();

		this.world.createExplosion((Entity) null, this.posX, this.posY, this.posZ, 0.0F, false);
		List var7 = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(this.spread));
		int var3;
		for (var3 = 0; var3 < var7.size(); ++var3)
		{
			EntityLivingBase var8 = (EntityLivingBase) var7.get(var3);

			DamageSource var201 = this.damageSource();
			var8.attackEntityFrom(var201, (float) this.damage);
			var8.hurtResistantTime = 0;
		}
	}



	public void setRange(double rangeIn)
	{
		this.range = rangeIn;
	}

	public double getRange()
	{
		return this.range;
	}
	public void setSpread(double damageIn)
	{
		this.spread = damageIn;
	}
	public double getSpread()
	{
		return this.spread;
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
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setDouble("spread", this.spread);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.spread = compound.getDouble("spread");
	}

}
