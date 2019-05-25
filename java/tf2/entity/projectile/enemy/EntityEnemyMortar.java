package tf2.entity.projectile.enemy;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import tf2.TF2Core;
import tf2.TFDamageSource;
import tf2.util.RegistryHandler;

public class EntityEnemyMortar extends EntityEnemyProjectile
{
	public EntityEnemyMortar(World worldIn)
	{
		super(worldIn);
		this.ignoreFrustumCheck = true;
		this.setSize(0.1F, 0.1F);
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
	public float inWaterSpeed()
	{
		return 0.99F;
	}

	@Override
	protected int plusTickAir()
	{
		return 50;
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
		List var7 = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(8.0D));
		int var3;
		for (var3 = 0; var3 < var7.size(); ++var3)
		{
			EntityLivingBase var8 = (EntityLivingBase) var7.get(var3);

			DamageSource var201 = this.damageSource();
			var8.attackEntityFrom(var201, (float) this.damage);
			var8.hurtResistantTime = 0;
		}
	}
	@Override
    public void setDead()
	{
		for (int ix = 0; ix < 3; ++ix)
		{
			for (int iz = 0; iz < 3; ++iz)
			{
				this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX - 4D + (ix * 4D), this.posY, this.posZ - 4D + (iz * 4D), 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
		super.setDead();
	}
	@Override
	public void onHit(RayTraceResult raytraceResultIn)
	{

	}
}
