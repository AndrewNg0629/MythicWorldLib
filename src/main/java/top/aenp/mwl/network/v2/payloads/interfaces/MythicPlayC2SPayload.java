package top.aenp.mwl.network.v2.payloads.interfaces;

import net.minecraft.util.Identifier;
import top.aenp.mwl.network.v2.interfaces.MythicServerPlayNetworkHandler;

public interface MythicPlayC2SPayload {
    Identifier mythicId();
    void handle(MythicServerPlayNetworkHandler handler);
}
