package top.aenp.mwl.network.v2.payloads.interfaces;

import net.minecraft.util.Identifier;
import top.aenp.mwl.network.v2.interfaces.MythicClientPlayNetworkHandler;

public interface MythicPlayS2CPayload {
    Identifier mythicId();
    void handle(MythicClientPlayNetworkHandler handler);
}
