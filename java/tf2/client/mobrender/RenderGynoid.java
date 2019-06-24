package tf2.client.mobrender;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import objmodel.AdvancedModelLoader;
import objmodel.IModelCustom;
import tf2.client.model.ModelGynoid;
import tf2.entity.mob.frend.EntityGynoid;
import tf2.entity.mob.frend.EntityTF77B;
import tf2.entity.mob.frend.EntityTF78R;
import tf2.entity.mob.frend.EntityTF80G;

@SideOnly(Side.CLIENT)
public abstract class RenderGynoid<T extends EntityGynoid> extends RenderLiving<T>
{
    private static final ResourceLocation DEFAULT_RES_LOC = new ResourceLocation("tf2:textures/mob/gynoid.png");
	private static final ResourceLocation WEAPON_RES_LOC = new ResourceLocation("tf2:textures/mob/gynoid_weapon.png");
	private static final ResourceLocation WEAPON_LIGHT_RES_LOC = new ResourceLocation("tf2:textures/mob/gynoid_weapon_light.png");
	private static final IModelCustom WEAPON_OBJ = AdvancedModelLoader.loadModel(new ResourceLocation("tf2:textures/mob/tf77b_weapon.obj"));

	protected static ModelGynoid layerModel;

    public RenderGynoid(RenderManager renderManagerIn, ModelGynoid modelBipedIn, float shadowSize)
    {
        super(renderManagerIn, modelBipedIn, shadowSize);
        this.layerModel = modelBipedIn;
    }

	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
        GlStateManager.pushMatrix();
        this.bindEntityTexture(entity);
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

		this.bindTexture(this.getEntityWeaponTexture());

		GlStateManager.pushMatrix();

		if(entity.deathTime <= 0)
		{

			GlStateManager.translate((float) x, (float) y, (float) z);
			GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);
			GlStateManager.enableRescaleNormal();

			if(entity.getMechaMode() != 0 && !entity.isRiding())
			{
				this.renderWeapon(entity);
			}

	        float f4 = MathHelper.wrapDegrees(entity.rotationYawHead - entity.rotationYaw);

	        double d3 = Math.abs(f4);

	        if(d3 > 70F)
	        {
	        	entity.rotationYaw = this.interpolateRotation(entity.rotationYaw, entity.rotationYawHead, 0.05F);
	        }

	        double d0 = entity.posX - entity.prevPosX;
			double d1 = entity.posZ - entity.prevPosZ;
			float f = (float)(d0 * d0 + d1 * d1);
			if(f > 0F)
			{
				float tick = entity.ticksExisted;
				float t = (float)Math.sin(tick / ((float)Math.PI * 0.9662F)) * 0.1F;
				if(t < 0)
				{
					t = -(t);
				}
				GL11.glTranslatef(0F, 0.0F, 0F);

				entity.rotationYaw = entity.rotationYawHead;

				float k = 180.0F - entity.rotationYaw;

				GlStateManager.rotate((float)Math.cos(k), 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(-((float)Math.sin(k)), 0.0F, 0.0F, 1.0F);
			}

			this.renderHead(entity);

			GlStateManager.rotate(180 - entity.renderYawOffset, 0.0F, 1.0F, 0.0F);


			this.getEntityWeaponObj().renderPart("backGear");

			if(!entity.isRiding())
			{
				if(entity.getMechaMode() == 0)
				{

					this.renderLegGear(entity, entity.ticksExisted);
				}
				this.renderWing(entity, entity.ticksExisted);
			}

			this.bindTexture(this.getEntityWeaponLightTexture());

			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
	        float lastx = OpenGlHelper.lastBrightnessX;
	        float lasty = OpenGlHelper.lastBrightnessY;
	        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

			this.getEntityWeaponObj().renderPart("backGear");

			if(!entity.isRiding())
			{
				if(entity.getMechaMode() == 0)
				{
					this.renderLegGear(entity, entity.ticksExisted);
				}
				this.renderWing(entity, entity.ticksExisted);
			}

			if(entity instanceof EntityTF77B && entity.getMechaMode() != 0)
			{
				GlStateManager.pushMatrix();
				GlStateManager.translate(-0.0F, 0F, 0F);
				GlStateManager.translate(0F, 0F, 0.0F);
				GlStateManager.rotate(entity.ticksExisted * 7.5F, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0.0F, 0F, 0F);
				GlStateManager.translate(0F, 0F, -0.0F);
				this.getEntityWeaponObj().renderPart("gear_ring");
				GlStateManager.popMatrix();

				this.renderPanel(entity, entity.ticksExisted);
			}

			GlStateManager.rotate(180 - entity.renderYawOffset, 0.0F, -1.0F, 0.0F);

			this.renderHead(entity);

			if(entity.getMechaMode() != 0 && !entity.isRiding())
			{
				this.renderWeapon(entity);
			}
	        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastx, lasty);
			OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
			GlStateManager.enableLighting();

