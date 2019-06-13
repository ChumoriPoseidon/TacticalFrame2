package tf2.entity.mob.enemy;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMobTF extends EntityMob
{
	public int attackTime;
	public int deathTicks;
	public EntityMobTF(World par1World)
    {
        super(par1World);
    }
    @Override
    public void setInWeb() {}

	@Override
	public boolean attackEntityFrom(DamageSource var1, float var2)
	{
		// defeatedcrowさんのHeatAndClimateModの寒暖ダメーシに対応
		return var1.damageType == "dcs_cold" ? false :
				var1.damageType == "dcs_heat" ? false :

				var1.damageType == "grenade" ? false :

				var1.damageType == "starve" ? false :
				var1.damageType == "fall" ? false :
				var1.damageType == "inFire" ? false :
				var1.damageType == "onFire" ? false :
				var1.damageType == "hotFloor" ? false :
				var1.damageType == "cactus" ? false : super.attackEntityFrom(var1, var2);
	}

	@Override
	public boolean isPotionApplicable(PotionEffect potioneffectIn)
	{
		if (potioneffectIn.getPotion() == MobEffects.POISON ||  potioneffectIn.getPotion() == MobEffects.WITHER)
		{
			return false;
		}
		return super.isPotionApplicable(potioneffectIn);
	}

	//--------ここからアニメーション関係
	@Override
	public void onEntityUpdate()
    {
		super.onEntityUpdate();

		if (this.attackTime > 0)
		{
			--this.attackTime;
		}
    }
}
