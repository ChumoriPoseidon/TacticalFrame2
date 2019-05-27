package tf2.entity.mob.frend;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import tf2.TFItems;
import tf2.TFSoundEvents;
import tf2.entity.mob.ai.EntityAIAttackRangedGun;
import tf2.entity.projectile.player.EntityFriendShell;


public class EntityTF77B extends EntityGynoid
{
	private static final double defaultDamage = 0;
	private static final double upAttack = 0.068;
	private static final double upArmor = 0.19;
	private static final double upArmorToughness = 0.028;
	private static final double upMaxHealth = 1.76;

	private static final double defaultArmor = 1.0D;
	private static final double defaultArmorToughness = 0.0D;
	private static final double defaultMaxHealth = 20.0D;

	public EntityTF77B(World worldIn)
	{
		super(worldIn, defaultDamage, upAttack, defaultArmor, upArmor, defaultArmorToughness, upArmorToughness, defaultMaxHealth, upMaxHealth);
	}

	protected void initEntityAI()
	{
		super.initEntityAI();
		this.tasks.addTask(1, new EntityAIAttackRangedGun(this, 1.0D, 30.0F));
	}

    @Override
	public void isUpLevel()
    {
    	super.isUpLevel();
    	if(this.getMechaLevel() == 44)
    	{
    		this.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 0.5F, 1.0F);
    		ItemStack stack = new ItemStack(TFItems.SKILL_ARMEDFORM_ALPHA);

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
    		this.getInventoryMechaEquipment().setHasSkill(new ItemStack(TFItems.SKILL_AUTOREPAIR));
    	}

    	if(this.getMechaLevel() == 59)
    	{
    		this.getInventoryMechaEquipment().setHasSkill(new ItemStack(TFItems.SKILL_REPAIRDOUBLING));
    	}
    }

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
	{
		double var3 = target.posX - this.posX;
		double var8 = target.posY - this.posY;
		double var5 = target.posZ - this.posZ;

		if (this.attackTime <= 15 && this.attackTime % 4 == 0)
		{
			EntityFriendShell var7 = new EntityFriendShell(this.world, this);
			var7.setDamage(var7.getDamage() + this.getMechaATK() + 3);
			this.playSound(TFSoundEvents.FAMAS, 2.5F, 2.5F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			var7.shoot(var3, var8, var5, 2.0F, 3.0F);
			this.world.spawnEntity(var7);
		}

		if (this.attackTime <= 0)
		{
			this.attackTime = 80;
		}
	}

	@Override
	public void onLivingUpdate()
	{
		if(this.getMechaLevel() >= 44 && this.ticksExisted % 600 == 0)
		{
			List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityFriendMecha.class, this.getEntityBoundingBox().grow(32.0D), Predicates.<EntityLivingBase>and(EntitySelectors.NOT_SPECTATING));

			if(!list.isEmpty())
			{
				if(!this.world.isRemote)
				{
					list.forEach( entity ->{entity.heal(this.getMaxHealth() * 0.02F);} );
				}
			}

		}

		super.onLivingUpdate();
	}
}
