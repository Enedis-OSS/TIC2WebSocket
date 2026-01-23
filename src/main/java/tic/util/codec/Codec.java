// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.codec;

public interface Codec<T1, T2> {
  T2 encode(T1 object) throws Exception;

  T1 decode(T2 object) throws Exception;
}
