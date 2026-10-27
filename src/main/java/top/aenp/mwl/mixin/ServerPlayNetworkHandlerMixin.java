package top.aenp.mwl.mixin;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.aenp.mwl.network.v2.MythicNetwork;
import top.aenp.mwl.network.v2.interfaces.MythicServerPlayNetworkHandler;
import top.aenp.mwl.network.v2.interfaces.PacketCodec;
import top.aenp.mwl.network.v2.payloads.interfaces.MythicPlayC2SPayload;

@Mixin(value = ServerPlayNetworkHandler.class, priority = 990)
public abstract class ServerPlayNetworkHandlerMixin {
    @Inject(method = "onCustomPayload", at = @At(value = "HEAD"), cancellable = true)
    private void handleMythicPayloads(CustomPayloadC2SPacket packet, CallbackInfo info) {
        Identifier channelName = packet.getChannel();
        if (MythicNetwork.MYTHIC_PLAY_C2S_ID.equals(channelName)) {
            PacketByteBuf packetByteBuf = packet.getData();
            Identifier mythicId = packetByteBuf.readIdentifier();
            PacketCodec<PacketByteBuf, ? extends MythicPlayC2SPayload> codec = MythicNetwork.INSTANCE.PLAY_C2S_CODECS.get(mythicId);
            MythicPlayC2SPayload playC2SPayload = codec.decode(packetByteBuf);
            playC2SPayload.handle((MythicServerPlayNetworkHandler) this);
            info.cancel();
        }
    }
}
