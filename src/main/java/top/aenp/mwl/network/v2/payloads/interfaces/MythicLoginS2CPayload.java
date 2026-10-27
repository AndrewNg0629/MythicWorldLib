package top.aenp.mwl.network.v2.payloads.interfaces;

import net.minecraft.util.Identifier;
import top.aenp.mwl.network.v2.interfaces.MythicClientLoginNetworkHandler;

public interface MythicLoginS2CPayload {
    Identifier mythicId();

    void handle(MythicClientLoginNetworkHandler handler);
}
