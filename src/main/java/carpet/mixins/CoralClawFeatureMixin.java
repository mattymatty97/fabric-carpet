package carpet.mixins;

import carpet.helpers.CoralHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.world.gen.feature.CoralClawFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CoralClawFeature.class)
public class CoralClawFeatureMixin {

    @ModifyVariable(method = "generateCoral", at=@At("STORE"), ordinal = 0)
    private Direction getDirection(Direction rand){
        Direction attempt = CoralHelper.getDirection((CoralClawFeature)(Object)this);
        return (attempt==null)?rand:attempt;
    }

}
