package top.aenp.mwl.mixin;

import io.netty.channel.Channel;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkState;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.aenp.mwl.network.v2.MythicNetwork;

@Mixin(value = ClientConnection.class, priority = 990)
public abstract class ClientConnectionMixin {

    @Shadow
    private int packetsSentCounter;

    @Shadow
    protected abstract NetworkState getState();

    @Shadow
    private Channel channel;

    @Shadow
    protected abstract void sendInternal(Packet<?> packet, @Nullable PacketCallbacks callbacks, NetworkState packetState, NetworkState currentState);

    @SuppressWarnings("resource")
    @Inject(method = "sendImmediately", at = @At(value = "HEAD"), cancellable = true)
    private void sendImmediately(Packet<?> packet, @Nullable PacketCallbacks callbacks, CallbackInfo info) {
        if (packet instanceof LoginQueryRequestS2CPacket queryPacket) {
            if (queryPacket.getQueryId() == MythicNetwork.QUERY_ID) {
                NetworkState networkState = NetworkState.getPacketHandlerState(packet);
                NetworkState networkState2 = getState();
                packetsSentCounter++;
                if (networkState2 != networkState) {
                    if (networkState == null) {
                        throw new IllegalStateException("Encountered packet without set protocol: " + packet);
                    }
                    channel.config().setAutoRead(false);
                }
                if (channel.eventLoop().inEventLoop()) {
                    sendInternal(packet, callbacks, networkState, networkState2);
                } else {
                    channel.eventLoop().execute(() -> sendInternal(packet, callbacks, networkState, networkState2));
                }
                info.cancel();
            }
        }
    }
}
