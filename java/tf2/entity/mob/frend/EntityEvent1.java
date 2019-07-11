package tf2.entity.mob.frend;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import tf2.entity.mob.enemy.EntityEnemyMTT4;
import tf2.util.TFAdvancements;

public class EntityEvent1 extends EntityMobNPC
{
	private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(this.getScoreName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_6));

	public int count;
	public int maxCount;
	private static final DataParameter<Integer> COUNT = EntityDataManager.<Integer> createKey(EntityEvent1.class, DataSerializers.VARINT);

	protected int eventTime2;
	private static final DataParameter<Integer> EVENT_TIME2 = EntityDataManager.<Integer> createKey(EntityEvent1.class, DataSerializers.VARINT);

	public EntityEvent1(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.8F);
		this.count = 8;
		this.maxCount = count;
	}

	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(2, new EntityAILookIdle(this));
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(COUNT, Integer.valueOf(0));
		this.dataManager.register(EVENT_TIME2, Integer.valueOf(1));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
	}

	public ITextComponent getScoreName()
	{
		TextComponentString textcomponentstring = new TextComponentString(I18n.format("tf.mission.potential"));
		return textcomponentstring;
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return null;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.ENTITY_PLAYER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_PLAYER_DEATH;
	}

	@Override
	protected boolean canDespawn()
	{
		return false;
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		if (!world.isRemote)
		{
			this.setCount();
			this.setEventTime2();
		}
		else
		{
			this.count = this.dataManager.get(COUNT).intValue();
			this.eventTime2 = this.dataManager.get(EVENT_TIME2).intValue();
		}
		if (this.count <= 0)
		{
			++this.eventTime2;
			this.isClear();
		}

		this.isMission();

		if (this.count <= 0)
		{
			this.bossInfo.setVisible(false);
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("Count", this.count);
		compound.setInteger("EventTime2", this.eventTime2);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.count = compound.getInteger("Count");
		this.eventTime2 = compound.getInteger("EventTime2");
	}

	public void setEventTime2()
	{
		this.dataManager.set(EVENT_TIME2, new Integer(this.eventTime2));
	}

	public void setCount()
	{
		this.dataManager.set(COUNT, new Integer(this.count));
	}

	public int getCount()
	{
		return this.dataManager.get(COUNT);
	}

	public int getMaxCount()
	{
		return this.maxCount;
	}

	@Override
	public void addTrackingPlayer(EntityPlayerMP player)
	{
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player)
	{
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	protected void updateAITasks()
	{
		super.updateAITasks();
		this.bossInfo.setPercent((float) this.getCount() / this.getMaxCount());
	}

	public void isChat(String text)
	{
		List k = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(30.0D));
		for (int u = 0; u < k.size(); ++u)
		{
			EntityPlayer playerall = (EntityPlayer) k.get(u);
			if (!this.world.isRemote)
			{
				playerall.sendMessage(new TextComponentTranslation(text, new Object[0]));
			}
		}
	}

	public void isMission()
	{
		if (this.eventTime == 200)
		{
			String d = I18n.format("tf.mission1.txt1");
			this.isChat(d);
		}
		if (this.eventTime == 300)
		{
			String d = I18n.format("tf.mission1.txt2");
			this.isChat(d);
		}
		if (this.eventTime == 400)
		{
			String d = I18n.format("tf.mission1.txt3");
			this.isChat(d);
		}
		if (this.eventTime == 500)
		{
			String d = I18n.format("tf.mission1.txt4");
			this.isChat(d);
		}
		if (this.eventTime == 600)
		{
			String d = I18n.format("tf.mission1.txt5");
			this.isChat(d);
		}

		if (this.count > 0)
		{
			if (this.eventTime >= 800 && this.eventTime <= 3600)
			{
				if (this.ticksExisted % 200 == 0)
				{
					EntityEnemyMTT4 var7 = new EntityEnemyMTT4(this.world);
					this.isSpawn(var7, 20F);
				}
			}

			if (this.eventTime > 4000)
			{
				this.setDead();
			}
		}
	}

	public void isClear()
	{
		if (this.eventTime2 == 100)
		{
			String d = I18n.format("tf.mission1.txt6");
			this.isChat(d);
		}
		if (this.eventTime2 == 200)
		{
			String d = I18n.format("tf.mission1.txt7");
			this.isChat(d);
		}
		if (this.eventTime2 == 400)
		{
			String d = I18n.format("tf.mission1.txt8");
			this.isChat(d);
		}
		if (this.eventTime2 == 500)
		{
			String d = I18n.format("tf.mission1.txt9");
			this.isChat(d);

			List<EntityPlayerMP> k = this.world.getEntitiesWithinAABB(EntityPlayerMP.class, this.getEntityBoundingBox().grow(40.0D));
			for (int u = 0; u < k.size(); ++u)
			{
				EntityPlayerMP playerall = k.get(u);

				TFAdvancements.MISSION_01.trigger(playerall);
			}
		}
		if (this.eventTime2 > 600)
		{
			this.setDead();
		}
	}

	protected void isSpawn(EntityLivingBase var1, float range)
	{
		if (!this.world.isRemote)
		{
			double t = this.rand.nextDouble() * 2 * Math.PI;

			var1.posX = this.posX + 0.5D + range * Math.sin(t);
			var1.posZ = this.posZ + 0.5D + range * Math.cos(t);
			var1.posY = this.posY + 15 + (10 * (Math.random() - 0.5));

			int i = MathHelper.floor(var1.posX);
			int j = MathHelper.floor(var1.posY);
			int k = MathHelper.floor(var1.posZ);

			BlockPos blockpos = new BlockPos(i, j, k);

			if (world.isBlockLoaded(blockpos))
			{
				boolean flag1 = false;

				while (!flag1 && j > 0)
				{
					BlockPos blockpos1 = blockpos.down();
					IBlockState iblockstate = world.getBlockState(blockpos1);

					if (iblockstate.getMaterial().blocksMovement())
					{
						flag1 = true;
					}
					else
					{
						--var1.posY;
						--j;
						blockpos = blockpos1;
					}
				}

				if (flag1)
				{
					var1.setPosition((double) i, (double) j, (double) k);
					var1.setLocationAndAngles((double) i, (double) j, (double) k, this.rand.nextFloat() * 360.0F, 0.0F);
				}
			}
			this.world.spawnEntity(var1);
		}
	}
}
