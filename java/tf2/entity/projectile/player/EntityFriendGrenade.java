package tf2.entity.projectile.player;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tf2.TF2Core;
import tf2.TFDamageSource;
import tf2.util.RegistryHandler;

public class EntityFriendGrenade extends EntityFriendProjectile
{
	protected double spread;

    public EntityFriendGrenade(World worldIn)
	{
		super(worldIn);
		this.setSize(0.2F, 0.2F);
	}

	public EntityFriendGrenade(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityFriendGrenade(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}

	public static void registerEntity(Class<EntityFriendGrenade> clazz, ResourceLocation registryName, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(registryName, clazz, registryName.getResourceDomain() + "." + registryName.getResourcePath(), RegistryHandler.entityId++, TF2Core.INSTANCE, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

	@Override
	public DamageSource damageSource()
	{
		if (this.thrower == null)
		{
			return TFDamageSource.causeBombDamage(this);
		}
		else
		{
			return TFDamageSource.causeBombDamage(this.thrower);
		}
	}
	@Override
	public int plusTickAir()
	{
		return 70;
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
    public void setEntityDead()
    {
        super.setDead();

    	this.world.createExplosion((Entity) null, this.posX, this.posY,this.posZ, 0.0F, false);
 		List var7 = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(3.0D));
 		int var3;
 		for (var3 = 0; var3 < var7.size(); ++var3)
 		{
 			EntityLivingBase var8 = (EntityLivingBase)var7.get(var3);

 			if(var8 != this.thrower && !(var8 instanceof EntityPlayer) && !(var8 instanceof EntityGolem))
 			{
 				DamageSource var201 = this.damageSource();
 	 			var8.attackEntityFrom(var201, (float)this.damage * 0.5F);
 	 			var8.hurtResistantTime = 0;
 			}
 		}
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void generateRandomParticles()
    {
        double var211 = this.prevPosX - this.posX;
        double var231 = this.prevPosY - this.posY;
        double var23 = this.prevPosZ - this.posZ;

        if (this.world.isRemote)
        {
        	for (int var24 = 0; var24 < 5; ++var24)
            {
        		float var16 = 0.2F * (float)var24;

                this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,  this.posX + var211 * (double)var16, this.posY + 0.1D + var231 * (double)var16, this.posZ + var23 * (double)var16, 0.0D, 0.0D, 0.0D, new int[15]);
            }
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
