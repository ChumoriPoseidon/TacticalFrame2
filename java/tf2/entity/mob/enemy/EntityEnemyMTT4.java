package tf2.entity.mob.enemy;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tf2.TFSoundEvents;
import tf2.entity.mob.ai.EntityAIAttackRangedGun;
import tf2.entity.mob.frend.EntityEvent1;
import tf2.entity.projectile.enemy.EntityEnemyBullet;

public class EntityEnemyMTT4 extends EntityMobTF implements IRangedAttackMob
{

	public EntityEnemyMTT4(World worldIn)
	{
		super(worldIn);
		this.setSize(1.2F, 1.5F);
	}

	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAttackRangedGun(this, 1.0D, 30.0F));
		this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(4, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.experienceValue = 5;
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.3D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30D);
	}
	@Override
	protected void entityInit()
	{
		super.entityInit();
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase var1, float var2)
	{
		double var3 = var1.posX - this.posX;
		double var8 = var1.posY - this.posY;
		double var5 = var1.posZ - this.posZ;

		if (this.attackTime <= 30 && this.attackTime % 10 == 0)
		{
			EntityEnemyBullet var7 = new EntityEnemyBullet(this.world, this);
			var7.setDamage(var7.getDamage() + 5.0D);
			this.playSound(TFSoundEvents.M16, 2.3F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			var7.shoot(var3, var8, var5, 2.0F, 3.0F);
			this.world.spawnEntity(var7);
		}
		if (this.attackTime <= 0)
		{
			this.attackTime = 120;
		}
	}
	@Override
	protected SoundEvent getAmbientSound()
	{
		return TFSoundEvents.TM_SAY;
	}
	@Override
	protected SoundEvent getDeathSound()
	{
		return TFSoundEvents.TM_DEATH;
	}
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.ENTITY_IRONGOLEM_HURT;
	}
	@Override
	protected void playStepSound(BlockPos pos, Block blockIn)
	{}

	@Override
	public void setSwingingArms(boolean swingingArms)
	{}

	@Override
	protected void onDeathUpdate()
	{
		if (this.deathTime == 1)
        {
			List k = this.world.getEntitiesWithinAABB(EntityEvent1.class, this.getEntityBoundingBox().grow(30.0D));
			for (int u = 0; u < k.size(); ++u)
			{
				EntityEvent1 target = (EntityEvent1) k.get(u);

				int targetCount = target.getCount();
				if(targetCount > 0)
				{
					--target.count;
					break;
				}
			}
        }
		super.onDeathUpdate();
	}
}