package carpet.mixins;

import carpet.helpers.CoralHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CoralWallFanBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.CoralClawFeature;
import net.minecraft.world.level.levelgen.feature.CoralFeature;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CoralWallFanBlock.class)
public abstract class CoralWallFanBlock_renewableCoralMixin implements BonemealableBlock
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
        Direction direction = blockState.getValue(BaseCoralWallFanBlock.FACING);
        CoralFeature coral = CoralHelper.getFeature(direction);
        CoralHelper.processGrowth(worldIn, random, pos, blockState, coral);
    }
}
