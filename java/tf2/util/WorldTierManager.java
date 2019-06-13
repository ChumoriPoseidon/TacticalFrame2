package tf2.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import tf2.TF2Core;

public class WorldTierManager
{

	public static void createWorldTierFile(World world)
	{
		TF2Core.loadConfig();

//		if (!dirWorld.exists())
//		{
//			try
//			{
//				dirWorld.mkdirs();
//			} catch (Exception e)
//			{
//				TF2Core.logger.warn("Could not create a TacticalFrame config world directory.");
//				TF2Core.logger.warn(e.getLocalizedMessage());
//			}
//		}

		File dirWorld = new File(Loader.instance().getConfigDir(),"tacticalframe" + File.separatorChar + "world");

		if(!dirWorld.isDirectory())
		{
			dirWorld.delete();
			dirWorld.mkdirs();
		}

		File fileWorld = new File(dirWorld, world.getWorldInfo().getWorldName() + ".tf");
		if (!fileWorld.exists() || !fileWorld.isFile())
		{
			try
			{
				fileWorld.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(fileWorld));
				bw.write("#TacticalFrame2 読み込み専用ファイル");	bw.newLine();
				bw.newLine();
				bw.write("tier1=" + String.valueOf(false));	bw.newLine();
				bw.write("tier2=" + String.valueOf(false));	bw.newLine();
				bw.write("tier3=" + String.valueOf(false));	bw.newLine();
				bw.close();

				TF2Core.config.getCategory("all").get("tf.config.tier1").set(false);
				TF2Core.config.getCategory("all").get("tf.config.tier2").set(false);
				TF2Core.config.getCategory("all").get("tf.config.tier3").set(false);

				TF2Core.syncConfig();

			} catch (FileNotFoundException ex)
			{
				ex.printStackTrace();
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			try {
				if(fileWorld.canRead())
				{
					BufferedWriter bw = new BufferedWriter(new FileWriter(fileWorld));
					bw.close();


					BufferedReader br = new BufferedReader(new FileReader(fileWorld));
					String str;
					while ((str = br.readLine()) != null)
					{
						String[] args = str.split("=");

						switch(args[0])
						{
							case "tier1":
								args[1] = TF2Core.config.getCategory("all").get("tf.config.tier1").toString();
								TF2Core.config.getCategory("all").get("tf.config.tier1").set(args[1]);
								break;
							case "tier2":
								args[1] = TF2Core.config.getCategory("all").get("tf.config.tier2").toString();
								TF2Core.config.getCategory("all").get("tf.config.tier2").set(args[1]);
								break;
							case "tier3":
								args[1] = TF2Core.config.getCategory("all").get("tf.config.tier3").toString();
								TF2Core.config.getCategory("all").get("tf.config.tier3").set(args[1]);
								break;
						}
					}
					br.close();
				}
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void saveWorldTierFile(World world)
	{
//		TF2Core.loadConfig();

		File dirWorld = new File(Loader.instance().getConfigDir(),"tacticalframe" + File.separatorChar + "world");
		File fileWorld = new File(dirWorld, world.getWorldInfo().getWorldName() + ".tf");

		if (!fileWorld.exists() || !fileWorld.isFile())
		{
			createWorldTierFile(world);
		}

		try {
			if(fileWorld.canRead())
			{
				BufferedReader br = new BufferedReader(new FileReader(fileWorld));
				String str;
				while ((str = br.readLine()) != null)
				{
					String[] args = str.split("=");

					if(args[0].equals("tier1"))
					{
						args[1] = TF2Core.config.getCategory("all").get("tf.config.tier1").getString();
						//TF2Core.config.getCategory("all").get("tf.config.tier1").set(args[1]);
					}
					if(args[0].equals("tier2"))
					{
						args[1] = TF2Core.config.getCategory("all").get("tf.config.tier2").getString();
						//TF2Core.config.getCategory("all").get("tf.config.tier2").set(args[1]);
					}
					if(args[0].equals("tier3"))
					{
						args[1] = TF2Core.config.getCategory("all").get("tf.config.tier3").getString();
						//TF2Core.config.getCategory("all").get("tf.config.tier3").set(args[1]);
					}
				}
				br.close();
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}


//		TF2Core.syncConfig();
	}
}
