package net.mexicanminion.bountyhunt.mixin;

import net.mexicanminion.bountyhunt.managers.BountyManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerPlayerEntity.class)
public class OnDeathMixin {

    @Inject(at=@At("TAIL"), method="onDeath")
    private void onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        // This code is injected into the start of MinecraftServer.loadWorld()V
        //this.sendMessage(Text.of("You have added  diamond blocks to's bounty"), false);
        System.out.println("Hello from the mixin!");
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        checkBounty(player, damageSource);

    }

    private void checkBounty(PlayerEntity target, DamageSource damageSource){
        target.sendMessage(Text.of("You have added  diamond blocks to's bounty"), false);
        if((damageSource.getAttacker() == null)){
            target.sendMessage(Text.of("You were killed by nature!"), false);
            return;
        }
        if(damageSource.isIndirect()) {
            target.sendMessage(Text.of("You can't claim your own bounty!"), false);
            return;
        }
        if(BountyManager.getBounty(target.getUuid()) != damageSource.getAttacker().getUuid()){

        }
    }

}
