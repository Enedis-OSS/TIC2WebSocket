// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

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
