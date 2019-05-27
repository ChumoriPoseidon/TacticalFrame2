package tf2.tile.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tf2.TFItems;
import tf2.tile.container.ContainerMachineStation;


@SideOnly(Side.CLIENT)
public class GuiMachineStation extends GuiContainer
{
    private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation("tf2:textures/gui/container/machinestation.png");

    private int updateCount;

    private GuiRecipeButton nextRecipeButton;
    private GuiRecipeButton previousRecipeButton;
    private int currentRecipeIndex;
    private ItemStack itemid;

    public GuiMachineStation(InventoryPlayer playerInv, World worldIn, BlockPos blockPosition)
    {
    	super(new ContainerMachineStation(playerInv, worldIn, blockPosition));
        this.xSize = 244;
		this.ySize = 210;
    }
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString(I18n.format("gui.machinestation", new Object[0]), 8, 6, 0xFFFF00);
        this.fontRenderer.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 0xFFFF00);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        ++this.updateCount;

        this.nextRecipeButton.enabled = this.currentRecipeIndex < 8;
        this.previousRecipeButton.enabled = this.currentRecipeIndex > 0;
    }
    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        boolean flag = false;

        if (par1GuiButton == this.nextRecipeButton)
        {
            ++this.currentRecipeIndex;
            flag = true;
        }
        else if (par1GuiButton == this.previousRecipeButton)
        {
            --this.currentRecipeIndex;
            flag = true;
        }

        if (flag)
        {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

            try
            {
                dataoutputstream.writeInt(this.currentRecipeIndex);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
    }
    @Override
    public void initGui()
	{
		   super.initGui();
	        int i = (this.width - this.xSize) / 2;
	        int j = (this.height - this.ySize) / 2;
	        this.allowUserInput = true;
	        this.buttonList.add(this.nextRecipeButton = new GuiRecipeButton(1, i + 225, j + 113, true));
	        this.buttonList.add(this.previousRecipeButton = new GuiRecipeButton(2, i + 183, j + 113, false));
	        this.currentRecipeIndex = 0;
	}


    @Override
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int xMouse, int yMouse)
	{
		//GL11.glPushMatrix();
    	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
		int x = (this.width  - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	        RenderHelper.enableGUIStandardItemLighting();
	        GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	        GL11.glEnable(GL11.GL_COLOR_MATERIAL);

	        itemRender.zLevel = 100.0F;

	    ItemStack item0 = this.itemid;
	    ItemStack item1 = this.itemid;
	    ItemStack item2 = this.itemid;
	    ItemStack item3 = this.itemid;
	    ItemStack item4 = this.itemid;
	    ItemStack item5 = this.itemid;
	    ItemStack item6 = this.itemid;
	    ItemStack item7 = this.itemid;
	    ItemStack item8 = this.itemid;
	    ItemStack item9 = this.itemid;

	    ItemStack itemnull = ItemStack.EMPTY;

	    int i = this.currentRecipeIndex;

        switch (i)
        {
        case 0:
        	item0 = itemnull;
        	item1 = new ItemStack(Items.IRON_INGOT);
			item2 = new ItemStack(Items.IRON_INGOT);
			item3 = new ItemStack(Items.IRON_INGOT);
			item4 = new ItemStack(Items.FLINT);
			item5 = new ItemStack(Blocks.CRAFTING_TABLE);
			item6 = new ItemStack(Items.FLINT);
			item7 = new ItemStack(Items.IRON_INGOT);
			item8 = new ItemStack(Items.IRON_INGOT);
			item9 = new ItemStack(Items.IRON_INGOT);
			break;
        case 1:
        	item0 = itemnull;
        	item1 = new ItemStack(Items.IRON_INGOT);
			item2 = new ItemStack(Items.IRON_INGOT);
			item3 = new ItemStack(Items.IRON_INGOT);
			item4 = new ItemStack(Items.IRON_INGOT);
			item5 = itemnull;
			item6 = new ItemStack(Items.IRON_INGOT);
			item7 = new ItemStack(Items.IRON_INGOT);
			item8 = new ItemStack(Items.IRON_INGOT);
			item9 = new ItemStack(Items.IRON_INGOT);
			break;
        case 2:
        	item0 = itemnull;
        	item1 = new ItemStack(Items.IRON_INGOT);
			item2 = new ItemStack(Items.IRON_INGOT);
			item3 = new ItemStack(Items.IRON_INGOT);
			item4 = new ItemStack(Items.IRON_INGOT);
			item5 = itemnull;
			item6 = new ItemStack(Items.IRON_INGOT);
			item7 = new ItemStack(Items.IRON_INGOT);
			item8 = new ItemStack(Items.IRON_INGOT);
			item9 = new ItemStack(Items.IRON_INGOT);
			break;
        case 3:
        	item0 = new ItemStack(TFItems.BOX_RIFLE);
        	item1 = itemnull;
        	item2 = itemnull;
        	item3 = itemnull;
        	item4 = itemnull;
        	item5 = new ItemStack(Items.GUNPOWDER);
			item6 = new ItemStack(Items.IRON_INGOT);
			item7 = itemnull;
			item8 = itemnull;
			item9 = itemnull;
			break;
        case 4:
        	item0 = new ItemStack(TFItems.BOX_SHOT);
        	item1 = itemnull;
        	item2 = itemnull;
        	item3 = itemnull;
			item4 = new ItemStack(Items.PAPER);
			item5 = new ItemStack(Items.GUNPOWDER);
			item6 = new ItemStack(Items.IRON_INGOT);
			item7 = itemnull;
			item8 = itemnull;
			item9 = itemnull;
			break;
        case 5:
        	item0 = new ItemStack(TFItems.BOX_SNIPER);
        	item1 = itemnull;
        	item2 = itemnull;
        	item3 = itemnull;
			item4 = new ItemStack(Items.IRON_INGOT);
			item5 = new ItemStack(Items.GUNPOWDER);
			item6 = new ItemStack(Items.IRON_INGOT);
			item7 = itemnull;
			item8 = itemnull;
			item9 = itemnull;
			break;
        case 6:
        	item0 = new ItemStack(TFItems.BOX_GRENADE);
        	item1 = itemnull;
        	item2 = itemnull;
        	item3 = itemnull;
			item4 = new ItemStack(Items.IRON_INGOT);
			item5 = new ItemStack(Items.IRON_INGOT);
			item6 = new ItemStack(Items.IRON_INGOT);
			item7 = itemnull;
			item8 = itemnull;
			item9 = itemnull;
			break;
        case 7:
        	item0 = new ItemStack(Items.IRON_INGOT);
        	item1 = itemnull;
			item2 = new ItemStack(Items.IRON_INGOT);
			item3 = itemnull;
			item4 = new ItemStack(Items.IRON_INGOT);
			item5 = itemnull;
			item6 = new ItemStack(Items.IRON_INGOT);
			item7 = itemnull;
			item8 = new ItemStack(Items.IRON_INGOT);
			item9 = itemnull;
			break;
        case 8:
        	item0 = new ItemStack(Items.IRON_INGOT);
        	item1 = itemnull;
			item2 = new ItemStack(Items.GOLD_INGOT);
			item3 = itemnull;
			item4 = new ItemStack(Items.GOLD_INGOT);
			item5 = itemnull;
			item6 = new ItemStack(Items.GOLD_INGOT);
			item7 = itemnull;
			item8 = new ItemStack(Items.GOLD_INGOT);
			item9 = itemnull;
			break;
        }

		itemRender.renderItemAndEffectIntoGUI(item0, x + 202, y + 17);

        itemRender.renderItemAndEffectIntoGUI(item1, x + 184, y + 57);
        itemRender.renderItemAndEffectIntoGUI(item2, x + 202, y + 57);
        itemRender.renderItemAndEffectIntoGUI(item3, x + 220, y + 57);

        itemRender.renderItemAndEffectIntoGUI(item4, x + 184, y + 75);
        itemRender.renderItemAndEffectIntoGUI(item5, x + 202, y + 75);
        itemRender.renderItemAndEffectIntoGUI(item6, x + 220, y + 75);

        itemRender.renderItemAndEffectIntoGUI(item7, x + 184, y + 93);
        itemRender.renderItemAndEffectIntoGUI(item8, x + 202, y + 93);
        itemRender.renderItemAndEffectIntoGUI(item9, x + 220, y + 93);
		itemRender.zLevel = 0.0F;
		//GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();


//        if (item0 != null && this.func_146978_c(32, 32, 16, 16, xMouse, yMouse))
//        {
//            this.renderToolTip(item0, xMouse, yMouse);
//        }
//
//        if (item5 != null && this.func_146978_c(32, 96, 16, 16, xMouse, yMouse))
//        {
//            this.renderToolTip(item5, xMouse, yMouse);
//        }
	}

    @Override
	public void drawScreen(int x, int y, float par3)
	{
		this.drawDefaultBackground();
		super.drawScreen(x, y, par3);
		this.renderHoveredToolTip(x, y);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
        this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
        int x1 = (this.width  - this.xSize) / 2;
		int y1 = (this.height - this.ySize) / 2;

        RenderHelper.enableStandardItemLighting();
	}

	   @SideOnly(Side.CLIENT)
	    static class GuiRecipeButton extends GuiButton
	        {
	            private final boolean field_146157_o;

	            public GuiRecipeButton(int p_i1095_1_, int p_i1095_2_, int p_i1095_3_, boolean p_i1095_4_)
	            {
	                super(p_i1095_1_, p_i1095_2_, p_i1095_3_, 12, 17, "");
	                this.field_146157_o = p_i1095_4_;
	            }

	            /**
	             * Draws this button to the screen.
	             */
	            public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
	            {
	                if (this.visible)
	                {
	                    p_146112_1_.getTextureManager().bindTexture(craftingTableGuiTextures);
	                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	                    boolean flag = p_146112_2_ >= this.x && p_146112_3_ >= this.y && p_146112_2_ < this.x + this.width && p_146112_3_ < this.y + this.height;
	                    int k = 222;
	                    int l = 0;

	                    if (!this.enabled)
	                    {
	                        l += this.width * 2;
	                    }
	                    else if (flag)
	                    {
	                        l += this.width;
	                    }

	                    if (!this.field_146157_o)
	                    {
	                        k += this.height;
	                    }

	                    this.drawTexturedModalRect(this.x, this.y, l, k, this.width, this.height);
	                }
	            }
	        }
}