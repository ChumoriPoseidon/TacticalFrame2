package tf2.entity.mob.frend;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import tf2.TFItems;
import tf2.entity.mob.ai.EntityAIAttackRangedGun;

public class EntityTF80G extends EntityGynoid
{
	private static final double defaultDamage = 0;
	private static final double upAttack = 0.068;
	private static final double upArmor = 0.203;
	private static final double upArmorToughness = 0.028;
	private static final double upMaxHealth = 2.44;

	private static final double defaultArmor = 2.0D;
	private static final double defaultArmorToughness = 0.0D;
	private static final double defaultMaxHealth = 20.0D;

	private int jumpTime;

	public EntityTF80G(World worldIn)
	{
		super(worldIn, defaultDamage, upAttack, defaultArmor, upArmor, defaultArmorToughness, upArmorToughness, defaultMaxHealth, upMaxHealth);
	}

	protected void initEntityAI()
	{
		super.initEntityAI();
		this.tasks.addTask(1, new EntityAIAttackRangedGun(this, 1.0D, 10.0F));
	}

    @Override
	public void isUpLevel()
    {
    	super.isUpLevel();
    	if(this.getMechaLevel() == 44)
    	{
    		this.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 0.5F, 1.0F);
    		ItemStack stack = new ItemStack(TFItems.SKILL_ARMEDFORM_DELTA);

			ITextComponent text = new TextComponentString("[");
			text.getStyle().setColor(TextFormatting.GREEN);
			text.getStyle().setColor(TextFormatting.LIGHT_PURPLE);

			ITextComponent itemName = new TextComponentString(stack.getDisplayName());
	        text.appendSibling(itemName);
	        text.appendText("]");

	        String skillText = "skill.get";

	        if (this.getOwner() != null && this.getOwner() instanceof EntityPlayerMP)
	        {
	            this.getOwner().sendMessage(new TextComponentTranslation(skillText, new Object[] {this.getDisplayName() , text}));
	        }
    	}

    	if(this.getMechaLevel() == 29)
    	{
    		//this.getInventoryMechaEquipment().setHasSkill(new ItemStack(TFItems.SKILL_CENTEROFATTENTION));
    	}

    	if(this.getMechaLevel() == 59)
    	{
    		//this.getInventoryMechaEquipment().setHasSkill(new ItemStack(TFItems.SKILL_ADDITIONALARMOR));
    	}
    }

    @Override
    public void onLivingUpdate()
    {

    	if(this.getAttackTarget() != null)
    	{
    		EntityLivingBase target = this.getAttackTarget();

    		boolean flag = this.getEntitySenses().canSee(target);
    		boolean flag1 = this.jumpTime > 0;
    		if (flag != flag1)
    		{
    			this.jumpTime = 0;
    		}
    		if (flag)
    		{
    			++this.jumpTime;
    		}
    		else
    		{
    			--this.jumpTime;
    		}

    		double dx = target.posX - this.posX;
    		double dz = target.posZ - this.posZ;
    		double dy = target.posY - this.posY;

    		if (this.jumpTime == 30)
    		{

    			double f2 = MathHelper.sqrt(dx * dx + dz * dz);

    			double y1 = Math.min(dy, 0D);
    			double y2 = MathHelper.sqrt(y1 * y1);
    			double y3 = Math.min(y2, 10D);

    			float i = 1.1F;

    			if(!this.onGround)
    			{
    				i = 0.9F;
    			}

    			this.motionX = (dx / f2) * MathHelper.sqrt(f2) * i;
    			this.motionZ = (dz / f2) * MathHelper.sqrt(f2) * i;
    			this.motionY = -(0.9F * y3);
    			this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 2.5F, 1.5F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
    			this.jumpTime = 0;
    		}
    	}

		if(this.getMechaLevel() >= 44)
		{
			List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityFriendMecha.class, this.getEntityBoundingBox().grow(32.0D), Predicates.<EntityLivingBase>and(EntitySelectors.NOT_SPECTATING));

			if(!list.isEmpty())
			{
				if(!this.world.isRemote)
				{
					list.forEach( entity ->{entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 20, 0, false, false));} );
				}
			}
		}

    	super.onLivingUpdate();
    }

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
	{
		double var3 = target.posX - this.posX;
		double var8 = target.posY - this.posY;
		double var5 = target.posZ - this.posZ;

        float distance = this.getDistance(target);

        if(this.onGround && this.jumpTime == 20 && this.rand.nextInt(2) == 0)
        {
        	this.motionY = 0.55D;
        }

        if(distance < 1.5F)
        {
        	target.attackEntityFrom(DamageSource.causeMobDamage(this), this.getMechaATK() + 4);
        }

		if (this.attackTime <= 0)
		{
			this.attackTime = 80;
		}
	}

}