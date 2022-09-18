package carpet.helpers;

import carpet.CarpetSettings;
import carpet.fakes.CoralFeatureInterface;
import net.minecraft.block.*;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.CoralClawFeature;
import net.minecraft.world.gen.feature.CoralFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;


import java.util.*;
import java.util.stream.Collectors;

public class CoralHelper {

    static Map<Direction, CoralClawFeature> directionToFeatureMap = new HashMap<>();
    static Map<CoralClawFeature, Direction> featureToDirectionMap = new HashMap<>();

    static Map<Block, Block> plantToBlockMap;

    static{
        CoralClawFeature feature = new CoralClawFeature(DefaultFeatureConfig.CODEC);
        directionToFeatureMap.put(Direction.NORTH,feature);
        featureToDirectionMap.put(feature, Direction.NORTH);
        feature = new CoralClawFeature(DefaultFeatureConfig.CODEC);
        directionToFeatureMap.put(Direction.SOUTH,feature);
        featureToDirectionMap.put(feature, Direction.SOUTH);
        feature = new CoralClawFeature(DefaultFeatureConfig.CODEC);
        directionToFeatureMap.put(Direction.EAST,feature);
        featureToDirectionMap.put(feature, Direction.EAST);
        feature = new CoralClawFeature(DefaultFeatureConfig.CODEC);
        directionToFeatureMap.put(Direction.WEST,feature);
        featureToDirectionMap.put(feature, Direction.WEST);
    }


    public static void processGrowth(World worldIn, Random random, BlockPos pos, BlockState blockState, CoralFeature coral) {
        if (plantToBlockMap==null)
            loadCoralMap(worldIn);
        Block curr = blockState.getBlock();
        BlockState proper_block = plantToBlockMap.getOrDefault(curr,BlockTags.CORAL_BLOCKS.getRandom(random)).getDefaultState();
        worldIn.setBlockState(pos, Blocks.WATER.getDefaultState(), 4);

        if (!((CoralFeatureInterface)coral).growSpecific(worldIn, random, pos, proper_block))
        {
            worldIn.setBlockState(pos, blockState, 3);
        }/*
        else
        {
            if (worldIn.random.nextInt(10)==0)
            {
                BlockPos randomPos = pos.add(worldIn.random.nextInt(16)-8,worldIn.random.nextInt(8),worldIn.random.nextInt(16)-8  );
                if (BlockTags.CORAL_BLOCKS.contains(worldIn.getBlockState(randomPos).getBlock()))
                {
                    worldIn.setBlockState(randomPos, Blocks.WET_SPONGE.getDefaultState(), 3);
                }
            }
        }*/
    }

    public static boolean canBonemeal(BlockView var1, BlockPos var2, BlockState var3) {
        if (!(var1 instanceof World level))
            return false;
        else {
            Optional<RegistryKey<Biome>> optional = level.getBiomeKey(var2);
            boolean isWarm = Objects.equals(optional, Optional.of(BiomeKeys.WARM_OCEAN)) || Objects.equals(optional, Optional.of(BiomeKeys.DEEP_WARM_OCEAN));
            return (CarpetSettings.renewableCoral == CarpetSettings.RenewableCoralMode.EXPANDED
                        || (CarpetSettings.renewableCoral == CarpetSettings.RenewableCoralMode.TRUE
                            && isWarm))
                    && var3.get(CoralParentBlock.WATERLOGGED)
                    && var1.getBlockState(var2.up()).isOf(Blocks.WATER);
        }
    }

    public static boolean isBonemealSuccess(World var1) {
        return (double) var1.random.nextFloat() < 0.15D;
    }

    public static CoralClawFeature getFeature(Direction direction){
        return directionToFeatureMap.getOrDefault(direction, new CoralClawFeature(DefaultFeatureConfig.CODEC));
    }
    public static Direction getDirection(CoralClawFeature feature){
        return featureToDirectionMap.get(feature);
    }


    private static void loadCoralMap(World worldIn){
        Map<Block, Block> tmp_map = new HashMap<>();
        Set<Block> baseCoralSet;
        {
            baseCoralSet = new HashSet<>(BlockTags.CORALS.values());
            baseCoralSet.addAll(BlockTags.WALL_CORALS.values());
        }
        for (Block coralPlant : baseCoralSet){
            BlockState proper_block = coralPlant.getDefaultState();
            MapColor color = proper_block.getMapColor(worldIn, BlockPos.ORIGIN);
            for (Block block: BlockTags.CORAL_BLOCKS.values())
            {
                proper_block = block.getDefaultState();
                if (proper_block.getMapColor(worldIn, BlockPos.ORIGIN) == color)
                {
                    break;
                }
            }
            tmp_map.put(coralPlant,proper_block.getBlock());
        }
        plantToBlockMap = Collections.unmodifiableMap(tmp_map);
    }
}
