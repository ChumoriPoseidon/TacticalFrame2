package tf2.entity.projectile.player;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import tf2.TF2Core;
import tf2.TFDamageSource;
import tf2.util.RegistryHandler;

public class EntityBulletBig extends EntityPlayerProjectile
{
	public EntityBulletBig(World worldIn)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
	}

	public EntityBulletBig(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityBulletBig(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}

	public static void registerEntity(Class<EntityBulletBig> clazz, ResourceLocation registryName, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(registryName, clazz, registryName.getResourceDomain() + "." + registryName.getResourcePath(), RegistryHandler.entityId++, TF2Core.INSTANCE, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

	@Override
	public int plusTickAir()
	{
		return 20;
	}

	@Override
	public float inWaterSpeed()
	{
		return 0.9F;
	}

	@Override
	public void bulletHit(EntityLivingBase living)
	{
		if(living instanceof EntityEnderman)
		{
			living.attackEntityFrom(TFDamageSource.causeBombDamage(this.thrower), (float) this.damage);
		}
	}
}
