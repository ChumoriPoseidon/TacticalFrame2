package tf2;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import tf2.blocks.BlockBase;
import tf2.blocks.BlockBioGenerator;
import tf2.blocks.BlockCarryline;
import tf2.blocks.BlockCarrylinePowered;
import tf2.blocks.BlockCarryline_down;
import tf2.blocks.BlockCarryline_up;
import tf2.blocks.BlockCatapult;
import tf2.blocks.BlockCokeOven;
import tf2.blocks.BlockCupola;
import tf2.blocks.BlockGunCraft;
import tf2.blocks.BlockIronFrame;
import tf2.blocks.BlockMachineChassis;
import tf2.blocks.BlockMachineStation;
import tf2.blocks.BlockMechaDock;
import tf2.blocks.BlockPulverizer;
import tf2.blocks.BlockSkillStation;
import tf2.blocks.BlockStoneMaker;
import tf2.blocks.BlockTFOre;

public class TFBlocks
{
	public static List<Block> BLOCKS = new ArrayList<Block>();

	public static final Block GUNCRAFT = new BlockGunCraft("guncraft").setHardness(2F).setResistance(20F);
	public static final Block SKILLSTATION = new BlockSkillStation("skillstation").setHardness(2F).setResistance(20F);
	public static final Block MACHINESTATION = new BlockMachineStation("machinestation").setHardness(2F).setResistance(20F);
	public static final Block MECHADOCK = new BlockMechaDock("mechadock").setHardness(2F).setResistance(20F);
	public static final Block CUPOLA = new BlockCupola("cupola").setHardness(3F).setResistance(20F);
	public static final Block COKE_OVEN = new BlockCokeOven("cokeoven").setHardness(3F).setResistance(20F);
	public static final Block BIO_GENERATOR = new BlockBioGenerator("biogenerator").setHardness(3F).setResistance(20F);
	public static final Block PULVERIZER = new BlockPulverizer("pulverizer").setHardness(3F).setResistance(20F);
	public static final Block STONEMAKER = new BlockStoneMaker("stonemaker").setHardness(3F).setResistance(20F);

	public static final Block ORE_NITER = new BlockTFOre("ore_niter", 1).setHardness(3F).setResistance(5F);
	public static final Block ORE_SULFUR = new BlockTFOre("ore_sulfur", 1).setHardness(3F).setResistance(5F);
	public static final Block ORE_MAGNETITE = new BlockTFOre("ore_magnetite", 1).setHardness(3F).setResistance(5F);
	public static final Block ORE_PYRODITE = new BlockTFOre("ore_pyrodite", 3).setHardness(10F).setResistance(200F);

	public static final Block COKE_BLOCK = new BlockBase("cokeblock", Material.ROCK, SoundType.STONE).setHardness(2F).setResistance(5F);
	public static final Block REINFORCED_IRON_BLOCK = new BlockBase("reinforced_ironblock", Material.IRON, SoundType.METAL).setHardness(3F).setResistance(20F);
	public static final Block RIGIDO_BLOCK = new BlockBase("rigidoblock", Material.IRON, SoundType.METAL).setHardness(5F).setResistance(2000F);

	public static final Block IRON_FRAME = new BlockIronFrame("ironframe").setHardness(3F).setResistance(20F);

	public static final Block MACHINE_CHASSIS = new BlockMachineChassis("machinechassis", Material.IRON, SoundType.METAL).setHardness(3F).setResistance(20F);

	public static final Block CARRYLINE = new BlockCarryline("carryline").setHardness(2F).setResistance(20F);
	public static final Block CARRYLINE_DOWN = new BlockCarryline_down("carryline_down").setHardness(2F).setResistance(20F);
	public static final Block CARRYLINE_UP = new BlockCarryline_up("carryline_up").setHardness(2F).setResistance(20F);
	public static final Block CARRYLINE_POWERED = new BlockCarrylinePowered("carryline_powered").setHardness(2F).setResistance(20F);
	public static final Block CATAPULT = new BlockCatapult("catapult").setHardness(2F).setResistance(20F);
}
