package tf2.recipes.guncraft;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import tf2.util.Reference;

public class CraftingManagerGunCraft {

	private static final Logger LOGGER = LogManager.getLogger();
	public static Map<ResourceLocation, IRecipe> REGISTRY = Maps.<ResourceLocation, IRecipe>newLinkedHashMap();

	public static boolean init() {

		try {

			registerRecipes();
			customParseJsonRecipes();
			return true;
		}
		catch(Throwable t) {

			return false;
		}
	}

	private static void registerRecipes() {

		/** 定形レシピ(Shaped Recipes)
		 * 	register(par1, new ShapedOreRecipe(null, par2, par3);
		 *
		 *  par1(String): 他のレシピと被らない名前(レシピ内容を表すと分かりやすい)
		 *  par2(Block or Item or ItemStack): 作り出されるアイテム
		 *  par3(Object...): 従来同様、""で素材の配置、''とその直後のItemStack(その他Item, Block, Ingredient)で各要素
		 *
		 *  ex1: 縦3上から羊毛[wool]、木材[plank]、棒[stick] > 松明[torch] x8
		 *  register("more_torch", new ShapedOreRecipe(null, new ItemStack(Blocks.TORCH, 8), new Object[] {"W", "P", "S", 'W', Blocks.WOOL, 'P', Blocks.PLANKS, 'S', Items.STICK}));
		 *
		 *	ex2: 十字(上 x1、中 x3、下 x1)で上に火打ち石[flint]、中に小麦(orニンジンorジャガイモ)[wheat or carrot or potato]、下にボウル[bowl] > 種[seed] x8
		 *	register("grind_crop", new ShapedOreRecipe(null, new ItemStack(Items.WHEAT_SEEDS, 8), new Object[] {" F ", "CCC", " B ", 'F', Items.FLINT, 'C', Ingredient.fromItems(Items.WHEAT, Items.CARROT, Items.POTATO), 'B', Items.BOWL}));
		 */

//		register("gear_iron", new ShapedOreRecipe(null, new ItemStack(TFItems.GEAR_IRON), new Object[]{" A ", "A A", " A ", 'A', Items.IRON_INGOT}));
//		register("gear_gold", new ShapedOreRecipe(null, new ItemStack(TFItems.GEAR_GOLD), new Object[]{" A ", "A A", " A ", 'A', Items.GOLD_INGOT}));
//
//		register("ak47", new ShapedOreRecipe(null, new ItemStack(TFItems.AK47), new Object[]{"AAA", "BCB", "AAA", 'A', Items.IRON_INGOT, 'B', Items.FLINT, 'C', Blocks.CRAFTING_TABLE}));
//		register("m16a1", new ShapedOreRecipe(null, new ItemStack(TFItems.M16A1), new Object[]{"AAA", "BCB", "AAA", 'A', Items.IRON_INGOT, 'B', Items.FLINT, 'C', Blocks.CRAFTING_TABLE}));
//		register("m24", new ShapedOreRecipe(null, new ItemStack(TFItems.M24), new Object[]{"AAA", "BCB", "AAA", 'A', Items.IRON_INGOT, 'B', Items.FLINT, 'C', Blocks.CRAFTING_TABLE}));
//		register("svd", new ShapedOreRecipe(null, new ItemStack(TFItems.SVD), new Object[]{"AAA", "BCB", "AAA", 'A', Items.IRON_INGOT, 'B', Items.FLINT, 'C', Blocks.CRAFTING_TABLE}));
//		register("spas12", new ShapedOreRecipe(null, new ItemStack(TFItems.SPAS12), new Object[]{"AAA", "BCB", "AAA", 'A', Items.IRON_INGOT, 'B', Items.FLINT, 'C', Blocks.CRAFTING_TABLE}));
//		register("saiga12", new ShapedOreRecipe(null, new ItemStack(TFItems.SAIGA12), new Object[]{"AAA", "BCB", "AAA", 'A', Items.IRON_INGOT, 'B', Items.FLINT, 'C', Blocks.CRAFTING_TABLE}));
//		register("uzi", new ShapedOreRecipe(null, new ItemStack(TFItems.UZI), new Object[]{"AAA", "BCB", "AAA", 'A', Items.IRON_INGOT, 'B', Items.FLINT, 'C', Blocks.CRAFTING_TABLE}));
//		register("evo3", new ShapedOreRecipe(null, new ItemStack(TFItems.EVO3), new Object[]{"AAA", "BCB", "AAA", 'A', Items.IRON_INGOT, 'B', Items.FLINT, 'C', Blocks.CRAFTING_TABLE}));
//		register("ppsh41", new ShapedOreRecipe(null, new ItemStack(TFItems.PPSH41), new Object[]{"AAA", "BCB", "AAA", 'A', Items.IRON_INGOT, 'B', Items.FLINT, 'C', Blocks.CRAFTING_TABLE}));
//		register("m1911", new ShapedOreRecipe(null, new ItemStack(TFItems.M1911), new Object[]{"AAA", "BCB", "AAA", 'A', Items.IRON_INGOT, 'B', Items.FLINT, 'C', Blocks.CRAFTING_TABLE}));
//		register("deserteagle", new ShapedOreRecipe(null, new ItemStack(TFItems.DESERTEAGLE), new Object[]{"AAA", "BCB", "AAA", 'A', Items.IRON_INGOT, 'B', Items.FLINT, 'C', Blocks.CRAFTING_TABLE}));
//		register("m1a1", new ShapedOreRecipe(null, new ItemStack(TFItems.M1A1), new Object[]{"AAA", "BCB", "AAA", 'A', Items.IRON_INGOT, 'B', Items.FLINT, 'C', Blocks.CRAFTING_TABLE}));
//
//		register("ak74", new ShapedOreRecipe(null, new ItemStack(TFItems.AK74), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.AK47}));
//		register("famas", new ShapedOreRecipe(null, new ItemStack(TFItems.FAMAS), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.AK47}));
//
//		register("m4a1", new ShapedOreRecipe(null, new ItemStack(TFItems.M4A1), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.M16A1}));
//		register("sig550", new ShapedOreRecipe(null, new ItemStack(TFItems.SIG550), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.M16A1}));
//
//		register("m200", new ShapedOreRecipe(null, new ItemStack(TFItems.M200), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.M24}));
//		register("wa2000", new ShapedOreRecipe(null, new ItemStack(TFItems.WA2000), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.SVD}));
//		register("mosinnagant", new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "mosinnagant"), new ItemStack(TFItems.MOSINNAGANT), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.M24}));
//		register("mosinnagant_2", new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "mosinnagant"), new ItemStack(TFItems.MOSINNAGANT), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.SVD}));
//		register("m82a1", new ShapedOreRecipe(null, new ItemStack(TFItems.M82A1), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.M24}));
//		register("m82a1_2", new ShapedOreRecipe(null, new ItemStack(TFItems.M82A1), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.SVD}));
//
//		register("m1014", new ShapedOreRecipe(null, new ItemStack(TFItems.M1014), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.SPAS12}));
//		register("vepr12", new ShapedOreRecipe(null, new ItemStack(TFItems.VEPR12), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.SAIGA12}));
//
//		register("p90", new ShapedOreRecipe(null, new ItemStack(TFItems.P90), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.UZI}));
//		register("mp7", new ShapedOreRecipe(null, new ItemStack(TFItems.MP7), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.EVO3}));
//		register("mg42", new ShapedOreRecipe(null, new ItemStack(TFItems.MG42), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.PPSH41}));
//		register("m20", new ShapedOreRecipe(null, new ItemStack(TFItems.M20), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.STEEL_INGOT, 'B', TFItems.MECHA_PARTS, 'C', TFItems.M1A1}));
//
//		register("ak12", new ShapedOreRecipe(null, new ItemStack(TFItems.AK12), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.AK74}));
//		register("hk416", new ShapedOreRecipe(null, new ItemStack(TFItems.HK416), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.M4A1}));
//		register("ots14", new ShapedOreRecipe(null, new ItemStack(TFItems.OTS14), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.FAMAS}));
//		register("g36", new ShapedOreRecipe(null, new ItemStack(TFItems.G36), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.SIG550}));
//
//
//		register("scar", new ShapedOreRecipe(null, new ItemStack(TFItems.SCAR), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.AK74}));
//		register("scar_2", new ShapedOreRecipe(null, new ItemStack(TFItems.SCAR), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.M4A1}));
//		register("scar_3", new ShapedOreRecipe(null, new ItemStack(TFItems.SCAR), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.FAMAS}));
//		register("scar_4", new ShapedOreRecipe(null, new ItemStack(TFItems.SCAR), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.SIG550}));
//		register("m14ebr", new ShapedOreRecipe(null, new ItemStack(TFItems.M14EBR), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.AK74}));
//		register("m14ebr_2", new ShapedOreRecipe(null, new ItemStack(TFItems.M14EBR), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.M4A1}));
//		register("m14ebr_3", new ShapedOreRecipe(null, new ItemStack(TFItems.M14EBR), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.FAMAS}));
//		register("m14ebr_4", new ShapedOreRecipe(null, new ItemStack(TFItems.M14EBR), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.SIG550}));
//
//		register("pp19", new ShapedOreRecipe(null, new ItemStack(TFItems.PP19), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.P90}));
//		register("ump9", new ShapedOreRecipe(null, new ItemStack(TFItems.UMP9), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.MP7}));
//		register("m60e1", new ShapedOreRecipe(null, new ItemStack(TFItems.M60E1), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.MG42}));
//
//		register("pgm", new ShapedOreRecipe(null, new ItemStack(TFItems.PGM), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.M82A1}));
//
//		register("xm2010", new ShapedOreRecipe(null, new ItemStack(TFItems.XM2010), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.M200}));
//		register("xm2010_2", new ShapedOreRecipe(null, new ItemStack(TFItems.XM2010), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.MOSINNAGANT}));
//		register("psg1", new ShapedOreRecipe(null, new ItemStack(TFItems.PSG1), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.WA2000}));
//		register("psg1_2", new ShapedOreRecipe(null, new ItemStack(TFItems.PSG1), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.MOSINNAGANT}));
//
//		register("m870", new ShapedOreRecipe(null, new ItemStack(TFItems.M870), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.M1014}));
//
//		register("aa12", new ShapedOreRecipe(null, new ItemStack(TFItems.AA12), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.M1014}));
//		register("aa12_2", new ShapedOreRecipe(null, new ItemStack(TFItems.AA12), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.VEPR12}));
//
//		register("mgl140", new ShapedOreRecipe(null, new ItemStack(TFItems.MGL140), new Object[]{"AAA", "BCB", "AAA", 'A', TFItems.RIGIDO_INGOT, 'B', TFItems.LEPRE_CUBE, 'C', TFItems.M20}));
//
//
//		register("aidstone_0", new ShapelessOreRecipe(null, new ItemStack(TFBlocks.AID_STONE, 1, 1), new Object[] {new ItemStack(TFBlocks.AID_STONE, 1, 0)}));
//		register("aidstone_1", new ShapelessOreRecipe(null, new ItemStack(TFBlocks.AID_STONE, 1, 3), new Object[] {new ItemStack(TFBlocks.AID_STONE, 1, 0)}));
//		register("aidstone_2", new ShapelessOreRecipe(null, new ItemStack(TFBlocks.AID_STONE, 1, 4), new Object[] {new ItemStack(TFBlocks.AID_STONE, 1, 0)}));
//		register("aidstone_3", new ShapelessOreRecipe(null, new ItemStack(TFBlocks.AID_STONE, 1, 5), new Object[] {new ItemStack(TFBlocks.AID_STONE, 1, 0)}));
//		register("aidstone_4", new ShapelessOreRecipe(null, new ItemStack(TFBlocks.AID_STONE, 1, 6), new Object[] {new ItemStack(TFBlocks.AID_STONE, 1, 0)}));
//		register("aidstone_5", new ShapelessOreRecipe(null, new ItemStack(TFBlocks.AID_STONE, 1, 7), new Object[] {new ItemStack(TFBlocks.AID_STONE, 1, 0)}));
//		register("aidstone_6", new ShapelessOreRecipe(null, new ItemStack(TFBlocks.AID_STONE, 1, 8), new Object[] {new ItemStack(TFBlocks.AID_STONE, 1, 0)}));
//		register("aidstone_7", new ShapelessOreRecipe(null, new ItemStack(TFBlocks.AID_STONE, 1, 9), new Object[] {new ItemStack(TFBlocks.AID_STONE, 1, 0)}));
//		register("aidstone_8", new ShapelessOreRecipe(null, new ItemStack(TFBlocks.AID_STONE, 1, 11), new Object[] {new ItemStack(TFBlocks.AID_STONE, 1, 0)}));
//		register("aidstone_9", new ShapelessOreRecipe(null, new ItemStack(TFBlocks.AID_STONE, 1, 12), new Object[] {new ItemStack(TFBlocks.AID_STONE, 1, 0)}));
//		register("aidstone_10", new ShapelessOreRecipe(null, new ItemStack(TFBlocks.AID_STONE, 1, 14), new Object[] {new ItemStack(TFBlocks.AID_STONE, 1, 0)}));
//		register("aidstone_11", new ShapelessOreRecipe(null, new ItemStack(TFBlocks.AID_STONE, 1, 15), new Object[] {new ItemStack(TFBlocks.AID_STONE, 1, 0)}));
//
//		register("box_rifle", new ShapelessOreRecipe(null, new ItemStack(TFItems.BOX_RIFLE, 1), new Object[] {new ItemStack(Items.IRON_INGOT), new ItemStack(Items.GUNPOWDER)}));
//		register("box_shot", new ShapelessOreRecipe(null, new ItemStack(TFItems.BOX_SHOT, 1), new Object[] {new ItemStack(Items.IRON_INGOT), new ItemStack(Items.GUNPOWDER), new ItemStack(Items.PAPER)}));
//		register("box_sniper", new ShapelessOreRecipe(null, new ItemStack(TFItems.BOX_SNIPER, 1), new Object[] {new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.GUNPOWDER)}));
//		register("box_grenade", new ShapelessOreRecipe(null, new ItemStack(TFItems.BOX_GRENADE, 1), new Object[] {new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(TFBlocks.EXPLOSIVE)}));
//
//
//		/** 非定形レシピ(Shapeless Recipes)
//		 *  register(par1, new ShapelessOreRecipe(null, par2, par3);
//		 *
//		 * 	par1(String): 他のレシピと被らない名前(レシピ内容を表すと分かりやすい)
//		 *  par2(Block or Item or ItemStack): 作り出されるアイテム
//		 *  par3(Object...): 従来同様、ItemStack(その他Item, Block, Ingredient)を連続で必要な素材集
//		 *
//		 *  ex1: 土ブロック系[dirt] x2 > ダイヤモンド[diamond]
//		 *  register("dirt2_to_diamond", new ShapelessOreRecipe(null, new ItemStack(Items.DIAMOND), Blocks.DIRT, Blocks.DIRT));
//		 *
//		 *  ex2: ゾンビ肉[rotten_flesh]とニンジン(orジャガイモ)[carrot or potato] > ゾンビ肉[rotten_flesh] x2
//		 *  register("dup_rotten_flesh", new ShapelessOreRecipe(null, new ItemStack(Items.ROTTEN_FLESH, 2), Items.ROTTEN_FLESH, Ingredient.fromItems(Items.CARROT, Items.POTATO)));
//		 */
//
//		register("planks_0", new ShapelessOreRecipe(null, new ItemStack(Blocks.PLANKS, 4, 0), new Object[] {new ItemStack(Blocks.LOG, 1, 32767)}));
//		register("planks_1", new ShapelessOreRecipe(null, new ItemStack(Blocks.PLANKS, 4, 1), new Object[] {new ItemStack(Blocks.LOG, 1, 32767)}));
//		register("planks_2", new ShapelessOreRecipe(null, new ItemStack(Blocks.PLANKS, 4, 2), new Object[] {new ItemStack(Blocks.LOG, 1, 32767)}));
//		register("planks_3", new ShapelessOreRecipe(null, new ItemStack(Blocks.PLANKS, 4, 3), new Object[] {new ItemStack(Blocks.LOG, 1, 32767)}));
//		register("planks_4", new ShapelessOreRecipe(null, new ItemStack(Blocks.PLANKS, 4, 4), new Object[] {new ItemStack(Blocks.LOG, 1, 32767)}));
//		register("planks_5", new ShapelessOreRecipe(null, new ItemStack(Blocks.PLANKS, 4, 5), new Object[] {new ItemStack(Blocks.LOG, 1, 32767)}));
//		register("planks_6", new ShapelessOreRecipe(null, new ItemStack(Blocks.CRAFTING_TABLE, 1), new Object[] {new ItemStack(Blocks.LOG, 1, 32767)}));
//
//		register("2planks_0", new ShapelessOreRecipe(null, new ItemStack(Blocks.PLANKS, 4, 0), new Object[] {new ItemStack(Blocks.LOG2, 1, 32767)}));
//		register("2planks_1", new ShapelessOreRecipe(null, new ItemStack(Blocks.PLANKS, 4, 1), new Object[] {new ItemStack(Blocks.LOG2, 1, 32767)}));
//		register("2planks_2", new ShapelessOreRecipe(null, new ItemStack(Blocks.PLANKS, 4, 2), new Object[] {new ItemStack(Blocks.LOG2, 1, 32767)}));
//		register("2planks_3", new ShapelessOreRecipe(null, new ItemStack(Blocks.PLANKS, 4, 3), new Object[] {new ItemStack(Blocks.LOG2, 1, 32767)}));
//		register("2planks_4", new ShapelessOreRecipe(null, new ItemStack(Blocks.PLANKS, 4, 4), new Object[] {new ItemStack(Blocks.LOG2, 1, 32767)}));
//		register("2planks_5", new ShapelessOreRecipe(null, new ItemStack(Blocks.PLANKS, 4, 5), new Object[] {new ItemStack(Blocks.LOG2, 1, 32767)}));
//		register("2planks_6", new ShapelessOreRecipe(null, new ItemStack(Blocks.CRAFTING_TABLE, 1), new Object[] {new ItemStack(Blocks.LOG2, 1, 32767)}));
//
//		register("gem_0_0", new ShapelessOreRecipe(null, new ItemStack(Items.IRON_INGOT, 10), new Object[] {new ItemStack(TFItems.GEM, 1, 0)}));
//		register("gem_0_1", new ShapelessOreRecipe(null, new ItemStack(Items.GOLD_INGOT, 3), new Object[] {new ItemStack(TFItems.GEM, 1, 0)}));
//		register("gem_0_2", new ShapelessOreRecipe(null, new ItemStack(Items.COAL, 20), new Object[] {new ItemStack(TFItems.GEM, 1, 0)}));
//		register("gem_0_3", new ShapelessOreRecipe(null, new ItemStack(Items.GUNPOWDER, 5), new Object[] {new ItemStack(TFItems.GEM, 1, 0)}));
//		register("gem_0_4", new ShapelessOreRecipe(null, new ItemStack(Items.QUARTZ, 3), new Object[] {new ItemStack(TFItems.GEM, 1, 0)}));
//		register("gem_0_5", new ShapelessOreRecipe(null, new ItemStack(Items.REDSTONE, 5), new Object[] {new ItemStack(TFItems.GEM, 1, 0)}));
//		register("gem_0_6", new ShapelessOreRecipe(null, new ItemStack(Items.GLOWSTONE_DUST, 4), new Object[] {new ItemStack(TFItems.GEM, 1, 0)}));
//		register("gem_0_7", new ShapelessOreRecipe(null, new ItemStack(Items.BLAZE_POWDER, 2), new Object[] {new ItemStack(TFItems.GEM, 1, 0)}));
//		register("gem_0_8", new ShapelessOreRecipe(null, new ItemStack(TFItems.WILLOW_SEED, 4), new Object[] {new ItemStack(TFItems.GEM, 1, 0)}));
//		register("gem_0_9", new ShapelessOreRecipe(null, new ItemStack(TFItems.COFFEE_BERRY, 4), new Object[] {new ItemStack(TFItems.GEM, 1, 0)}));
//		register("gem_0_10", new ShapelessOreRecipe(null, new ItemStack(TFItems.COTTON_SEED, 4), new Object[] {new ItemStack(TFItems.GEM, 1, 0)}));
//		register("gem_0_11", new ShapelessOreRecipe(null, new ItemStack(Blocks.RAIL, 34), new Object[] {new ItemStack(TFItems.GEM, 1, 0)}));
//
//		register("gem_1_0", new ShapelessOreRecipe(null, new ItemStack(Items.IRON_INGOT, 15), new Object[] {new ItemStack(TFItems.GEM, 1, 1)}));
//		register("gem_1_1", new ShapelessOreRecipe(null, new ItemStack(Items.GOLD_INGOT, 5), new Object[] {new ItemStack(TFItems.GEM, 1, 1)}));
//		register("gem_1_2", new ShapelessOreRecipe(null, new ItemStack(Items.DIAMOND, 1), new Object[] {new ItemStack(TFItems.GEM, 1, 1)}));
//		register("gem_1_3", new ShapelessOreRecipe(null, new ItemStack(Items.GUNPOWDER, 8), new Object[] {new ItemStack(TFItems.GEM, 1, 1)}));
//		register("gem_1_4", new ShapelessOreRecipe(null, new ItemStack(Items.QUARTZ, 5), new Object[] {new ItemStack(TFItems.GEM, 1, 1)}));
//		register("gem_1_5", new ShapelessOreRecipe(null, new ItemStack(Items.REDSTONE, 8), new Object[] {new ItemStack(TFItems.GEM, 1, 1)}));
//		register("gem_1_6", new ShapelessOreRecipe(null, new ItemStack(Items.GLOWSTONE_DUST, 7), new Object[] {new ItemStack(TFItems.GEM, 1, 1)}));
//		register("gem_1_7", new ShapelessOreRecipe(null, new ItemStack(Items.BLAZE_ROD, 2), new Object[] {new ItemStack(TFItems.GEM, 1, 1)}));
//		register("gem_1_8", new ShapelessOreRecipe(null, new ItemStack(Items.DYE, 4, 3), new Object[] {new ItemStack(TFItems.GEM, 1, 1)}));
//		register("gem_1_9", new ShapelessOreRecipe(null, new ItemStack(Items.DYE, 16, 4), new Object[] {new ItemStack(TFItems.GEM, 1, 1)}));
//		register("gem_1_10", new ShapelessOreRecipe(null, new ItemStack(Items.EMERALD, 1), new Object[] {new ItemStack(TFItems.GEM, 1, 1)}));
//		register("gem_1_11", new ShapelessOreRecipe(null, new ItemStack(Blocks.CACTUS, 4), new Object[] {new ItemStack(TFItems.GEM, 1, 1)}));
//
//		register("gem_2_0", new ShapelessOreRecipe(null, new ItemStack(Items.IRON_INGOT, 20), new Object[] {new ItemStack(TFItems.GEM, 1, 2)}));
//		register("gem_2_1", new ShapelessOreRecipe(null, new ItemStack(Items.GOLD_INGOT, 8), new Object[] {new ItemStack(TFItems.GEM, 1, 2)}));
//		register("gem_2_2", new ShapelessOreRecipe(null, new ItemStack(Items.DIAMOND, 2), new Object[] {new ItemStack(TFItems.GEM, 1, 2)}));
//		register("gem_2_3", new ShapelessOreRecipe(null, new ItemStack(Items.GUNPOWDER, 10), new Object[] {new ItemStack(TFItems.GEM, 1, 2)}));
//		register("gem_2_4", new ShapelessOreRecipe(null, new ItemStack(Items.QUARTZ, 10), new Object[] {new ItemStack(TFItems.GEM, 1, 2)}));
//		register("gem_2_5", new ShapelessOreRecipe(null, new ItemStack(Items.REDSTONE, 10), new Object[] {new ItemStack(TFItems.GEM, 1, 2)}));
//		register("gem_2_6", new ShapelessOreRecipe(null, new ItemStack(Items.GLOWSTONE_DUST, 10), new Object[] {new ItemStack(TFItems.GEM, 1, 2)}));
//		register("gem_2_7", new ShapelessOreRecipe(null, new ItemStack(Items.BLAZE_ROD, 4), new Object[] {new ItemStack(TFItems.GEM, 1, 2)}));
//		register("gem_2_8", new ShapelessOreRecipe(null, new ItemStack(Blocks.NETHER_BRICK, 64), new Object[] {new ItemStack(TFItems.GEM, 1, 2)}));
//		register("gem_2_9", new ShapelessOreRecipe(null, new ItemStack(Items.DYE, 20, 4), new Object[] {new ItemStack(TFItems.GEM, 1, 2)}));
//		register("gem_2_10", new ShapelessOreRecipe(null, new ItemStack(Items.EMERALD, 2), new Object[] {new ItemStack(TFItems.GEM, 1, 2)}));
//		//register("gem_2_11", new ShapelessOreRecipe(null, new ItemStack(Blocks.CACTUS, 4), new Object[] {new ItemStack(TFItems.GEM, 1, 2)}));
//
//		register("gem_3_0", new ShapelessOreRecipe(null, new ItemStack(Items.IRON_INGOT, 25), new Object[] {new ItemStack(TFItems.GEM, 1, 3)}));
//		register("gem_3_1", new ShapelessOreRecipe(null, new ItemStack(Items.GOLD_INGOT, 10), new Object[] {new ItemStack(TFItems.GEM, 1, 3)}));
//		register("gem_3_2", new ShapelessOreRecipe(null, new ItemStack(Items.DIAMOND, 3), new Object[] {new ItemStack(TFItems.GEM, 1, 3)}));
//		register("gem_3_3", new ShapelessOreRecipe(null, new ItemStack(Items.GUNPOWDER, 12), new Object[] {new ItemStack(TFItems.GEM, 1, 3)}));
//		register("gem_3_4", new ShapelessOreRecipe(null, new ItemStack(Items.QUARTZ, 15), new Object[] {new ItemStack(TFItems.GEM, 1, 3)}));
//		register("gem_3_5", new ShapelessOreRecipe(null, new ItemStack(Items.REDSTONE, 16), new Object[] {new ItemStack(TFItems.GEM, 1, 3)}));
//		register("gem_3_6", new ShapelessOreRecipe(null, new ItemStack(Items.GLOWSTONE_DUST, 16), new Object[] {new ItemStack(TFItems.GEM, 1, 3)}));
//		register("gem_3_7", new ShapelessOreRecipe(null, new ItemStack(TFItems.CERAMIC, 4), new Object[] {new ItemStack(TFItems.GEM, 1, 3)}));
//		register("gem_3_8", new ShapelessOreRecipe(null, new ItemStack(TFItems.RIGIDO_PLATE, 6), new Object[] {new ItemStack(TFItems.GEM, 1, 3)}));
//		register("gem_3_9", new ShapelessOreRecipe(null, new ItemStack(Items.DYE, 24, 4), new Object[] {new ItemStack(TFItems.GEM, 1, 3)}));
//		register("gem_3_10", new ShapelessOreRecipe(null, new ItemStack(Items.EMERALD, 3), new Object[] {new ItemStack(TFItems.GEM, 1, 3)}));

	}

