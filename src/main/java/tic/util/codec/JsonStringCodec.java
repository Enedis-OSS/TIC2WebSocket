package tic.util.codec;

public interface JsonStringCodec<T> {
    int DEFAULT_INDENT = 2;

    default String encodeToJsonString(T object) throws Exception {
        return encodeToJsonString(object, DEFAULT_INDENT);
    }

    default T decodeFromJsonString(String jsonString) throws Exception {
        return decodeFromJsonString(jsonString, DEFAULT_INDENT);
    }

    String encodeToJsonString(T object, int indentFactor) throws Exception;

    T decodeFromJsonString(String jsonString, int indentFactor) throws Exception;
}
