package carpet.mixins;

import carpet.CarpetSettings;
import carpet.fakes.CoralFeatureInterface;
import carpet.helpers.CoralHelper;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(CoralFanBlock.class)
public abstract class CoralFanBlockMixin implements Fertilizable
{
    public boolean isFertilizable(BlockView var1, BlockPos var2, BlockState var3, boolean var4)
    {
        return CoralHelper.canBonemeal(var1,var2,var3);
    }

    public boolean canGrow(World var1, Random var2, BlockPos var3, BlockState var4)
    {
        return CoralHelper.isBonemealSuccess(var1);
    }

    public void grow(ServerWorld worldIn, Random random, BlockPos pos, BlockState blockState)
    {

        CoralFeature coral = new CoralMushroomFeature(DefaultFeatureConfig.CODEC);

        CoralHelper.processGrowth(worldIn,random,pos,blockState,coral);
    }
}
