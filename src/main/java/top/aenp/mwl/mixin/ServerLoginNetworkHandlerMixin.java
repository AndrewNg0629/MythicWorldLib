package top.aenp.mwl.mixin;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.aenp.mwl.network.v2.MythicNetwork;
import top.aenp.mwl.network.v2.interfaces.MythicServerLoginNetworkHandler;
import top.aenp.mwl.network.v2.interfaces.PacketCodec;
import top.aenp.mwl.network.v2.payloads.interfaces.MythicLoginC2SPayload;

@Mixin(value = ServerLoginNetworkHandler.class, priority = 990)
public abstract class ServerLoginNetworkHandlerMixin {
    @Shadow
    public abstract void disconnect(Text reason);

    @Inject(method = "onQueryResponse", at = @At(value = "HEAD"), cancellable = true)
    private void handleResponse(LoginQueryResponseC2SPacket packet, CallbackInfo info) {
        if (packet.getQueryId() == MythicNetwork.QUERY_ID) {
            PacketByteBuf packetByteBuf = packet.getResponse();
            if (packetByteBuf != null) {
                Identifier mythicId = packetByteBuf.readIdentifier();
                PacketCodec<PacketByteBuf, ? extends MythicLoginC2SPayload> codec = MythicNetwork.INSTANCE.LOGIN_C2S_CODECS.get(mythicId);
                MythicLoginC2SPayload payload = codec.decode(packetByteBuf);
                payload.handle((MythicServerLoginNetworkHandler) this);
            } else {
                disconnect(Text.of(String.format("Please have MythicWorldLib %s installed.", MythicNetwork.MOD_VERSION)));
            }
            info.cancel();
        }
    }
}
