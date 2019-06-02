package tf2.entity.projectile.enemy;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import tf2.TFDamageSource;
import tf2.entity.projectile.EntityTFProjectile;
import tf2.entity.projectile.IEnemyProjectile;
import tf2.potion.TFPotionPlus;

public class EntityEnemyBulletHE extends EntityTFProjectile implements IEnemyProjectile
{
	protected int amplifier;

	public EntityEnemyBulletHE(World worldIn)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
	}

	public EntityEnemyBulletHE(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityEnemyBulletHE(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
		this.setTickAir(50);
	}

	@Override
	public float inWaterSpeed()
	{
		return 0.95F;
	}

	@Override
	public void bulletHit(EntityLivingBase living)
	{
		if (living instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) living;
			if (!player.isActiveItemStackBlocking())
			{
				if (!this.world.isRemote)
				{
					living.addPotionEffect(new PotionEffect(TFPotionPlus.HEAT, 200, this.amplifier));
				}
			}
		}
		else
		{
			if (!this.world.isRemote)
			{
				living.addPotionEffect(new PotionEffect(TFPotionPlus.HEAT, 200, this.amplifier));
			}
		}
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
		List var7 = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(3.0D));
		int var3;
		for (var3 = 0; var3 < var7.size(); ++var3)
		{
			EntityLivingBase var8 = (EntityLivingBase) var7.get(var3);

			DamageSource var201 = this.damageSource();
			var8.attackEntityFrom(var201, (float) this.damage * 0.5F);
			var8.hurtResistantTime = 0;

		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("Amplifier", this.amplifier);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		if (compound.hasKey("Amplifier", 99))
		{
			this.amplifier = compound.getInteger("Amplifier");
		}

	}

	public void setAmplifier(int amplifierIn)
	{
		this.amplifier = amplifierIn;
	}

	public int getAmplifier()
	{
		return this.amplifier;
	}
}
