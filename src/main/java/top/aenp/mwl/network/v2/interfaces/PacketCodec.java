package top.aenp.mwl.network.v2.interfaces;

public interface PacketCodec<B, V> {
    V decode(B buf);
    void encode(B buf, V value);
}
