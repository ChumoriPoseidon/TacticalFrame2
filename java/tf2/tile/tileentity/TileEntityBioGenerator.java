package tf2.tile.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tf2.recipes.BioGeneratorRecipes;

public class TileEntityBioGenerator extends TileEntity implements ITickable, ISidedInventory
{
	public int burnTime;

	public int currentItemBurnTime;

	public int burnTime2;
	public int currentItemBurnTime2;
	public int burnTime3;
	public int currentItemBurnTime3;

	//調理時間
	public int cookTime;

	private static final int[] slots = new int[] {0,1,2,3,4};

	private NonNullList<ItemStack> furnaceItemStacks = NonNullList.<ItemStack> withSize(5, ItemStack.EMPTY);

	/*
	 * フィールドをNBTから読み込むメソッド.
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.furnaceItemStacks = NonNullList.<ItemStack> withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.furnaceItemStacks);
		this.burnTime = compound.getInteger("BurnTime");
		this.burnTime2 = compound.getShort("BurnTime2");
		this.burnTime3 = compound.getShort("BurnTime3");
		this.cookTime = compound.getInteger("CookTime");
		this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks.get(1));
		this.currentItemBurnTime2 = getItemBurnTime(this.furnaceItemStacks.get(2));
		this.currentItemBurnTime3 = getItemBurnTime(this.furnaceItemStacks.get(3));
	}


	/*
	 * フィールドの保存のためにNBTに変換するメソッド.
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("BurnTime", (short) this.burnTime);
		compound.setShort("BurnTime2", (short)this.burnTime2);
		compound.setShort("BurnTime3", (short)this.burnTime3);
		compound.setShort("CookTime", (short)this.cookTime);
		ItemStackHelper.saveAllItems(compound, this.furnaceItemStacks);
		return compound;
	}

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		this.writeToNBT(nbtTagCompound);
		return new SPacketUpdateTileEntity(pos, -50, nbtTagCompound);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	//矢印の描画
		@SideOnly(Side.CLIENT)
		public int getCookProgressScaled(int par1)
		{
			return this.cookTime * par1 / 30000;
		}
		//燃焼の描画
		@SideOnly(Side.CLIENT)
		public int getBurnTimeRemainingScaled(int par1)
		{
			if (this.currentItemBurnTime == 0)
			{
				this.currentItemBurnTime = 200;
			}
			return this.burnTime * par1 / this.currentItemBurnTime;
		}
		@SideOnly(Side.CLIENT)
		public int getBurnTimeRemainingScaled2(int par1)
		{
			if (this.currentItemBurnTime2 == 0)
			{
				this.currentItemBurnTime2 = 200;
			}
			return this.burnTime2 * par1 / this.currentItemBurnTime2;
		}
		@SideOnly(Side.CLIENT)
		public int getBurnTimeRemainingScaled3(int par1)
		{
			if (this.currentItemBurnTime3 == 0)
			{
				this.currentItemBurnTime3 = 200;
			}
			return this.burnTime3 * par1 / this.currentItemBurnTime3;
		}
		public boolean isBurning()
		{
			return this.burnTime > 0 || this.burnTime2 > 0 || this.burnTime3 > 0;
		}
	@Override
	public void update()
    {
		boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.burnTime > 0)
        {
            --this.burnTime;
        }
        if (this.burnTime2 > 0)
        {
            --this.burnTime2;
        }
        if (this.burnTime3 > 0)
        {
            --this.burnTime3;
        }

        if (this.cookTime > 30000)
		 {
			 this.cookTime = 30000;
		 }

        if (!this.world.isRemote)
        {
        	ItemStack itemstack1 = this.furnaceItemStacks.get(1);
            if (this.burnTime != 0 || !itemstack1.isEmpty())
            {
                if (this.burnTime == 0 && this.cookTime < 30000)
                {
                	this.currentItemBurnTime = this.burnTime = getItemBurnTime(itemstack1);

                    if (this.burnTime > 0)
                    {
                        flag1 = true;
                        if (!itemstack1.isEmpty())
                        {
                        	Item item = itemstack1.getItem();
							itemstack1.shrink(1);

							if (itemstack1.isEmpty())
							{
								ItemStack item1 = item.getContainerItem(itemstack1);
								this.furnaceItemStacks.set(1, item1);
							}
                        }
                    }
                }
                if (this.isBurning() && this.cookTime <= 30000)
                {
                    ++this.cookTime;
                }
            }
            ItemStack itemstack2 = this.furnaceItemStacks.get(2);
            if (this.burnTime2 != 0 || !itemstack2.isEmpty())
            {
                if (this.burnTime2 == 0 && this.cookTime < 30000)
                {
                    this.currentItemBurnTime2 = this.burnTime2 = getItemBurnTime(itemstack2);

                    if (this.burnTime2 > 0)
                    {
                        flag1 = true;
                        if (!itemstack2.isEmpty())
                        {
                        	Item item = itemstack2.getItem();
							itemstack2.shrink(1);

							if (itemstack2.isEmpty())
							{
								ItemStack item1 = item.getContainerItem(itemstack2);
								this.furnaceItemStacks.set(2, item1);
							}
                        }
                    }
                }
                if (this.isBurning() && this.cookTime <= 30000)
                {
                    ++this.cookTime;
                }
            }
            ItemStack itemstack3 = this.furnaceItemStacks.get(3);
            if (this.burnTime3 != 0 || !itemstack3.isEmpty())
            {
                if (this.burnTime3 == 0 && this.cookTime < 30000)
                {
                    this.currentItemBurnTime3 = this.burnTime3 = getItemBurnTime(itemstack3);

                    if (this.burnTime3 > 0)
                    {
                        flag1 = true;
                        if (!itemstack3.isEmpty())
                        {
                        	Item item = itemstack3.getItem();
							itemstack3.shrink(1);

							if (itemstack3.isEmpty())
							{
								ItemStack item1 = item.getContainerItem(itemstack3);
								this.furnaceItemStacks.set(3, item1);
							}
                        }
                    }
                }
                if (this.isBurning() && this.cookTime <= 30000)
                {
                    ++this.cookTime;
                }
            }
            if (this.cookTime >= 300 && this.canSmelt())
            {
                this.cookTime = this.cookTime - 300;
                this.smeltItem();
                flag1 = true;
            }
            if (flag != this.burnTime > 0 || flag != this.burnTime2 > 0 || flag != this.burnTime3 > 0)
            {
                flag1 = true;
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }


	private boolean canSmelt()
	{
		if (((ItemStack) this.furnaceItemStacks.get(0)).isEmpty())
		{
			return false;
		}
		else
		{
			ItemStack itemstack = BioGeneratorRecipes.instance().getSmeltingResult(this.furnaceItemStacks.get(0));
			if (itemstack.isEmpty())return false;
			else
			{
				ItemStack itemstack1 = this.furnaceItemStacks.get(4);
				if (itemstack1.isEmpty()) return true;
				if (!itemstack1.isItemEqual(itemstack)) return false;
				int result = itemstack1.getCount() + itemstack.getCount();
				return result <= getInventoryStackLimit() && result <= itemstack1.getMaxStackSize();
			}
		}
	}
	public void smeltItem()
    {
        if (this.canSmelt())
        {
        	ItemStack itemstack0 = this.furnaceItemStacks.get(0);
			ItemStack itemstack1 = BioGeneratorRecipes.instance().getSmeltingResult(itemstack0);
			ItemStack itemstack2 = this.furnaceItemStacks.get(4);

			if (itemstack2.isEmpty())
			{
				this.furnaceItemStacks.set(4, itemstack1.copy());
			}
			else if (itemstack2.getItem() == itemstack1.getItem())
			{
				itemstack2.grow(itemstack1.getCount());
			}
			itemstack0.shrink(1);
        }
    }


	 public static int getItemBurnTime(ItemStack stack)
	    {
		 if (stack.isEmpty())
			{
				return 0;
			}
	        else
	        {
	            Item item = stack.getItem();

	            if (item == Item.getItemFromBlock(Blocks.MELON_BLOCK))
	            {
	                return 180;
	            }
	            else if (item == Item.getItemFromBlock(Blocks.PUMPKIN))
	            {
	                return 80;
	            }
	            else if (item == Item.getItemFromBlock(Blocks.PURPUR_BLOCK))
	            {
	                return 60;
	            }
	            else if (item == Item.getItemFromBlock(Blocks.HAY_BLOCK))
	            {
	                return 360;
	            }
	            else if (item == Item.getItemFromBlock(Blocks.DEADBUSH))
	            {
	                return 20;
	            }
	            else if (item == Item.getItemFromBlock(Blocks.TALLGRASS))
	            {
	                return 20;
	            }
	            else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.LEAVES)
	            {
	                return 30;
	            }
	            else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.CACTUS)
	            {
	                return 30;
	            }
	            else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.PLANTS)
	            {
	                return 30;
	            }
	            if (item == Item.getItemFromBlock(Blocks.SAPLING)) return 30;
	            if (item == Items.APPLE) return 40;
	            if (item == Items.CARROT) return 40;
	            if (item == Items.BEETROOT) return 40;
	            if (item == Items.MELON) return 20;
	            if (item == Items.POTATO) return 40;
	            if (item == Items.WHEAT) return 40;
	            if (item == Items.REEDS) return 40;
	            //if (item == TFCore.willow_branch) return 40;
	            if (item instanceof ItemSeeds)
	    		{
	            	return 20;
	    		}
	            return 0;
	        }
	    }
	  //かまどの処理
	  	public static boolean isItemFuel(ItemStack par0ItemStack)
	  	{
	  		return getItemBurnTime(par0ItemStack) > 0;
	  	}

	//IInventoryの実装
	//---------------------------------------------------------------------------------------
	/*
	 * IInventoryはインベントリ機能を提供するインタフェース.
	 * インベントリに必要なメソッドを適切にオーバーライドする.
	 */

