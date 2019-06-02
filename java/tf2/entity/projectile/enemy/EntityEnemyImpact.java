package tf2.entity.projectile.enemy;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import tf2.TFDamageSource;
import tf2.entity.mob.enemy.EntityMobTF;
import tf2.entity.projectile.IEnemyProjectile;
import tf2.entity.projectile.player.EntityImpact;

public class EntityEnemyImpact extends EntityImpact implements IEnemyProjectile
{
	private int knockbackStrength;

	public EntityEnemyImpact(World worldIn)
	{
		super(worldIn);
		this.setSize(0.5F, 0.5F);
	}

	public EntityEnemyImpact(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityEnemyImpact(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
		this.setTickAir(50);
	}

	@Override
	public void onHit(RayTraceResult raytraceResultIn)
	{
	    Entity entity = raytraceResultIn.entityHit;

	    if (entity != null)
	    {
	    	if (!(entity instanceof EntityMobTF))
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
	    }
	}
}
