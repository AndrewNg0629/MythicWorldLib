package top.aenp.mwl.mixin;

import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.aenp.mwl.network.v2.MythicNetwork;
import top.aenp.mwl.network.v2.interfaces.MythicServerLoginNetworkHandler;
import top.aenp.mwl.network.v2.payloads.interfaces.MythicLoginC2SPayload;

@Mixin(value = ServerLoginNetworkHandler.class, priority = 990)
public abstract class ServerLoginNetworkHandlerMixin {
    @Shadow
    public abstract void disconnect(Text reason);

    @Inject(method = "onQueryResponse", at = @At(value = "HEAD"), cancellable = true)
    private void handleResponse(LoginQueryResponseC2SPacket packet, CallbackInfo info) {
        if (packet.queryId() == MythicNetwork.QUERY_ID) {
            if (packet.response() != null) {
                MythicLoginC2SPayload payload = (MythicLoginC2SPayload) packet.response();
                payload.handle((MythicServerLoginNetworkHandler) this);
            } else {
                disconnect(Text.of(String.format("Please have MythicWorldLib %s installed.", MythicNetwork.MOD_VERSION)));
            }
            info.cancel();
        }
    }
}
