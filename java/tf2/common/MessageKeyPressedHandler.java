package tf2.common;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tf2.entity.mob.frend.EntityMobCF;
import tf2.items.guns.ItemTFGuns;

public class MessageKeyPressedHandler implements IMessageHandler<MessageKeyPressed, IMessage>
{
	@Override
	public IMessage onMessage(MessageKeyPressed message, MessageContext ctx)
	{
		EntityPlayer entityPlayer = ctx.getServerHandler().player;
		//受け取ったMessageクラスのkey変数の数字をチャットに出力

		if (message.key == 1)
		{
			ItemStack itemstack = ((EntityPlayer) (entityPlayer)).getHeldItemMainhand();
			if (itemstack != null && itemstack.getItem() instanceof ItemTFGuns)
			{
				int i = itemstack.getMaxDamage();
				itemstack.setItemDamage(i);
			}
		} /**/

//		if (message.key == 5)
//		{
//			EntityLivingBase en = (EntityLivingBase) entityPlayer.world.getEntityByID(message.target);
//			if (entityPlayer.getRidingEntity() instanceof EntityTank && entityPlayer.getRidingEntity() != null && entityPlayer.getRidingEntity() == en)
//			{
//				EntityTank base = (EntityTank) en;
//				base.server1 = true;
//			}
//		}
//		if (message.key == 6)
//		{
//			EntityLivingBase en = (EntityLivingBase) entityPlayer.world.getEntityByID(message.target);
//			if (entityPlayer.getRidingEntity() instanceof EntityTank && entityPlayer.getRidingEntity() != null && entityPlayer.getRidingEntity() == en)
//			{
//				EntityTank base = (EntityTank) en;
//				base.server2 = true;
//			}
//		}

		if (message.key == 10)
		{
			EntityLivingBase en = (EntityLivingBase) entityPlayer.world.getEntityByID(message.target);
			if (entityPlayer.getRidingEntity() instanceof EntityMobCF && entityPlayer.getRidingEntity() != null && entityPlayer.getRidingEntity() == en)
			{
				EntityMobCF base = (EntityMobCF) en;
				base.serverGetoff = true;
			}
		}
		if (message.key == 11)
		{
			EntityLivingBase en = (EntityLivingBase) entityPlayer.world.getEntityByID(message.target);
			if (entityPlayer.getRidingEntity() instanceof EntityMobCF && entityPlayer.getRidingEntity() != null && entityPlayer.getRidingEntity() == en)
			{
				EntityMobCF base = (EntityMobCF) en;
				base.serverLeftclick = true;
			}
		}
		if (message.key == 12)
		{
			EntityLivingBase en = (EntityLivingBase) entityPlayer.world.getEntityByID(message.target);
			if (entityPlayer.getRidingEntity() instanceof EntityMobCF && entityPlayer.getRidingEntity() != null && entityPlayer.getRidingEntity() == en)
			{
				EntityMobCF base = (EntityMobCF) en;
				base.serverRightclick = true;
			}
		}
		if (message.key == 13)
		{
			EntityLivingBase en = (EntityLivingBase) entityPlayer.world.getEntityByID(message.target);
			if (entityPlayer.getRidingEntity() instanceof EntityMobCF && entityPlayer.getRidingEntity() != null && entityPlayer.getRidingEntity() == en)
			{
				EntityMobCF base = (EntityMobCF) en;
				base.serverBoost = true;
			}
		}
		if (message.key == 14)
		{
			EntityLivingBase en = (EntityLivingBase) entityPlayer.world.getEntityByID(message.target);
			if (entityPlayer.getRidingEntity() instanceof EntityMobCF && entityPlayer.getRidingEntity() != null && entityPlayer.getRidingEntity() == en)
			{
				EntityMobCF base = (EntityMobCF) en;
				base.serverShift = true;
			}
		}

		return null;
	}
}