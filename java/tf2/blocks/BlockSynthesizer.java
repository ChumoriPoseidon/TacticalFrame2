package tf2.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tf2.TF2Core;
import tf2.tile.tileentity.TileEntitySynthesizer;

public class BlockSynthesizer extends BlockContainerFacingBase
{
	public BlockSynthesizer(String name)
	{
		super(name);
	}
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		else
		{
			playerIn.openGui(TF2Core.INSTANCE, TF2Core.guiSynthesizer, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntitySynthesizer();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntitySynthesizer)
		{
			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntitySynthesizer) tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
		}
		super.breakBlock(worldIn, pos, state);
	}
}
