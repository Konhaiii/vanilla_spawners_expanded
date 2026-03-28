package konhaiii.vanilla_spawners_expanded.mixin;

import konhaiii.vanilla_spawners_expanded.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobMixin {

    @Inject(
            method = "checkAndHandleImportantInteractions(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void vanillaSpawnersExpanded$checkAndHandleImportantInteractions(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

        Mob mob = (Mob)(Object)this;
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(ModItems.CURSED_BOTTLE)) {
            InteractionResult result = stack.interactLivingEntity(player, mob, hand);
            if (result.consumesAction()) {
                cir.setReturnValue(result);
            }
        }
    }
}