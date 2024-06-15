package net.mexicanminion.bountyhunt.mixin;

import net.mexicanminion.bountyhunt.managers.BountyDataManager;
import net.mexicanminion.bountyhunt.managers.BountyManager;
import net.mexicanminion.bountyhunt.managers.RewardManager;
import net.mexicanminion.bountyhunt.util.CommonMethods;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerPlayerEntity.class)
public abstract class OnDeathMixin {

    @Shadow @Final public MinecraftServer server;

    @Shadow public abstract void attack(Entity target);

    /**
     * INJECTION TO onDeath IN MINECRAFT CODE
     * onPlayerDeath()
     * Description: This mixin is used to check if a player has a bounty on them when they die.
     *              If they do, the player who killed them will receive the bounty and the player who died will lose the bounty.
     * @param damageSource
     * @param ci
     */
    @Inject(at=@At("TAIL"), method="onDeath")
    private void onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        // This code is injected into the start of MinecraftServer.loadWorld()V
        //this.sendMessage(Text.of("You have added  diamond blocks to's bounty"), false);
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        checkBounty(player, damageSource);
    }

    /**
     * checkBounty()
     * Description: This method checks if the player who died had a bounty on them.
     *              If they did, the player who killed them will receive the bounty and the player who died will lose the bounty.
     * @param target
     * @param damageSource
     */
    @Unique
    private void checkBounty(ServerPlayerEntity target, DamageSource damageSource){
        if(!BountyManager.getBounty(target.getUuid())){
            return;
        }
        if(target.getAttacker() == null){
            return;
        }

        LivingEntity temp = target.getAttacker();
        if(!(temp instanceof ServerPlayerEntity)){
            return;
        }

        if(damageSource.isIndirect()) {
            return;
        }
        //check if the player who died had a bounty
        if(BountyManager.getBounty(target.getUuid())){
            ServerPlayerEntity attacker = (ServerPlayerEntity) target.getAttacker();
            ServerPlayerEntity fallen = target;
            //if they did, give the bounty to the player who killed them (SET REWARD, REMOVE BOUNTY AND CURRENCY)
            int rewardAmount = BountyManager.getBountyValue(fallen.getUuid()) + RewardManager.getReward(attacker.getUuid());

            if(BountyDataManager.getBountyData(fallen.getUuid()).getBountier() == null){

            }else{
                BountyManager.removeFromBountyList(BountyDataManager.getBountyData(fallen.getUuid()).getBountier(), fallen.getUuid());
            }
            RewardManager.setReward(attacker.getUuid(), true, rewardAmount, attacker.getGameProfile(), attacker.getName().getString());
            BountyManager.setBounty(fallen.getUuid(), false, 0, fallen.getGameProfile(), fallen.getName().getString(), null);//TODO marker just incase this breaks something, you know

            target.getAttacker().sendMessage(Text.of("You have claimed " + target.getName().getString() + "'s bounty!"));
            //TODO: add a message to say the next command
            target.sendMessage(Text.of("You have been cleared of your burden"), false);
            if(RewardManager.getReward(attacker.getUuid()) >= CommonMethods.announceAmount){
                for (ServerPlayerEntity players : server.getPlayerManager().getPlayerList()) {
                    if ((players == target) || (players == target.getAttacker())) {
                        continue;
                    }
                    players.sendMessage(Text.of("The bounty on " + target.getName().getString() + " has been claimed!"), false);
                }
            }
        }
    }

}
