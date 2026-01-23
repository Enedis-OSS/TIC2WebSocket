package tic.util.codec;

public interface Codec<T1, T2> {
    T2 encode(T1 object) throws Exception;

    T1 decode(T2 object) throws Exception;
}
