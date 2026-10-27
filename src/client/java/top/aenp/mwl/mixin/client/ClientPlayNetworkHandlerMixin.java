package top.aenp.mwl.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.aenp.mwl.network.v2.MythicNetwork;
import top.aenp.mwl.network.v2.interfaces.MythicClientPlayNetworkHandler;
import top.aenp.mwl.network.v2.interfaces.PacketCodec;
import top.aenp.mwl.network.v2.payloads.interfaces.MythicPlayS2CPayload;

@Mixin(value = ClientPlayNetworkHandler.class, priority = 990)
public abstract class ClientPlayNetworkHandlerMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onCustomPayload", at = @At(value = "HEAD"), cancellable = true)
    private void handleMythicPayload(CustomPayloadS2CPacket packet, CallbackInfo info) {
        NetworkThreadUtils.forceMainThread(packet, (ClientPlayPacketListener) this, client);
        Identifier channelName = packet.getChannel();
        if (MythicNetwork.MYTHIC_PLAY_S2C_ID.equals(channelName)) {
            PacketByteBuf packetByteBuf = packet.getData();
            Identifier mythicId = packetByteBuf.readIdentifier();
            PacketCodec<PacketByteBuf, ? extends MythicPlayS2CPayload> codec = MythicNetwork.INSTANCE.PLAY_S2C_CODECS.get(mythicId);
            MythicPlayS2CPayload payload = codec.decode(packetByteBuf);
            payload.handle((MythicClientPlayNetworkHandler) this);
            info.cancel();
        }
    }
}
