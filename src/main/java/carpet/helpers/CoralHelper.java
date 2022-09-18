package carpet.helpers;

import carpet.CarpetSettings;
import carpet.fakes.CoralFeatureInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.CoralClawFeature;
import net.minecraft.world.level.levelgen.feature.CoralFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.MaterialColor;

import java.util.*;
import java.util.stream.Collectors;

public class CoralHelper {

    static Map<Direction, CoralClawFeature> directionToFeatureMap = new HashMap<>();
    static Map<CoralClawFeature, Direction> featureToDirectionMap = new HashMap<>();

    static Map<Block, Block> plantToBlockMap;

    static Set<Block> coralBlockSet;

    static{
        CoralClawFeature feature = new CoralClawFeature(NoneFeatureConfiguration.CODEC);
        directionToFeatureMap.put(Direction.NORTH,feature);
        featureToDirectionMap.put(feature, Direction.NORTH);
        feature = new CoralClawFeature(NoneFeatureConfiguration.CODEC);
        directionToFeatureMap.put(Direction.SOUTH,feature);
        featureToDirectionMap.put(feature, Direction.SOUTH);
        feature = new CoralClawFeature(NoneFeatureConfiguration.CODEC);
        directionToFeatureMap.put(Direction.EAST,feature);
        featureToDirectionMap.put(feature, Direction.EAST);
        feature = new CoralClawFeature(NoneFeatureConfiguration.CODEC);
        directionToFeatureMap.put(Direction.WEST,feature);
        featureToDirectionMap.put(feature, Direction.WEST);
    }


    public static void processGrowth(ServerLevel worldIn, RandomSource random, BlockPos pos, BlockState blockState, CoralFeature coral) {
        if (plantToBlockMap==null)
            loadCoralMap(worldIn);
        Block curr = blockState.getBlock();
        BlockState proper_block = plantToBlockMap.getOrDefault(curr,coralBlockSet.iterator().next()).defaultBlockState();
        worldIn.setBlock(pos, Blocks.WATER.defaultBlockState(), 4);

        if (!((CoralFeatureInterface)coral).growSpecific(worldIn, random, pos, proper_block))
        {
            worldIn.setBlock(pos, blockState, 3);
        }
        else
        {
            if (worldIn.random.nextInt(10)==0)
            {
                BlockPos randomPos = pos.offset(worldIn.random.nextInt(16)-8, worldIn.random.nextInt(8), worldIn.random.nextInt(16)-8  );
                if (coralBlockSet.contains(worldIn.getBlockState(randomPos).getBlock()))
                {
                    worldIn.setBlock(randomPos, Blocks.WET_SPONGE.defaultBlockState(), 3);
                }
            }
        }
    }

    public static boolean canBonemeal(BlockGetter var1, BlockPos var2, BlockState var3) {
        if (!(var1 instanceof Level level))
            return false;
        else {
            return (CarpetSettings.renewableCoral == CarpetSettings.RenewableCoralMode.EXPANDED
                        || (CarpetSettings.renewableCoral == CarpetSettings.RenewableCoralMode.TRUE
                            && level.getBiome(var2).is(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)))
                    && var3.getValue(BaseCoralPlantTypeBlock.WATERLOGGED)
                    && var1.getFluidState(var2.above()).is(FluidTags.WATER);
        }
    }

    public static boolean isBonemealSuccess(Level var1) {
        return (double) var1.random.nextFloat() < 0.15D;
    }

    public static CoralClawFeature getFeature(Direction direction){
        return directionToFeatureMap.getOrDefault(direction, new CoralClawFeature(NoneFeatureConfiguration.CODEC));
    }
    public static Direction getDirection(CoralClawFeature feature){
        return featureToDirectionMap.get(feature);
    }


    private static void loadCoralMap(ServerLevel worldIn){
        Map<Block, Block> tmp_map = new HashMap<>();
        Set<Block> baseCoralSet;
        coralBlockSet = worldIn.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY).getTag(BlockTags.CORAL_BLOCKS).orElseThrow().stream().map(Holder::value).collect(Collectors.toUnmodifiableSet());
        {
            Set<Block> coralPlantSet = worldIn.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY).getTag(BlockTags.CORAL_PLANTS).orElseThrow().stream().map(Holder::value).collect(Collectors.toUnmodifiableSet());
            Set<Block> coralWallSet = worldIn.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY).getTag(BlockTags.WALL_CORALS).orElseThrow().stream().map(Holder::value).collect(Collectors.toUnmodifiableSet());
            baseCoralSet = new HashSet<>(coralPlantSet);
            baseCoralSet.addAll(coralWallSet);
        }
        for (Block coralPlant : baseCoralSet){
            BlockState proper_block = coralPlant.defaultBlockState();
            MaterialColor color = proper_block.getMapColor(worldIn, BlockPos.ZERO);
            for (Block block: coralBlockSet)
            {
                proper_block = block.defaultBlockState();
                if (proper_block.getMapColor(worldIn, BlockPos.ZERO) == color)
                {
                    break;
                }
            }
            tmp_map.put(coralPlant,proper_block.getBlock());
        }
        plantToBlockMap = Collections.unmodifiableMap(tmp_map);
    }
}
