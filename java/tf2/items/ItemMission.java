package tf2.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import tf2.TFItems;
import tf2.entity.mob.frend.EntityEvent1;

public class ItemMission extends ItemBase
{
	public ItemMission(String name)
	{
		super(name);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		pos = pos.offset(facing);
		ItemStack itemstack = player.getHeldItem(hand);

		if (!player.canPlayerEdit(pos, facing, itemstack))
		{
			return EnumActionResult.FAIL;
		}
		else
		{
			if (!worldIn.isRemote)
			{
				if (itemstack.getItem() == TFItems.MISSION_1)
				{
					EntityEvent1 entity = new EntityEvent1(worldIn);
					entity.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
					entity.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
					worldIn.spawnEntity(entity);
				}
			}

			if (!player.capabilities.isCreativeMode)
			{
				itemstack.shrink(1);
			}
			return EnumActionResult.SUCCESS;
		}
	}

//	@Override
//	@SideOnly(Side.CLIENT)
//	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
//	{
//		if (stack.getItem() == TFItems.MISSION_3)
//		{
//			tooltip.add(TextFormatting.RED + I18n.translateToLocal("tf.build_road"));
//		}
//		super.addInformation(stack, worldIn, tooltip, flagIn);
//	}
}