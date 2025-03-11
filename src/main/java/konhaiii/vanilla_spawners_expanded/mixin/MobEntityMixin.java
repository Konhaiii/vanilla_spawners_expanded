package konhaiii.vanilla_spawners_expanded.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import konhaiii.vanilla_spawners_expanded.item.ModItems;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    @Inject(method = "interactWithItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    private void injected(CallbackInfoReturnable<ActionResult> cir, @Local(argsOnly = true) PlayerEntity player, @Local(argsOnly = true) Hand hand) {

        MobEntity thisObject = (MobEntity)(Object)this;
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(ModItems.CURSED_BOTTLE)) {
            ActionResult actionResult = itemStack.useOnEntity(player, thisObject, hand);
            if (actionResult.isAccepted()) {
                cir.setReturnValue(actionResult);
            }
        }
    }

}
