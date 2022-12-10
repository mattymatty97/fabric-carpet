package carpet.mixins;

import carpet.CarpetSettings;
import carpet.fakes.CoralFeatureInterface;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CoralPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.CoralClawFeature;
import net.minecraft.world.level.levelgen.feature.CoralFeature;
import net.minecraft.world.level.levelgen.feature.CoralMushroomFeature;
import net.minecraft.world.level.levelgen.feature.CoralTreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.MaterialColor;

@Mixin(CoralPlantBlock.class)
public abstract class CoralPlantBlock_renewableCoralMixin implements BonemealableBlock
{
    public boolean isValidBonemealTarget(BlockGetter var1, BlockPos var2, BlockState var3, boolean var4)
    {
        return CoralHelper.canBonemeal(var1, var2, var3);
    }

    public boolean isBonemealSuccess(Level var1, RandomSource var2, BlockPos var3, BlockState var4)
    {
        return CoralHelper.isBonemealSuccess(var1);
    }

    public void performBonemeal(ServerLevel worldIn, RandomSource random, BlockPos pos, BlockState blockState)
    {
        CoralFeature coral = new CoralTreeFeature(NoneFeatureConfiguration.CODEC);
        CoralHelper.processGrowth(worldIn, random, pos, blockState, coral);
    }

}
