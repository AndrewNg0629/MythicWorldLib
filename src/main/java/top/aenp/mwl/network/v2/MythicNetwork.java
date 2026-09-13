package top.aenp.mwl.network.v2;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import top.aenp.mwl.MythicWorldLib;
import top.aenp.mwl.network.v2.payloads.interfaces.MythicLoginC2SPayload;
import top.aenp.mwl.network.v2.payloads.interfaces.MythicLoginS2CPayload;

import java.util.concurrent.ConcurrentHashMap;

public class MythicNetwork {
    public static final MythicNetwork INSTANCE = new MythicNetwork();
    public static final int QUERY_ID = -2147483600;
    public static final String MOD_VERSION = FabricLoader.getInstance().getModContainer(MythicWorldLib.MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();

    public final ConcurrentHashMap<Identifier, PacketCodec<PacketByteBuf, ? extends MythicLoginS2CPayload>> LOGIN_S2C_CODECS = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Identifier, PacketCodec<PacketByteBuf, ? extends MythicLoginC2SPayload>> LOGIN_C2S_CODECS = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Identifier, PacketCodec<PacketByteBuf, ? extends CustomPayload>> CUSTOM_PAYLOAD_CODECS = new ConcurrentHashMap<>();
}
