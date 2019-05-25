package tf2.entity.projectile.player;

import java.util.List;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tf2.TF2Core;
import tf2.TFDamageSource;
import tf2.entity.mob.enemy.EntityMobTF;
import tf2.util.RegistryHandler;

public class EntityFriendImpact extends EntityFriendProjectile
{
	public EntityFriendImpact(World worldIn)
	{
		super(worldIn);
		this.setSize(0.5F, 0.5F);
	}

	public EntityFriendImpact(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityFriendImpact(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}

	public static void registerEntity(Class<EntityFriendImpact> clazz, ResourceLocation registryName, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(registryName, clazz, registryName.getResourceDomain() + "." + registryName.getResourcePath(), RegistryHandler.entityId++, TF2Core.INSTANCE, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

	@Override
	public float inWaterSpeed()
	{
		return 0.99F;
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
             return DamageSource.causeMobDamage(this.thrower).setDamageBypassesArmor();
         }
	}

	@Override
	public void isGravity()
	{
 		List var7 = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.5D));
 		int var3;
 		for (var3 = 0; var3 < var7.size(); ++var3)
 		{
 			EntityLivingBase var8 = (EntityLivingBase)var7.get(var3);

 			if ((var8 instanceof EntityMobTF || var8 instanceof IMob))
        	{
 				DamageSource var201 = this.damageSource();
 	 			var8.attackEntityFrom(var201, (float)this.damage);
        	}
 		}
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void generateRandomParticles()
	{
		if (this.world.isRemote)
		{
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX, this.posY - 0.1D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

    public void onUpdate()
    {
    	if(this.inGround)
    	{
    		this.setDead();
    	}

        super.onUpdate();
    }

	 @Override
	 public void onHit(RayTraceResult raytraceResultIn)
	    {
	        Entity entity = raytraceResultIn.entityHit;

	        if (entity != null)
	        {
	        	if ((entity instanceof EntityMobTF || entity instanceof IMob))
	        	{
	        		DamageSource damagesource;

	                if (this.thrower == null)
	                {
	                    damagesource = TFDamageSource.causeBulletDamage(this, this);
	                }
	                else
	                {
	                    damagesource = TFDamageSource.causeBulletDamage(this, this.thrower);
	                }

	                if (this.isBurning())
	                {
	                    entity.setFire(5);
	                }

	                if (entity.attackEntityFrom(damagesource, (float)this.damage))
	                {
	                    if (entity instanceof EntityLivingBase)
	                    {
	                        EntityLivingBase entitylivingbase = (EntityLivingBase)entity;

	                        if (this.knockbackStrength > 0)
	                        {
	                            float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

	                            if (f1 > 0.0F)
	                            {
	                                entitylivingbase.addVelocity(this.motionX * (double)this.knockbackStrength * 0.6000000238418579D / (double)f1, 0.1D, this.motionZ * (double)this.knockbackStrength * 0.6000000238418579D / (double)f1);
	                            }
	                        }

	                        if (this.thrower instanceof EntityLivingBase)
	                        {
	                            EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.thrower);
	                            EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this.thrower, entitylivingbase);
	                        }

	                        this.bulletHit(entitylivingbase);
	                        entitylivingbase.hurtResistantTime = 0;

	                        if (this.thrower != null && entitylivingbase != this.thrower && entitylivingbase instanceof EntityPlayer && this.thrower instanceof EntityPlayerMP)
	                        {
	                            ((EntityPlayerMP)this.thrower).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
	                        }
	                    }
	                }
	        	}
	        	else if(entity instanceof IProjectile)
	        	{
	        		entity.setDead();
	        	}
	        }
	    }
}
