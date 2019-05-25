package tf2.tile.tileentity;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
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
import tf2.TFItems;
import tf2.recipes.CokeOvenRecipes;

public class TileEntityCokeOven extends TileEntity implements ITickable, ISidedInventory
{
	public int cookTime1;
	public int cookTime2;
	public int cookTime3;
	public int cookTime4;
	public int cookTime5;
	public int cookTime6;
	public int cookTime7;
	public int cookTime8;

	public int burnStack;
	public int coalStack;

	private static final int[] slots = new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};

	private NonNullList<ItemStack> furnaceItemStacks = NonNullList.<ItemStack> withSize(18, ItemStack.EMPTY);

	// NBTの実装
	//---------------------------------------------------------------------------------------
	/*
	 * NBT(Named By Tag)の読み込み.
	 * TileEntityやEntity, ItemStackのように実行中にインスタンスを生成するようなクラスはフィールドを別途保存しておく必要がある.
	 * そのためにNBTという形式で保存/読み込みをしている.
	 */

	/*
	 * フィールドをNBTから読み込むメソッド.
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.furnaceItemStacks = NonNullList.<ItemStack> withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.furnaceItemStacks);
		this.cookTime1 = compound.getShort("CookTime1");
        this.cookTime2 = compound.getShort("CookTime2");
        this.cookTime3 = compound.getShort("CookTime3");
        this.cookTime4 = compound.getShort("CookTime4");
        this.cookTime5 = compound.getShort("CookTime5");
        this.cookTime6 = compound.getShort("CookTime6");
        this.cookTime7 = compound.getShort("CookTime7");
        this.cookTime8 = compound.getShort("CookTime8");
        this.burnStack = compound.getShort("BurnStack");
        this.coalStack = compound.getShort("CoalStack");
	}

	/*
	 * フィールドの保存のためにNBTに変換するメソッド.
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setShort("CookTime1", (short)this.cookTime1);
		compound.setShort("CookTime2", (short)this.cookTime2);
		compound.setShort("CookTime3", (short)this.cookTime3);
		compound.setShort("CookTime4", (short)this.cookTime4);
		compound.setShort("CookTime5", (short)this.cookTime5);
		compound.setShort("CookTime6", (short)this.cookTime6);
		compound.setShort("CookTime7", (short)this.cookTime7);
		compound.setShort("CookTime8", (short)this.cookTime8);
		compound.setShort("BurnStack", (short)this.burnStack);
		compound.setShort("CoalStack", (short)this.coalStack);
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
	public int getCookProgressScaled1(int par1)
	{
		return this.cookTime1 * par1 / 2000;
	}
	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled2(int par1)
	{
		return this.cookTime2 * par1 / 2000;
	}
	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled3(int par1)
	{
		return this.cookTime3 * par1 / 2000;
	}
	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled4(int par1)
	{
		return this.cookTime4 * par1 / 2000;
	}
	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled5(int par1)
	{
		return this.cookTime5 * par1 / 2000;
	}
	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled6(int par1)
	{
		return this.cookTime6 * par1 / 2000;
	}
	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled7(int par1)
	{
		return this.cookTime7 * par1 / 2000;
	}
	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled8(int par1)
	{
		return this.cookTime8 * par1 / 2000;
	}
	@SideOnly(Side.CLIENT)
	public int getStackProgressScaled1(int par1)
	{
		return this.burnStack * par1 / 12000;
	}
	@SideOnly(Side.CLIENT)
	public int getStackProgressScaled2(int par1)
	{
		return this.coalStack * par1 / 18000;
	}

	@Override
	public void update()
    {
        boolean flag1 = false;

        if (!this.world.isRemote)
        {
        	int i = 0;

        	if (this.canSmelt(0))
            {
        		++this.cookTime1;
                if (this.canSmelt2()){++this.burnStack;}
                if (this.canSmelt3()){++this.coalStack;}

                if (this.cookTime1 >= 2000)
                {
                    this.cookTime1 = 0;
                    this.smeltItem(0);
                    flag1 = true;
                }
            }
        	else
        	{
        		this.cookTime1 = 0;
        	}
        	if (this.canSmelt(1))
            {
        		 ++this.cookTime2;
                if (this.canSmelt2()){++this.burnStack;}
                if (this.canSmelt3()){++this.coalStack;}

                if (this.cookTime2 >= 2000)
                {
                    this.cookTime2 = 0;
                    this.smeltItem(1);
                    flag1 = true;
                }
            }
        	else
        	{
        		this.cookTime2 = 0;
        	}
        	if (this.canSmelt(2))
            {
                ++this.cookTime3;
                if (this.canSmelt2()){++this.burnStack;}
                if (this.canSmelt3()){++this.coalStack;}

                if (this.cookTime3 >= 2000)
                {
                    this.cookTime3 = 0;
                    this.smeltItem(2);
                    flag1 = true;
                }
            }
        	else
        	{
        		this.cookTime3 = 0;
        	}
        	if (this.canSmelt(3))
            {
                ++this.cookTime4;
                if (this.canSmelt2()){++this.burnStack;}
                if (this.canSmelt3()){++this.coalStack;}

                if (this.cookTime4 >= 2000)
                {
                    this.cookTime4 = 0;
                    this.smeltItem(3);
                    flag1 = true;
                }
            }
        	else
        	{
        		this.cookTime4 = 0;
        	}
        	if (this.canSmelt(4))
            {
                ++this.cookTime5;
                if (this.canSmelt2()){++this.burnStack;}
                if (this.canSmelt3()){++this.coalStack;}

                if (this.cookTime5 >= 2000)
                {
                    this.cookTime5 = 0;
                    this.smeltItem(4);
                    flag1 = true;
                }
            }
        	else
        	{
        		this.cookTime5 = 0;
        	}
        	if (this.canSmelt(5))
            {
                ++this.cookTime6;
                if (this.canSmelt2()){++this.burnStack;}
                if (this.canSmelt3()){++this.coalStack;}

                if (this.cookTime6 >= 2000)
                {
                    this.cookTime6 = 0;
                    this.smeltItem(5);
                    flag1 = true;
                }
            }
        	else
        	{
        		this.cookTime6 = 0;
        	}
        	if (this.canSmelt(6))
            {
                ++this.cookTime7;
                if (this.canSmelt2()){++this.burnStack;}
                if (this.canSmelt3()){++this.coalStack;}

                if (this.cookTime7 >= 2000)
                {
                    this.cookTime7 = 0;
                    this.smeltItem(6);
                    flag1 = true;
                }
            }
        	else
        	{
        		this.cookTime7 = 0;
        	}
        	if (this.canSmelt(7))
            {
                ++this.cookTime8;
                if (this.canSmelt2()){++this.burnStack;}
                if (this.canSmelt3()){++this.coalStack;}

                if (this.cookTime8 >= 2000)
                {
                    this.cookTime8 = 0;
                    this.smeltItem(7);
                    flag1 = true;
                }
            }
        	else
        	{
        		this.cookTime8 = 0;
        	}

        	if (this.burnStack >= 12000 && this.canSmelt2())
            {
                this.burnStack = 0;
                this.smeltItem2();
                flag1 = true;
            }
        	if (this.coalStack >= 18000 && this.canSmelt3())
            {
                this.coalStack = 0;
                this.smeltItem3();
                flag1 = true;
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }


	private boolean canSmelt(int index)
	{
		if (((ItemStack) this.furnaceItemStacks.get(index)).isEmpty())
		{
			return false;
		}
		else
		{
			ItemStack itemstack = CokeOvenRecipes.instance().getSmeltingResult(this.furnaceItemStacks.get(index));

			if (itemstack.isEmpty())return false;

			for(int i = 8; i < 16; i++)
			{
				ItemStack itemstack1 = this.furnaceItemStacks.get(i);
				if (itemstack1.isEmpty()) return true;
				if(!itemstack1.isEmpty() && itemstack1.isItemEqual(itemstack) && itemstack1.getCount() < itemstack.getMaxStackSize())
				{
					int result = itemstack1.getCount() + itemstack.getCount();
					return (result <= this.getInventoryStackLimit() && result <= itemstack.getMaxStackSize());
				}
			}
		}
		return false;
	}

	public void smeltItem(int index)
	{
		if (this.canSmelt(index))
		{
			ItemStack itemstack = this.furnaceItemStacks.get(index);
			ItemStack itemstack1 = CokeOvenRecipes.instance().getSmeltingResult(itemstack);
			for (int i = 8; i < 16; i++)
			{
				ItemStack itemstack2 = this.furnaceItemStacks.get(i);

				if (itemstack2.isEmpty())
				{
					this.furnaceItemStacks.set(i, itemstack1.copy());
					break;
				}
				if (itemstack2.getItem() == itemstack1.getItem() && itemstack2.getCount() < itemstack1.getMaxStackSize())
				{
					itemstack2.grow(itemstack1.getCount());
					break;
				}
			}

			itemstack.shrink(1);

			if (itemstack.getCount() <= 0)
            {
				itemstack.isEmpty();
            }
		}
	}

	private boolean canSmelt2()
	{
		ItemStack itemstack = new ItemStack(TFItems.WASTE_OIL);
		ItemStack itemstack1 = this.furnaceItemStacks.get(16);
		if (itemstack1.isEmpty()) return true;
		if (!itemstack1.isItemEqual(itemstack)) return false;
		int result = itemstack1.getCount() + itemstack.getCount();
		return result <= getInventoryStackLimit() && result <= itemstack1.getMaxStackSize();
	}


	public void smeltItem2()
    {
		if (this.canSmelt2())
        {
			ItemStack itemstack = new ItemStack(TFItems.WASTE_OIL);

			ItemStack itemstack2 = this.furnaceItemStacks.get(16);
			if (itemstack2.isEmpty())
			{
				this.furnaceItemStacks.set(16, itemstack.copy());
			}
			else if (itemstack2.getItem() == itemstack.getItem() && itemstack2.getCount() < itemstack.getMaxStackSize())
			{
				itemstack2.grow(itemstack.getCount());
			}

        }
    }

	private boolean canSmelt3()
	{
		ItemStack itemstack = new ItemStack(TFItems.COALTAR);
		ItemStack itemstack1 = this.furnaceItemStacks.get(17);
		if (itemstack1.isEmpty()) return true;
		if (!itemstack1.isItemEqual(itemstack)) return false;
		int result = itemstack1.getCount() + itemstack.getCount();
		return result <= getInventoryStackLimit() && result <= itemstack1.getMaxStackSize();
	}

	public void smeltItem3()
    {
		if (this.canSmelt2())
        {
			ItemStack itemstack = new ItemStack(TFItems.COALTAR);

			ItemStack itemstack2 = this.furnaceItemStacks.get(17);
			if (itemstack2.isEmpty())
			{
				this.furnaceItemStacks.set(17, itemstack.copy());
			}
			else if (itemstack2.getItem() == itemstack.getItem() && itemstack2.getCount() < itemstack.getMaxStackSize())
			{
				itemstack2.grow(itemstack.getCount());
			}

        }
    }

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

	/*
	 * InventoryにItemStackを入れるメソッド.
	 * 引数は(スロット番号, そのスロットに入れるItemStack)
	 */
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
		if (index >= 8)
		{
			return false;
		}
		else
		{
			return !CokeOvenRecipes.instance().getSmeltingResult(stack).isEmpty();
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
        return index >= 8 && index <= 17;
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
                return this.burnStack;
            case 1:
                return this.cookTime1;
            case 2:
                return this.cookTime2;
            case 3:
                return this.cookTime3;
            case 4:
                return this.cookTime4;
            case 5:
                return this.cookTime5;
            case 6:
                return this.cookTime6;
            case 7:
                return this.cookTime7;
            case 8:
                return this.cookTime8;
            case 9:
                return this.coalStack;
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
                this.burnStack = value;
                break;
            case 1:
                this.cookTime1 = value;
                break;
            case 2:
                this.cookTime2 = value;
                break;
            case 3:
                this.cookTime3 = value;
                break;
            case 4:
                this.cookTime4 = value;
                break;
            case 5:
                this.cookTime5 = value;
                break;
            case 6:
                this.cookTime6 = value;
                break;
            case 7:
                this.cookTime7 = value;
                break;
            case 8:
                this.cookTime8 = value;
                break;
            case 9:
                this.coalStack = value;
                break;
        }
    }

	@Override
	public int getFieldCount()
    {
        return 10;
    }

	@Override
	public void clear()
	{
		this.furnaceItemStacks.clear();
	}

	@Override
	public String getName()
	{
		return "gui.cokeoven";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}
}
