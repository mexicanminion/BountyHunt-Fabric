package net.mexicanminion.bountyhunt.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MinecraftServer.class)
public class OnDeathMixin {

    @Inject(at=@At("TAIL"), method="loadWorld")
    private void onPlayerDeath(CallbackInfo ci) {
        // This code is injected into the start of MinecraftServer.loadWorld()V
    }

}
