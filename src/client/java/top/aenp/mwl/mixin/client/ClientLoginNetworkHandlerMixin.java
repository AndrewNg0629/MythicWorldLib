package top.aenp.mwl.mixin.client;

import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.aenp.mwl.network.v2.MythicNetwork;
import top.aenp.mwl.network.v2.interfaces.MythicClientLoginNetworkHandler;
import top.aenp.mwl.network.v2.payloads.interfaces.MythicLoginS2CPayload;

@Mixin(value = ClientLoginNetworkHandler.class, priority = 990)
public class ClientLoginNetworkHandlerMixin {
    @Inject(method = "onQueryRequest", at = @At(value = "HEAD"), cancellable = true)
    private void handleRequest(LoginQueryRequestS2CPacket packet, CallbackInfo info) {
        if (packet.queryId() == MythicNetwork.QUERY_ID) {
            MythicLoginS2CPayload payload = (MythicLoginS2CPayload) packet.payload();
            payload.handle((MythicClientLoginNetworkHandler) this);
            info.cancel();
        }
    }
}