			GlStateManager.disableRescaleNormal();
		}
		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	public void renderPanel(EntityLivingBase entity, int tick)
	{
		float t = entity.ticksExisted;

		GlStateManager.pushMatrix();
		t = (float) Math.sin((tick - 0.25) / ((float) Math.PI * 5F)) * 0.2F;
		GlStateManager.translate(0F, (t / 1.5) + 0.025F, 0F);
		this.getEntityWeaponObj().renderPart("gear_panel_1");
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		t = (float) Math.sin((tick + 0.25) / ((float) Math.PI * 5F)) * 0.2F;
		GlStateManager.translate(0F, -(t / 1.5) + 0.025F, 0F);
		this.getEntityWeaponObj().renderPart("gear_panel_2");
		GlStateManager.popMatrix();
	}

	public void renderHead(EntityLivingBase entity)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(180.0F - entity.rotationYawHead, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(entity.rotationPitch, 1.0F, 0.0F, 0.0F);
		GlStateManager.translate(0.0F, -1.0F, 0.0F);
		this.getEntityWeaponObj().renderPart("right_headGear");
		this.getEntityWeaponObj().renderPart("left_headGear");
		GlStateManager.popMatrix();
	}

	public void renderLegGear(EntityLivingBase entity, int tick)
	{
		GlStateManager.pushMatrix();
		float t = (float) Math.sin(tick / ((float) Math.PI * 5F)) * 0.2F;
		GlStateManager.translate(0F, (t / 2) + 0.05F, 0F);
		this.getEntityWeaponObj().renderPart("right_leggear_1");
		this.getEntityWeaponObj().renderPart("left_leggear_1");
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0F, -(t / 4) + 0.025F, 0F);
		this.getEntityWeaponObj().renderPart("right_leggear_2");
		this.getEntityWeaponObj().renderPart("left_leggear_2");
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0F, -(t / 1.5) + 0.025F, 0F);
		this.getEntityWeaponObj().renderPart("right_cube");
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		t = (float) Math.sin((tick + 0.25) / ((float) Math.PI * 5F)) * 0.2F;
		GlStateManager.translate(0F, (t / 1.5) + 0.025F, 0F);
		this.getEntityWeaponObj().renderPart("left_cube");
		if(entity instanceof EntityTF77B && ((EntityTF77B) entity).getMechaMode() != 0)
		{
			this.getEntityWeaponObj().renderPart("gear_panel_2");
		}
		GlStateManager.popMatrix();
	}

	public void renderWing(EntityLivingBase entity, int tick)
	{
		GlStateManager.pushMatrix();
		float t = (float) Math.sin(tick / ((float) Math.PI * 5F)) * 0.2F;
		GlStateManager.translate(0F, 1.31F, 0F);
		GlStateManager.rotate((t * 10), 0.0F, 0.0F, 1.0F);
		GlStateManager.translate(0F, -1.31F, 0F);
		this.getEntityWeaponObj().renderPart("right_wing");
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0F, 1.31F, 0F);
		GlStateManager.rotate((t * 10), 0.0F, 0.0F, -1.0F);
		GlStateManager.translate(0F, -1.31F, 0F);
		this.getEntityWeaponObj().renderPart("left_wing");
		GlStateManager.popMatrix();
	}

	public void renderWeapon(T entity)
	{

		GlStateManager.pushMatrix();
		GlStateManager.rotate(180 - entity.renderYawOffset, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate((this.layerModel.bipedRightArm.rotationPointX / 10) / 1.375, (this.layerModel.bipedRightArm.rotationPointY / 10) / 1.625, this.layerModel.bipedRightArm.rotationPointZ / 10);

		GlStateManager.rotate(this.layerModel.bipedRightArm.rotateAngleX, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(this.layerModel.bipedRightArm.rotateAngleY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(this.layerModel.bipedRightArm.rotateAngleZ, 0.0F, 0.0F, -1.0F);

		GlStateManager.rotate((float)(entity.limbSwingAmount * 1.15F * 51.5), 0.0F, 0.0F, -1.0F);
		GlStateManager.translate((entity.limbSwingAmount * 1.15F * -0.2575), 0.0F, 0.0F);

		GlStateManager.translate(0.0F, 0.0F, MathHelper.cos(entity.limbSwing * 0.6662F + (float)Math.PI) * -1.15F * entity.limbSwingAmount * 0.1F);

		if(entity instanceof EntityTF77B)
		{
			GlStateManager.scale(0.65F, 1.0F, 0.65F);
		}
		else if(entity instanceof EntityTF78R)
		{
			GlStateManager.scale(1.0F, 2.75F, 1.05F);
		}
		else if(entity instanceof EntityTF80G)
		{
			GlStateManager.scale(1.0F, 2.75F, 2.0F);
		}
		else
		{
			GlStateManager.scale(1.0F, 2.75F, 1.05F);
		}

//		if(entity.getMechaLevel() >= 14)
//		{
			this.getEntityWeaponObj().renderPart("weapon");
//		}
			GlStateManager.popMatrix();

		if(!(entity instanceof EntityTF80G))
		{
			GlStateManager.pushMatrix();
			GlStateManager.rotate(180 - entity.renderYawOffset, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate((this.layerModel.bipedLeftArm.rotationPointX / 10) / 1.375, (this.layerModel.bipedLeftArm.rotationPointY / 10) / 1.625, this.layerModel.bipedLeftArm.rotationPointZ / 10);

			GlStateManager.rotate(this.layerModel.bipedLeftArm.rotateAngleX, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(this.layerModel.bipedLeftArm.rotateAngleY, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(this.layerModel.bipedLeftArm.rotateAngleZ, 0.0F, 0.0F, 1.0F);

			GlStateManager.rotate((float)(entity.limbSwingAmount * 1.15F * 51.5), 0.0F, 0.0F, 1.0F);
			GlStateManager.translate((entity.limbSwingAmount * 1.15F * 0.2575), 0.0F, 0.0F);

			GlStateManager.translate(0.0F, 0.0F, MathHelper.cos(entity.limbSwing * 0.6662F + (float)Math.PI) * 1.15F * entity.limbSwingAmount * 0.1F);

			if(entity instanceof EntityTF77B)
			{
				GlStateManager.scale(0.65F, 1.0F, 0.65F);
			}
			else if(entity instanceof EntityTF78R)
			{
				GlStateManager.scale(1.0F, 2.75F, 1.05F);
			}
			else
			{
				GlStateManager.scale(1.0F, 2.75F, 1.05F);
			}

//			if(entity.getMechaLevel() >= 14)
//			{
				this.getEntityWeaponObj().renderPart("weapon");
//			}
			GlStateManager.popMatrix();
		}
	}

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityGynoid entity)
    {
        return DEFAULT_RES_LOC;
    }

    protected ResourceLocation getEntityWeaponTexture()
    {
        return WEAPON_RES_LOC;
    }

    protected ResourceLocation getEntityWeaponLightTexture()
    {
        return WEAPON_LIGHT_RES_LOC;
    }

    protected IModelCustom getEntityWeaponObj()
    {
		return WEAPON_OBJ;
    }

    public void transformHeldFull3DItemLayer()
    {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }
}
