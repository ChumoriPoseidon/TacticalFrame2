package tf2.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tf2.TF2Core;
import tf2.TFBlocks;
import tf2.TFItems;
import tf2.util.IHasModel;

public class BlockGunCraft extends Block implements IHasModel
{
	public BlockGunCraft(String name)
	{
		super(Material.ROCK);
		this.setSoundType(SoundType.STONE);

		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(TF2Core.tabstfBlocks);
		TFBlocks.BLOCKS.add(this);
		TFItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void registerModel()
	{
		TF2Core.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "Inventory");
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		} else
		{
			playerIn.openGui(TF2Core.INSTANCE, TF2Core.guiGun, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}

	}

}
