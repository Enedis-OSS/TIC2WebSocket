package tic.util.codec;

public interface JsonObjectCodec<T> {
    Object encodeToJsonObject(T object) throws Exception;

    T decodeFromJsonObject(Object jsonObject) throws Exception;
}
