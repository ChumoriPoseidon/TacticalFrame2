package tf2.entity.projectile.player;

import java.util.List;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tf2.TFDamageSource;
import tf2.entity.projectile.EntityTFProjectile;

public class EntityShell extends EntityTFProjectile
{
	public EntityShell(World worldIn)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
		this.setTickAir(200);
	}

	public EntityShell(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityShell(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}

	@Override
	public float inWaterSpeed()
	{
		return 0.8F;
	}

	@Override
	public void setEntityDead()
    {
        super.setDead();

    	this.world.createExplosion((Entity) null, this.posX, this.posY,this.posZ, 0.0F, false);
 		List var7 = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(10.0D));
 		int var3;
 		for (var3 = 0; var3 < var7.size(); ++var3)
 		{
 			EntityLivingBase var8 = (EntityLivingBase)var7.get(var3);
 			if(var8 != this.thrower)
 			{
 	 			var8.attackEntityFrom(this.damageSource(), (float)this.damage);
 	 			var8.hurtResistantTime = 0;
 			}
 		}
    }
	@Override
    public void setDead()
	{
    	for (int var3 = 0; var3 < 10; ++var3)
		{
			double r = this.rand.nextDouble() * 8.0D;
			double i = this.rand.nextDouble() * Math.PI * 2.0D;
			double parX = this.posX + r * Math.sin(i);
			double parZ = this.posZ + r * Math.cos(i);
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, parX, this.posY + this.rand.nextDouble(), parZ, 0.0D, 0.0D, 0.0D, new int[0]);
		}
    	for (int i = 0; i < 180; ++i)
		{
			double r = 1.0D + this.rand.nextDouble() * 1.5D;
			double t = this.rand.nextDouble() * 2 * Math.PI;

			double e0 = r * Math.sin(t);
			double e2 = r * Math.cos(t);

			double r1 = 3.0D;

			double d4 = this.posX + 0.5D + r1 * Math.sin(t);
			double d6 = this.posZ + 0.5D + r1 * Math.cos(t);
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, d4, this.posY, d6, e0, 0F, e2, new int[0]);
		}
		super.setDead();
	}

	@Override
	public void isGravity()
	{
		if (!this.hasNoGravity())
        {
            this.motionY -= 0.05D;
        }
		else if (this.isInWater())
		{
			this.motionY -= 0.05D;
		}
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
	public void bulletHit(EntityLivingBase living)
	{
		if(living instanceof EntityEnderman)
		{
			this.setEntityDead();
		}
	}

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

                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL,  this.posX + var211 * (double)var16, this.posY + 0.1D + var231 * (double)var16, this.posZ + var23 * (double)var16, 0.0D, 0.0D, 0.0D, new int[15]);
            }
        }
    }

    @Override
    public void onHit(RayTraceResult raytraceResultIn)
    {
        Entity entity = raytraceResultIn.entityHit;

        if (entity != null)
        {
            if (this.isBurning())
            {
                entity.setFire(5);
            }

            if (entity.attackEntityFrom(this.damageSource(), (float)this.damage * 0.25F))
            {
                if (entity instanceof EntityLivingBase)
                {
                    EntityLivingBase entitylivingbase = (EntityLivingBase)entity;

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

                if (!(entity instanceof EntityEnderman))
                {
                	 this.setEntityDead();
                }
            }
            else
            {
            	this.setEntityDead();
            }
        }
        else
        {
        	super.onHitBlock(raytraceResultIn);
        }
    }
}
