package carpet.mixins;

import carpet.helpers.CoralHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.feature.CoralClawFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CoralClawFeature.class)
public class CoralClawFeature_renewableCoralMixin {

    @ModifyVariable(method ="placeFeature", at = @At("STORE"), ordinal = 0)
    public Direction featureDirection(Direction value){
        Direction dir = CoralHelper.getDirection((CoralClawFeature)(Object)this);
        return (dir==null)?value:dir;
    }

}