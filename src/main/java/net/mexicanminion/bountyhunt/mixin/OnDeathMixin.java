package net.mexicanminion.bountyhunt.mixin;

import net.mexicanminion.bountyhunt.managers.BountyManager;
import net.mexicanminion.bountyhunt.managers.CurrencyManager;
import net.mexicanminion.bountyhunt.managers.RewardManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerPlayerEntity.class)
public class OnDeathMixin {

    @Shadow @Final public MinecraftServer server;

    @Inject(at=@At("TAIL"), method="onDeath")
    private void onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        // This code is injected into the start of MinecraftServer.loadWorld()V
        //this.sendMessage(Text.of("You have added  diamond blocks to's bounty"), false);
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        checkBounty(player, damageSource);

    }

    private void checkBounty(PlayerEntity target, DamageSource damageSource){
        if(!BountyManager.getBounty(target.getUuid())){
            target.sendMessage(Text.of("No Active bounty"), false);
            return;
        }
        if((damageSource.getAttacker() == null)){
            target.sendMessage(Text.of("You were killed by nature!"), false);
            return;
        }
        if(damageSource.isIndirect()) {
            target.sendMessage(Text.of("You can't claim your own bounty!"), false);
            return;
        }
        if(BountyManager.getBounty(target.getUuid())){
            BountyManager.setBounty(target.getUuid(), false);
            RewardManager.setReward(damageSource.getAttacker().getUuid(), CurrencyManager.getCurrency(target.getUuid()));
            CurrencyManager.emptyCurrency(target.getUuid());
            damageSource.getAttacker().sendMessage(Text.of("You have claimed " + target.getEntityName() + "'s bounty!"));
            target.sendMessage(Text.of("You have been cleared of your burden"), false);
            for (ServerPlayerEntity players : server.getPlayerManager().getPlayerList()) {
                players.sendMessage(Text.of("The bounty on " + target.getEntityName() + " has been claimed!"), false);
            }
        }
    }

}