	/*
	 * Inventoryの要素数を返すメソッド.
	 */
	@Override
	public int getSizeInventory()
	{
		return this.furnaceItemStacks.size();
	}


	/*
	 * スロットの中身を返すメソッド.
	 * 引数はスロット番号
	 */
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return this.furnaceItemStacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(this.furnaceItemStacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.furnaceItemStacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		ItemStack itemstack = this.furnaceItemStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.furnaceItemStacks.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit())
		{
			stack.setCount(this.getInventoryStackLimit());
		}
	}

	/*
	 * 1スロットあたりの最大スタック数
	 */
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	/*
	 * 主にContainerで利用する, GUIを開けるかどうかを判定するメソッド.
	 */
	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }
	@Override
	public boolean isEmpty()
	{
		for (ItemStack itemstack : this.furnaceItemStacks)
		{
			if (!itemstack.isEmpty())
			{
				return false;
			}
		}

		return true;
	}

	/*
	 * trueではHopperでアイテムを送れるようになる.
	 */

	@Override
	   public boolean isItemValidForSlot(int index, ItemStack stack)
	    {
	        if (index == 4)
	        {
	            return false;
	        }
	        else if (index == 0)
	        {
	        	return !BioGeneratorRecipes.instance().getSmeltingResult(stack).isEmpty();
	        }
	        else
	        {
	            ItemStack itemstack = this.furnaceItemStacks.get(1);
	            return isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && (itemstack == null || itemstack.getItem() != Items.BUCKET);
	        }
	    }
	@Override
	    public int[] getSlotsForFace(EnumFacing side)
	    {
	        return slots;
	    }

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return this.isItemValidForSlot(index, itemStackIn);
    }

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return index == 4 ;
    }

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.cookTime;
            case 1:
                return this.burnTime;
            case 2:
                return this.burnTime2;
            case 3:
                return this.burnTime3;
            case 4:
                return this.currentItemBurnTime;
            case 5:
                return this.currentItemBurnTime2;
            case 6:
                return this.currentItemBurnTime3;
            default:
                return 0;
        }
    }

	@Override
	 public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.cookTime = value;
                break;
            case 1:
                this.burnTime = value;
                break;
            case 2:
                this.burnTime2 = value;
                break;
            case 3:
                this.burnTime3 = value;
                break;
            case 4:
                this.currentItemBurnTime = value;
                break;
            case 5:
                this.currentItemBurnTime2 = value;
                break;
            case 6:
                this.currentItemBurnTime3 = value;
                break;
        }
    }

	@Override
	public int getFieldCount()
    {
        return 7;
    }

	@Override
	public void clear()
	{
		this.furnaceItemStacks.clear();
	}

	@Override
	public String getName()
	{
		return "gui.biogenerator";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}
}