	private static void customParseJsonRecipes() {

		FileSystem file = null;
		Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
		try {

			URL url = CraftingManagerGunCraft.class.getResource("/assets/.tacticalframeroot");
			if(url != null) {

				URI uri = url.toURI();
				Path path;
				if("file".equals(uri.getScheme())) {

					path = Paths.get(CraftingManagerGunCraft.class.getResource("/assets/tf/customrecipes").toURI());
				}
				else {

					if(!"jar".equals(uri.getScheme())) {

						LOGGER.error("Unsupported scheme " + uri + " trying to list all recipes");
						return; // CHANGE
					}
					file = FileSystems.newFileSystem(uri, Collections.emptyMap());
					path = file.getPath("/assets/tf/recipes");
				}
				Iterator<Path> iterator = Files.walk(path).iterator();
				while(iterator.hasNext()) {

					Path path1 = iterator.next();
					if("json".equals(FilenameUtils.getExtension(path1.toString()))) {

						Path path2 = path.relativize(path1);
						String s = FilenameUtils.removeExtension(path2.toString()).replaceAll("\\\\", "/");
						ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID, s); // "[json_recipe]"
						BufferedReader bufferedReader = null;
						try {

							try {

								bufferedReader = Files.newBufferedReader(path1);
								register(resourceLocation, customParseRecipeJson((JsonObject)JsonUtils.fromJson(gson, bufferedReader, JsonObject.class)));
							}
							catch(JsonParseException jsonParseException) {

								LOGGER.error("Parsing error loading recipe " + resourceLocation, (Throwable)jsonParseException);
								return; // CHANGE
							}
							catch(IOException ioException) {

								LOGGER.error("Couldn't read recipe " + resourceLocation + " from " + path1, (Throwable)ioException);
								return; // CHANGE
							}
						}
						finally {

							IOUtils.closeQuietly((Reader)bufferedReader);
						}
					}
				}
				return; // CHANGE
			}
			LOGGER.error("Couldn't find .mcassetsroot");
		}
		catch(IOException | URISyntaxException uriSyntaxException) {

			LOGGER.error("Couldn't get a list of all recipe files", (Throwable)uriSyntaxException);
			return; // CHANGE
		}
		finally {

			IOUtils.closeQuietly((Closeable)file);
		}
	}

	private static IRecipe customParseRecipeJson(JsonObject jsonObject) {

		String s = JsonUtils.getString(jsonObject, "type");
		if("crafting_shaped".equals(s)) {

			return deserializeShapedRecipe(jsonObject);
		}
		else if("crafting_shapeless".equals(s)) {

			return deserializeShapelessRecipe(jsonObject);
		}
		else if("forge:ore_shaped".equals(s)) {

			return ShapedOreRecipe.factory(new JsonContext(Reference.MOD_ID), jsonObject);
		}
		else if("forge:ore_shapeless".equals(s)) {

			return ShapelessOreRecipe.factory(new JsonContext(Reference.MOD_ID), jsonObject);
		}
		else {

			throw new JsonSyntaxException("Invalid or unsupported recipe type '" + s + "'");
		}
	}

	private static IRecipe deserializeShapedRecipe(JsonObject jsonObject) {

		String s = JsonUtils.getString(jsonObject, "group", "");
		Map<String, Ingredient> map = customDeserializeKey(JsonUtils.getJsonObject(jsonObject, "key"));
		String[] astring = customShrink(customPatternFromJson(JsonUtils.getJsonArray(jsonObject, "pattern")));
		int i = astring[0].length();
		int j = astring.length;
		NonNullList<Ingredient> nnList = customDeserializeIngredients(astring, map, i, j);
		ItemStack itemstack = ShapedRecipes.deserializeItem(JsonUtils.getJsonObject(jsonObject, "result"), true);
		return new ShapedRecipes(s, i, j, nnList, itemstack);
	}

	private static Map<String, Ingredient> customDeserializeKey(JsonObject jsonObject) {

		Map<String, Ingredient> map = Maps.<String, Ingredient>newHashMap();
		for(Entry<String, JsonElement> entry : jsonObject.entrySet()) {

			if(((String)entry.getKey()).length() != 1) {

				throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
			}
			if(" ".equals(entry.getKey())) {

				throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol");
			}
			map.put(entry.getKey(), ShapedRecipes.deserializeIngredient(entry.getValue()));
		}
		map.put(" ", Ingredient.EMPTY);
		return map;
	}

	@VisibleForTesting
	private static String[] customShrink(String... string) {

		int i = Integer.MAX_VALUE;
		int j = 0;
		int k = 0;
		int l = 0;
		for(int i1 = 0; i1 < string.length; i1++) {

			String s = string[i1];
			i = Math.min(i, customFirstNonSpace(s));
			int j1 = customLastNonSpace(s);
			j = Math.max(j, j1);
			if(j1 < 0) {

				if(k == i1) {

					k++;
				}
				l++;
			}
			else {

				l = 0;
			}
		}
		if(string.length == l) {

			return new String[0];
		}
		else {

			String[] astring = new String[string.length - l - k];
			for(int k1 = 0; k1 < astring.length; k1++) {

				astring[k1] = string[k1 + k].substring(i, j + 1);
			}
			return astring;
		}
	}

	private static int customFirstNonSpace(String s) {

		int i;
		for(i = 0; i < s.length() && s.charAt(i) == ' '; i++) {

			;
		}
		return i;
	}

	private static int customLastNonSpace(String s) {

		int i;
		for(i = s.length() - 1; i >= 0 && s.charAt(i) == ' '; i--) {

			;
		}
		return i;
	}

	private static String[] customPatternFromJson(JsonArray jsonArray) {

		String[] astring = new String[jsonArray.size()];
		if(astring.length > 3) { // crafting slot row

			throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
		}
		else if(astring.length == 0) {

			throw new JsonSyntaxException("Invalid patter: empty pattern not allowed");
		}
		else {

			for(int i = 0; i < astring.length; i++) {

				String s = JsonUtils.getString(jsonArray.get(i), "pattern[" + i + "]");
				if(s.length() > 3) { // crafting slot column

					throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
				}
				if(i > 0 && astring[0].length() != s.length()) {

					throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
				}
				astring[i] = s;
			}
			return astring;
		}
	}

	private static NonNullList<Ingredient> customDeserializeIngredients(String[] astring, Map<String, Ingredient> map, int a, int b) {

		NonNullList<Ingredient> nnList = NonNullList.<Ingredient>withSize(a * b, Ingredient.EMPTY);
		Set<String> set = Sets.newHashSet(map.keySet());
		set.remove(" ");
		for(int i = 0; i < astring.length; i++) {

			for(int j = 0; j < astring[i].length(); j++) {

				String s = astring[i].substring(j, j + 1);
				Ingredient ingredient = map.get(s);
				if(ingredient == null) {

					throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
				}
				set.remove(s);
				nnList.set(j + a * i, ingredient);
			}
		}
		if(!set.isEmpty()) {

			throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
		}
		else {

			return nnList;
		}
	}

	private static IRecipe deserializeShapelessRecipe(JsonObject jsonObject) {

		String s = JsonUtils.getString(jsonObject, "group", "");
		NonNullList<Ingredient> nnList = customDeserializeIngredients(JsonUtils.getJsonArray(jsonObject, "ingredients"));
		if(nnList.isEmpty()) {

			throw new JsonParseException("No ingredients for shapeless recipe");
		}
		else if(nnList.size() > 9) { // crafting slot num

			throw new JsonParseException("Too many ingredients for shapeless recipe");
		}
		else {

			ItemStack itemstack = ShapedRecipes.deserializeItem(JsonUtils.getJsonObject(jsonObject, "result"), true);
			return new ShapelessRecipes(s, itemstack, nnList);
		}
	}

	private static NonNullList<Ingredient> customDeserializeIngredients(JsonArray jsonArray) {

		NonNullList<Ingredient> nnList = NonNullList.<Ingredient>create();
		for(int i = 0; i < jsonArray.size(); i++) {

			Ingredient ingredient = ShapedRecipes.deserializeIngredient(jsonArray.get(i));
			if(ingredient != Ingredient.EMPTY) {

				nnList.add(ingredient);
			}
		}
		return nnList;
	}

	private static void register(String name, IRecipe recipe) {

		register(new ResourceLocation(Reference.MOD_ID, name), recipe); //  "[java_recipe]"
	}

	private static void register(ResourceLocation name, IRecipe recipe) {

		REGISTRY.put(name, recipe);
	}

	public static List<ItemStack> findMatchingResult(InventoryCrafting craftMatrix, World worldIn) {

		List<ItemStack> result = new ArrayList();
		for(Map.Entry<ResourceLocation, IRecipe> entry : REGISTRY.entrySet()) {

			if(entry.getValue().matches(craftMatrix, worldIn)) {

				result.add(entry.getValue().getCraftingResult(craftMatrix));
			}
		}
		return result;
	}

	public static NonNullList<ItemStack> getRemainingItems(InventoryCrafting craftMatrix, World worldIn) {

		for(Map.Entry<ResourceLocation, IRecipe> entry : REGISTRY.entrySet()) {

			if(entry.getValue().matches(craftMatrix, worldIn)) {

				return entry.getValue().getRemainingItems(craftMatrix);
			}
		}
		NonNullList<ItemStack> nnList = NonNullList.<ItemStack>withSize(craftMatrix.getSizeInventory(), ItemStack.EMPTY);
		for(int i = 0; i < nnList.size(); i++) {

			nnList.set(i, craftMatrix.getStackInSlot(i));
		}
		return nnList;
	}


	// ↓いらない
	public static String getInputs(IRecipe recipe) {

		String inputs = "";
		NonNullList<Ingredient> list = recipe.getIngredients();
		Iterator iterator = list.iterator();
		while(iterator.hasNext()) {

			Ingredient ingredient = (Ingredient)iterator.next();
			ItemStack[] stack = ingredient.getMatchingStacks();
			if(stack.length > 1) {

				String buffer = "[ ";
				for(int i = 0; i < stack.length; i++) {

					ItemStack itemStack = stack[i];
					if(!itemStack.isEmpty()) {

						buffer += itemStack.getItem().getRegistryName();
					}
					if(i < stack.length - 1) {

						buffer += ",";
					}
					buffer += " ";
				}
				buffer += "]";
				inputs += buffer + ", ";
			}
			else if(stack.length == 1) {

				ItemStack itemStack = stack[0];
				if(!itemStack.isEmpty()) {

					inputs += itemStack.getItem().getRegistryName();
				}
			}
			if(iterator.hasNext()) {

				inputs += ", ";
			}
		}
		return inputs;
	}
}
