package tf2.event;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tf2.TFItems;
import tf2.items.guns.ItemTFGuns;

public class TFAnvilEvent
{
	//@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent event)
	{
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();
		ItemStack ret = event.getOutput();

		/* Moldのレシピ登録 */
		//		if (!right.isEmpty())
		//		{
		//			if (!left.isEmpty() && left.getItem() instanceof ItemBaseLevelUp && right.getItem() == TFItems.INSTALLER && ret.isEmpty())
		//			{
		//				System.out.println("a");
		//				NBTTagCompound tag = new NBTTagCompound();
		//
		//				if (left.hasTagCompound())
		//				{
		//					NBTTagCompound tag2 = left.getTagCompound();
		//					if (tag2 != null && tag2.hasKey("tf.level"))
		//					{
		//						int level = tag2.getInteger("tf.level");
		//
		//						if(level < 10)
		//						{
		//							ItemStack next = new ItemStack(left.getItem());
		//							tag.setInteger("tf.level", level + 1);
		//							next.setTagCompound(tag);
		//
		//							event.setOutput(next);
		//							event.setCost(level * 3);
		//							event.setMaterialCost(1);
		//		            	}
		//					}
		//
		//				}
		//				else
		//				{
		//					ItemStack next = new ItemStack(left.getItem());
		//					tag.setInteger("tf.level", 1);
		//					next.setTagCompound(tag);
		//
		//					event.setOutput(next);
		//					event.setCost(3);
		//					event.setMaterialCost(1);
		//				}
		//			}
		//		}
		//	}

		if (!right.isEmpty())
		{
			if (!left.isEmpty() && left.getItem() instanceof ItemTFGuns && right.getItem() == TFItems.UPGRADE_0 && ret.isEmpty())
			{
				System.out.println("a");
				NBTTagCompound tag = new NBTTagCompound();

				NBTTagCompound tag2 = left.getTagCompound();
				if (tag2 != null && tag2.hasKey("tf.level"))
				{
					int level = tag2.getInteger("tf.level");

					if (level < 3)
					{
						ItemStack next = new ItemStack(left.getItem());
						tag.setInteger("tf.level", level + 1);
						next.setTagCompound(tag);

						event.setOutput(next);
						event.setCost(3 + (level * 3));
						event.setMaterialCost(1);
					}
				}
				else
				{
					ItemStack next = new ItemStack(left.getItem());
					tag.setInteger("tf.level", 1);
					next.setTagCompound(tag);

					event.setOutput(next);
					event.setCost(3);
					event.setMaterialCost(1);
				}
			}
			if (!left.isEmpty() && left.getItem() instanceof ItemTFGuns && right.getItem() == TFItems.UPGRADE_1 && ret.isEmpty())
			{
				System.out.println("a");
				NBTTagCompound tag = new NBTTagCompound();

				NBTTagCompound tag2 = left.getTagCompound();
				if (tag2 != null && tag2.hasKey("tf.level"))
				{
					int level = tag2.getInteger("tf.level");

					if (3 <= level && level < 7)
					{
						ItemStack next = new ItemStack(left.getItem());
						tag.setInteger("tf.level", level + 1);
						next.setTagCompound(tag);

						event.setOutput(next);
						event.setCost(3 + (level * 3));
						event.setMaterialCost(1);
					}
				}
			}
			if (!left.isEmpty() && left.getItem() instanceof ItemTFGuns && right.getItem() == TFItems.UPGRADE_2 && ret.isEmpty())
			{
				System.out.println("a");
				NBTTagCompound tag = new NBTTagCompound();

				NBTTagCompound tag2 = left.getTagCompound();
				if (tag2 != null && tag2.hasKey("tf.level"))
				{
					int level = tag2.getInteger("tf.level");

					if (7 <= level && level < 10)
					{
						ItemStack next = new ItemStack(left.getItem());
						tag.setInteger("tf.level", level + 1);
						next.setTagCompound(tag);

						event.setOutput(next);
						event.setCost(3 + (level * 3));
						event.setMaterialCost(1);
					}
				}
			}
		}
	}

	//	@SubscribeEvent
	//	public void onCraftingEvent(PlayerEvent.ItemCraftedEvent event)
	//	{
	//		EntityPlayer player = event.player;
	//		IInventory matrix = event.craftMatrix;
	//		ItemStack craft = event.crafting;
	//
	//		if (craft.getItem() instanceof ItemBaseLevelUp)
	//		{
	//			int count = 0;
	//			int k = 0;
	//
	//			NBTTagCompound tag = new NBTTagCompound();
	//			for (int i = 0; i < matrix.getSizeInventory(); i++)
	//			{
	//				ItemStack check = matrix.getStackInSlot(i);
	//				if(check != null && check.getItem() == TFItems.INSTALLER)
	//				{
	//					count = 1;
	//				}
	//				if(check != null && check.getItem() instanceof ItemBaseLevelUp)
	//				{
	//					NBTTagCompound tag2 = check.getTagCompound();
	//					if (tag2 != null && tag2.hasKey("tf.weaponLevel"))
	//					{
	//						k = tag2.getInteger("tf.weaponLevel");
	//					}
	//				}
	//
	////				if (OreDictionary.itemMatches(craft, check, true))
	////				{
	////					NBTTagCompound tag2 = check.getTagCompound();
	////					if (tag2 != null && tag2.hasKey("dcs.itemdam"))
	////					{
	////						count += tag2.getInteger("dcs.itemdam");
	////					}
	////				}
	//			}
	//			System.out.println(count + k);
	//			if(count > 0)
	//			{
	//				tag.setInteger("tf.weaponLevel", count + k);
	//				craft.setTagCompound(tag);
	//			}
	//		}
	//	}
}
