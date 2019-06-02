package tf2.entity.projectile.player;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tf2.TFDamageSource;
import tf2.entity.projectile.EntityTFProjectile;

public class EntityMortar extends EntityTFProjectile
{
	protected float velocity;
	protected double range;
	protected double spread;

    public EntityMortar(World worldIn)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);

	}

	public EntityMortar(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityMortar(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
		this.setTickAir(200);
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
			return TFDamageSource.causeBombDamage(this);
		}
		else
		{
			return TFDamageSource.causeBombDamage(this.thrower);
		}
	}

    @Override
    public void setEntityDead()
    {
        super.setDead();

    	this.world.createExplosion((Entity) null, this.posX, this.posY,this.posZ, 0.0F, false);
 		List var7 = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(this.spread));
 		int var3;
 		for (var3 = 0; var3 < var7.size(); ++var3)
 		{
 			EntityLivingBase var8 = (EntityLivingBase)var7.get(var3);

 			if(var8 != this.thrower && !(var8 instanceof EntityGolem && !(var8 instanceof IMob)))
 			{
 				DamageSource var201 = this.damageSource();
 	 			var8.attackEntityFrom(var201, (float)this.damage);
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