package com.mraof.minestuck.world.gen;

public class OreGeneration
{
	public static final int cruxiteVeinsPerChunk = 10;
	public static final int uraniumVeinsPerChunk = 10;
	public static final int baseCruxiteVeinSize = 6;
	public static final int baseUraniumVeinSize = 3;
	public static final int bonusCruxiteVeinSize = 3;
	public static final int bonusUraniumVeinSize = 3;
	public static final int cruxiteStratumMin = 0;
	public static final int uraniumStratumMin = 0;
	public static final int cruxiteStratumMax = 60;
	public static final int uraniumStratumMax = 30;
	
	public static void setupOverworldOreGeneration()
	{
		/*for(Biome biome : ForgeRegistries.BIOMES) TODO might now need BiomeLoadingEvent and a replacement to biome dictionary
		{
			if(BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD))
			{
				if(MinestuckConfig.SERVER.generateCruxiteOre.get())
				{
					biome.addFeature(Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(FillerBlockType.NATURAL_STONE, MSBlocks.STONE_CRUXITE_ORE.getDefaultState(), baseCruxiteVeinSize)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(cruxiteVeinsPerChunk, cruxiteStratumMin, cruxiteStratumMin, cruxiteStratumMax))));
				}
				if(MinestuckConfig.SERVER.generateUraniumOre.get())
				{
					biome.addFeature(Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(FillerBlockType.NATURAL_STONE, MSBlocks.STONE_URANIUM_ORE.getDefaultState(), baseUraniumVeinSize)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(uraniumVeinsPerChunk, uraniumStratumMin, uraniumStratumMin, uraniumStratumMax))));
				}
			}
		}*/
	}
}