package tf2.entity.projectile.enemy;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import tf2.entity.projectile.IEnemyProjectile;
import tf2.entity.projectile.player.EntityMortar;

public class EntityEnemyMortar extends EntityMortar implements IEnemyProjectile
{
	public EntityEnemyMortar(World worldIn)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
		this.setTickAir(200);
	}

	public EntityEnemyMortar(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityEnemyMortar(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}

	@Override
	public void setEntityDead()
	{
		super.setDead();

		this.world.createExplosion((Entity) null, this.posX, this.posY, this.posZ, 0.0F, false);
		List var7 = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(this.spread));
		int var3;
		for (var3 = 0; var3 < var7.size(); ++var3)
		{
			EntityLivingBase var8 = (EntityLivingBase) var7.get(var3);

			DamageSource var201 = this.damageSource();
			var8.attackEntityFrom(var201, (float) this.damage);
			var8.hurtResistantTime = 0;
		}
	}
}
