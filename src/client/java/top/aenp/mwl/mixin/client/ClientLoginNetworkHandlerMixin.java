package top.aenp.mwl.mixin.client;

import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.aenp.mwl.network.v2.MythicNetwork;
import top.aenp.mwl.network.v2.interfaces.MythicClientLoginNetworkHandler;
import top.aenp.mwl.network.v2.interfaces.PacketCodec;
import top.aenp.mwl.network.v2.payloads.interfaces.MythicLoginS2CPayload;

@Mixin(value = ClientLoginNetworkHandler.class, priority = 990)
public class ClientLoginNetworkHandlerMixin {
    @Inject(method = "onQueryRequest", at = @At(value = "HEAD"), cancellable = true)
    private void handleRequest(LoginQueryRequestS2CPacket packet, CallbackInfo info) {
        if (packet.getQueryId() == MythicNetwork.QUERY_ID) {
            PacketByteBuf packetByteBuf = packet.getPayload();
            Identifier mythicId = packetByteBuf.readIdentifier();
            PacketCodec<PacketByteBuf, ? extends MythicLoginS2CPayload> codec= MythicNetwork.INSTANCE.LOGIN_S2C_CODECS.get(mythicId);
            MythicLoginS2CPayload payload = codec.decode(packetByteBuf);
            payload.handle((MythicClientLoginNetworkHandler) this);
            info.cancel();
        }
    }
}
