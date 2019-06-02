package tf2.entity.projectile.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import tf2.entity.projectile.IFriendProjectile;

public class EntityFriendBullet extends EntityBullet implements IFriendProjectile
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

	@Override
	protected void onHit(RayTraceResult raytraceResultIn)
	{
		Entity entity = raytraceResultIn.entityHit;
		if (entity != null)
		{
			if (!(entity instanceof EntityPlayer) || !(entity instanceof EntityGolem && !(entity instanceof IMob)))
			{
				super.onHit(raytraceResultIn);
			}
		}
		else
		{
			super.onHitBlock(raytraceResultIn);
		}
	}
}
