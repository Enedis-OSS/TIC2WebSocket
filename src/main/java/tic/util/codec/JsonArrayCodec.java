package tic.util.codec;

public interface JsonArrayCodec<T> {
    Object encodeToJsonArray(T object) throws Exception;

    T decodeFromJsonArray(Object jsonArray) throws Exception;
}
