package tf2.entity.mob.frend;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMobNPC extends EntityGolem
{
	public int attackTime;

	public int deathTicks;

	public int animationTime1;
	public int animationTime2;
	public int animationTime3;
	public int maxAnimationTime1;

	protected int eventTime;
	private static final DataParameter<Integer> EVENT_TIME = EntityDataManager.<Integer> createKey(EntityMobNPC.class, DataSerializers.VARINT);


	public EntityMobNPC(World par1World)
	{
		super(par1World);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(EVENT_TIME, Integer.valueOf(0));
	}
	@Override
	protected boolean canDespawn()
	{
		return false;
	}

//	@Override
//	public void setInWeb()
//	{}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		Entity entity = source.getTrueSource();
		if(entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)entity;
			return !player.capabilities.isCreativeMode ? false : super.attackEntityFrom(source, amount);
		}
		if(entity instanceof EntityMobNPC)
		{
			return false;
		}
		return super.attackEntityFrom(source, amount);
	}

	//--------ここからアニメーション関係
	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		if (!world.isRemote)
		{
			this.setEventTime();
		}
		else
		{
			this.eventTime = this.dataManager.get(EVENT_TIME).intValue();
		}

		++this.eventTime;


		if (this.attackTime > 0)
		{
			--this.attackTime;
		}
		if (this.animationTime1 > 0)
		{
			--this.animationTime1;
		}
		if (this.animationTime2 > 0)
		{
			--this.animationTime2;
		}
		if (this.animationTime3 > 0)
		{
			--this.animationTime3;
		}
	}

	public void setAnimetion1()
	{
		this.world.setEntityState(this, (byte) 42);
	}

	public void setAnimetion2()
	{
		this.world.setEntityState(this, (byte) 43);
	}

	public void setAnimetion3()
	{
		this.world.setEntityState(this, (byte) 44);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setShort("AnimationTime1", (short) this.animationTime1);
		compound.setShort("AnimationTime2", (short) this.animationTime2);
		compound.setShort("AnimationTime3", (short) this.animationTime3);

		compound.setInteger("EventTime", this.eventTime);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.animationTime1 = compound.getShort("AnimationTime1");
		this.animationTime2 = compound.getShort("AnimationTime2");
		this.animationTime3 = compound.getShort("AnimationTime3");

		this.eventTime = compound.getInteger("EventTime");
	}

	public void setEventTime()
	{
		this.dataManager.set(EVENT_TIME, new Integer(this.eventTime));
	}
}
