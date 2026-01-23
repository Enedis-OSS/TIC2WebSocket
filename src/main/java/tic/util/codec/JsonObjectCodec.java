// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.codec;

public interface JsonObjectCodec<T> {
    Object encodeToJsonObject(T object) throws Exception;

    T decodeFromJsonObject(Object jsonObject) throws Exception;
}
