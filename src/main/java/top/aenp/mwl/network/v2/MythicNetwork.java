package top.aenp.mwl.network.v2;

import io.netty.buffer.Unpooled;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import top.aenp.mwl.MythicWorldLib;
import top.aenp.mwl.network.v2.interfaces.PacketCodec;
import top.aenp.mwl.network.v2.payloads.interfaces.MythicLoginC2SPayload;
import top.aenp.mwl.network.v2.payloads.interfaces.MythicLoginS2CPayload;
import top.aenp.mwl.network.v2.payloads.interfaces.MythicPlayC2SPayload;
import top.aenp.mwl.network.v2.payloads.interfaces.MythicPlayS2CPayload;

import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"unchecked", "unused"})
public class MythicNetwork {
    public static final MythicNetwork INSTANCE = new MythicNetwork();
    public static final int QUERY_ID = -2147483600;
    public static final String MOD_VERSION = FabricLoader.getInstance().getModContainer(MythicWorldLib.MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();

    public final ConcurrentHashMap<Identifier, PacketCodec<PacketByteBuf, ? extends MythicLoginS2CPayload>> LOGIN_S2C_CODECS = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Identifier, PacketCodec<PacketByteBuf, ? extends MythicLoginC2SPayload>> LOGIN_C2S_CODECS = new ConcurrentHashMap<>();

    public final ConcurrentHashMap<Identifier, PacketCodec<PacketByteBuf, ? extends MythicPlayS2CPayload>> PLAY_S2C_CODECS = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Identifier, PacketCodec<PacketByteBuf, ? extends MythicPlayC2SPayload>> PLAY_C2S_CODECS = new ConcurrentHashMap<>();

    private static final Identifier DUMMY_LOGIN_S2C_ID = new Identifier("mwt", "login_s2c");
    public static final Identifier MYTHIC_PLAY_S2C_ID = new Identifier("mwt", "play_s2c");
    public static final Identifier MYTHIC_PLAY_C2S_ID = new Identifier("mwt", "play_c2s");


    public static LoginQueryRequestS2CPacket createLoginS2CPacket(MythicLoginS2CPayload payload) {
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        Identifier mythicId = payload.mythicId();
        PacketCodec<PacketByteBuf, MythicLoginS2CPayload> codec = (PacketCodec<PacketByteBuf, MythicLoginS2CPayload>) MythicNetwork.INSTANCE.LOGIN_S2C_CODECS.get(mythicId);
        packetByteBuf.writeIdentifier(mythicId);
        codec.encode(packetByteBuf, payload);
        return new LoginQueryRequestS2CPacket(QUERY_ID, DUMMY_LOGIN_S2C_ID, packetByteBuf);
    }

    public static LoginQueryResponseC2SPacket createLoginC2SPacket(MythicLoginC2SPayload payload) {
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        Identifier mythicId = payload.mythicId();
        PacketCodec<PacketByteBuf, MythicLoginC2SPayload> codec = (PacketCodec<PacketByteBuf, MythicLoginC2SPayload>) MythicNetwork.INSTANCE.LOGIN_C2S_CODECS.get(mythicId);
        packetByteBuf.writeIdentifier(mythicId);
        codec.encode(packetByteBuf, payload);
        return new LoginQueryResponseC2SPacket(QUERY_ID, packetByteBuf);
    }

    public static CustomPayloadS2CPacket createPlayS2CPacket(MythicPlayS2CPayload payload) {
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        Identifier mythicId = payload.mythicId();
        PacketCodec<PacketByteBuf, MythicPlayS2CPayload> codec = (PacketCodec<PacketByteBuf, MythicPlayS2CPayload>) MythicNetwork.INSTANCE.PLAY_S2C_CODECS.get(mythicId);
        packetByteBuf.writeIdentifier(mythicId);
        codec.encode(packetByteBuf, payload);
        return new CustomPayloadS2CPacket(MYTHIC_PLAY_S2C_ID, packetByteBuf);
    }

    public static CustomPayloadC2SPacket createPlayC2SPacket(MythicPlayC2SPayload payload) {
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        Identifier mythicId = payload.mythicId();
        PacketCodec<PacketByteBuf, MythicPlayC2SPayload> codec = (PacketCodec<PacketByteBuf, MythicPlayC2SPayload>) MythicNetwork.INSTANCE.PLAY_C2S_CODECS.get(mythicId);
        packetByteBuf.writeIdentifier(mythicId);
        codec.encode(packetByteBuf, payload);
        return new CustomPayloadC2SPacket(MYTHIC_PLAY_C2S_ID, packetByteBuf);
    }
}
