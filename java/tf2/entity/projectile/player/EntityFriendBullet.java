package tf2.entity.projectile.player;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import tf2.TF2Core;
import tf2.util.RegistryHandler;

public class EntityFriendBullet extends EntityFriendProjectile
{
	public EntityFriendBullet(World worldIn)
	{
		super(worldIn);
		this.setSize(0.2F, 0.2F);
	}

	public EntityFriendBullet(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityFriendBullet(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}

	public static void registerEntity(Class<EntityFriendBullet> clazz, ResourceLocation registryName, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(registryName, clazz, registryName.getResourceDomain() + "." + registryName.getResourcePath(), RegistryHandler.entityId++, TF2Core.INSTANCE, trackingRange, updateFrequency, sendsVelocityUpdates);
	}
}