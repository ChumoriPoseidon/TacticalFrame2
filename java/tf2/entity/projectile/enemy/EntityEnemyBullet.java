package tf2.entity.projectile.enemy;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tf2.TF2Core;
import tf2.util.RegistryHandler;

public class EntityEnemyBullet extends EntityEnemyProjectile
{
	public EntityEnemyBullet(World worldIn)
	{
		super(worldIn);
		this.setSize(0.2F, 0.2F);
	}

	public EntityEnemyBullet(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityEnemyBullet(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}

	public static void registerEntity(Class<EntityEnemyBullet> clazz, ResourceLocation registryName, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(registryName, clazz, registryName.getResourceDomain() + "." + registryName.getResourcePath(), RegistryHandler.entityId++, TF2Core.INSTANCE, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

	@SideOnly(Side.CLIENT)
	public void generateRandomParticles()
	{
		double x = this.prevPosX - this.posX;
		double y = this.prevPosY - this.posY;
		double z = this.prevPosZ - this.posZ;

		if (this.world.isRemote)
		{
			for (int var1 = 0; var1 < 5; ++var1)
			{
				float var16 = 0.2F * (float) var1;
				this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + x * (double) var16, this.posY + 0.1D + y * (double) var16, this.posZ + z * (double) var16, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
	}
}
