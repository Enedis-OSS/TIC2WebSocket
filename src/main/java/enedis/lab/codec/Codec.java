// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.codec;

/**
 * Codec interface
 *
 * @param <T>
 * @param <K>
 *
 */
public interface Codec<T, K>
{
	/**
	 * Decode type K to type T
	 *
	 * @param object
	 * @return instance of T
	 * @throws CodecException
	 */
	public T decode(K object) throws CodecException;

	/**
	 * Encode type T to type K
	 *
	 * @param object
	 * @return instance of K
	 * @throws CodecException
	 */
	public K encode(T object) throws CodecException;
}
