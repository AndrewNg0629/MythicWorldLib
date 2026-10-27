package top.aenp.mwl.network.v2.payloads.interfaces;

import net.minecraft.util.Identifier;
import top.aenp.mwl.network.v2.interfaces.MythicServerLoginNetworkHandler;

public interface MythicLoginC2SPayload {
    Identifier mythicId();

    void handle(MythicServerLoginNetworkHandler handler);
}
